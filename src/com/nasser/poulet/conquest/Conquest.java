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
import com.nasser.poulet.conquest.controller.Timer;
import com.nasser.poulet.conquest.menu.GameView;
import com.nasser.poulet.conquest.menu.Menu;
import com.nasser.poulet.conquest.network.Network;
import com.nasser.poulet.conquest.player.*;
import com.nasser.poulet.conquest.controller.Turn;
import com.nasser.poulet.conquest.model.*;
import com.nasser.poulet.conquest.network.ClientConquest;
import com.nasser.poulet.conquest.network.ServerConquest;
import com.nasser.poulet.conquest.server.HTTP;
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
    static public String versionNumber = "pre 0.1 alpha";

    private boolean fullscreen;
    private boolean noClick = true;

    private ServerConquest server;
    private ClientConquest client;

    public Board mainBoard;

    private DisplayMode[] modes = null;
    private int activeMode = 7;

    Player[] players = new Player[3];

    Menu[] menus = new Menu[4]; // Menu preloader

    private String serverBrowserIp = "5.135.190.151";
    //private String serverBrowserIp = "127.0.0.1";

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
            splash.updateVariable(0,versionNumber, "version");
            while(splash.action!="continue"){splash.render();}
        }

        // Menu
        Menu mainMenu = menus[0];
        String[] text = null;
        try {
            text = HTTP.getPage("https://raw.githubusercontent.com/skoopsk8/Conquest/master/changelog");
            for(String line: text){
                mainMenu.updateText(line,"changelog");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        text = null;
        try {
            text = HTTP.getPage("https://raw.githubusercontent.com/skoopsk8/Conquest/master/news");
            for(String line: text){
                mainMenu.updateText(line,"news");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                mainBoard = null;
                mainBoard = new Board(29, 20, true);
                players[0] = new Human(Loyalty.BLUE,mainBoard);
                players[1] = new IA(Loyalty.GREEN,mainBoard);
                players[2] = new IA(Loyalty.YELLOW,mainBoard);
            }
            else if(playMenu.action.equals("green")){
                mainBoard = null;
                mainBoard = new Board(29, 20, true);
                players[0] = new Human(Loyalty.GREEN,mainBoard);
                players[1] = new IA(Loyalty.BLUE,mainBoard);
                players[2] = new IA(Loyalty.YELLOW,mainBoard);
            }
            else if(playMenu.action.equals("yellow")){
                mainBoard = null;
                mainBoard = new Board(29, 20, true);
                players[0] = new Human(Loyalty.YELLOW,mainBoard);
                players[1] = new IA(Loyalty.GREEN,mainBoard);
                players[2] = new IA(Loyalty.BLUE,mainBoard);
            }
        }while (playMenu.action.equals(""));
        if(!playMenu.action.equals("quit")){
            this.startGame(players);
        }
    }

    private void settingsMenu(){
        Menu settingsMenu = menus[3];
        settingsMenu.updateText(modes[activeMode].toString(),"resolution");
        settingsMenu.updateText(serverBrowserIp.toString(),"Browser");
        do {
            if(settingsMenu.action.equals("resolution")){
                if(activeMode+1 < modes.length)
                    activeMode++;
                else
                    activeMode=0;
                settingsMenu.updateText(modes[activeMode].toString(),"resolution");
            }
            settingsMenu.render();
            if(settingsMenu.action.equals("cancel"))
                return;
        }while (!settingsMenu.action.equals("save"));

        // Change the resolution
        Display.destroy();
        initializeDisplay();
        initializeOpenGL(Display.getWidth(), Display.getHeight());

        Image.destroy();

        System.out.println(Display.getWidth() + " " + Display.getHeight());

        // Change server Browser IP
        serverBrowserIp = settingsMenu.getText("Browser");

        for (Menu menu: menus){
            menu.reload();
        }
    }

    boolean validation = false, response = false;

    private void multiplayerMenu(){
        Menu multiplayerMenu  = menus[2];
        multiplayerMenu.updateText(serverBrowserIp.toString(),"Current");
        boolean connected = false;

        do {
            if(multiplayerMenu.action.equals("connect")){
                // Contact the server browser
                try {
                    client = new ClientConquest(serverBrowserIp);

                    connected = true;

                    // Send the credentials
                    client.sendCredentials(multiplayerMenu.getText("Username"), multiplayerMenu.getText("Password"));

                    client.getClient().addListener(new Listener() {
                        public void received (Connection connection, Object object) {
                            if (object instanceof Network.CredentialsValidation) {
                                response = true;
                                validation = ((Network.CredentialsValidation) object).isValidation();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    multiplayerMenu.updateText("Failed to reach the given server browser!","Error");
                }
            }

            if(connected){
                if(response){
                    if (validation){
                        lobbyMenu();
                        response = false;
                        connected = false;
                        client.close();
                    }
                    else{
                        multiplayerMenu.updateText("Please check your credentials!","Error");
                        multiplayerMenu.updateText("Password", "Password");
                        response = false;
                        connected = false;
                        client.close();
                    }
                }
            }

            multiplayerMenu.render();
        }while (!multiplayerMenu.action.equals("cancel"));
    }

    public Object startGameData;

    private void lobbyMenu(){
        final Menu lobbyMenu = new Menu("lobby");

        client.registerClient();

        client.getClient().addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Network.ChatMessage) {
                    lobbyMenu.updateText(((Network.ChatMessage) object).getMessage(),"chat");
                }
                if (object instanceof Network.game_server_startGame) {
                    lobbyMenu.action = "start_multiplayer";
                    startGameData = object;
                }
            }
        });

        do {
            if(lobbyMenu.action.equals("send_chat")){
                client.sendChat(lobbyMenu.getText("input_chat"));
            }
            else if(lobbyMenu.action.equals("start_multiplayer")){
                startRemoteGame((Network.game_server_startGame) startGameData);
            }
            lobbyMenu.render();
        }while (!lobbyMenu.action.equals("disconnect"));
    }
    Board multiplayerBoard = null;
    int turnMultiplayer = 0;

    private void startRemoteGame(Network.game_server_startGame object){

        final Menu gameMenu = new Menu("game");
        multiplayerBoard = new Board();
        multiplayerBoard.stateArray = new State[object.width][object.height];
        multiplayerBoard.format(object.width, object.height, object.board, object.productivity);


        GL11.glDisable(GL11.GL_BLEND);
        RenderBoard renderer = new RenderBoard(gameMenu.getElement("gameView").getPosX(),gameMenu.getElement("gameView").getPosY(), ((GameView)gameMenu.getElement("gameView")).getWidth(),((GameView)gameMenu.getElement("gameView")).getHeight());

        Human human = new Human(Loyalty.values()[object.Loyalty], multiplayerBoard);

        client.getClient().addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Network.game_server_sendBoardSync) {
                    multiplayerBoard.format(((Network.game_server_sendBoardSync) object).width, ((Network.game_server_sendBoardSync) object).height, ((Network.game_server_sendBoardSync) object).board, ((Network.game_server_sendBoardSync) object).productivity);
                    turnMultiplayer = ((Network.game_server_sendBoardSync) object).turn;
                }
                if (object instanceof Network.game_server_sendBoardSyncUnit) {
                    multiplayerBoard.formatUnit(((Network.game_server_sendBoardSyncUnit) object).width, ((Network.game_server_sendBoardSyncUnit) object).height, ((Network.game_server_sendBoardSyncUnit) object).units);
                }
                if (object instanceof Network.ChatMessage) {
                    gameMenu.updateText(((Network.ChatMessage) object).getMessage(), "chat");
                }
            }
        });

        GL11.glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
        while(!Display.isCloseRequested()){
            // Clear display
            if(!multiplayerBoard.lock)
                GL11.glClear (GL11.GL_COLOR_BUFFER_BIT);

            // GL safe zone for the menu
            GL11.glEnable(GL11.GL_BLEND);
            gameMenu.renderNoDisplay();  // Render the surrounding menu
            GL11.glDisable(GL11.GL_BLEND);

            if(gameMenu.action.equals("gameView")){
                if(human.click(Mouse.getX()-renderer.getOffsetX(), -(Mouse.getY()-Display.getHeight())-renderer.getOffsetY(), renderer.getTILE_SIZE())){
                    client.sendClick(human.fromPosX, human.fromPosY, human.toPosX, human.toPosY);
                    human.abort();
                }
            }
            if(gameMenu.action.equals("send_chat")){
                client.sendChat(gameMenu.getText("input_chat"));
                
            }

            gameMenu.updateVariable(0, Integer.toString(turnMultiplayer), "year");

            renderer.render(multiplayerBoard);

            Display.update();
        }

        System.out.println("The winner is " + this.getWinner(mainBoard));

        GL11.glEnable(GL11.GL_BLEND);
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

        // Enable Keyboard Repeat
        Keyboard.enableRepeatEvents(true);
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
        GL11.glShadeModel(GL11.GL_FLAT);
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
           // if(players[i] == null)
               // players[i] = new MultiplayerRemote(Loyalty.values()[i], mainBoard, client.getClient());
        }
       // mainBoard.format(width, height, board, prod, );
        this.startGame(players);

        client.close();
        if(server!=null) server.close();
    }

    private void startGame( Player[] players ){
        final Menu gameMenu = new Menu("game");

        GL11.glDisable(GL11.GL_BLEND);
        RenderBoard renderer = new RenderBoard(gameMenu.getElement("gameView").getPosX(),gameMenu.getElement("gameView").getPosY(), ((GameView)gameMenu.getElement("gameView")).getWidth(),((GameView)gameMenu.getElement("gameView")).getHeight());
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
        GL11.glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
        while( this.endGame(mainBoard, turn.getTurnNumber()) && !Display.isCloseRequested()){
            // Clear display
            GL11.glClear (GL11.GL_COLOR_BUFFER_BIT);

            // GL safe zone for the menu
            GL11.glEnable(GL11.GL_BLEND);
                gameMenu.renderNoDisplay();  // Render the surrounding menu
            GL11.glDisable(GL11.GL_BLEND);

            inputAction = this.pollInput();

            if(gameMenu.action.equals("gameView"))
                //human.click(Mouse.getX()-renderer.getOffsetX(), -(Mouse.getY()-Display.getHeight())-renderer.getOffsetY());

            if(inputAction == Action.ECHAP)
                human.abort();
            turn.update();
            if(turn.getTurnNumber() != currentTurn) {
            	human.update();
                remote1.update();
                remote2.update();
            	currentTurn = turn.getTurnNumber();
            }

            gameMenu.updateVariable(0, Integer.toString(currentTurn), "year");
            
            renderer.render(mainBoard);

            Display.update();
        }

        System.out.println("The winner is " + this.getWinner(mainBoard));

        turn.stop();
        GL11.glEnable(GL11.GL_BLEND);
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
        //client = new ClientConquest(address);
    }


}