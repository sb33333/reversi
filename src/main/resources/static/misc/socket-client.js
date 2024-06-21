//rename to remote-game-client.js

import ResultStatus from "./data-interface/result-status.js";

class RemoteGameClient {
    
    #socketConnection = null;
    #model = null;
    #groupSessionId = null;
    
    constructor (socketConnection, model, groupSessionId) {
        this.#socketConnection = socketConnection;
        this.#model = model;
        this.#groupSessionId = groupSessionId;
    }
    get groupSessionId() {
        return this.#groupSessionId;
    }
    get model() {
        return this.#model;
    }
    get socketConnection() {
        return this.#socketConnection;
    }
    
    _connectGroupSession() {
        throw new Error("not implemented");
    }
    /*
    connect (isSecured) {
        return openSocket(this.#serverURI, isSecured, this._connectionMessage(), this, this._overrideSocketEventHandler())
            .then(conn => this.#socketConnection = conn);
    }
    */
    
    gameMessage(action, row, col) {
        if (this.#socketConnection === null) throw "socket connection is null";
        else if (this.#groupSessionId === null) throw "game session id is null";
        var textContent = {
            action:action
        }
        if(action === "PLAY") {
            textContent["row"] = row;
            textContent["col"] = col;
        }
        var message = {
            messageType:"GAME",
            groupSessionId: this.#groupSessionId,
            text:JSON.stringify(textContent)
        }
        this.#socketConnection.send(JSON.stringify(message));
    }
}

class Host extends RemoteGameClient {
    constructor (socketConnection, model, groupSessionId) {
        super(socketConnection, model, groupSessionId);
        this._connectGroupSession();
    }
    
    _connectGroupSession () {
        var {groupSessionId, model} = this;
        this.socketConnection.addEventListener("message", function(event) {
            var {data}= event;
            var jsonMessage = JSON.parse(data);
            var {messageType, resultStatus, userMessagePayload} = jsonMessage;
            if (resultStatus !== ResultStatus.SUCCESS) {
                console.log(jsonMessage);
                return;
            }
            switch(messageType) {
                case "SYSTEM":
                    if(userMessagePayload.text.indexOf("leave session") > -1) {
                        alert("Your opponent has left the game.");
                        model.isRemote(false);
                    }
                    break;
                case "GAME":
                    var gameMessage = JSON.parse(userMessagePayload.text);
                    switch(gameMessage.action) {
                        case "INIT" : model.initRemoteGame(); break;
                        case "PLAY" : model.playTurn(gameMessage.row, gameMessage.col, true);break;
                    }
                    break;
                default:
            }
        });
        this.socketConnection.addEventListener("open", function (event) {
            this.send(JSON.stringify({
                messageType:"SYSTEM",
                userRole:"HOST",
                groupSessionId: groupSessionId ? groupSessionId : ""
            }));
        });
    }
}

class Client extends SocketClient {
    constructor(socketConnection, model, groupSessionId) {
        super(socketConnection, model, groupSessionId);
        this._connectGroupSession();
    }
    _connectGroupSession () {
        var {groupSessionId, model, socketConnection} = this;
        this.socketConnection.addEventListener("message", function(event) {
            var {data}= event;
            var jsonMessage = JSON.parse(data);
            var {messageType, resultStatus, userMessagePayload} = jsonMessage;
            if (resultStatus !== ResultStatus.SUCCESS) {
                console.log(jsonMessage);
                return;
            }
            switch(messageType) {
                case "SYSTEM":
                    if(userMessagePayload.text.indexOf("join session") > -1) {
                        var message = {
                            messageType:"GAME",
                            userRole:"CLIENT",
                            groupSessionId:groupSessionId,
                            text:JSON.stringify({action:"INIT"})
                        }
                        socketConnection.send(JSON.stringify(message));
                    } else if(userMessagePayload.text.indexOf("leave session") > -1) {
                        alert("Your opponent has left the game.");
                        model.isRemote(false);
                    }
                    break;
                case "GAME":
                    var gameMessage = JSON.parse(userMessagePayload.text);
                    switch(gameMessage.action) {
                        case "PLAY" : model.playTurn(gameMessage.row, gameMessage.col, true);break;
                    }
                    break;
                default:
            }
        });
        this.socketConnection.addEventListener("open", function (event) {
            this.send(JSON.stringify({
                messageType:"SYSTEM",
                userRole:"CLIENT",
                groupSessionId: groupSessionId ? groupSessionId : ""
            }));
        });
    }
}

export {Host,Client};


/// 
//split into socket-connection.js


let instance = null;
function openSocket (address, secured){
    var a = (secured === true) ? "wss://" : "ws://";
    a += address;
    
    const socket= new WebSocket(a);
    socket.addEventListener("open", function(event) {
        console.log("opened", socket.url);
    });
    socket.addEventListener("", function(event) {
        throw new Error("websocket error", event);
    });
    socket.addEventListener("", function(event) {
        var message = JSON.parse(event.data);
        console.log(message);
    });
    socket.addEventListener("", function(event) {
        console.log("socket closed");
        instance = null;
    });
    return socket;
    
}

let _address, _secured = null;

const builder = (function() {
    const setAddress = function(a) {
        _address = a;
        return this;
    }
    const setSecured = function(s) {
        _secured = s;
        return this;
    }
    const connect = function () {
        if(instance != null) instance.close();
        instance = openSocket(_address, _secured);
    }
    
    reutnr {
        address:setAddress,
        secured:setSecured,
        connect:connect,
    }
})();


function close () {
    if(instance == null) throw new Error("connection is not established");
    if(instance.readyState === WebSocket.OPEN) {
        instance.close();
        instance =null;
    }
}

function send(payload) {
    if(instance == null) throw new Error("connection is not established");
    if(instance.readyState === WebSocket.OPEN) {
        instance.send(payload);
    }
}

function addEventListener(eventType, handler) {
    if(instance == null) throw new Error("connection is not established");
    instance.addEventListener(eventType, handler);
}

export {builder, close, send, addEventListener};