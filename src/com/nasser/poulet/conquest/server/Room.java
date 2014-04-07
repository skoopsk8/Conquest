package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Server;
import com.nasser.poulet.conquest.server.chat.Chat;

import java.util.ArrayList;

/**
 * Created by Thomas on 4/4/14.
 */
public class Room {
    private ArrayList<Connection> Player;
    private String roomName;
    private Chat chat = null;
    public static int idNum = 0;
    private com.esotericsoftware.kryonet.Server server = null;

    public Room( com.esotericsoftware.kryonet.Server server, String roomName, int id){
        this.roomName = roomName;
        this.server = server;

        chat = new Chat(server);
    }

    public void join(){

    }

    public void receivedMessage(String message){
        System.out.println("New message form ");
        chat.decodeChat(message);
    }
}
