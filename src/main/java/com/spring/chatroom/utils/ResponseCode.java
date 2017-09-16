package com.spring.chatroom.utils;


@SuppressWarnings({"unused"})
public enum ResponseCode {

    OK(100, "Ok"),
    JOIN_SUCCESSFUL(200, "Joined Successfully"),
    JOIN_FAILED(201, "Join Failed"),
    SOMEONE_JOINED(202, "Someone has joined room"),
    LEFT_SUCCESSFULLY(203, "Left Successfully"),
    LEFT_UNSUCCESSFUL(204, "Failed"),
    SOMEONE_LEFT(205, "Someone Left"),
    MESSAGE_RECEIVED(210, "Message Received"),
    FORWARD_MESSAGE(211, "Message Forwarded");

    int code;
    String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }

}
