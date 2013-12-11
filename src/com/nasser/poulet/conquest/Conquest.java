package com.nasser.poulet.conquest;
/**
 * Created by Lord on 10/12/13.
 */

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.State;
import com.nasser.poulet.conquest.view.RenderBoard;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Conquest {
    public Conquest(){
        try {
            System.out.println("Launching ...");
            Display.setDisplayMode(new DisplayMode(900, 600));
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
        //mainBoard.addState(new State(0,0));
       // mainBoard.addState(new State(5,4));
        RenderBoard renderer = new RenderBoard(mainBoard);

        while(!Display.isCloseRequested()){
            renderer.render();
            Display.update();
        }
        Display.destroy();
    }

    public static void main(String[] args){
        new Conquest();
    }
}