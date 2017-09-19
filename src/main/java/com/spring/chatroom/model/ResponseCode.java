package com.spring.chatroom.model;


/**
 * Constants denoting the Response Codes and their descriptions.
 */
@SuppressWarnings({"unused"})
public enum ResponseCode {

    OK(100, "Ok"),
    JOIN_SUCCESSFUL(200, "Joined Successfully"),
    JOIN_FAILED(201, "Join Failed"),
    SOMEONE_JOINED(202, "Someone has joined room"),
    LEFT_SUCCESSFULLY(203, "Left Successfully"),
    LEFT_UNSUCCESSFUL(204, "Failed"),
    SOMEONE_LEFT(205, "Someone Left"),
    ALREADY_JOINED(206, "Room already joined!"),
    MESSAGE_RECEIVED(210, "Message Received"),
    FORWARD_MESSAGE(211, "Message Forwarded"),
    MEMBER_LIST(220, "Members List"),
    EMPTY_ROOM(221, "Room is empty"),
    ROOM_LIST(230, "Rooms List"),
    NO_ROOMS(231, "No More Rooms");

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
