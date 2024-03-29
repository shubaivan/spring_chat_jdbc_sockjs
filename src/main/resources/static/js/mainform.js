var stompClient = null;
var chatId = null;
var userId = null;

$(document).ready(function () {

    $("#side-menu").on('click', '.chats_info', function () {
        var current = $(this);
        var currentChatId = current.data('elId');
        if (currentChatId === chatId) {
            splash({element: $('.chat-header'), addedClass: 'splash'});
        } else {
            chatId = currentChatId;
            extracted(currentChatId);
        }
    });

    $("#side-menu").on('click', '.chats_edit', function () {
        var current = $(this);
        var chatId = current.data('elId');
        document.location.href = 'chats/chatprofile/' + chatId;
    });

    $("#side-menu").on('click', '.chats_delete', function () {
        var current = $(this);
        var chatId = current.data('elId');

        $.ajax({
            type: "DELETE",
            url: 'api/chats/' + chatId,
            success: function (result) {
                $("#side-menu").find("[data-el-id=" + chatId + "]").closest('div').remove();
            },
            error: function (result) {
                console.log(result)
            }
        })
    });

    $("#side-menu").on('click', '.chats_info_private', function () {
        var current = $(this);
        var currentChatId = current.data('elId');
        if (currentChatId === chatId) {
            splash({element: $('.chat-header'), addedClass: 'splash'});
        } else {
            chatId = currentChatId;
            extracted(currentChatId);
        }
    });

    $('body').on('click', '.rightBottom', function () {
        var el = $(this);

        var chatId = el.data('elId');
        var chatName = el.data('elName');
        handleChatProcess(chatId, chatName, true);
    });

    $('body').on('click', '.leftBottom', function () {
        var el = $(this);

        var chatId = el.data('elId');
        var chatName = el.data('elName');
        var userId = $('#username').data('elId');

        stompClient.send("/app/chat/" + chatId + "/leftUser",
            {},
            JSON.stringify({userId: userId, sender: username, type: 'LEAVE', chatId: chatId})
        );
        $("#side-menu").find("[data-el-id=" + chatId + "]").closest('div').remove();

        createAllPublicChatEl(chatId, chatName);

        var replaceContainer = '<div id="main_container">SPD-Talks. Let\'s start conversation</div>';
        $('#main_container').replaceWith(replaceContainer);
    });

    $('body').on('click', '#clear', function () {
        var el = $(this);
        $('#messageArea').empty();
        $('#keyword').val('');
        getChatMessages(el.data('elId'));
    });

    $('body').on('click', '.message_edit', function () {
        editMessage($(this));
    });

    $('body').on('click', '.message_delete', function () {
        var confirmFromUser = confirm("Are you sure");
        if (confirmFromUser == true) {
            deleteMessage($(this));
        }
    });

    $('body').on('click', '.user_in_chat', function () {
        var el = $(this);
        createChatProcess(
            $('#username').text() + '_' + el.text(),
            $('#username').data('elId'),
            el.data('userId')
        );
    })
});

function createAllPublicChatEl(chatId, chatName) {
    $("#publicChats").append('<div class="parentDivChat">\n' +
        '<a href="#" data-el-id="' + chatId + '">\n' +
        ' <h>' + chatName + '</h>\n' +
        '<i class="fas fa-plus rightBottom" data-el-id="' + chatId + '" data-el-name="' + chatName + '"></i>\n' +
        '</a>\n' +
        '</div>');
}

function createChatEl(chatId, chatName, identity) {
    $("#" + identity).append('<div>\n' +
        '<a href="#" class="chats_info" data-el-id="' + chatId + '">\n' +
        '<h>' + chatName + '</h>\n' +
        '</a>\n' +
        '<br>\n' +
        '</div>');
}

function extracted(currentChatId) {
    $.ajax({
        type: "GET",
        url: '/chats/chat/' + currentChatId,
        success: function (data) {
            console.log(data);
            var replaceContainer = '<div id="main_container">' + data + '</div>';
            $('#main_container').replaceWith(replaceContainer);
            getChatMessages(currentChatId, true);
            getUsersFromChat(currentChatId);
        },
        error: function (result) {
            console.log(result)
        }
    })
}

function getUsersFromChat(currentChatId) {
    $.ajax({
        type: "GET",
        url: 'api/users/chat/' + currentChatId,
        success: function (data) {
            console.log(data);
            var users = JSON.parse(data);

            $.each(users, function (key, val) {
                parseUser(val);
            });

        },
        error: function (result) {
            console.log(result)
        }
    })
}

function getChatMessages(currentChatId, useStomp) {
    $.ajax({
        type: "POST",
        data: JSON.stringify({
            chatId: currentChatId
        }),
        contentType: "application/json",
        dataType: "json",
        url: 'api/messages/chat',
        success: function (data) {

            $.each(data, function (key, val) {
                console.log(val);
                var payload = {
                    type: val.messageType,
                    content: val.text,
                    sender: val.fullName,
                    createdDate: val.createdDate,
                    createdTime: val.createdTime,
                    authorId: val.authorID,
                    id: val.id
                };

                if (val.avatarId) {
                    payload.avatarId = val.avatarId;
                }

                parseMessage(payload);
            });

            if (useStomp !== 'undefined' && useStomp) {
                stomp();
            }
        },
        error: function (result) {
            console.log(result)
        }
    })
}

function searchMessagesInChat() {
    var currentChatId = document.getElementById("search-in-chat").getAttribute("field");
    var keyword = document.getElementById("keyword").value;

    $('#messageArea').empty();

    $.ajax({
        type: "POST",
        data: JSON.stringify({
            chatId: currentChatId,
            keyword: keyword
        }),
        contentType: "application/json",
        dataType: "json",
        url: 'api/messages/chat',
        success: function (data) {

            $.each(data, function (key, val) {
                console.log(val);
                var payload = {
                    type: val.messageType,
                    content: val.text,
                    sender: val.fullName,
                    createdDate: val.createdDate,
                    createdTime: val.createdTime,
                    authorId: val.authorID,
                    id: val.id
                };

                if (val.avatarId) {
                    payload.avatarId = val.avatarId;
                }

                parseMessage(payload);
            });
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
    var replaceMessages = document.querySelector('#replace-messages');
    var userArea = document.querySelector('#userArea');
    var connectingElement = document.querySelector('#connecting');
    chatId = $('#chatId').data('elId');
    userId = $('#username').data('elId');

    // var stompClient = null;
    // var username = null;
    var username = $('#username').text().trim();
    if (stompClient) {
        onConnected();
    } else {
        connect();
    }

    function connect() {
        //username = document.querySelector('#name').value.trim();


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
        var propertyNamesTopicPublic = Object.keys(stompClient.subscriptions)
            .filter(function (propertyName) {
                return propertyName.indexOf("chatId" + chatId) === 0;
            });
        if (!propertyNamesTopicPublic.length) {
            // Subscribe to the Public Topic
            stompClient.subscribe('/topic/public/' + chatId, onMessageReceived, {id: "chatId" + chatId});
        }

        var propertyNamesChatTyping = Object.keys(stompClient.subscriptions)
            .filter(function (propertyName) {
                return propertyName.indexOf("typing" + chatId) === 0;
            });

        if (!propertyNamesChatTyping.length) {
            stompClient.subscribe('/topic/chat/' + chatId + '/typing', onTypingReceived, {id: "typing" + chatId});
        }

        //edit message
        var propertyNamesChatEditMessage = Object.keys(stompClient.subscriptions)
            .filter(function (propertyName) {
                return propertyName.indexOf("edit-message" + chatId) === 0;
            });

        if (!propertyNamesChatEditMessage.length) {
            stompClient.subscribe('/topic/chat/' + chatId + '/edit-message', handleEditMessage, {id: "edit-message" + chatId});
        }

        //delete message
        var propertyNamesChatEditMessage = Object.keys(stompClient.subscriptions)
            .filter(function (propertyName) {
                return propertyName.indexOf("delete-message" + chatId) === 0;
            });

        if (!propertyNamesChatEditMessage.length) {
            stompClient.subscribe('/topic/chat/' + chatId + '/delete-message', handleDeleteMessage,
                {id: "delete-message" + chatId});
        }

        // Tell your username to the server
        stompClient.send("/app/chat/" + chatId + "/addUser",
            {},
            JSON.stringify({userId: userId, sender: username, type: 'JOIN', chatId: chatId})
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
                chatId: chatId,
                userId: userId
            };
            stompClient.send("/app/chat/" + chatId + "/sendMessage", {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
        event.preventDefault();
    }

    messageForm.addEventListener('submit', sendMessage, true);

    $(messageInput).on('keyup', function (eventObject) {
        stompClient.send("/app/chat/" + chatId + "/typing",
            {},
            JSON.stringify({userId: userId, chatId: chatId})
        )
    })

    function sendEditedMessage(messageId, newContent) {
        stompClient.send("/app/chat/" + chatId + "/edit-message",
            {},
            JSON.stringify({
                chatId: chatId,
                messageId: messageId,
                newContent: newContent
            })
        )
    }
}

function onTypingReceived(payload) {
    var chatTyping = JSON.parse(payload.body);
    var userTyping = $("#userArea").find("[data-user-id=" + chatTyping.userId + "]");

    if (userTyping.find('#typing' + chatTyping.userId).length === 0) {
        userTyping.append('<span id="typing' + chatTyping.userId + '" style="color: red">typing...</span>');
        setTimeout(function () {
            $("#typing" + chatTyping.userId).remove();
        }, 2000);
    }
}

function handleEditMessage(payload) {
    var editedMessage = JSON.parse(payload.body);
    var currentInput = $("#messageArea")
        .find("input[data-el-id="+editedMessage.id+"]");
    var currentLi = currentInput.closest('li');
    currentLi.find('.keyup').remove();

    if (editedMessage.status === 1) {
        if (currentInput.length > 0) {
            currentInput
                .replaceWith('<p>'+editedMessage.content+'</p>');
        } else {
            currentLi = $("#messageArea")
                .find("li[data-el-id="+editedMessage.id+"]");
            currentLi.find('p')
                .replaceWith('<p>'+editedMessage.content+'</p>');
        }

        var keyup_finished = '<div class="keyup_finished" data-el-id="'+
            editedMessage.id + '">editing was finished</div>';
        currentLi.append(keyup_finished);
        splash({element: currentLi, addedClass: 'splash'});
        setTimeout(function () {
            currentLi.find('.keyup_finished').remove();
        }, 2000);
    } else {
        var errorBlock = '<div class="keyup_error" data-el-id="'+
            editedMessage.id + '">'+ editedMessage.content +'</div>';
        currentLi.append(errorBlock);
        splash({element: currentLi, addedClass: 'splash_error'});
        setTimeout(function () {
            currentLi.find('.keyup_error').remove();
        }, 6000);
    }
}

function handleDeleteMessage(payload) {
    var deletedMessage = JSON.parse(payload.body);
    var currentLi = $("#messageArea")
        .find("li[data-el-id="+deletedMessage.id+"]");

    if (deletedMessage.status === 1) {
        var keyup_finished = '<div class="keyup_finished" data-el-id="'+
            deletedMessage.id + '">delete was finished</div>';
        currentLi.append(keyup_finished);
        setTimeout(function () {
            currentLi.remove();
        }, 2000);
    } else {
        var errorBlock = '<div class="keyup_error" data-el-id="'+
            deletedMessage.id + '">'+ deletedMessage.content +'</div>';
        currentLi.append(errorBlock);

        splash({element: currentLi, addedClass: 'splash_error'});
        setTimeout(function () {
            currentLi.find('.keyup_error').remove();
        }, 6000);
    }
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    parseMessage(message, true);
}

function parseUser(user) {
    if (userArea === 'indefined') {
        return null;
    }
    var para = document.createElement("p"); // create a <p> element
    para.setAttribute('data-user-id', user.id); // add attribute
    para.className += 'user_in_chat'; // add attribute
    var t = document.createTextNode(user.fullName); // create a text node
    para.appendChild(t);
    userArea.appendChild(para);
}

function editMessage(el) {
    var actualTag = el.closest('li').find('p');
    var replaceInput = $('<input>').attr({
        'data-el-id': el.data('elId'),
        value: actualTag.text()
    });
    actualTag.replaceWith(replaceInput);

    handleEditMessageProcess(replaceInput)
}

function deleteMessage(el) {
    var currentChatId = $("#chatId").data('elId');
    stompClient.send("/app/chat/" + currentChatId + "/delete-message",
        {},
        JSON.stringify({
            authorId: userId,
            chatId: currentChatId,
            id: el.data('elId')
        })
    )
}

function handleEditMessageProcess(handleEditMessage) {
    handleEditMessage.keypress(function (event) {
        var currentElement = $(this);
        if(!currentElement.closest('li').find('.keyup').length) {
            var keyup = '<div class="keyup" data-el-id="'+currentElement.data('elId')+'">editing</div>';
            $(keyup).insertAfter(currentElement);
        }
        if (event.keyCode == 13) {
            $(this).trigger("enterKey");
        }
    });

    handleEditMessage.on('change', function () {
        var currentElement = $(this);
        updateItem(currentElement);
    })
}

function updateItem(currentInput) {
    var currentChatId = $("#chatId").data('elId');
    stompClient.send("/app/chat/" + currentChatId + "/edit-message",
        {},
        JSON.stringify({
            content: currentInput.val(),
            authorId: userId,
            chatId: currentChatId,
            id: currentInput.data('elId')
        })
    )
}

function parseMessage(message, socket) {
    var messageElement = document.createElement('li');
    var currentUserId = $('#username').data('elId');

    var fromMessageCurrentUser = message.authorId;
    console.log('From message current user' + fromMessageCurrentUser);

    messageElement.setAttribute('id', 'idMessage');

    messageElement.setAttribute('data-el-id', message.id);

    var dateTime = message.createdDate + ' ' + message.createdTime;
    var checkExistUsername = $("#userArea").find("[data-user-id=" + message.userId + "]");
    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined! at ' + dateTime;
        if (socket) {
            if (!checkExistUsername.length) {
                parseUser({id: message.userId, fullName: message.sender});
            }
        }
    } else if (message.type === 'LEAVE') {
        if (socket) {
            if (checkExistUsername.length) {
                checkExistUsername.remove();
            }
        }
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left! at ' + dateTime;
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');

        if (message.avatarId) {
            var full = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');
            avatarElement.style.backgroundImage = "url('" + full + '/api/file_entities/' + message.avatarId + "')";
        } else {
            var avatarText = document.createTextNode(message.sender);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(message.sender);
        }

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var brElement = document.createElement('br');
        var usernameText = document.createTextNode(
            message.sender
        );

        var dateTimeText = document.createTextNode(
            dateTime
        );
        usernameElement.appendChild(usernameText);
        usernameElement.appendChild(brElement);
        usernameElement.appendChild(dateTimeText);

        messageElement.appendChild(usernameElement);

        console.log("UserId = " + currentUserId);

        if (currentUserId === fromMessageCurrentUser || currentUserId === message.userId) {
            var editMessageElement = document.createElement('span');
            editMessageElement.classList.add('message_edit');
            editMessageElement.setAttribute('data-el-id', message.id);
            editMessageElement.innerHTML =
                '<i class="far fa-edit" ' +
                'style="position: initial; ' +
                'color: #43464b">' +
                '</i>';

            var deleteMessageElement = document.createElement('span');
            deleteMessageElement.classList.add('message_delete');
            deleteMessageElement.setAttribute('data-el-id', message.id);

            deleteMessageElement.innerHTML =
                '<i class="fas fa-trash" ' +
                'style="position: initial; ' +
                'color: #43464b">' +
                '</i>';

            messageElement.appendChild(editMessageElement);
            messageElement.appendChild(deleteMessageElement);
        }
    }

    var textElement = document.createElement('p');

    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    if (typeof messageArea !== 'undefined') {
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function createChatProcess(chatName, ownerId, appendUserId) {
    function getName() {
        return "private " + chatName;
    }

    $.ajax({
        type: "POST",
        url: '/api/chats/createchat',
        data: JSON.stringify({
            name: getName(),
            chatType: 'PRIVATE',
            ownerId: ownerId,
            appendUserId: appendUserId
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (data) {
            console.log(data);

            if (data.checkExist === 1) {
                if (data.id === chatId) {
                    splash({element: $('.chat-header'), addedClass: 'splash'});
                } else {
                    chatId = data.id;
                    extracted(data.id);
                }
            } else {
                handleChatProcess(data.id, getName(), false);
            }
        },
        error: function (result) {
            console.log(result)
        }
    });
}

function handleChatProcess(chatId, chatName, notPrivate) {
    $.ajax({
        type: "POST",
        url: '/api/users/chat',
        data: JSON.stringify({
            chatId: chatId
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (data) {
            console.log(data);
            extracted(chatId);
        },
        error: function (result) {
            console.log(result)
        },
        complete: function (jqXHR, textStatus) {
            if (notPrivate) {
                $("a[data-el-id=" + chatId + "]").parent(".parentDivChat").remove();
                createChatEl(chatId, chatName, 'allChats');
            } else {
                createChatEl(chatId, chatName, 'privateChats');
            }
        }
    });
}

function openSideMenu() {
    document.getElementById('side-menu').style.width = '225px';
    document.getElementById('main').style.marginLeft = '225px';
}

function closeSideMenu() {
    document.getElementById('side-menu').style.width = '0px';
    document.getElementById('main').style.marginLeft = '0px';
}

function splash(parameters) {
    var element = parameters.element;
    var addedClass = parameters.addedClass;
    element.addClass(addedClass);
    setTimeout(function () {
        element.removeClass(parameters.addedClass);
    }, 1000);
}