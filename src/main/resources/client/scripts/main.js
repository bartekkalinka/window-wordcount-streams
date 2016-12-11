"use strict";

var wsUri = "ws://" + window.location.hostname + ":" + window.location.port + "/window-streams?clientId=aaa";
var websocket;

// INIT MODULE
function init() {
    initWebSocket();
}
function initWebSocket() {
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) {
        onOpen(evt)
    };
    websocket.onclose = function(evt) {
        onClose(evt)
    };
    websocket.onmessage = function(evt) {
        onMessage(evt)
    };
    websocket.onerror = function(evt) {
        onError(evt)
    };
}
// WEBSOCKET EVENTS MODULE
function onOpen(evt) {
}
function onClose(evt) {
}
function onMessage(evt) {
   document.getElementById("output").innerHTML = evt.data;
}
function onError(evt) {
}
function doSend(message) {
}
init();
