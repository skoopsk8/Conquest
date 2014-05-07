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
    
    public Button() {
    	super("data/img/button-left.png", "data/img/button-center.png", "data/img/button-right.png");
        font = "Arial";
        size = Integer.toString(Display.getHeight() / 20 - 20);
    }

    @Override
    public void render() {
    	super.render();
        int ratioX = Display.getWidth()/30;
        int ratioY = Display.getHeight()/20;

		String line = "a";
		
		int car = (this.width * ratioX) /  Font.getFont(font+":"+size).getWidth(line);
		
        if(text.length() < car) {
        	Font.getFont(font+":"+size).drawString(posX*ratioX + ((this.getWidth()*ratioX)-Font.getFont(font+":"+size).getWidth(text))/2, posY*ratioY + 10, text, color);
        }
        else {
        	Font.getFont(font+":"+size).drawString(posX*ratioX + ((this.getWidth()*ratioX)-Font.getFont(font+":"+size).getWidth(text.substring(text.length() - car)))/2, posY*ratioY + 10, text.substring(text.length() - car), color);
        }
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

    @Override
    public void reload(){
        size = Integer.toString(Display.getHeight() / 20 - 20);
        super.reload();
    }
}
