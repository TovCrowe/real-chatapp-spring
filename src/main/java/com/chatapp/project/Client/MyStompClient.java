package com.chatapp.project.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.chatapp.project.Models.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class MyStompClient {
    private StompSession session;
    private String user;

    public MyStompClient(MessageListener messageListener, String user) throws ExecutionException, InterruptedException {
        this.user = user;

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler(messageListener, user);
        String url = "ws://localhost:8080/ws"; // Use ws:// for WebSocket

        session = stompClient.connectAsync(url, sessionHandler).get();


    }
    public void sendMessage(Message message){
        try {
            session.send("/app/message", message);
            System.out.println("Message sent to server: " + message.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectUser(String user){
        session.send("/app/disconnect", user);
        System.out.println("Disconnect : " + user);

    }

}
