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
    private boolean ready = false;

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public MultiplayerRemote(Loyalty loyalty, Board board) {
        super(loyalty, board);

    }

    @Override
    public void start() {

    }
}
