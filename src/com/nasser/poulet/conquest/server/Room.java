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
    protected ArrayList<Connection> user = new ArrayList<Connection>();
    protected ArrayList<ChatMessage> messageList = new ArrayList<ChatMessage>();
    protected String roomName;
    public static int idNum = 0;

    public String getRoomName() {
        return roomName;
    }

    public Room( String roomName, int id){
        this.roomName = roomName;
        idNum = id;

    }

    public ChatMessage receivedMessage(Network.ChatMessage object, Server server, Connection connection){
        System.out.println("New message from " + connection.getID());
        messageList.add(new ChatMessage(object.getMessage()));

        Date t1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        if(!messageList.get(messageList.size()-1).isCommand()){
            sendToAllClient(server, "["+df.format(t1)+" - "+roomName+"]"+ messageList.get(messageList.size()-1).getMessage());
            return null;
        }
        else{
            sendToClient(server, connection, "[Server]"+messageList.get(messageList.size() - 1).getAnswer());
            return messageList.get(messageList.size()-1);
        }
    }

    public void addClient(Connection connection, Server server){
        sendToAllClient(server, "[Server] A new client joined your room");
        user.add(connection);
    }

    public static int getIdNum() {
        return idNum;
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

    public boolean quit(Connection connection){  // bool for auto destruction
        user.remove(connection);
        if(user.size()==0)
            return true;
        return false;
    }
}
