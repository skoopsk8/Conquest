package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Server;
import com.nasser.poulet.conquest.network.Network;
import com.nasser.poulet.conquest.server.chat.Chat;
import com.nasser.poulet.conquest.server.chat.ChatMessage;

import java.util.ArrayList;
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

        if(!messageList.get(messageList.size()-1).isCommand())
            sendToAllClient(server, messageList.get(messageList.size()-1).getMessage());
        else
            sendToClient(server, connection, messageList.get(messageList.size() - 1).getAnswer());
    }

    public void addClient(Connection connection){
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
