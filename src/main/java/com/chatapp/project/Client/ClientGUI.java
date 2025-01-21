package com.chatapp.project.Client;

import com.chatapp.project.Models.Message;
import org.apache.logging.slf4j.SLF4JLogBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends javax.swing.JFrame implements MessageListener {

    private JPanel connectedUsersPanel, messagePanel;
    private String user;
    private MyStompClient myStompClient;
    public ClientGUI(String user) throws ExecutionException, InterruptedException {
        super("User"  + user);
        this.user = user;
        myStompClient = new MyStompClient(this, user);

        setSize(1218, 685);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                if(option ==  JOptionPane.YES_OPTION){
                    myStompClient.disconnectUser(user);
                    ClientGUI.this.dispose();
                }
            }
        });

        getContentPane().setBackground(Utilities.PRIMARY);
        addGuiComponents();


    }

    private void addGuiComponents() {
        addConnectedUsers();
        addChatComponents();
    }
    private void addConnectedUsers(){
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setBorder(Utilities.addPadding(10,10,10,10));
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY);
        connectedUsersPanel.setPreferredSize(new Dimension(200,getHeight()));

        JLabel connectedUsersLabel = new JLabel("Connected Users");
        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD, 18));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUsersPanel.add(connectedUsersLabel);

        add(connectedUsersPanel, BorderLayout.WEST);



    }

    private void addChatComponents(){
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT);

        messagePanel = new JPanel();
        messagePanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT);
        chatPanel.add(messagePanel, BorderLayout.CENTER);


        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.TRANSPARENT);



        JTextField messageField = new JTextField();
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
               if(e.getKeyChar() == KeyEvent.VK_ENTER){
                   String input = messageField.getText();

                   if(input.isEmpty()){
                       return;
                   }

                   messageField.setText("");



                   myStompClient.sendMessage(new Message(user, input));
               }
            }
        });
        messageField.setBackground(Utilities.SECONDARY);
        messageField.setForeground(Utilities.TEXT_COLOR);
        messageField.setBorder(Utilities.addPadding(0, 10, 0, 10));
        messageField.setFont(new Font("Inter", Font.PLAIN, 16));
        messageField.setPreferredSize(new Dimension(inputPanel.getWidth(), 50));
        inputPanel.add(messageField, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);
    }

    private JPanel createChatMessageComponent(Message message){
        JPanel chatMessage = new JPanel();
        chatMessage.setBackground(Utilities.TRANSPARENT);
        chatMessage.setLayout(new BoxLayout(chatMessage, BoxLayout.Y_AXIS));

        JLabel  usernameLabel = new JLabel(message.getUser());
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 18));
        usernameLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(usernameLabel);

        JLabel messageLabel = new JLabel(message.getMessage());
        messageLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        messageLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLabel);

        return chatMessage;


    }

    @Override
    public void onMessageReceive(Message message) {
        messagePanel.add(createChatMessageComponent(message));
        revalidate();
        repaint();

    }

    @Override
    public void onActiveUsersUpdated(ArrayList<String> users) {
        if(connectedUsersPanel.getComponents().length >= 2){
            connectedUsersPanel.remove(1);
        }

        JPanel userListPanel = new JPanel();
        userListPanel.setBackground(Utilities.TRANSPARENT);
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        for(String user: users){
            JLabel username = new JLabel();
            username.setText(user);
            username.setForeground(Utilities.TEXT_COLOR);
            username.setFont(new Font("Inter", Font.BOLD, 18));
            userListPanel.add(username);

        }
        connectedUsersPanel.add(userListPanel);
        revalidate();
        repaint();
    }
}
