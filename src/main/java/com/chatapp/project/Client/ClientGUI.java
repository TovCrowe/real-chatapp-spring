package com.chatapp.project.Client;

import com.chatapp.project.Models.Message;

import java.util.concurrent.ExecutionException;

public class ClientGUI {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyStompClient myStompClient = new MyStompClient("tovi");
        myStompClient.sendMessage(new Message("Taptap", "Hola como estas"));
    }

}
