import * as GameSession from "./game-session.js";
import * as SocketConnection from "./socket-connection.js";
import {TextareaDomMessagePresenter as MessagePresenter} from "./message-presenter.js";
import * as ChatModule from "./chat.js";

(function() {
    var buttonArea = document.querySelector("#button-area");
    var template = document.querySelector("#button-area-template").content.cloneNode(true);
    template.querySelector("#local").addEventListener("click", function() {
        GameSession.local();
        buttonArea.querySelector("#remote-info")?.remove();
    });

    template.querySelector("#remote").addEventListener("click", function() {
        var template2 = document.querySelector("#remote-game-form").content.cloneNode(true);
        template2.querySelector("#connect").addEventListener("click", function() {
            var remoteInfo = document.querySelector("#remote-info");
            var isCreate = Number(remoteInfo.querySelector("[name=isCreate]").value);
            var url = remoteInfo.querySelector("[name=serverUrl]").value;
            var groupSessionId = remoteInfo.querySelector("[name=groupSessionId]").value;
            
            SocketConnection.builder
                .address(url)
                .secured(false)
                .connect();
            GameSession.remote(SocketConnection, isCreate, groupSessionId);
            
            var messageAreaTemplate = document.querySelector("#message-area-template").content.cloneNode(true);
            var messagePresenter = new MessagePresenter(messageAreaTemplate.querySelector("textarea"));
            ChatModule.builder
                .groupSessionId(groupSessionId)
                .chatMessagePresenter(messagePresenter)
                .init();
            messageAreaTemplate.querySelector("input").addEventListener("keydown", function(event) {
                if(event.key === "Enter") {
                    ChatModule.chat(event.target.value);
                    event.target.value="";
                }
            });
            var messageArea =document.querySelector("#message-area");
            messageArea.replaceChildren(messageAreaTemplate);
        });
        document.querySelector("#remote-info").replaceChildren(template2);
    });
    buttonArea.appendChild(template);
})();
