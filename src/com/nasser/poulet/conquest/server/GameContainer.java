package com.nasser.poulet.conquest.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.nasser.poulet.conquest.model.Game;
import com.nasser.poulet.conquest.network.Network;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thomas on 5/6/14.
 */
public class GameContainer {
    GameIdentGenerator generator = new GameIdentGenerator();

    private Map<String,Game> games = new HashMap<String,Game>();

    public String createNewGame(Server server, Connection connections){
        String ident = generator.generateGameIdent();

        games.put(ident,new Game(connections, server, ident));   // Create a new game

        return ident;
    }

    public String addPlayerToGame(Connection connection, String ident){
        if(!games.get(ident).isActive()){   // Check that it's a good ident ;)
            games.get(ident).addPlayer(connection);
            return "You are now connected to the game, GL HF";
        }
        else
            return "Sorry, the game is currently active";
    }

    public void sendClickToGame(Connection connection, Object object){
        /*if(!games.get(((Network.game_client_action)object).gameIdent).isActive()){
            games.get(((Network.game_client_action)object).gameIdent).getMessageFromClient(object, connection);
        }*/
    }

    public void runGames(){
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                for (Map.Entry<String, Game> entry : games.entrySet()){
                    if(entry.getValue().playerLeft()){
                        if(entry.getValue().isActive())
                            entry.getValue().nextTick();
                    }
                    else
                       games.remove(entry.getKey());
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
}
