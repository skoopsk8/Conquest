package com.nasser.poulet.conquest.model;

import com.sun.swing.internal.plaf.synth.resources.synth_sv;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Cursor;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.beans.Customizer;

/**
 * Created by Thomas on 12/29/13.
 */
public class Button extends UIElement {
    private int width, height;
    private String action;
    private TrueTypeFont font = new TrueTypeFont(new Font("Impact", Font.BOLD, 24), true);

    @Override
    public void render() {
        font.drawString(posX, posY, text, Color.white);
    }

    public String click( int posX, int posY){
        if(inside(posX, posY)){
            return this.action;
        }
        return null;
    }

    @Override
    public String hover(int posX, int posY) {
        if(inside(posX, posY)){
            // TODO : add cursor change when hover
            return null;
        }
        return null;
    }

    private boolean inside( int posX, int posY ){
        if(posX>=this.posX && posX<=this.posX+this.width){
            if(posY>=this.posY && posY<=this.posY+this.height){
                return true;
            }
        }
        return false;
    }

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
