var contextPath;
var roomName;
var subscribeTo;
var nickname;
var sessionId = null;
var jSessionId = null;
var webSocket;

// Todo: Implement Spring Security

/**
 * Perform validation and send request to server
 * to join the entered room.
 */
function signIn() {
    var error = false;

    nickname = document.getElementById('nickname').value;
    if (nickname.trim() === 0) {
        $('#nickname').css({"border": '#FF0000 1px solid'});
        error = true;
    } else {
        $('#nickname').css({"border": '#000000 1px solid'});
    }

    roomName = document.getElementById('roomname').value;
    if (roomName === "") {
        $('#roomname').css({"border": '#FF0000 1px solid'});
        error = true;
    } else if (roomName === 'other') {
        roomName = document.getElementById('newroomname').value;
        if (roomName.trim().length === 0) {
            $('#newroomname').css({"border": '#FF0000 1px solid'});
            error = true;
        } else {
            $('#newroomname').css({"border": '#000000 1px solid'});
        }
    } else {
        $('#roomname').css({"border": 'initial'});
    }

    if (!error) {
        subscribeTo = "/" + roomName + "/messages";
        $('#nickname').css("border: #000000 1px solid;");
        $('#roomname').css("border: #000000 1px solid;");
        $('#newroomname').css("border: #000000 1px solid;");
        connect();
    }
}


/**
 * Create connection to the room and subscribe for messages.
 */
function connect() {

    webSocket = new WebSocket("ws://localhost:8080/app/chat");

    webSocket.onopen = function (event) {
        if (event === undefined) {
            return;
        }

        if (webSocket.readyState === 1) {
            var requestToSend = JSON.stringify({
                action: "joinRoom",
                roomName: roomName,
                nickName: nickname,
                sessionId: sessionId,
                jSessionId: jSessionId,
                message: null
            });
            console.log("SENDING: " + requestToSend);
            webSocket.send(requestToSend);
        }
    };

    webSocket.onclose = function () {
        console.log("SERVER CLOSED CONNECTION ...");
        localStorage.clear();
        pageLoad(contextPath);
    };

    webSocket.onmessage = function (msg) {

        console.log("GOT ... " + msg.data + " ||| type ... " + typeof msg.data);
        var response = JSON.parse(msg.data);
        // noinspection JSUnresolvedVariable
        var responseCode = response.responseCode;
        // Handling based on Response Code

        // 200 - JOINED SUCCESSFULLY
        if (responseCode === 200) {
            sessionId = response.sessionId;
            jSessionId = response.jSessionId;
            // Store the session id in local storage
            localStorage.setItem('sessionId', sessionId);
            localStorage.setItem('jSessionId', jSessionId);

            console.log("CONNECTED : SESSION ... " + sessionId + " ... " + jSessionId);
            onConnected();

            // 201 - JOIN FAILED - Todo
        } else if (responseCode === 201) {
            alert("Cannot join the specified room.");

            // 202 - Someone Joined
        } else if (responseCode === 202) {
            showMessage(responseCode, response);

            // 203 - Left Successfully 'Viewer left room'
        } else if (responseCode === 203) {
            pageLoad(contextPath);

            // 204 - Left failed - Todo
        } else if (responseCode === 204) {
            alert("Cannot leave room!");

            // 205 - Someone has left the room
        } else if (responseCode === 205) {
            showMessage(responseCode, response);

            // 206 - Viewer has already joined the room in another tab
        } else if (responseCode === 206) {
            alert("You have already joined this room. Please try again after closing that window.");

            // 210 - Message Received 'by server'
        } else if (responseCode === 210) {
            showMessage(responseCode, response);

            // 211 - Message Forwarded 'Message available in room'
        } else if (responseCode === 211) {
            showMessage(responseCode, response);

            // 220 - Other members of group
        } else if (responseCode === 220) {
            showMessage(responseCode, response);

            // 221 - Empty Room 'current room is empty'
        } else if (responseCode === 221) {
            showMessage(responseCode, response);

            // 230 - Room list
        } else if (responseCode === 230) {
            showMessage(responseCode, response);

            // 231 - No rooms except current
        } else if (responseCode === 231) {
            showMessage(responseCode, response);

            // 232 - Your rooms list
        } else if (responseCode === 232) {
            showMessage(responseCode, response);

            // On other response codes
        } else {
            console.log("UNDEFINED -- GOT: ... " + msg.data);
        }

    };

    webSocket.onerror = function (event) {
        console.log("ERROR: " + event.data);
    }

}


/**
 * Enables the chatbox and disables signin box
 */
function onConnected() {
    $('#signin').hide();
    $('#chatbox').show();
    $('#inroomname').text(roomName);
}


/**
 * Validate and send message to the room.
 */
function sendMessage() {

    var error = false;
    var msg = document.getElementById('message').value;

    if (msg.trim().length === 0) {
        $('#message').css({"border": '#FF0000 1px solid'});
        error = true;
    } else {
        $('#message').css({"border": '#000000 1px solid'});
    }

    if (!error) {
        var date = new Date();
        var msgToSend = JSON.stringify({
            action: "sendMessage",
            roomName: roomName,
            nickName: nickname,
            sessionId: sessionId,
            jSessionId: jSessionId,
            message: {
                from: nickname,
                message: msg,
                time: date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
            }
        });
        console.log("SENDING: " + msgToSend);
        webSocket.send(msgToSend);
        $('#message').val('');
    }

}


/**
 * Displays the other members present in the room
 */
function viewMembers() {
    var requestToSend = JSON.stringify({
        action: "viewMembers",
        nickName: nickname,
        roomName: roomName,
        sessionId: sessionId,
        jSessionId: jSessionId,
        message: null
    });
    console.log("SENDING ... " + requestToSend);
    webSocket.send(requestToSend);
}


/**
 * Displays the other rooms present
 */
function viewRooms() {
    var requestToSend = JSON.stringify({
        action: "viewRooms",
        nickName: nickname,
        roomName: roomName,
        sessionId: sessionId,
        jSessionId: jSessionId,
        message: null
    });
    console.log("SENDING ... " + requestToSend);
    webSocket.send(requestToSend);
}


/**
 * Displays the list of rooms the user is present in
 */
function myRooms() {
    var requestToSend = JSON.stringify({
        action: "myRooms",
        nickName: nickname,
        roomName: roomName,
        sessionId: sessionId,
        jSessionId: jSessionId,
        message: null
    });
    console.log("SENDING ... " + requestToSend);
    webSocket.send(requestToSend);
}


/**
 * Displays the obtained message
 * @param responseCode int indicating the response of server
 * @param jsonResponse json object containing message to display
 */
function showMessage(responseCode, jsonResponse) {
    var msg = jsonResponse.message;
    var str = '';

    // If someone joined the room
    if (responseCode === 202) {
        $('#messagebox').append("<span class='roomMsg' style='background-color: #f9ff93;'>" + msg.from + " ... " + msg.message + " on " + msg.time + "</span><br>");

        // If someone left the room
    } else if (responseCode === 205) {
        $('#messagebox').append("<span class='roomMsg' style='background-color: #ff885e;'>" + msg.from + " ... " + msg.message + " on " + msg.time + "</span><br>");

        // If response contains member list
    } else if (responseCode === 220) {
        str = roomName + " contains " + msg.message.length + " member(s)<br><br>";
        msg.message.forEach(function (item) {
            str += item + "<br>";
        });
        $('#messagebox').append("<span class='roomMsg' style='background-color: #b1c0d8;'>" + str + " <br>as of " + msg.time + "</span><br>");

        // If any other message occurs
    } else if (responseCode === 221) {
        $('#messagebox').append("<span class='roomMsg' style='background-color: #b1c0d8;'>You are the only member of " + roomName + " room.</span><br>");

        // If response contains room list
    } else if (responseCode === 230) {
        str = "There are " + msg.message.length + " active room(s)<br><br>";
        msg.message.forEach(function (item) {
            str += item + "<br>";
        });
        $('#messagebox').append("<span class='roomMsg' style='background-color: #9ae2b2;'>" + str + " <br>as of " + msg.time + "</span><br>");

        // If any other message occurs
    } else if (responseCode === 231) {
        $('#messagebox').append("<span class='roomMsg' style='background-color: #9ae2b2;'>This is the only room active</span><br>");

        // If list of user's rooms is returned
    } else if (responseCode === 232) {
        str = "You are active in " + msg.message.length + " room(s)<br><br>";
        msg.message.forEach(function (item) {
            str += item + "<br>";
        });
        $('#messagebox').append("<span class='roomMsg' style='background-color: #d1b1ff;'>" + str + " <br>as of " + msg.time + "</span><br>");

        // If any other message occurs
    } else {
        var from = msg.from;
        var fromContainer = 'container';
        var fromClass = 'from';
        var fromMsgClass = 'msg';
        if (from === nickname) {
            from = 'You';
            fromClass = 'my-from';
            fromMsgClass = 'my-msg';
            fromContainer = 'my-container';
        }
        $('#messagebox').append('<div class="' + fromContainer + '"><span class="' + fromClass + '">' + from + '</span><br><div class="' + fromMsgClass + '">' + msg.message + '</div><span class="time">' + msg.time + '</span><br></div>');
    }
}


/**
 * Enables the text input field for entering new room's name
 * depending on value passed
 * @param value if 'other' selected enable input
 */
function showInputIfOther(value) {
    if (value === "other") {
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
    // fetch session id from local storage
    sessionId = localStorage.getItem('sessionId');
    jSessionId = localStorage.getItem('jSessionId');

    contextPath = path;
    $('#signin').show();
    $('#inputnewroom').hide();
    $('#chatbox').hide();
    $('#messagebox').empty();
    $('#inroomname').empty();
    $('#nickname').val('');
    $('#roomname')[0].selectedIndex = 0;
    $('#newroomname').val('');
    $('#message').val('');
}


/**
 * Disconnect the subscriber and reinitialize the components
 */
function disconnect() {
    if (webSocket !== undefined) {
        var exitRequest = JSON.stringify({
            action: "leaveRoom",
            roomName: roomName,
            nickName: nickname,
            sessionId: sessionId,
            jSessionId: jSessionId,
            message: null
        });
        console.log("SENDING: " + exitRequest);
        webSocket.send(exitRequest);
    }
}