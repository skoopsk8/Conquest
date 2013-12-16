package com.nasser.poulet.conquest;
/**
 * Created by Lord on 10/12/13.
 */

import com.nasser.poulet.conquest.controller.Turn;
import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.State;
import com.nasser.poulet.conquest.view.RenderBoard;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Conquest {
    private boolean fullscreen;
    private Turn turn;
    private boolean debug;

    public Conquest(String[] args){
        // Check arguments
        for (String s: args) {
            if(s == "-dev") this.debug = true;                  // Not working
            if(s == "-fullscreen") this.fullscreen = true;      // Not working
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

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        Board mainBoard = new Board();
        RenderBoard renderer = new RenderBoard(mainBoard);

        turn = new Turn();

        turn.addEvent();

        // Have to stay just before the while
        turn.startTurn();
        while(!Display.isCloseRequested()){
            turn.update();
            renderer.render();
            Display.update();
            this.pollInput();
        }

        turn.stop();
        Display.destroy();
    }

    public static void main(String[] args){
        new Conquest(args);
    }

    public void pollInput(){
        while(Keyboard.next()){
            if(Keyboard.getEventKey() == Keyboard.KEY_SPACE){
               this.turn.setPause(true);
            }
        }
    }
}