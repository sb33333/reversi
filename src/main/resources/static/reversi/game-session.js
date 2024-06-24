import * as Board from "./board.js";
import Disk from "./disk.js";
import * as RemoteGameClient from "./remote-game-client.js";
import * as SocketConnection from "./socket-connection.js";

function local () {
    const model = Board.model();
    model.addChangeListener(render);
    model.addChangeListener(checkPlaceableState);
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
        playTurn(row, col);
    });
    document.addEventListener("keyup", function(e) {
        if(!e.ctrlKey || e.key !== "z") return;
        undo();
    });
}

function remote(isHost, groupSessionId) {
    const model = Board.model();
    var {isPlaceable, playTurn, isRemote} = model;

    isRemote(true);
    var remoteGameClient = null;
    if (isHost) {
        remoteGameClient = new RemoteGameClient.Host(SocketConnection, model, groupSessionId);
    } else {
        remoteGameClient = new RemoteGameClient.Client(SocketConnection, model, groupSessionId);
    }

    model.addChangeListener(render);
    model.addChangeListener(checkPlaceableState);

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
        playTurn(row, col);
        remoteGameClient.gameMessage("PLAY", row, col);
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
    th.setAttribute("colspan", Board.BOARD_SIZE);
    th.classList.add("turn");
    if (turn === Disk.DARK) {
        th.classList.add("dark");
        th.innerHTML="turn: DARK";
    } else if (turn === Disk.LIGHT){
        th.classList.add("light");
        th.innerHTML="turn: LIGHT";
    }
    tr.appendChild(th);
    thead.appendChild(tr);
    return thead;
}

function checkPlaceableState (state) {
    var {board} = state;
    var placeable = Board.checkValidMove(board);
    if (placeable[Disk.DARK].length < 1 && placeable[Disk.LIGHT].length < 1) {
        window.requestAnimationFrame(() => {
            alert(Board.gameOver(board));
        });
    }
}

export {local, remote};