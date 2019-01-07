var ws;
var fileInput = $("#file");

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    ws = new WebSocket('ws://localhost:8080/all');
    ws.binaryType = "arraybuffer";
    ws.onmessage = function (data) {
        includeContent(data.data);
    }
    setConnected(true);
    console.log(ws.CONNECTING);
}

function disconnect() {
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var data = JSON.stringify({'message': $("#message").val()})
    console.log(data);
    ws.send(data);
    console.log(data);
}

function includeContent(message) {
    if (message instanceof ArrayBuffer) {
        console.log('Got Image:');
        addImageToWindow(message);
    } else {
        $("#greetings").append("<tr><td> " + message + "</td></tr>");
        console.log("Message: " + message);
    }
}

function addImageToWindow(image) {
    let url = URL.createObjectURL(new Blob([image]));
    messageWindow.innerHTML += '<img src="${url}"/>';
}

function sendFile() {
    let file = fileInput.files[0];
    includeContent(file);
    ws.send(file);
    fileInput.value = null;
};

$(function() {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendMessage();
    });

    $("#sendFile").click(function () {
        sendFile();
    });
});

