package com.nasser.poulet.conquest.server.chat;

import com.esotericsoftware.kryonet.Server;

/**
 * Created by Thomas on 4/4/14.
 */
public class Chat {
    private Server server;

    public Chat(Server server){
        this.server=server;
    }

    public void sendChat(){

    }

    public void decodeChat( String message ){
        ChatMessage chatMessage = new ChatMessage(message);
        chatMessage.decode();
        if(!chatMessage.isCommand()){
            server.sendToAllTCP(chatMessage.getMessage());
        }
    }
}
