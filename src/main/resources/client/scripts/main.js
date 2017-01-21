"use strict";

var wsUri = "ws://" + window.location.hostname + ":" + window.location.port + "/window-streams?clientId=aaa";
var websocket;
var canvas = document.getElementById("canv");
var ctx = canvas.getContext("2d");

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
    var input = JSON.parse(evt.data);
    if(input.counts) {
        draw(input.counts);
    }
    if(input.beat) {
        heartbeat(true);
        setTimeout(function() { heartbeat(false); }, 500);
    }
}
function onError(evt) {
}
function doSend(message) {
}
function draw(data) {
    var leftMargin = 20;
    ctx.fillStyle = "#FFFFFF";
    ctx.fillRect(leftMargin, 0, canvas.width - leftMargin, canvas.height);
    var verticalStep = 60;
    var horizontalStep = (canvas.width - leftMargin) / data[0][0];
    for (var i = 0; i < data.length; i++) {
        var num = data[i][0];
        var word = data[i][1];
        ctx.fillStyle = "#8888FF";
        ctx.fillRect(leftMargin, i * verticalStep + verticalStep, num * horizontalStep, verticalStep - 1);
        ctx.font = "20px Arial";
        ctx.fillStyle = "#000000";
        ctx.fillText(word + ": " + num, leftMargin, i * verticalStep + verticalStep + verticalStep * 2 / 3);
    }
}
function heartbeat(on) {
    if(on) ctx.fillStyle = "#000000"; else ctx.fillStyle = "#FFFFFF"
    ctx.fillRect(0, 0, 20, 20);
}
init();
