package com.nasser.poulet.conquest;
/**
 * Created by Lord on 10/12/13.
 */

import com.nasser.poulet.conquest.menu.Menu;
import com.nasser.poulet.conquest.model.IA;
import com.nasser.poulet.conquest.controller.Turn;
import com.nasser.poulet.conquest.model.*;
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

    public Conquest(String[] args){
        // Check arguments
        for (String s: args) {
            if(s.equals("-dev"))
                this.debug = true;      // Not Working
            else if(s.equals("-fullscreen"))
                this.fullscreen = true; // Not Working
            else if(s.equals("-noSplash"))
                this.noSplash = true;   // Not Working
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
                    if(!s[0].equals("quit"))
                        this.startGame(s[0]);
                }
                else if (s[0].equals("multiplayer")){
                    s = this.startMenu("multiplayer");
                    if(s[0].equals("host")){
                        // Launch server
                    }
                    else if(s[0].equals("join")){
                        // Launch multiplayer session
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

    private void startGame( String playerLoyalty ){
        Board mainBoard = new Board(20, 15);
        RenderBoard renderer = new RenderBoard();

        Turn turn = new Turn();

       // IA IAGreen = new IA(Loyalty.GREEN, mainBoard);
        Human human = new Human(Loyalty.BLUE, mainBoard);

        int currentTurn = 1;
        Action inputAction;

        // Have to stay just before the while
        turn.startTurn();
       // IAGreen.start();
        while(!Display.isCloseRequested()){
            inputAction = this.pollInput();
            if(inputAction == Action.MOUSE)
                human.click(Mouse.getX(), (-Mouse.getY() + 600));
            else if(inputAction == Action.ECHAP)
                human.abort();
            turn.update();
            if(turn.getTurnNumber() != currentTurn) {
            	human.update();
            	currentTurn = turn.getTurnNumber();
            }
            
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
}