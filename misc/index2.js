import * as GameSession from "./game-session.js";

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
            var isHost = Number(remoteInfo.querySelector("[name=isHost]").value);
            var url = remoteInfo.querySelector("[name=serverUrl]").value;
            var gameSessionId = remoteInfo.querySelector("[name=gameSessionId]").value;
            GameSession.remote(url, isHost, gameSessionId);
        });
        this.insertAdjacentElement("afterend", template2.children[0]);
    });
    buttonArea.appendChild(template);
})();

let debug = false;
function setDebug(enable) {debug = enable;}
export {setDebug};