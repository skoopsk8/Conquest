package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.Connection;
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

    public Room( String roomName, int id){
        this.roomName = roomName;
    }

    public void join(){

    }

    public void receiveMessage(String message){
        System.out.println("New message form ");
    }
}
