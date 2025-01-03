package com.chatapp.project.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chatapp.project.Models.Message;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    public void handleMessage(Message message){
        System.out.println("Recive message from user :" + message.getUser() + " Message : " + message.getMessage() );
        messagingTemplate.convertAndSend("/topic/messages", message);
        System.out.println("Message sent to /topic/messages " + message.getUser() + ": " + message.getMessage() );

    }

}
