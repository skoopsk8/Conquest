package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Server;
import com.nasser.poulet.conquest.network.Network;
import com.nasser.poulet.conquest.server.chat.Chat;
import com.nasser.poulet.conquest.server.chat.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Thomas on 4/4/14.
 */
public class Room {
    private ArrayList<Connection> user = new ArrayList<Connection>();
    private ArrayList<ChatMessage> messageList = new ArrayList<ChatMessage>();
    private String roomName;
    public static int idNum = 0;

    public Room( String roomName, int id){
        this.roomName = roomName;
        idNum = id;
    }

    public void receivedMessage(Network.ChatMessage object, Server server, Connection connection){
        System.out.println("New message from " + connection.getID());
        messageList.add(new ChatMessage(object.getMessage()));

        Date t1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        if(!messageList.get(messageList.size()-1).isCommand())
            sendToAllClient(server, "["+df.format(t1)+"]"+ messageList.get(messageList.size()-1).getMessage());
        else
            sendToClient(server, connection, "[Server]"+messageList.get(messageList.size() - 1).getAnswer());
    }

    public void addClient(Connection connection, Server server){
        sendToAllClient(server, "[Server] A new client joined your room");
        user.add(connection);
    }

    public boolean isClientOf(Connection connection){
        for(Connection users: user){
            if(connection == users)
                return true;
        }
        return false;
    }

    private void sendToAllClient(Server server, String message){
        for(Connection connection: user)
            server.sendToTCP(connection.getID(), new Network.ChatMessage(message, idNum));
    }

    private void sendToClient(Server server, Connection connection, String message){
        server.sendToTCP(connection.getID(), new Network.ChatMessage(message, idNum));
    }

    private void sendStats(){

    }
}
