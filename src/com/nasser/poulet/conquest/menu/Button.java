package com.nasser.poulet.conquest.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

/**
 * Created by Thomas on 12/29/13.
 */
public class Button extends UIElementImage {
    private String action;
    private Color color = Color.white;
    
    public Button() {
    	super("data/img/button-left.png", "data/img/button-center.png", "data/img/button-right.png");
    }

    @Override
    public void render() {
    	super.render();
        font.getFont().drawString(posX + 7, posY + 5, text, color);
    }

    public String click( int posX, int posY){
        if(super.inside(posX, posY)){
            return this.action;
        }
        return null;
    }

    @Override
    public String hover(int posX, int posY) {
        if(super.inside(posX, posY)){
            color = Color.gray;
            super.hover(0, 0);
            return null;
        }
        color = Color.white;
        return null;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
