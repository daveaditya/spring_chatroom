package com.spring.chatroom.model;


/**
 * Constants denoting the Request Codes.
 */
public enum RequestCode {

    JOIN_ROOM("joinRoom"),
    LEAVE_ROOM("leaveRoom"),
    SEND_MESSAGE("sendMessage"),
    VIEW_MEMBERS("viewMembers"),
    VIEW_ROOMS("viewRooms"),
    MY_ROOMS("myRooms");

    String requestCode;

    RequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public static RequestCode fromAction(String action) {
        switch (action) {
            case "joinRoom":
                return JOIN_ROOM;
            case "leaveRoom":
                return LEAVE_ROOM;
            case "sendMessage":
                return SEND_MESSAGE;
            case "viewMembers":
                return VIEW_MEMBERS;
            case "viewRooms":
                return VIEW_ROOMS;
            case "myRooms":
                return MY_ROOMS;
            default:
                throw new EnumConstantNotPresentException(RequestCode.class, "Not present: " + action);
        }
    }

}
