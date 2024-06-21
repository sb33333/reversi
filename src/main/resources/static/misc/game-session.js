// rename to game-session.js
import * as Board from "./board.js";
import {Disk} from "./disk.js";

//import * as SocketClient from "./socket-client.js";
import * as RemoteGameClient from "./remote-game-client.js";

function local () {
    const model = Board.model();
    model.addChangeListener(render);
    var {isPlaceable, playTurn, undo} = model;

    const boardDiv = document.querySelector("#board");
    boardDiv.addEventListener("mouseover", function(e) {
        if(!e.target.classList.contains("square")) return;
        else if(e.target.classList.contains("disk")) return;
        if (isPlaceable(Number(e.target.dataset.row), Number(e.target.dataset.col))) {
            e.target.classList.add("hover");
        }
    });
    boardDiv.addEventListener("mouseout", function(e) {
        if(!e.target.classList.contains("square")) return;
        else if (e.target.classList.contains("disk")) return;
        e.target.classList.remove("hover");
    });
    boardDiv.addEventListener("click", function(e) {
        if (!e.target.classList.contains("square") || e.target.classList.contains("disk")) return;
        const row = Number(e.target.dataset.row);
        const col = Number(e.target.dataset.col);
        const c= isPlaceable(row, col);
        // if(debug) console.log(c);
        if(!c)return ;
        playTurn(row, col);
    });
    document.addEventListener("keyup", function(e) {
        if(!e.ctrlKey || e.key !== "z") return;
        undo();
    });
}

function remote(webSocketConnection, isHost, groupSessionId) {
    const model = Board.model();
    model.addChangeListener(render);
    var {isPlaceable, playTurn, isRemote} = model;

    isRemote(true);
    var connection = null;
    if (isHost) {
        connection = new SocketClient.Host(webSocketConnection, model, groupSessionId);
    } else {
        connection = new SocketClient.Client(webSocketConnection, model, groupSessionId);
    }
    connection.connect();

    const boardDiv = document.querySelector("#board");
    boardDiv.addEventListener("mouseover", function(e) {
        if(!e.target.classList.contains("square")) return;
        else if(e.target.classList.contains("disk")) return;
        if (isPlaceable(Number(e.target.dataset.row), Number(e.target.dataset.col))) {
            e.target.classList.add("hover");
        }
    });
    boardDiv.addEventListener("mouseout", function(e) {
        if(!e.target.classList.contains("square")) return;
        else if (e.target.classList.contains("disk")) return;
        e.target.classList.remove("hover");
    });
    boardDiv.addEventListener("click", function(e) {
        if (!e.target.classList.contains("square") || e.target.classList.contains("disk")) return;
        const row = Number(e.target.dataset.row);
        const col = Number(e.target.dataset.col);
        const c= isPlaceable(row, col);
        // if(debug) console.log(c);
        if(!c)return ;
        playTurn(row, col);
        connection.gameMessage("PLAY", row, col);
    });
}

function render (state) {
    window.requestAnimationFrame(() => {
        var newTable = document.createElement("table");
        var newTableBody = document.createElement("tbody");
        Object.entries(state.board).forEach(entry => {
            var tr=document.createElement("tr");
            var rowNum = entry[0];
            var row = entry[1];
            Object.values(row).forEach((square, colNum) => {
                var td=document.createElement("td");
                td.classList.add("square");
                td.dataset.row=rowNum;
                td.dataset.col=colNum;
                tr.appendChild(td);
                var div =document.createElement("div");
                if (square) {
                    div.classList.add("disk");
                    div.classList.add(square>0?"light":"dark");
                }
                td.appendChild(div);
            });
            newTableBody.appendChild(tr);
        });
        var newTableHead = renderTurn(state);
        newTable.appendChild(newTableHead);
        newTable.appendChild(newTableBody);
        var boardDiv = document.querySelector("#board");
        Array.from(boardDiv.children).filter(child=>"TABLE"===child.tagName).forEach(table=>table.replaceWith(newTable));
    });
}

function renderTurn(state) {
    var {turn} = state;
    var thead = document.createElement("thead");
    var tr = document.createElement("tr");
    var th = document.createElement("th");
    th.setAttribute("colspan", "8");
    th.classList.add("turn");
    if (turn === -1) {
        th.classList.add("dark");
        th.innerHTML="turn: DARK";
    } else if (turn === 1){
        th.classList.add("light");
        th.innerHTML="turn: LIGHT";
    }
    tr.appendChild(th);
    thead.appendChild(tr);
    return thead;
}

export {local, remote};