<%--suppress HtmlUnknownTarget --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home</title>
    <!-- StyleSheets -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/app.css">

    <!-- JavaScripts -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</head>
<body onload="pageLoad('${pageContext.request.contextPath}');" onclose="disconnect();">

<div>

    <div id="signin" style="border: dashed black 1px; padding: 10px;">

        <table>
            <tr>
                <td><label for="nickname">Nick Name: </label> </td>
                <td><input id="nickname" name="nickname" type="text" class="validate" maxlength="15" required></td>
            </tr>
            <tr>
                <td><label for="roomname">Room Name</label></td>
                <td>
                    <select id="roomname" name="roomname" class="validate" onchange="showInputIfOther(this.value);" required>
                        <option value="" selected>Select Room...</option>
                        <c:forEach items="${ROOM_NAMES}" var="name">
                            <option value="${name}">${name}</option>
                        </c:forEach>
                        <option value="other">Other</option>
                    </select>
                </td>
            </tr>
            <tr id="inputnewroom">
                <td><label for="newroomname">New Room Name</label></td>
                <td><input type="text" id="newroomname" class="validate" name="newroomname"></td>
            </tr>
            <tr>
                <td colspan="2"><button type="button" onclick="signIn()">Join</button></td>
            </tr>
        </table>

    </div>

    <div id="chatbox" style="padding-top: 10px; height: 100%">

        Welcome to, <span id="inroomname"></span><span style="float: right">
        <a id="viewrooms" onclick="viewRooms()">View Rooms</a> |
        <a id="viewmembers" onclick="viewMembers()">View Members</a> | <a id="disconnect"
                                                                          onclick="localStorage.removeItem('sessionId'); disconnect();">Disconnect</a>
    </span>
        <br><br>

        <div id="messagebox"
             style="height: 80%; text-align: center; padding: 10px; margin-bottom: 10px; overflow: auto; border: dotted #999999 2px"></div>

        <div id="sendmessage" style="height: 15%; width: 100%">
            <input id="message" name="message" placeholder="Enter message to send..."
                   style="width: 90%; height: 100px; border: 1px solid #999999; padding: 5px; background-color: gainsboro;">
            <button type="button" onclick="return sendMessage();" style="height: 100px; width: 9%; margin: 2px;">Send</button>
        </div>

    </div>

</div>

</body>
</html>