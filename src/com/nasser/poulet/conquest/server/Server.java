package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nasser.poulet.conquest.model.Game;
import com.nasser.poulet.conquest.network.Crypt;
import com.nasser.poulet.conquest.network.Network;
import com.nasser.poulet.conquest.server.chat.ChatMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 4/4/14.
 */

/*
TODO :
 V Lobby
 * Game Server Register
 V Chat
 * Chat multi room
 * Host multigame

*/
public class Server {
    private Lobby lobby;
    private RoomList roomList = new RoomList();
    private GameContainer gameList = new GameContainer();
    private com.esotericsoftware.kryonet.Server server;
    public static String apikey = "";

    public Server(String[] args){

        System.out.println("Parsing arguments ...");
        int i=0;
        for (String s: args) {
            if(s.equals("-apikey"))
                apikey = args[i+1];
            i++;
        }

        System.out.println("Starting ...");

        // Reset all connections in db
        try {
            HTTP.sendPost("reset_connections", "api_key=" + Server.apikey);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // Create the basic rooms -Lobby
        lobby = new Lobby();

        // Add the lobby to the roomList
        roomList.add(lobby);

        // Create the kryonet server
        server = new com.esotericsoftware.kryonet.Server(){
            protected Connection newConnection () {
                System.out.println("New client");
                return new GameConnection();
            }
        };
        Network.register(server);

        // Bind the listener
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Network.ChatMessage) {
                    Room connectionRoom = roomList.isClientOf(connection);
                    if(connection != null){
                        ChatMessage command = connectionRoom.receivedMessage((Network.ChatMessage)object, server, connection);
                        if(command != null){
                            parseCommand(command, connectionRoom, connection, server);
                        }
                    }
                }

                if (object instanceof Network.sendCredentials) {
                    try {
                        if(HTTP.sendPost("userLogin", "api_key="+apikey+"&username="+((Network.sendCredentials) object).getUsername()+"&password="+((Network.sendCredentials) object).getPassword()).equals("true")){
                            server.sendToTCP(connection.getID(), new Network.CredentialsValidation(true));
                            ((GameConnection)connection).name = ((Network.sendCredentials) object).getUsername();
                        }
                        else
                            server.sendToTCP(connection.getID(), new Network.CredentialsValidation(false));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(object instanceof Network.RegisterClient){
                    roomList.addClient(server, connection, "lobby");
                    ((GameConnection) connection).setCurrentRoom(roomList.getRoom("lobby"));
                }

                if(object instanceof Network.game_client_action){
                    ((GameConnection) connection).getGame().getMessageFromClient(object, connection);
                }
            }
        });

        // Bind the server
        try {
            System.out.println("Start binding ...");
            server.bind(Network.port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.start();
        System.out.println("Server up & running");

        gameList.runGames();
    }

    private void parseCommand(ChatMessage command, Room connectionRoom, Connection connection, com.esotericsoftware.kryonet.Server server){
        if(command.getCommand().equals("join")){    // join another chat room
            if(connectionRoom.quit(connection))
                roomList.removeRoom(connectionRoom);
            roomList.add(new Room(command.getMessage(), 1));
            ((GameConnection) connection).getCurrentRoom().removeClient(connection);
            roomList.addClient(server, connection, command.getMessage());
            ((GameConnection) connection).setCurrentRoom(roomList.getRoom(command.getMessage()));
        }
        else if(command.getCommand().equals("list")){   // list all the current chat room
            server.sendToTCP(connection.getID(), new Network.ChatMessage(roomList.getRoomList(),connectionRoom.getIdNum()));
        }
        else if(command.getCommand().equals("who")){    // list all the players
            server.sendToTCP(connection.getID(), new Network.ChatMessage(connectionRoom.getClientList(),connectionRoom.getIdNum()));
        }
        else if(command.getCommand().equals("create")){
            server.sendToTCP(connection.getID(), new Network.ChatMessage(gameList.createNewGame(server, connection), connectionRoom.getIdNum()));
        }
        else if(command.getCommand().equals("joingame")){
            server.sendToTCP(connection.getID(), new Network.ChatMessage(gameList.addPlayerToGame(connection, command.getMessage()),connectionRoom.getIdNum()));
        }
        else if(command.getCommand().equals("setready")){
            ((GameConnection)connection).getGame().setPlayerReady(connection);
        }
    }

    public static void main(String[] args){
        new Server(args);
    }

    public void checkActiveClient(){
        roomList.checkActiveClient();
    }
}
