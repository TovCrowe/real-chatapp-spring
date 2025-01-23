package com.chatapp.project.Client;

import javax.swing.*;
import javax.swing.SwingUtilities;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String user = JOptionPane.showInputDialog(null,
                    "Enter username (Max: 16 characters):",
                    "Username",
                    JOptionPane.PLAIN_MESSAGE
                );


                if(user == null || user.length() == 0 || user.length() > 16){
                    JOptionPane.showMessageDialog(null, "Invalid username", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                ClientGUI clientGUI = null;
                try {
                    clientGUI = new ClientGUI("tovi");
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientGUI.setVisible(true);
            }
        });
    }
}
