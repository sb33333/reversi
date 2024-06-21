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
    
    return {
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