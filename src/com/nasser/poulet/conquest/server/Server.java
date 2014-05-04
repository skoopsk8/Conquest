package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nasser.poulet.conquest.network.Crypt;
import com.nasser.poulet.conquest.network.Network;

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
    private List<Room> roomList = new ArrayList<Room>();
    private com.esotericsoftware.kryonet.Server server;

    public Server(String[] args){
        System.out.println("Starting ...");

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
                    for(Room room:roomList){
                        if(room.isClientOf(connection)){
                            room.receivedMessage((Network.ChatMessage)object, server, connection);
                        }
                    }
                }

                if (object instanceof Network.sendCredentials) {
                    if(((Network.sendCredentials) object).getUsername().equals("Lord_Nazdar") && ((Network.sendCredentials) object).getPassword().equals(Crypt.encrypt("aze123")))
                        server.sendToTCP(connection.getID(), new Network.CredentialsValidation(true));
                    else
                        server.sendToTCP(connection.getID(), new Network.CredentialsValidation(false));
                }

                if(object instanceof Network.RegisterClient){
                    roomList.get(0).addClient(connection);
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
    }

    public static void main(String[] args){
        new Server(args);
    }

    static class GameConnection extends Connection {
        public String name;
    }
}
