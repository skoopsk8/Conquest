package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.*;

/**
 * Created by Thomas on 4/4/14.
 */
public class Lobby extends Room {
    public Lobby() {
        super("lobby", 0);
        System.out.println("Lobby successfully created !");
    }

    @Override
    public boolean quit(Connection connection){  // bool for auto destruction
        user.remove(connection);
        return false;
    }
}
