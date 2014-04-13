package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nasser.poulet.conquest.network.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 4/4/14.
 */

/*
TODO :
 * Lobby
 * Game Server Register
 * Chat
 * Chat multi room
 * Host multigame

*/
public class Server {
    private Lobby lobby;
    private List<Room> roomList;
    private com.esotericsoftware.kryonet.Server server;

    public Server(String[] args){
        System.out.println("Starting ...");

        // Create the basic rooms -Lobby
        lobby = new Lobby(server);

        // Create the kryonet server
        server = new com.esotericsoftware.kryonet.Server(){
            protected Connection newConnection () {
                return new GameConnection();
            }
        };
        Network.register(server);

        // Bind the listener
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Network.ChatMessage) {
                    if (((Network.ChatMessage) object).getRoomId()==0)
                        lobby.receivedMessage(((Network.ChatMessage) object).getMessage());
                    else
                        roomList.get(((Network.ChatMessage) object).getRoomId()).receivedMessage(((Network.ChatMessage) object).getMessage());
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

        lobby.receivedMessage("/test 1234");
    }

    public static void main(String[] args){
        new Server(args);
    }

    static class GameConnection extends Connection {
        public String name;
    }
}
