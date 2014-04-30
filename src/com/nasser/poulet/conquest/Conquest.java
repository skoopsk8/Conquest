package com.nasser.poulet.conquest;
/**
 * Created by Lord on 10/12/13.
 */

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nasser.poulet.conquest.menu.Menu;
import com.nasser.poulet.conquest.network.Network;
import com.nasser.poulet.conquest.player.*;
import com.nasser.poulet.conquest.controller.Turn;
import com.nasser.poulet.conquest.model.*;
import com.nasser.poulet.conquest.network.ClientConquest;
import com.nasser.poulet.conquest.network.ServerConquest;
import com.nasser.poulet.conquest.view.Image;
import com.nasser.poulet.conquest.view.RenderBoard;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.ImageIOImageData;

public class Conquest {
    private boolean fullscreen;
    private boolean noClick = true;

    private ServerConquest server;
    private ClientConquest client;

    public Board mainBoard;

    private DisplayMode[] modes = null;
    private int activeMode = 7;

    Player[] players = new Player[3];

    Menu[] menus = new Menu[4]; // Menu preloader

    public static void main(String[] args){
        new Conquest(args);
    }

    public Conquest(String[] args){
        // Var init
        int i=0;
        boolean noSplash = false;

        // Arguments
        for (String s: args) {
            if(s.equals("-fullscreen"))
                this.fullscreen = true; // Not Working
            else if(s.equals("-noSplash"))
                noSplash = true;
            else if(s.equals("-port"))
                Network.port = Integer.parseInt(args[i+1]);

             i++;
        }

        // Set the display
        initializeDisplay();

        // Preload Menu
        menus[0] = new Menu("mainMenu");
        menus[1] = new Menu("play");
        menus[2] = new Menu("multiplayer");
        menus[3] = new Menu("settings");


        // Splash Screen
        if(!noSplash){
            Menu splash = this.startMenu("splash");
            while(splash.action!="continue"){splash.render();}
        }

        // Menu
        Menu mainMenu = menus[0];
        do {
            if (mainMenu.action.equals("play"))
                playMenu();
            else if (mainMenu.action.equals("multiplayer"))
                multiplayerMenu();
            else if (mainMenu.action.equals("settings"))
                settingsMenu();

            mainMenu.render();
        }while (!mainMenu.action.equals("quit"));

        // Display destroy & free
        Display.destroy();
    }

    private void playMenu(){
        Menu playMenu = menus[1];
        do {
            playMenu.render();
            if(playMenu.action.equals("blue")){
                players[0] = new Human(Loyalty.BLUE,mainBoard);
                players[1] = new IA(Loyalty.GREEN,mainBoard);
                players[2] = new IA(Loyalty.YELLOW,mainBoard);
            }
            else if(playMenu.action.equals("green")){
                players[0] = new Human(Loyalty.GREEN,mainBoard);
                players[1] = new IA(Loyalty.BLUE,mainBoard);
                players[2] = new IA(Loyalty.YELLOW,mainBoard);
            }
            else if(playMenu.action.equals("yellow")){
                players[0] = new Human(Loyalty.YELLOW,mainBoard);
                players[1] = new IA(Loyalty.GREEN,mainBoard);
                players[2] = new IA(Loyalty.BLUE,mainBoard);
            }
        }while (playMenu.action.equals(""));
        if(!playMenu.action.equals("quit")){
            mainBoard = null;
            mainBoard = new Board(20, 15, true);
            this.startGame(players);
        }
    }

    private void settingsMenu(){
        Menu settingsMenu = menus[3];
        settingsMenu.updateText(modes[activeMode].toString(),"resolution");
        do {
            if(settingsMenu.action.equals("resolution")){
                if(activeMode+1 < modes.length)
                    activeMode++;
                else
                    activeMode=0;
                settingsMenu.updateText(modes[activeMode].toString(),"resolution");
            }
            settingsMenu.render();
        }while (!settingsMenu.action.equals("save"));

        // Change the resolution
        Display.destroy();
        initializeDisplay();
        initializeOpenGL(Display.getWidth(), Display.getHeight());

        Image.destroy();

        for (Menu menu: menus){
            menu.reload();
        }
    }

    private void multiplayerMenu(){
        Menu multiplayerMenu  = menus[2];
        do {
            if(multiplayerMenu.action.equals("host")){
                // Create the server and connect the user client
                this.initServer();
                this.initClient("127.0.0.1");
                this.startMultiplayerGame();
            }
            else if(multiplayerMenu.action.equals("join")){
                String[] str = {"192.168.1.1","123"};
                // Launch multiplayer session

                if(str.length==1)
                    Network.port = 54555;
                else
                    Network.port = Integer.parseInt(str[1]);

                this.initClient(str[0]);
                this.startMultiplayerGame();
            }
            multiplayerMenu.render();
        }while (!multiplayerMenu.action.equals("quit"));
    }

    private void initializeDisplay(){
        try {
            System.out.println("Launching ...");
            modes = Display.getAvailableDisplayModes();
            Display.setDisplayMode(modes[activeMode]);
            if(this.fullscreen){
                System.out.println("Fullscreen");
                Display.setFullscreen(true);
            }
            Display.setTitle("Conquest");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // we set the icon
        try {
            Display.setIcon(new ByteBuffer[] {
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("data/img/icon16.png")), false, false, null),
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("data/img/icon32.png")), false, false, null),
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("data/img/icon128.png")), false, false, null)
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Change the draw ration
        this.initializeOpenGL(Display.getWidth(),Display.getHeight());    // Launch OpenGL
    }

    public Action pollInput(){
        while(Keyboard.next()){
            if(Keyboard.getEventKeyState()) {   // Only pressed keys
                if(Keyboard.getEventKey() == Keyboard.KEY_SPACE){
                   //this.turn.setPause(!this.turn.isPause());
                }
                if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
                    return Action.ECHAP;
                }
            }
        }
        if(Mouse.isButtonDown(0) && noClick){
            noClick = false;
            return Action.MOUSE;
        }
        if(!Mouse.isButtonDown(0)){
            noClick = true;
        }
        return Action.NONE;
    }

    private void initializeOpenGL( int width, int height ){
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private Menu startMenu( String filename ){
        Menu menu = new Menu(filename);
        return menu;
    }

    private void startLobby(){
        final Menu menu = new Menu("lobby");
        client.getClient().addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Network.Start) {
                    menu.action = "continue";
                    return;
                }
            }
        });
        client.sendReady();
        do {
            if(menu.action.equals("forceStart")){
                client.sendForceStart();
                client.sendReady();
            }
            menu.render();
        }while (!menu.action.equals("continue"));
    }


    private boolean wait =true;

    int[][] board, prod;
    int width, height;

    private void startMultiplayerGame(){
        mainBoard = null;
        mainBoard = new Board(20, 15, false);

        client.getClient().addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Network.SyncBoard) {
                    Network.SyncBoard syncBoard = (Network.SyncBoard)object;
                    width= syncBoard.getWidth();
                    height=syncBoard.getHeight();
                    board=syncBoard.getBoard();
                    prod = syncBoard.getProductivity();
                    return;
                }

                if (object instanceof Network.Start) {
                    wait=false;
                    return;
                }

                if (object instanceof Network.SelectMessageClient) {
                    Network.SelectMessageClient selectMessage = (Network.SelectMessageClient)object;
                    int loyalty = selectMessage.getSenderLoyalty();
                    if(players[loyalty] instanceof MultiplayerRemote)
                        players[loyalty].setSelected(mainBoard.getState(selectMessage.getPosX(), selectMessage.getPosY()));
                    return;
                }

                if (object instanceof Network.ActionMessageClient) {
                    Network.ActionMessageClient actionMessage = (Network.ActionMessageClient)object;
                    int loyalty = actionMessage.getSenderLoyalty();
                    if(players[loyalty] instanceof MultiplayerRemote){
                        if(players[loyalty].getLoyalty()!=null){
                            players[loyalty].action(actionMessage.getPosX(), actionMessage.getPosY());
                        }
                    }
                    return;
                }
            }
        });

        client.sendSyncRequest();

        startLobby();


        System.out.println(client.getClient().getID());
        players[client.getClient().getID()-1] = new Multiplayer(Loyalty.values()[client.getClient().getID()+1], mainBoard, client.getClient());
        for(int i=0; i<3; i++){
            if(players[i] == null)
                players[i] = new MultiplayerRemote(Loyalty.values()[i], mainBoard, client.getClient());
        }
        mainBoard.format(width, height, board, prod);
        this.startGame(players);

        client.close();
        if(server!=null) server.close();
    }

    private void startGame( Player[] players ){
        RenderBoard renderer = new RenderBoard();
        Human human = null;
        IA remote1 = null;
        IA remote2 = null;

        Turn turn = new Turn();

        for(Player player: players){
            if(player instanceof Human){
                human = (Human)player;
            }
            if(player instanceof IA){
                if(remote1 == null)
                    remote1 = (IA)player;
                else if(remote2 == null)
                    remote2 = (IA)player;
            }
        }

        int currentTurn = 1;
        Action inputAction;

        // Have to stay just before the while
        Board.numberOfUnit[0]=Board.numberOfUnit[1]=Board.numberOfUnit[2]=0;
        turn.startTurn();
        while( this.endGame(mainBoard, turn.getTurnNumber()) && !Display.isCloseRequested()){
            // Clear display
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
            GL11.glClear (GL11.GL_COLOR_BUFFER_BIT);

            inputAction = this.pollInput();
            if(inputAction == Action.MOUSE)
                human.click(Mouse.getX(), (-Mouse.getY() + 600));
            else if(inputAction == Action.ECHAP)
                human.abort();
            turn.update();
            if(turn.getTurnNumber() != currentTurn) {
            	human.update();
                remote1.update();
                remote2.update();
            	currentTurn = turn.getTurnNumber();
            }
            
            renderer.render(mainBoard);
            Display.update();
        }

        System.out.println("The winner is " + this.getWinner(mainBoard));

        turn.stop();
    }

    private int triggerTurn = -1;

    private String getWinner( Board board ){
        return board.getwinner();
    }

    private boolean endGame( Board board, int turnNumber){
        /*if(board.numberOfEmpty() == 0 && triggerTurn == -1){ // Start end game
            triggerTurn = turnNumber;
            System.out.println("Start end counter");
        }

        if(triggerTurn != -1 && triggerTurn + (60000/Turn.TURN_DURATION)<turnNumber){
            return false;
        }*/

        return true;
    }

    private enum Action{
        NONE,
        KEYBOARD,
        ECHAP,
        MOUSE
    }

    private void initServer(){
        server =  new ServerConquest();
    }

    private void initClient( String address ){
        client = new ClientConquest(address);
    }
}