var contextPath;
var roomName;
var subscribeTo;
var nickname;
var stompClient;


/**
 * Perform validation and send request to server
 * to join the entered room.
 */
function signIn() {
    var error = false;

    nickname = document.getElementById('nickname').value;
    if(nickname.trim() === 0) {
        $('#nickname').css({ "border": '#FF0000 1px solid'});
        error = true;
    }

    roomName = document.getElementById('roomname').value;
    if(roomName === "") {
        $('#roomname').css({ "border": '#FF0000 1px solid'});
        error = true;
    } else if(roomName === 'other') {
        roomName = document.getElementById('newroomname').value;
        if(roomName.trim().length === 0) {
            $('#newroomname').css({ "border": '#FF0000 1px solid'});
            error = true;
        }
    }

    if(!error) {
        subscribeTo = "/" + roomName + "/messages";
/*        var requestToSend = JSON.stringify({
            request: {
                nickname: nickname,
                roomname: roomName
            }
        });*/
        $('#nickname').css("all: initial;");
        $('#roomname').css("all: initial;");
        $('#newroomname').css("all: initial;");
        /*$.ajax({
            url: contextPath + "/",
            method: "post",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: requestToSend,
            success: function(jsonResponse) {
                if(jsonResponse.responseCode === 200) {
                    $('#inroomname').html(roomName);
                    connect();
                } else {
                    alert("Error: " + jsonResponse.responseDesc);
                }
            },
            error: function() {
                alert("Server side error occurred...!");
            }
        });*/
        connect();
    }
}


/**
 * Create connection to the room and subscribe for messages.
 */
function connect() {
    var socket = new SockJS(contextPath + "/app/chat");
    stompClient = Stomp.over(socket);

    $.ajax({
        url: "ws://" + contextPath + "/app/path",
        data: {
            request: {
                action: "joinRoom",
                roomName: roomName,
                nickName: nickname,
                message: null
            }
        },
        success: function(response) {
            if(response.responseCode === 200) {
                onConnected();
            } else {
                alert("Server error...!");
            }
        },
        error: function() {
            alert("Server error...!");
        }
    })
}


/**
 * Enables the chatbox and disables signin box
 */
function onConnected() {
    $('#signin').hide();
    $('#chatbox').show();
}


/**
 * Validate and send message to the room.
 */
function sendMessage() {

    var error = false;
    var msg = document.getElementById('message').value;

    if(msg.trim().length === 0) {
        $('#message').css({ "border": '#FF0000 1px solid'});
        error = true;
    }

    if(!error) {
        $('#message').css("all: initial;");
        var msgToSend = {
            request: {
                action: "sendMessage",
                roomName: roomName,
                message: {
                    from: nickname,
                    message: msg,
                    time: Date.now()
                }
            }
        };
        stompClient.send("/app/chat/", {}, JSON.stringify(msgToSend));
    }

}


/**
 * Displays the obtained message
 * @param jsonResponse json object containing message to display
 */
function showMessage(jsonResponse) {
    console.log("Got: " + JSON.stringify(jsonResponse));
    var msg = jsonResponse.message;
    var newMsgDiv = document.createElement('div');
    newMsgDiv.appendChild(document.createTextNode(msg.from + " " + msg.message + " " + msg.time));
    document.getElementById("messagebox").appendChild(newMsgDiv);
}


/**
 * Enables the text input field for entering new room's name
 * depending on value passed
 * @param value if 'other' selected enable input
 */
function showInputIfOther(value) {
    if(value === "other") {
        $('#inputnewroom').show();
    } else {
        $('#inputnewroom').hide();
    }
}


/**
 * Initialize components and sets contextPath
 * @param path contextPath from for the app
 */
function pageLoad(path) {
    contextPath = path;
    $('#signin').show();
    $('#inputnewroom').hide();
    $('#chatbox').hide();
    $('#nickname').val('');
    $('#roomname')[0].selectedIndex = 0;
    $('#newroomname').val('');
    $('#message').val('');
}


/**
 * Disconnect the subscriber and reinitialize the components
 */
function disconnect() {
    if(stompClient !== undefined) {
        stompClient.disconnect();
    }
    pageLoad(contextPath);
}