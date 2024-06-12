import * as Board from "./board.js";

class SocketClient {
    #serverURI = null;
    #socketConnection = null;
    #model = null;
    _gameSessionId = null;
    constructor (serverURI, model) {
        this.#serverURI = serverURI;
        this.#model = model;
    }
    get gameSessionId() {
        return this._gameSessionId;
    }
    get model() {
        return this.#model;
    }
    get socketConnection() {
        return this.#socketConnection;
    }
    get messageConsumer() {
        return this._messageConsumer();
    }
    _overrideSocketEventHandler () {
        return null;
    }
    _messageConsumer(jsonMessage) {
        var {messageType, responseStatus, response} = jsonMessage;
        switch(messageType) {
            case "SYSTEM":
                if (this._gameSessionId == null) this._gameSessionId = response;
                break;
            case "CHAT":
                console.log(response);
                break;
            case "GAME":
                var parsed =JSON.parse(response);
                switch(parsed.action) {
                    case "INIT": this.model.initRemoteFame(); break;
                    case "PLAY": this.model.remotePlay(parsed.row, parsed.col); break;
                }
                // console.log(parsed);
                break;
            default:
        }
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
        else if (this._gameSessionId === null) throw "game session id is null";
        var message = {
            messageType:"CHAT",
            text: msg,
            gameSessionId: this._gameSessionId
        }
        this.#socketConnection.send(JSON.stringify(message));
    }
    gameMessage(action, row, col) {
        if (this.#socketConnection === null) throw "socket connection is null";
        else if (this._gameSessionId === null) throw "game session id is null";
        var textContent = {
            action:action
        }
        if(action === "PLAY") {
            textContent["row"] = row;
            textContent["col"] = col;
        }
        var message = {
            messageType:"GAME",
            gameSessionId: this._gameSessionId,
            text:JSON.stringify(textContent)
        }
        this.#socketConnection.send(JSON.stringify(message));
    }
}

class Host extends SocketClient {
    constructor (serverURI, model) {
        super(serverURI, model);
    }
    _connectionMessage() {
        return {
            messageType:"SYSTEM",
            userRole:"HOST"
        };
    }
}

class Client extends SocketClient {
    constructor(serverURI, model, gameSessionId) {
        super(serverURI, model);
        this._gameSessionId = gameSessionId;
    }
    _connectionMessage() {
        return {
            messageType:"SYSTEM",
            userRole:"CLIENT".
            gameSessionId: this._gameSessionId
        };
    }
    _messageConsumer(jsonMessage) {
        var {messageType, responseStatus, response} = jsonMessage;
        switch(messageType) {
            case "SYSTEM":
                if (responseStatus === "OK") this._gameSessionId = response;
                var message = {
                    messageType:"GAME",
                    gameSessionId:this.gameSessionId,
                    text:JSON.stringify({action:"INIT"})
                }
                this.socketConnection.send(JSON.stringify(message));
                break;
            case "CHAT":
                console.log(response);
                break;
            case "GAME":
                var parsed =JSON.parse(response);
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