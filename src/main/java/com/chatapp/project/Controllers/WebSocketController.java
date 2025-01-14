package com.chatapp.project.Controllers;

import com.chatapp.project.Service.WebsocketSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chatapp.project.Models.Message;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final WebsocketSessionManager sessionManager;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate, WebsocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    public void handleMessage(Message message){
        System.out.println("Recive message from user :" + message.getUser() + " Message : " + message.getMessage() );
        messagingTemplate.convertAndSend("/topic/messages", message);
        System.out.println("Message sent to /topic/messages " + message.getUser() + ": " + message.getMessage() );

    }
    @MessageMapping("/connect")
    public void connectUser(String username) {
            sessionManager.addUsers(username);
            sessionManager.broadcastActiveUsers();
            System.out.println("username : " + username);
        }
    @MessageMapping("/disconnect")
    public void disconnectUser(String username) {
            sessionManager.removeUsers(username);
            sessionManager.broadcastActiveUsers();
            System.out.println("username : " + username);
    }

}
