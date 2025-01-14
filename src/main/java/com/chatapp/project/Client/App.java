package com.chatapp.project.Client;

import javax.swing.*;
import javax.swing.SwingUtilities;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientGUI clientGUI = new ClientGUI("tovi");
                clientGUI.setVisible(true);
            }
        });
    }
}
