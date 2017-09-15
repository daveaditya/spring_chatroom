package com.spring.chatroom.model;


@SuppressWarnings({"unused"})
public class Request {

    private String action;

    private String roomName;

    private String nickName;

    private Message message;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Request{" +
                "action='" + action + '\'' +
                ", roomName='" + roomName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", message=" + message +
                '}';
    }

}
