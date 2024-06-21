import * as SocketConnection from "./socket-connection.js";
import MessageType from "./data-interface/message-type.js";

let_groupSessionId, _chatMessagePresenter = null;

const builder = (function () {
    const setGroupSessionId = function(g) {
        _groupSessionId = g;
        return this;
    }
    const setChatMessagePresenter = function (p) {
        _chatMessagePresenter = p;
        return this;
    }
    const init = function () {
        SocketConnection.addEventListener("message", function(event){
            var {data}=event;
            var{userMessagePayload, messageType}=JSON.parse(data);
            if(messageType !== MessageType.CHAT) return;
            _chatMessagePresenter.pushMessage(">"+userMessagePayload.text));
            _chatMessagePresenter.process();
        });
    }
    return {
        groupSessionId:setGroupSessionId,
        chatMessagePresenter:setChatMessagePresenter,
        init:init,
    }
}) ();


function chat(chatMsg) {
    if (!_groupSessionId) throw new Error ("group session id cannot be null");
    var message = {
        messageType : MessageType.CHAT,
        groupSessionId:_groupSessionId,
        text:chatMsg,
    }
    SocketConnection.send(JSON.stringify(message));
    _chatMessagePresenter.pushMessage(chatMsg);
    _chatMessagePresenter.process();
}

export {builder, chat};