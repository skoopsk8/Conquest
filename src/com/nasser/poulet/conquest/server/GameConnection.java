package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.Connection;
import com.nasser.poulet.conquest.model.Game;
import com.nasser.poulet.conquest.network.Network;

/**
 * Created by Thomas on 5/7/14.
 */
public class GameConnection extends Connection {
    public String name;
    private Room currentRoom;
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    @Override
    public void close(){
        try {
            HTTP.sendPost("logout", "api_key="+Server.apikey+"&username="+name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(currentRoom!=null){
            System.out.println("The client "+name+" disconnected");
            currentRoom.removeClient(this);
        }
        super.close();
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}