package com.spring.chatroom.model;

/*
200 - JOINED SUCCESSFULLY
201 - OK
 */

public class Response {

    private int responseCode;
    private String responseDesc;
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
                ", message=" + message +
                '}';
    }

}