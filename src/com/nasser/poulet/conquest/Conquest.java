package com.nasser.poulet.conquest;
/**
 * Created by Lord on 10/12/13.
 */

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

public class Conquest {
    private boolean fullscreen;
    private boolean noSplash = false;
    private boolean debug;
    private boolean noClick;

    private ServerConquest server;
    private ClientConquest client;

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
                    System.out.println(s);
                    //if(!s[0].equals("quit"))
                        //this.startGame(s[0]);
                }
                else if (s[0].equals("multiplayer")){
                    s = this.startMenu("multiplayer");
                    if(s[0].equals("host")){
                        // Create the server
                        this.initServer("127.0.0.1", "1224");
                        this.initClient("127.0.0.1", "1224");
                        this.startMultiplayerGame();
                        //this.startGame("blue");
                    }
                    else if(s[0].equals("join")){
                        String[] str = s[1].split(":");
                        // Launch multiplayer session
                        this.initClient("127.0.0.1", "1224");
                        this.startMultiplayerGame();
                        //this.startGame("blue");
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
        return menu.render();
    }

    public Board mainBoard;
    private boolean wait =true;

    int[][] board, prod;
    int width, height;

    private void startMultiplayerGame(){
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
            }
        });
        client.sendSyncRequest();
        if(startLobby()[0].equals("continue")){
            Player[] players = new Player[3];
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
        //IA remote2 = null;

        Turn turn = new Turn();

        for(Player player: players){
            if(player instanceof Human){
                human = (Human)player;
            }
            if(player instanceof IA){
                if(remote1 == null)
                    remote1 = (IA)player;
                //else if(remote2 == null)
                 //   remote2 = (IA)player;
            }
        }

        Action inputAction;

        // Have to stay just before the while
        turn.startTurn();
        while(!Display.isCloseRequested()){
            inputAction = this.pollInput();
            if(inputAction == Action.MOUSE)
                human.click(Mouse.getX(), (-Mouse.getY() + 600));
            else if(inputAction == Action.ECHAP)
                human.abort();
            turn.update();
            renderer.render(mainBoard);
            Display.update();
        }

        turn.stop();
    }

    private enum Action{
        NONE,
        KEYBOARD,
        ECHAP,
        MOUSE
    }

    private void initServer( String address, String port ){
        server =  new ServerConquest(address, port);
    }

    private void initClient( String address, String port ){
        client = new ClientConquest(address, port);
    }
}