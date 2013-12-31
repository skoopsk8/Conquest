package com.nasser.poulet.conquest.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

/**
 * Created by Thomas on 12/29/13.
 */
public class Button extends UIElement {
    private int width, height;
    private String action;
    private Color color = Color.white;

    @Override
    public void render() {
        font.getFont().drawString(posX, posY, text, color);
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
            color = Color.gray;
            return null;
        }
        color = Color.white;
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
