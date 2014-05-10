package com.nasser.poulet.conquest.model;

import com.esotericsoftware.kryonet.Connection;
import com.nasser.poulet.conquest.controller.Turn;
import com.nasser.poulet.conquest.network.Network;
import com.nasser.poulet.conquest.player.MultiplayerRemote;
import com.nasser.poulet.conquest.player.Player;
import com.nasser.poulet.conquest.server.GameConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 5/6/14.
 */
public class Game {
    private Board board;

    public boolean isActive() {
        return active;
    }

    private Map<Connection, Player> players = new HashMap<Connection, Player>();
    private boolean active;
    private com.esotericsoftware.kryonet.Server server;
    private String ident;
    private int turnNumber;
    private Turn turn = new Turn();

    public Game(Connection connection, com.esotericsoftware.kryonet.Server server, String ident){
        this.server = server;
        this.ident = ident;

        board = null;
        board = new Board(29, 20, true);

        addPlayer(connection);
    }

    public int addPlayer(Connection connection){
        players.put(connection, new MultiplayerRemote(Loyalty.values()[players.values().size()+2], board));
        ((GameConnection)connection).setGame(this);

        return players.size()-1;
    }

    private boolean isEveryBodyReady(){
        for(Map.Entry<Connection, Player> player : players.entrySet()){
            if(!((MultiplayerRemote)player.getValue()).isReady())
                return false;
        }
        return true;
    }

    public void setPlayerReady(Connection connection){
        ((MultiplayerRemote)players.get(connection)).setReady(true);
        if(isEveryBodyReady()){
            active = true;

            int index = 0;
            Board.numberOfUnit[0]=Board.numberOfUnit[1]=Board.numberOfUnit[2]=0;

            startGame();
        }
    }

    public void startGame(){
        // Inform the players about the game start
        sendToAllClient(new Network.ChatMessage("Starting the game, please wait...", 0));

        Network.game_server_startGame startGame = new Network.game_server_startGame();

        startGame.width = board.getBoardWidth();
        startGame.height = board.getBoardHeight();
        startGame.board = board.explodeBoard();
        startGame.productivity = board.explodeProductivity();

        sendToAllClient(startGame);

        active = true;

        turn.startTurn();
    }

    public void nextTick(){
        // New tick in the game
        turnNumber++;
        //turn.startTurn();
        turn.update();

        for(Map.Entry<Connection, Player> player : players.entrySet()){
            player.getValue().update();
        }

        Network.game_server_sendBoardSync boardSync = new Network.game_server_sendBoardSync();
        Network.game_server_sendBoardSyncUnit boardSyncUnit = new Network.game_server_sendBoardSyncUnit();

        boardSync.width = boardSyncUnit.width = board.getBoardWidth();
        boardSync.height = boardSyncUnit.height = board.getBoardHeight();
        boardSync.board = board.explodeBoard();
        boardSync.productivity = board.explodeProductivity();
        boardSyncUnit.units = board.explodeUnits();
        boardSync.turn = turnNumber;

        sendToAllClient(boardSync);
        sendToAllClient(boardSyncUnit);
    }

    public void getMessageFromClient(Object object, Connection connection){
        if(object instanceof Network.game_client_action){  // The client wants to move a unit
            System.out.println("Move a unit from: "+((Network.game_client_action) object).fromPosX + ";"+((Network.game_client_action) object).fromPosY);
            System.out.println("Move a unit to: "+((Network.game_client_action) object).toPosX + ";"+((Network.game_client_action) object).toPosY);
            if(players.get(connection).select(((Network.game_client_action) object).fromPosX, ((Network.game_client_action) object).fromPosY)){   // The player can move this unit
                players.get(connection).action(((Network.game_client_action) object).toPosX, ((Network.game_client_action) object).toPosY);
                System.out.println("--------- Move this unit");
            }
        }
    }

    private void sendToAllClient(Object message){
        for(Map.Entry<Connection, Player> player : players.entrySet())
            server.sendToTCP(player.getKey().getID(), message);
    }

    private void sendToClient(Connection connection, Object message){
        server.sendToTCP(connection.getID(), message);
    }

}
