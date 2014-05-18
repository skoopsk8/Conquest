package com.nasser.poulet.conquest.model;

import com.esotericsoftware.kryonet.Connection;
import com.nasser.poulet.conquest.controller.BoardController;
import com.nasser.poulet.conquest.controller.Turn;
import com.nasser.poulet.conquest.network.Network;
import com.nasser.poulet.conquest.player.MultiplayerRemote;
import com.nasser.poulet.conquest.player.Player;
import com.nasser.poulet.conquest.server.GameConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 5/6/14.
 */
public class Game {
    private BoardController board;

    public boolean isActive() {
        return active;
    }

    private Map<Connection, Player> players = new HashMap<Connection, Player>();
    private int playerNumber = 0;
    private boolean active;
    private com.esotericsoftware.kryonet.Server server;
    private String ident;
    private int turnNumber;

    public Game(Connection connection, com.esotericsoftware.kryonet.Server server, String ident){
        this.server = server;
        this.ident = ident;

        board = new BoardController(new Board(29, 20, true));

        addPlayer(connection);
    }

    public int addPlayer(Connection connection){
        sendToAllClient("A new player as joined the game: " + ((GameConnection)connection).name);
        players.put(connection, new MultiplayerRemote(Loyalty.values()[playerNumber+2], board));
        ((GameConnection)connection).setGame(this);
        playerNumber++;
        sendToClient(connection,new Network.lobby_server_connected());

        return playerNumber;
    }

    private boolean isEveryBodyReady(){
        for(Map.Entry<Connection, Player> player : players.entrySet()){
            if(!((MultiplayerRemote)player.getValue()).isReady())
                return false;
        }
        return true;
    }

    public void setPlayerReady(Connection connection){
    	// TODO: Check the user is in a game to setready (else server crash)
        ((MultiplayerRemote)players.get(connection)).setReady(true);
        if(isEveryBodyReady()){
            active = true;

            board.getBoard().numberOfUnit[0]=board.getBoard().numberOfUnit[1]=board.getBoard().numberOfUnit[2]=0;

            startGame();
        }
    }

    public void startGame(){
        // Inform the players about the game start
        sendToAllClient(new Network.ChatMessage("Starting the game, please wait...", 0));

        Network.game_server_startGame startGame = new Network.game_server_startGame();

        startGame.width = board.getBoard().getBoardWidth();
        startGame.height = board.getBoard().getBoardHeight();
        startGame.board = board.getBoard().explodeBoard();
        startGame.productivity = board.getBoard().explodeProductivity();

        for(Map.Entry<Connection, Player> player : players.entrySet()){
            startGame.Loyalty = player.getValue().getLoyalty().ordinal();

            sendToClient(player.getKey(), startGame);
        }

        active = true;
    }

    public void nextTick(){
        turnNumber++;
        board.getBoard().updateEvents();

        for(Map.Entry<Connection, Player> player : players.entrySet()){
            player.getValue().update();
        }

        Network.game_server_sendBoardSync boardSync = new Network.game_server_sendBoardSync();
        Network.game_server_sendBoardSyncUnit boardSyncUnit = new Network.game_server_sendBoardSyncUnit();

        boardSync.width = boardSyncUnit.width = board.getBoard().getBoardWidth();
        boardSync.height = boardSyncUnit.height = board.getBoard().getBoardHeight();
        boardSync.board = board.getBoard().explodeBoard();
        boardSync.productivity = board.getBoard().explodeProductivity();
        boardSyncUnit.units = board.getBoard().explodeUnits();
        boardSync.turn = turnNumber;

        sendToAllClient(boardSync);
        sendToAllClient(boardSyncUnit);

        board.checkEndGame();

        if(board.isEndGameTimer()){
            if(board.getWinnerLoyalty()!=null){
                Network.game_server_endgame endmessage = new Network.game_server_endgame();
                endmessage.loyalty = board.getWinnerLoyalty().toString();
                sendToAllClient(endmessage);
            }
        }
    }

    public void getMessageFromClient(Object object, Connection connection){
        if(object instanceof Network.game_client_action){  // The client wants to move a unit
            if(players.get(connection).select(((Network.game_client_action) object).fromPosX, ((Network.game_client_action) object).fromPosY))  // The player can move this unit
                players.get(connection).action(((Network.game_client_action) object).toPosX, ((Network.game_client_action) object).toPosY);
        }
    }

    private void sendToAllClient(Object message){
        for(Map.Entry<Connection, Player> player : players.entrySet())
            server.sendToTCP(player.getKey().getID(), message);
    }

    private void sendToClient(Connection connection, Object message){
        server.sendToTCP(connection.getID(), message);
    }

    public boolean playerLeft(){    // return if there is any player left in the game
        for(Map.Entry<Connection, Player> player : players.entrySet()){
            if(player.getKey().isConnected())
                return true;
        }
        return false;
    }

}
