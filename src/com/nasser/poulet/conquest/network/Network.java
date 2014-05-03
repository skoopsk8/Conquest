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
        kryo.register(ChatMessage.class);
        kryo.register(CredentialsValidation.class);
        kryo.register(sendCredentials.class);
        kryo.register(RegisterClient.class);
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

    static public class ChatMessage{
        private String message;
        private int roomId;

        public ChatMessage() {}

        public ChatMessage(String message, int roomId) {
            this.message = message;
            this.roomId = roomId;
        }

        public String getMessage(){
            return message;
        }

        public void setMessage( String message ){
            this.message = message;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }
    }

    static public class RequestBoard{}       // Board Request

    static public class SetReady{}           // Rise Ready Flag

    static public class Start{}              // Rise Start Flag

    static public class forceStart{}              // Rise Force Start Flag

    static public class CredentialsValidation{
        private boolean validation;

        public CredentialsValidation(){}

        public CredentialsValidation(boolean validation){
            this.validation = validation;
        }

        public boolean isValidation() {
            return validation;
        }

        public void setValidation(boolean validation) {
            this.validation = validation;
        }
    }

    static public class sendCredentials{
        private String username, password;

        public sendCredentials(){

        }

        public sendCredentials(String username, String password){
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    static public class RegisterClient{

    }
}
