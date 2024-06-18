import ResultStatus from "./data-interface/result-status.js";

class SocketClient {
    #serverURI = null;
    #socketConnection = null;
    #model = null;
    _groupSessionId = null;
    constructor (serverURI, model, groupSessionId) {
        this.#serverURI = serverURI;
        this.#model = model;
        this._groupSessionId = groupSessionId;
    }
    get groupSessionId() {
        return this._groupSessionId;
    }
    get model() {
        return this.#model;
    }
    get socketConnection() {
        return this.#socketConnection;
    }
    get messageConsumer() {
        return this._messageConsumer;
    }
    _overrideSocketEventHandler () {
        return null;
    }
    _messageConsumer(jsonMessage) {
        throw "not implemented";
    }

    _connectionMessage() {
        throw "not implemented";
    }
    connect (isSecured) {
        return openSocket(this.#serverURI, isSecured, this._connectionMessage(), this, this._overrideSocketEventHandler())
            .then(conn => this.#socketConnection = conn);
    }
    chat (msg) {
        if (this.#socketConnection === null) throw "socket connection is null";
        else if (this._groupSessionId === null) throw "game session id is null";
        var message = {
            messageType:"CHAT",
            text: msg,
            groupSessionId: this._groupSessionId
        }
        this.#socketConnection.send(JSON.stringify(message));
    }
    gameMessage(action, row, col) {
        if (this.#socketConnection === null) throw "socket connection is null";
        else if (this._groupSessionId === null) throw "game session id is null";
        var textContent = {
            action:action
        }
        if(action === "PLAY") {
            textContent["row"] = row;
            textContent["col"] = col;
        }
        var message = {
            messageType:"GAME",
            groupSessionId: this._groupSessionId,
            text:JSON.stringify(textContent)
        }
        this.#socketConnection.send(JSON.stringify(message));
    }
}

class Host extends SocketClient {
    constructor (serverURI, model, groupSessionId) {
        super(serverURI, model, groupSessionId);
    }
    _connectionMessage() {
        return {
            messageType:"SYSTEM",
            userRole:"HOST",
            groupSessionId: this._groupSessionId?this._groupSessionId:""
        };
    }

    _messageConsumer(jsonMessage) {
        console.log(jsonMessage);
        var {messageType, resultStatus, userMessagePayload} = jsonMessage;
        switch(messageType) {
            case "SYSTEM":
                if (this._groupSessionId == null) this._groupSessionId = userMessagePayload;
                break;
            case "CHAT":
                console.log(userMessagePayload);
                break;
            case "GAME":
                var parsed =JSON.parse(userMessagePayload.text);
                switch(parsed.action) {
                    case "INIT": this.model.initRemoteGame(); break;
                    case "PLAY": this.model.remotePlay(parsed.row, parsed.col); break;
                }
                // console.log(parsed);
                break;
            default:
        }
    }
}

class Client extends SocketClient {
    constructor(serverURI, model, groupSessionId) {
        super(serverURI, model, groupSessionId);
    }
    _connectionMessage() {
        return {
            messageType:"SYSTEM",
            userRole:"CLIENT",
            groupSessionId: this._groupSessionId
        };
    }
    _messageConsumer(jsonMessage) {
        console.log(jsonMessage);
        var {messageType, resultStatus, userMessagePayload} = jsonMessage;
        switch(messageType) {
            case "SYSTEM":
                if (resultStatus === ResultStatus.SUCCESS) this._groupSessionId = userMessagePayload.groupSessionId;
                var message = {
                    messageType:"GAME",
                    userRole:"CLIENT",
                    groupSessionId: this.groupSessionId,
                    text:JSON.stringify({action:"INIT"})
                }
                this.socketConnection.send(JSON.stringify(message));
                break;
            case "CHAT":
                console.log(userMessagePayload);
                break;
            case "GAME":
                var parsed =JSON.parse(userMessagePayload.text);
                switch(parsed.action) {
                    case "PLAY": this.model.remotePlay(parsed.row, parsed.col); break;
                }
                // console.log(parsed);
                break;
            default:
        }       
    }
}

function openSocket (address, secured, onOpenMessage, connectionHolder, socketEventHandlers){
    var a = (secured === true) ? "wss://" : "ws://";
    a += address;
    return new Promise((resolve, reject) => {
        const socket = new WebSocket(a);
        socket.onopen = 
            socketEventHandlers?.["onopen"] 
            || function (e) {
                console.log("opened ", socket.url);
                socket.send(JSON.stringify(onOpenMessage));
                resolve(socket);
            }
        socket.onerror =
            socketEventHandlers?.["onerror"]
            || function(error) {
                console.log("websocket error:", error);
                reject(error);
            }
        socket.onmessage =
            socketEventHandlers?.["onmessage"]
            || function(e) {
                var message = JSON.parse(e.data);
                connectionHolder.messageConsumer(message);
                // console.log(message);
            }
        socket.onclose =
            socketEventHandlers?.["onclose"] 
            || function(e) {
                console.log("socket closed");
                // console.log(e);
            }
    });
}

export {Host,Client};