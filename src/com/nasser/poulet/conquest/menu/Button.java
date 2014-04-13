package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

/**
 * Created by Thomas on 12/29/13.
 */
public class Button extends UIElementImage {
    private String action;
    private Color color = Color.white;
    private Font font = null;
    
    public Button() {
    	super("data/img/button-left.png", "data/img/button-center.png", "data/img/button-right.png");
        font = new Font("Arial",Display.getHeight()/20-20);
    }

    @Override
    public void render() {
    	super.render();
        int ratioX = Display.getWidth()/30;
        int ratioY = Display.getHeight()/20;
        font.getFont().drawString(posX*ratioX + ((this.getWidth()*ratioX)-font.getFont().getWidth(text))/2, posY*ratioY + 10, text, color);
    }

    public String click( int posX, int posY){
        if(super.inside(posX, posY)){
            return this.action;
        }
        return "";
    }

    @Override
    public boolean hover(int posX, int posY) {
        if(super.hover(posX, posY)){
            color = Color.gray;
            return true;
        }
        color = Color.white;
        return false;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
