$(document).ready(function () {
    // var mainContainer = $('#main_container');
    $('.chats_info').on('click', function () {
        var current = $(this);
        var currentChatId = current.data('elId');

        $.ajax({
            type: "GET",
            url: '/chats/chat/' + currentChatId,
            success: function (data) {
                console.log(data);
                var replaceContainer = '<div id="main_container">' + data + '</div>';
                $('#main_container').replaceWith(replaceContainer);
                getChatMessages(currentChatId);
            },
            error: function (result) {
                console.log(result)
            }
        })
    });
});

function getChatMessages(currentChatId) {
    $.ajax({
        type: "GET",
        url: 'api/messages/chat/' + currentChatId,
        success: function (data) {

            $.each(data, function (key, val) {
                console.log(val);
                var payload = {
                    type: val.messageType,
                    content: val.text,
                    sender: val.fullName,
                    createdDate: val.createdDate,
                    createdTime: val.createdTime
                };

                parseMessage(payload);
            });
            stomp()
        },
        error: function (result) {
            console.log(result)
        }
    })
}

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function stomp() {
    // var usernamePage = document.querySelector('#username-page');
    // var chatPage = document.querySelector('#chat-page');
    // var usernameForm = document.querySelector('#usernameForm');
    var messageForm = document.querySelector('#messageForm');
    var messageInput = document.querySelector('#message');
    var messageArea = document.querySelector('#messageArea');
    var connectingElement = document.querySelector('#connecting');
    var chatId = $('#chatId').data('elId');

    var stompClient = null;
    var username = null;
    connect();

    function connect() {
        //username = document.querySelector('#name').value.trim();
        username = $('#username').text().trim()

        //if(username) {
        //usernamePage.classList.add('hidden');
        //chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
        //}
        // event.preventDefault();
    }


    function onConnected() {
        // Subscribe to the Public Topic
        stompClient.subscribe('/topic/public/' + chatId, onMessageReceived);

        // Tell your username to the server
        stompClient.send("/app/chat/" + chatId + "/addUser",
            {},
            JSON.stringify({sender: username, type: 'JOIN', chatId: chatId})
        )

        // connectingElement.classList.add('hidden');
    }


    function onError(error) {
        connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        connectingElement.style.color = 'red';
    }


    function sendMessage(event) {
        var messageContent = messageInput.value.trim();
        console.log("content");
        console.log(messageContent);
        console.log("message input");
        console.log(messageInput.value);

        if (messageContent && stompClient) {
            var chatMessage = {
                sender: username,
                content: messageInput.value,
                type: 'CHAT',
                chatId: chatId
            };
            stompClient.send("/app/chat/" + chatId + "/sendMessage", {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
        event.preventDefault();
    }

    messageForm.addEventListener('submit', sendMessage, true);
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    parseMessage(message);
}

function parseMessage(message) {
    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(0);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var brElement = document.createElement('br');
        var usernameText = document.createTextNode(
            message.sender
        );

        var dateTimeText = document.createTextNode(
            message.createdDate + ' ' + message.createdTime
        );
        usernameElement.appendChild(usernameText);
        usernameElement.appendChild(brElement);
        usernameElement.appendChild(dateTimeText);

        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    //Change it!!
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);


    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function openSideMenu() {
    document.getElementById('side-menu').style.width = '225px';
    document.getElementById('main').style.marginLeft = '225px';
}

function closeSideMenu() {
    document.getElementById('side-menu').style.width = '0px';
    document.getElementById('main').style.marginLeft = '0px';
}