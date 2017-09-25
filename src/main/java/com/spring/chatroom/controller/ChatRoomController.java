package com.spring.chatroom.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spring.chatroom.topic.RoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@SuppressWarnings({"unused", "SpringAutowiredFieldsWarningInspection"})
public class ChatRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomController.class);
    private static final Gson GSON = new GsonBuilder().create();
    private static final String ROOM_NAMES = "ROOM_NAMES";

    @Autowired
    private RoomManager roomManager;


    @RequestMapping("/")
    public String goHome(ModelMap modelMap) {
        modelMap.addAttribute(ROOM_NAMES, roomManager.getCurrentRoomNames());
        return "home";
    }


    @RequestMapping("/auth")
    public String getAuth(ModelMap modelMap) {
        return "auth";
    }


    @RequestMapping("/genAuth")
    public String generateAuth(HttpServletRequest request) {
        String name = request.getParameter("name");
        String emailId = request.getParameter("emailId");
        String mobileNo = request.getParameter("mobileNo");
        return "auth";
    }

}