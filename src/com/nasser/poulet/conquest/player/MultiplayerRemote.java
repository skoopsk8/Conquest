package com.nasser.poulet.conquest.player;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.Loyalty;
import com.nasser.poulet.conquest.network.Network;

/**
 * Created by Thomas on 1/2/14.
 */
public class MultiplayerRemote extends IA {
    Client client;
    public MultiplayerRemote(Loyalty loyalty, Board board, Client client) {
        super(loyalty, board);
        this.client = client;

        this.addListener();
    }


    private void addListener(){
        client.addListener(new Listener() {
            public void connected (Connection connection) {
                System.out.println("Connected");
            }

            public void received (Connection connection, Object object) {
                if (object instanceof Network.SelectMessageClient) {
                    abort();
                    Network.SelectMessageClient selectMessage = (Network.SelectMessageClient)object;
                    selected=boardController.getBoard().getState(selectMessage.getPosX(), selectMessage.getPosY());
                    return;
                }
                if (object instanceof Network.ActionMessageClient) {
                    Network.ActionMessageClient actionMessage = (Network.ActionMessageClient)object;
                    if(selected!=null)
                        System.out.println("Call action with "+client.getID());
                    action(actionMessage.getPosX(), actionMessage.getPosY());
                    return;
                }
            }

            public void disconnected (Connection connection) {
                System.out.println("Disconnected!");
            }
        });
    }

    @Override
    public void start() {

    }
}
