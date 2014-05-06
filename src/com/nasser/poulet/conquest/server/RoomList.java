package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 5/5/14.
 */
public class RoomList {
    private List<Room> roomList = new ArrayList<Room>();

    public void add(Room room){
        if(!exist(room))
            roomList.add(room);
    }

    public Room getRoom(String roomName){
        for(Room rooms: roomList){
            if(rooms.getRoomName().equals(roomName))
                return rooms;
        }
        return null;
    }

    private boolean exist(Room room){
        for(Room rooms: roomList){
            if(rooms.getRoomName().equals(room.getRoomName()))
                return true;
        }
        return  false;
    }

    public Room isClientOf(Connection connection){
        for(Room rooms: roomList){
            if(rooms.isClientOf(connection))
                return rooms;
        }
        return null;
    }

    public void removeRoom(Room room){
        roomList.remove(room);
    }

    public void addClient(com.esotericsoftware.kryonet.Server server, Connection connection, String roomName){
        for(Room rooms: roomList){
            if(rooms.getRoomName().equals(roomName))
                rooms.addClient(connection, server);
        }
    }

    public String getRoomList(){
        StringBuilder builder = new StringBuilder();

        for (Room room : roomList) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(room.getRoomName());
        }

        return builder.toString();
    }

    public String getClientList(Connection connection){
        return isClientOf(connection).getClientList();
    }

    public void checkActiveClient(){

    }
}
