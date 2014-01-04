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

    }

    @Override
    public void start() {

    }
}
