package com.nasser.poulet.conquest.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.sun.swing.internal.plaf.synth.resources.synth_sv;

import java.io.IOException;

/**
 * Created by Thomas on 1/2/14.
 */
public class ClientConquest{
    private Client client;

    public ClientConquest( String address ) throws IOException {
        client = new Client();
        client.start();

        Network.register(client);

        client.addListener(new Listener() {
            public void connected (Connection connection) {
               System.out.println("Connected");
            }

            public void disconnected (Connection connection) {
                System.out.println("Disconnected!");
            }
        });

        client.connect(10000,address,Network.port);
    }

    public void sendSyncRequest(){
        client.sendTCP(new Network.RequestBoard());
    }

    public void sendReady(){
        client.sendTCP(new Network.SetReady());
    }

    public void sendForceStart(){
        client.sendTCP(new Network.forceStart());
    }

    public Client getClient() {
        return client;
    }

    public void sendCredentials(String username, String password){
        System.out.println("send credentials: "+username +" "+Crypt.encrypt(password));
        client.sendTCP(new Network.sendCredentials(username, Crypt.encrypt(password)));
    }

    public void close(){
        client.close();
    }

    public void registerClient(){
        client.sendTCP(new Network.RegisterClient());
    }

    public void sendChat(String message){
    	if(!message.equals("")) {
	        System.out.println("Send message " + message);
	        client.sendTCP(new Network.ChatMessage(message,0));
    	}
    }

    public void sendClick(int fromPosX, int fromPosY, int toPosX, int toPosY){
        client.sendTCP(new Network.game_client_action(fromPosX, fromPosY, toPosX, toPosY));
    }
}
