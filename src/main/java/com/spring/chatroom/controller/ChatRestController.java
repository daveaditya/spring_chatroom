package com.spring.chatroom.controller;


import com.spring.chatroom.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class ChatRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRestController.class);

    @RequestMapping(value = "/enter", method = RequestMethod.POST)
    public Response enter(String request) {
        // Todo: get details and create room and viewer if not present
        System.out.println("GOT ... : " + request);
        Response response = new Response();
        response.setResponseCode(200);
        response.setResponseDesc("OK");
        LOGGER.info("SENDING ... " + response.toString());
        return response;
    }

}