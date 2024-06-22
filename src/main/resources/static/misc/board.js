import Disk from "./disk.js";
import Direction from "./direction.js";

const BLANK = 0;
const BOARD_SIZE = 8;
const INITIAL_STATE = {
    board: initializeBoard(),
    turn: Disk.DARK,
    history: [],
    remote: false,
    waitingMessage: true,
    placeable: {
        [Disk.DARK] : null,
        [Disk.LIGHT] : null,
    },
};

function createBoard () {
    var board = {};
    for (var row = 0; row < BOARD_SIZE; row++) {
        if(!board[row]) board[row] = {};
        for (var col = 0; col<BOARD_SIZE; col++) {
            board[row][col]=BLANK;
        }
    }
    return board;
}

function initializeBoard () {
    var board = createBoard();

    board[0][0] = Disk.DARK;
    board[1][0] = Disk.LIGHT;
    board[3][3] = Disk.LIGHT;
    //board[4][4] = Disk.LIGHT;
    board[4][3] = Disk.DARK;
    board[3][4] = Disk.DARK;
    return board;
}

const cloneDeep = function (x) {
    return JSON.parse(JSON.stringify(x));
}

const freeze = function(x) {
    return Object.freeze(cloneDeep(x));
}

function model (initialState = INITIAL_STATE) {
    const state = cloneDeep(initialState);
    let listeners = [];
    const addChangeListener= function(listener) {
        listeners.push(listener);
        listener(freeze(state));
        // unsubscribe function
        return function() {
            listeners = listeners.filter(l => l!==listener);
        }
    }
    const invokeListeners = function () {
        const data = freeze(state);
        listeners.forEach(l=>l(data));
    }
    const changeTurn = function () {
        if(state.turn === Disk.DARK) state.turn = Disk.LIGHT;
        else if (state.turn === Disk.LIGHT) state.turn = Disk.DARK;
    }
    const placeDisk = function(row, col) {
        var {board, turn, history} = state;
        var newBoard = _placeDisk(board, row, col, turn);

        state.board = newBoard;
        history.push({
            row:row, col:col, disk:turn
        });
        state.history = history;

        var validMove = checkValidMove(newBoard);
        if (validMove[Disk.getOpponent(turn)].length > 0) {
            changeTurn();
        } else if (validMove[turn].length > 0) {
            alert("There are no valid moves available, passing the turn.");
        } else {
            // game over condition.
        }
        
    }
    const playTurn = function (row, col, isRemoteMessage) {
        if (state.remote) {
            if ((state.waitingMessage && isRemoteMessage) || (!state.waitingMessage && !isRemoteMessage)) {
                placeDisk(row, col);
            }
            state. waitingMessage = (isRemoteMessage) ? false: true;
        } else {
            placeDisk(row, col);
        }
        invokeListeners();
    }
    
    const isPlaceable=function(row, col) {
        var {turn, board} = state;
        return _isPlaceable(board, row, col, turn);
    }
    const undo = function() {
        var {history} = state;
        if (history.length < 1) return;
        const newState = cloneDeep(INITIAL_STATE);
        var lastHistory = history.pop();
        newState.board = history.reduce((acc, cur)=>{
            return _placeDisk(acc, cur.row, cur.col, cur.disk);
        }, newState.board);
        state.board = newState.board;
        state.turn = lastHistory.disk;
        invokeListeners();
    }

    const isRemote = function (boolean) {
        state.remote = boolean;
    }
    const initRemoteGame = function(){
        state.waitingMessage = false;
    }

    return {
        changeTurn,
        isPlaceable,
        addChangeListener,
        playTurn,
        undo,
        isRemote,
        initRemoteGame,
    }
    
}

function checkValidMove (board) {
    var d = [];
    var l = [];
    for (var row = 0; row < BOARD_SIZE; row++) {
        for (var col = 0; col <BOARD_SIZE; col++) {
            var dPlaceable = _isPlaceable(board, row, col, Disk.DARK);
            var lPlaceable = _isPlaceable(board, row, col, Disk.LIGHT);
            if(dPlaceable) d.push({"row": row, "col": col});
            if(lPlaceable) l.push({"row": row, "col": col});
            
        }
    }
    var result = {};
    result[Disk.DARK] = d;
    result[Disk.LIGHT] = l;
    // console.log(result);
    return result
}

function findStraightLineSquares(board, row, col) {
    return Object.entries(Direction)
    .map(entry => {
        var squares = {};
        squares["squares"] = findContiguousDisksInDirection(board, row, col, entry[1]);
        squares["direction"] = entry[0];
        return squares;
    })
    .map(squaresObj => squaresObj["squares"]);
}

function findContiguousDisksInDirection(board, row, col, direction) {
    var arr = [];
    var rowCount = row + direction[0];
    var colCount = col + direction[1];
    while(rowCount > -1 && rowCount < BOARD_SIZE && colCount > -1 && colCount < BOARD_SIZE) {
        var next = board[rowCount]?.[colCount];
        if(!next) break;
        else {
            var obj = {};
            obj["disk"]=next;
            obj["row"]=rowCount;
            obj["col"]=colCount;
            arr.push(obj);
        }
        rowCount += direction[0];
        colCount += direction[1];
    }
    return arr;
}

function _placeDisk(board, row, col, side) {
    var copied = cloneDeep(board);
    copied[row][col] = side;
    findStraightLineSquares(copied, row, col).forEach(lines => {
        var arr = [];
        var has = false;
        for (var i = 0; i < lines.length; i++) {
            var disk=lines[i];
            arr.push(disk);
            if(disk["disk"] === side) {
                has = true;
                break;
            }
        }
        if(!has) return;
        arr.forEach(square => {
            copied[square["row"]][square["col"]] = side;
        });
    });
    return copied;
}

function _countDisk (board) {
    var d =0;
    var l = 0;
    Object.values(board).forEach(row=>{
        Object.values(row).forEach(square=>{
            if(square===Disk.DARK) d++;
            else if (square === Disk.LIGHT) l++;
            else ;
        });
    });
    var rtn = {};
    rtn[Disk.DARK] =d;
    rtn[Disk.LIGHT] = l;
    return rtn;
}

function _isPlaceable (board, row, col, side) {
    var opponent = (side == Disk.LIGHT) ? Disk.DARK : Disk.LIGHT;
    var square =board?.[row]?.[col];
    if(square === undefined) return false;
    else if (square !== 0) return false;
    return findStraightLineSquares(board, row, col)
    .map((contiguous) => {
        var firstDisk = contiguous[0]?.["disk"]===opponent;
        if(!firstDisk) return false;
        var has = contiguous.slice(1).filter(square=>square?.["disk"] === side);
        return has.length>0;
    }).reduce((acc,cur)=>{
        if(acc===true) return true;
        return cur;
    }, false);
}

function gameOver (board) {
    var result = _countDisk(board);
    return `DARK:${result[Disk.DARK]}\nLIGHT:${result[Disk.LIGHT]}\n${result[Disk.DARK] > result[Disk.LIGHT] ? "DARK " : "LIGHT "}wins!`;
}
export {model, BOARD_SIZE, checkValidMove, gameOver};