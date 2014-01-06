package com.nasser.poulet.conquest.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Created by Thomas on 1/2/14.
 */
public class Network {
    static public int port = 54555;

    // Prepare the objects for serialization
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(SelectMessage.class);
        kryo.register(ActionMessage.class);
        kryo.register(SelectMessageClient.class);
        kryo.register(ActionMessageClient.class);
        kryo.register(int[][].class);
        kryo.register(int[].class);
        kryo.register(SyncBoard.class);
        kryo.register(RequestBoard.class);
        kryo.register(SetReady.class);
        kryo.register(Start.class);
        kryo.register(forceStart.class);
    }

    static public class SelectMessage{
        private int posX, posY;

        public int getPosX() {
            return posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public int getPosY() {
            return posY;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }
    }   // Unit selection

    static public class ActionMessage{
        private int posX, posY;

        public int getPosX() {
            return posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public int getPosY() {
            return posY;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }
    }   // Unit Action

    static public class SelectMessageClient{
        private int posX, posY, senderLoyalty;

        public int getPosX() {
            return posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public int getPosY() {
            return posY;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }

        public int getSenderLoyalty() {
            return senderLoyalty;
        }

        public void setSenderLoyalty(int senderLoyalty) {
            this.senderLoyalty = senderLoyalty;
        }
    }   // Unit selection

    static public class ActionMessageClient{
        private int posX, posY, senderLoyalty;

        public int getPosX() {
            return posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public int getPosY() {
            return posY;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }

        public int getSenderLoyalty() {
            return senderLoyalty;
        }

        public void setSenderLoyalty(int senderLoyalty) {
            this.senderLoyalty = senderLoyalty;
        }
    }   // Unit Action

    static public class SyncBoard{
        private int[][] board, productivity;
        private int width, height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int[][] getBoard() {
            return board;
        }

        public void setBoard(int[][] board) {
            this.board = board;
        }

        public int[][] getProductivity() {
            return productivity;
        }

        public void setProductivity(int[][] productivity) {
            this.productivity = productivity;
        }
    }       // Board Synchronisation

    static public class RequestBoard{}       // Board Request

    static public class SetReady{}           // Rise Ready Flag

    static public class Start{}              // Rise Start Flag

    static public class forceStart{}              // Rise Force Start Flag
}
