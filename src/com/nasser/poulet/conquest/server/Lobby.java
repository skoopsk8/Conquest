package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.*;

/**
 * Created by Thomas on 4/4/14.
 */
public class Lobby extends Room {
    public Lobby() {
        super("Lobby", 0);
        System.out.println("Lobby successfully created !");
    }
}
