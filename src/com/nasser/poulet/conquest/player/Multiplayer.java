package com.nasser.poulet.conquest.player;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.Loyalty;
import com.nasser.poulet.conquest.network.Network;
import com.nasser.poulet.conquest.player.Human;

import java.io.IOException;

/**
 * Created by Thomas on 12/28/13.
 */
public class Multiplayer extends Human {
    Client client;

    public Multiplayer(Loyalty loyalty, Board board, Client client) {
        super(loyalty, board);
        this.client = client;
    }

    private void sendToServerSelect( int posX, int posY ){
        Network.SelectMessage selectMessage = new Network.SelectMessage();
        selectMessage.setPosX(posX);
        selectMessage.setPosY(posY);
        System.out.println("Ready to send select ("+selectMessage.getPosX()+";"+selectMessage.getPosY()+")");
        client.sendTCP(selectMessage);
    }

    private void sendToServerAction( int posX, int posY ){
        Network.ActionMessage actionMessage = new Network.ActionMessage();
        actionMessage.setPosX(posX);
        actionMessage.setPosY(posY);
        client.sendTCP(actionMessage);
    }

    @Override
    public void start() {

    }

    @Override
    public boolean click( int posX, int posY, int x ){
        posX = (int)Math.floor(posX/40);
        posY = (int)Math.floor(posY/40);
        if(this.selected == null){   // No previous selection
            this.select(posX, posY);
            this.sendToServerSelect(posX, posY);
        }
        else{    // There is a previous selection
            this.action(posX, posY);
            System.out.println("Send action");
            this.sendToServerAction(posX, posY);
        }
        return true;
    }
}
