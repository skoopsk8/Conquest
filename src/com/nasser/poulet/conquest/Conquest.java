package com.nasser.poulet.conquest;
/**
 * Created by Lord on 10/12/13.
 */

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

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
    private boolean noSplash = false;
    private boolean debug;
    private boolean noClick;

    private ServerConquest server;
    private ClientConquest client;

    public Board mainBoard;

    Player[] players = new Player[3];

    public Conquest(String[] args){
        // Check arguments
        int i=0;
        for (String s: args) {
            if(s.equals("-dev"))
                this.debug = true;      // Not Working
            else if(s.equals("-fullscreen"))
                this.fullscreen = true; // Not Working
            else if(s.equals("-noSplash"))
                this.noSplash = true;
            else if(s.equals("-port"))
                Network.port = Integer.parseInt(args[i+1]);

             i++;
        }

        noClick = true;
        
        // we set the icon
        try {
			Display.setIcon(new ByteBuffer[] {
			        new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("data/img/icon16.png")), false, false, null),
			        new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("data/img/icon32.png")), false, false, null),
			        new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("data/img/icon128.png")), false, false, null)
			        });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
            System.out.println("Launching ...");
            Display.setDisplayMode(new DisplayMode(800, 600));
            if(this.fullscreen) Display.setFullscreen(true);
            Display.setTitle("Conquest");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        this.initializeOpenGL(800, 600);    // Launch OpenGL

        String[] s = new String[2];
        if(!noSplash)s=this.startMenu("splash");
        else s[0]="continue";
        if (s[0].equals("continue")){
            do {
                s = this.startMenu("mainMenu");
                if (s[0].equals("play")){
                    s = this.startMenu("play");
                    mainBoard = null;
                    mainBoard = new Board(20, 15, true);
                    if(s[0].equals("blue")){
                        players[0] = new Human(Loyalty.BLUE,mainBoard);
                        players[1] = new IA(Loyalty.GREEN,mainBoard);
                        players[2] = new IA(Loyalty.YELLOW,mainBoard);
                    }
                    else if(s[0].equals("green")){
                        players[0] = new Human(Loyalty.GREEN,mainBoard);
                        players[1] = new IA(Loyalty.BLUE,mainBoard);
                        players[2] = new IA(Loyalty.YELLOW,mainBoard);
                    }
                    else if(s[0].equals("yellow")){
                        players[0] = new Human(Loyalty.YELLOW,mainBoard);
                        players[1] = new IA(Loyalty.GREEN,mainBoard);
                        players[2] = new IA(Loyalty.BLUE,mainBoard);
                    }
                    this.startGame(players);
                }
                else if (s[0].equals("multiplayer")){
                    s = this.startMenu("multiplayer");
                    if(s[0].equals("host")){
                        // Create the server and connect the user client
                        this.initServer();
                        this.initClient("127.0.0.1");
                        this.startMultiplayerGame();
                    }
                    else if(s[0].equals("join")){
                        String[] str = s[1].split(":");
                        // Launch multiplayer session

                        if(str.length==1)
                            Network.port = 54555;
                        else
                            Network.port = Integer.parseInt(str[1]);

                        this.initClient(str[0]);
                        this.startMultiplayerGame();
                    }
                }
            }while (!s[0].equals("quit"));
        }

        Display.destroy();
    }

    public static void main(String[] args){
        new Conquest(args);
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
    }

    private String[] startMenu( String filename ){
        Menu menu = new Menu(filename);
        return menu.render();
    }

    private String[] startLobby(){
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
        String[] menuReturn;
        do {
             menuReturn = menu.render();
            if(menuReturn[0].equals("forceStart")){
                client.sendForceStart();
                client.sendReady();
            }
        }while (!menuReturn[0].equals("continue"));

        return menuReturn;
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

        if(startLobby()[0].equals("continue")){
            System.out.println(client.getClient().getID());
            players[client.getClient().getID()-1] = new Multiplayer(Loyalty.values()[client.getClient().getID()+1], mainBoard, client.getClient());
            for(int i=0; i<3; i++){
                if(players[i] == null)
                    players[i] = new MultiplayerRemote(Loyalty.values()[i], mainBoard, client.getClient());
            }
            mainBoard.format(width, height, board, prod);
            this.startGame(players);
        }

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
        if(board.numberOfEmpty() == 0 && triggerTurn == -1){ // Start end game
            triggerTurn = turnNumber;
            System.out.println("Start end counter");
        }

        if(triggerTurn != -1 && triggerTurn + (60000/Turn.TURN_DURATION)<turnNumber){
            return false;
        }

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