package com.spring.chatroom.model;


public enum RequestCode {

    JOIN_ROOM("joinRoom"),
    LEAVE_ROOM("leaveRoom"),
    SEND_MESSAGE("sendMessage"),
    VIEW_MEMBERS("viewMembers"),
    VIEW_ROOMS("viewRooms");

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
            default:
                throw new EnumConstantNotPresentException(RequestCode.class, "Not present: " + action);
        }
    }

}
