package com.nasser.poulet.conquest.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.nasser.poulet.conquest.model.Board;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Thomas on 1/2/14.
 */
public class ServerConquest{
    private Server server;
    private Board board;

    private boolean[] ready ={false,false};

    public ServerConquest( String address, String port ){
        //Generate the board
        board = new Board(20,15, true);

        server = new Server(){
            protected Connection newConnection () {
                return new GameConnection();
            }
        };

        Network.register(server);

        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Network.SelectMessage) {
                    Network.SelectMessageClient selectMessage = new Network.SelectMessageClient();
                    selectMessage.setPosX(((Network.SelectMessage) object).getPosX());
                    selectMessage.setPosY(((Network.SelectMessage) object).getPosY());
                    selectMessage.setSenderLoyalty(connection.getID()-1);
                    System.out.println("We have a selection in "+selectMessage.getPosX()+";"+selectMessage.getPosY());
                    server.sendToAllExceptTCP(connection.getID(),selectMessage);
                    return;
                }
                if (object instanceof Network.ActionMessage) {
                    Network.ActionMessageClient actionMessage = new Network.ActionMessageClient();
                    actionMessage.setPosX(((Network.ActionMessage) object).getPosX());
                    actionMessage.setPosY(((Network.ActionMessage) object).getPosY());
                    actionMessage.setSenderLoyalty(connection.getID()-1);
                    System.out.println("We have an action in " + actionMessage.getPosX() + ";" + actionMessage.getPosY());
                    server.sendToAllExceptTCP(connection.getID(),actionMessage);
                    return;
                }
                if (object instanceof Network.RequestBoard) {
                    Network.SyncBoard syncBoard = new Network.SyncBoard();
                    syncBoard.setBoard(board.explodeBoard());
                    syncBoard.setProductivity(board.explodeProductivity());
                    syncBoard.setWidth(board.getBoardWidth());
                    syncBoard.setHeight(board.getBoardHeight());
                    server.sendToTCP(connection.getID(),syncBoard);
                    return;
                }

                if (object instanceof Network.SetReady) {
                    ready[connection.getID()-1]=true;
                    System.out.println("Got a ready from " + connection.getID());
                    if(ready[0] && ready[1]){
                        System.out.println("Send Start");
                        server.sendToAllTCP(new Network.Start());
                    }
                    return;
                }
            }
        });

        try {
            server.bind(Network.port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.start();
    }

    static class GameConnection extends Connection {
        public String name;
    }

    public void run() {

}
    
    public void close(){
        server.close();
    }
}
