package com.spring.chatroom.model;


@SuppressWarnings({"unused"})
public class Response {

    private int responseCode;

    private String responseDesc;

    private String sessionId;

    private String jSessionId;

    private Message message;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getjSessionId() {
        return jSessionId;
    }

    public void setjSessionId(String jSessionId) {
        this.jSessionId = jSessionId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseCode=" + responseCode +
                ", responseDesc='" + responseDesc + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", jSessionId='" + jSessionId + '\'' +
                ", message=" + message +
                '}';
    }

}