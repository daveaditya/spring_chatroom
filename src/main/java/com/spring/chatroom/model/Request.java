package com.spring.chatroom.model;


@SuppressWarnings({"unused"})
public class Request {

    private String action;

    private String roomName;

    private String nickName;

    private String sessionId;

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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
                ", sessionId='" + sessionId + '\'' +
                ", message=" + message +
                '}';
    }

}
