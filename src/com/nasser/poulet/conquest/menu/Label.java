package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

/**
 * Created by Thomas on 12/29/13.
 */
public class Label extends UIElement{

    public void render(){
        int ratioX = Display.getWidth()/30;
        int ratioY = Display.getHeight()/20;

        // Reset the size
        size = Integer.toString(Display.getHeight() / 20);

        Font.getFont(font + ":" + size).drawString(posX*ratioX - Font.getFont(font+":"+size).getWidth(text)/2, posY*ratioY, text, Color.white);
    }

    @Override
    public String click(int posX, int posY) {
        return "";
    }

    @Override
    public boolean hover(int posX, int posY) {
        return false;
    }

    @Override
    public String getAction() {
        return null;
    }

    @Override
    public void reload() {
        Font.destroy();
    }
}
