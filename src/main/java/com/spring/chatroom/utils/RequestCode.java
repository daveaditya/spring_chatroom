package com.spring.chatroom.utils;


public enum RequestCode {

    JOIN_ROOM("joinRoom"),
    LEAVE_ROOM("leaveRoom"),
    SEND_MESSAGE("sendMessage");

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
            default:
                throw new EnumConstantNotPresentException(RequestCode.class, "Not present: " + action);
        }
    }

}
