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
                    case "PLAY" : model.playTurn(gameMessage.row, gameMessage.col, true); break;
                    case "GAMEOVER" : model.gameOver(); break;
                    case "PASSTURN" : model.changeTurn(); break;
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

class Client extends RemoteGameClient {
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