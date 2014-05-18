package com.nasser.poulet.conquest;
/**
 * Conquest
 * Created by Lord on 10/12/13.
 */

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nasser.poulet.conquest.controller.BoardController;
import com.nasser.poulet.conquest.menu.GameView;
import com.nasser.poulet.conquest.menu.Menu;
import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.Loyalty;
import com.nasser.poulet.conquest.model.Seasons;
import com.nasser.poulet.conquest.model.State;
import com.nasser.poulet.conquest.network.ClientConquest;
import com.nasser.poulet.conquest.network.Network;
import com.nasser.poulet.conquest.player.Human;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class Conquest {
    static public String versionNumber = "pre 0.3 alpha";   // Version number
    private String serverBrowserIp = "conquest.nagyzzer.com";   // Default server browser

    private boolean fullscreen; // Fullscreen?

    private ClientConquest client;  // Client for server contact

    private DisplayMode[] modes = null; // Available display modes
    private int activeMode = 7;         // Current display mode -> 7 seems to be the optimized one

    Menu[] menus = new Menu[4]; // Array for menu preloader

    // Argument width and height
    int argWidth = 0;
    int argHeight = 0;

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
            else if(s.equals("-resolution")){
                argWidth = Integer.parseInt(args[i+1]);
                argHeight = Integer.parseInt(args[i+2]);
            }

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
            while(!splash.action.equals("continue")){splash.render();}
        }

        // Menu
        Menu mainMenu = menus[0];
        String[] text;

        // Update the changelog
        try {
            text = HTTP.getPage("https://raw.githubusercontent.com/skoopsk8/Conquest/master/changelog");
            for(String line: text){
                mainMenu.updateText(line,"changelog");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Update the news
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
                connectionMenu();
            else if(mainMenu.action.equals("website"))
                launchWebsite();
            else if (mainMenu.action.equals("settings"))
                settingsMenu();

            mainMenu.render();
        }while (!mainMenu.action.equals("quit"));

        // Display destroy & free
        Display.destroy();
    }

    private void launchWebsite(){
        String url = "http://conquest.nagyzzer.com/";

        if (Desktop.isDesktopSupported()) {
            // Windows
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            // Linux
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("/usr/bin/firefox -new-window " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void settingsMenu(){
        // Load the menu
        Menu settingsMenu = menus[3];

        // Update the input boxes text
        settingsMenu.updateText(modes[activeMode].toString(),"resolution");
        settingsMenu.updateText(serverBrowserIp,"Browser");

        int oldActiveMode = activeMode; // Resolution update check

        // Menu render loop
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
            if(settingsMenu.getText("Browser").contains(String.valueOf(Keyboard.KEY_INSERT))) {
        		settingsMenu.updateText(settingsMenu.getText("Browser").replaceAll(String.valueOf(Keyboard.KEY_INSERT), ""), "Browser");
        		settingsMenu.action = "save";
        	}
        }while (!(settingsMenu.action.equals("save") || settingsMenu.action.equals("quit")));

        // Change the resolution
        if(oldActiveMode != activeMode){
            Display.destroy();
            initializeDisplay();
            initializeOpenGL(Display.getWidth(), Display.getHeight());
            Image.destroy();    // Destroy the image atlas

            for (Menu menu: menus){
                menu.reload();
            }
        }

        // Change server Browser IP
        serverBrowserIp = settingsMenu.getText("Browser");
    }

    private void connectionMenu(){
        // Load the menu
        Menu connectionMenu  = menus[2];

        // Update current server browser address
        connectionMenu.updateText(serverBrowserIp, "Current");

        // Connection variables
        final boolean[] validation = {false};
        final boolean[] response = {false};

        // Menu render loop
        do {     		
            if(connectionMenu.action.equals("connect") || connectionMenu.getText("Username").endsWith(String.valueOf(Keyboard.KEY_INSERT)) || connectionMenu.getText("Password").endsWith(String.valueOf(Keyboard.KEY_INSERT))){
            	if(connectionMenu.getText("Username").endsWith(String.valueOf(Keyboard.KEY_INSERT)))
            		connectionMenu.updateText(connectionMenu.getText("Username").replaceAll(String.valueOf(Keyboard.KEY_INSERT), ""), "Username");
            	else if(connectionMenu.getText("Password").endsWith(String.valueOf(Keyboard.KEY_INSERT)))
            		connectionMenu.updateText(connectionMenu.getText("Password").replaceAll(String.valueOf(Keyboard.KEY_INSERT), ""), "Password");
            	
            	
            	// Contact the server browser
                try {
                    client = new ClientConquest(serverBrowserIp);

                    // Send the credentials
                    client.sendCredentials(connectionMenu.getText("Username"), connectionMenu.getText("Password"));

                    // Create the listener
                    client.getClient().addListener(new Listener() {
                        public void received (Connection connection, Object object) {
                            if (object instanceof Network.CredentialsValidation) {
                                response[0] = true;
                                validation[0] = ((Network.CredentialsValidation) object).isValidation();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    connectionMenu.updateText("Failed to reach the given server browser!", "Error");
                }
            }

            // If the server responded
            if(response[0]){
                if (validation[0]){ // If the credentials are valid
                    lobbyMenu();
                    response[0] = false;
                    client.close();
                }
                else{
                    connectionMenu.updateText("Please check your credentials!", "Error");
                    connectionMenu.updateText("Password", "Password");
                    response[0] = false;
                    client.close();
                }
            }

            connectionMenu.render();
        }while (!(connectionMenu.action.equals("cancel") || connectionMenu.action.equals("quit")));
    }

    private void lobbyMenu(){
        // Load the menu
        final Menu lobbyMenu = new Menu("lobby");

        // Inform the server about our connection
        client.registerClient();

        // Object containing game info
        final Network.game_server_startGame[] startGameData = new Network.game_server_startGame[1];

        // Bind listener for the client
        client.getClient().addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Network.ChatMessage) {    // Chat message
                    lobbyMenu.updateText(((Network.ChatMessage) object).getMessage(),"chat");
                }
                if (object instanceof Network.game_server_startGame) {  // Start multiplayer game
                    lobbyMenu.action = "start_multiplayer";
                    startGameData[0] = (Network.game_server_startGame)object;
                }
                if (object instanceof Network.lobby_server_connected) {
                    lobbyMenu.getElement("ready").setHidden(false);
                }
            }
        });

        // Render menu loop
        do {
            // Send chat message
        	if(lobbyMenu.action.equals("send_chat") || lobbyMenu.getText("input_chat").endsWith(String.valueOf(Keyboard.KEY_INSERT))){
        		lobbyMenu.updateText(lobbyMenu.getText("input_chat").trim().replaceAll(String.valueOf(Keyboard.KEY_INSERT), ""), "input_chat");
        		client.sendChat(lobbyMenu.getText("input_chat").replaceAll("^\\s+|\\s+$", ""));
                lobbyMenu.getElement("input_chat").setText("");
            }

            // Start multiplayer game
            else if(lobbyMenu.action.equals("start_multiplayer")){
                lobbyMenu.getElement("ready").setHidden(true);
                startRemoteGame(startGameData[0]);
            }
        	
            else if(lobbyMenu.action.equals("create")) {
            	client.sendChat("/create");
            }
        	
            else if(lobbyMenu.action.equals("join")) {
            	client.sendChat("/joingame " + lobbyMenu.getText("friend"));
            }
        	
            else if(lobbyMenu.action.equals("ready")) {
            	client.sendChat("/setready");
            }

            lobbyMenu.render();
        }while (!(lobbyMenu.action.equals("disconnect")|| lobbyMenu.action.equals("quit")));
    }

    private void startRemoteGame(Network.game_server_startGame object){
        // Load the menu
        final Menu gameMenu = new Menu("game");

        // Gameplay variables
        final int[] turnMultiplayer = {0};
        BoardController multiplayerBoard;
        final String[] winnerLoyalty = {null};

        // Build a new board based on the received information
        multiplayerBoard = new BoardController(new Board());
        multiplayerBoard.getBoard().stateArray = new State[object.width][object.height];
        multiplayerBoard.getBoard().format(object.width, object.height, object.board, object.productivity);

        // Create the board renderer
        RenderBoard renderer = new RenderBoard(gameMenu.getElement("gameView").getPosX(),gameMenu.getElement("gameView").getPosY(), ((GameView)gameMenu.getElement("gameView")).getWidth(),((GameView)gameMenu.getElement("gameView")).getHeight());

        // Create a new human player
        Human human = new Human(Loyalty.values()[object.Loyalty], multiplayerBoard);

        final BoardController finalMultiplayerBoard = multiplayerBoard;

        // Bind the listener
        client.getClient().addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Network.game_server_sendBoardSync) {
                    finalMultiplayerBoard.getBoard().format(((Network.game_server_sendBoardSync) object).width, ((Network.game_server_sendBoardSync) object).height, ((Network.game_server_sendBoardSync) object).board, ((Network.game_server_sendBoardSync) object).productivity);
                    turnMultiplayer[0] = ((Network.game_server_sendBoardSync) object).turn;
                }
                if (object instanceof Network.game_server_sendBoardSyncUnit) {
                    finalMultiplayerBoard.getBoard().formatUnit(((Network.game_server_sendBoardSyncUnit) object).width, ((Network.game_server_sendBoardSyncUnit) object).height, ((Network.game_server_sendBoardSyncUnit) object).units);
                }
                if (object instanceof Network.ChatMessage) {
                    gameMenu.updateText(((Network.ChatMessage) object).getMessage(), "chat");
                }
                if (object instanceof Network.game_server_endgame) {
                    winnerLoyalty[0] = ((Network.game_server_endgame) object).loyalty;
                }
            }
        });


        // Render loop
        do{
            // Clear display
            GL11.glClear (GL11.GL_COLOR_BUFFER_BIT);

            // GL blend safe zone for the menu
            GL11.glEnable(GL11.GL_BLEND);
                gameMenu.renderNoDisplay();  // Render the surrounding menu
            GL11.glDisable(GL11.GL_BLEND);

            // Click in the game view
            if(gameMenu.action.equals("gameView")){
                if(human.click(Mouse.getX()-renderer.getOffsetX(), -(Mouse.getY()-Display.getHeight())-renderer.getOffsetY(), renderer.getTILE_SIZE())){
                    client.sendClick(human.fromPosX, human.fromPosY, human.toPosX, human.toPosY);
                    human.abort();  // Clear select
                }
            }
            else if(gameMenu.action.equals("send_chat") || gameMenu.getText("input_chat").endsWith(String.valueOf(Keyboard.KEY_INSERT))){   // Send chat
            	gameMenu.updateText(gameMenu.getText("input_chat").trim().replaceAll(String.valueOf(Keyboard.KEY_INSERT), ""), "input_chat");
                client.sendChat(gameMenu.getText("input_chat").trim());
                gameMenu.getElement("input_chat").setText("");
            }

            // Update the year display
            gameMenu.updateVariable(0, Seasons.values()[turnMultiplayer[0] %4].toString(), "year");
            gameMenu.updateVariable(1, Integer.toString(turnMultiplayer[0] /4), "year");

            renderer.render(multiplayerBoard.getBoard());

            Display.update();
        }while (!(gameMenu.action.equals("disconnect") || gameMenu.action.equals("quit")) && winnerLoyalty[0] == null);

        if(winnerLoyalty[0] != null){
            System.out.println(winnerLoyalty[0] + " has won the game !");
        }

        // Restore OpenGL blend
        GL11.glEnable(GL11.GL_BLEND);
    }

    private void initializeDisplay(){
        // We create the icon
        try {
            Display.setIcon(new ByteBuffer[] {
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("data/img/icon16.png")), false, false, null),
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("data/img/icon32.png")), false, false, null),
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("data/img/icon128.png")), false, false, null)
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the display
        try {
            modes = Display.getAvailableDisplayModes();

            // The user have specified a resolution
            if(argWidth != 0 && argHeight!=0)
                Display.setDisplayMode(new DisplayMode(argWidth,argHeight));
            else
                Display.setDisplayMode(modes[activeMode]);

            // Create the Fullscreen display
            if(this.fullscreen)
                Display.setFullscreen(true);

            Display.setTitle("Conquest");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Change the draw ration
        this.initializeOpenGL(Display.getWidth(),Display.getHeight());    // Launch OpenGL

        // Enable Keyboard Repeat
        Keyboard.enableRepeatEvents(true);
    }

    private void initializeOpenGL( int width, int height ){
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, 1, -1);
    }

    private Menu startMenu( String filename ){
        return new Menu(filename);
    }
}