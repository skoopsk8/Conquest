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

        String fontSize = size;

        // Reset the size
        if(size.equals("auto"))
            fontSize = Integer.toString(Display.getHeight() / 20);
        else if(size.equals("auto2"))
            fontSize = Integer.toString(Display.getHeight() / 40);

        Font.getFont(font + ":" + fontSize).drawString(posX*ratioX - Font.getFont(font+":"+fontSize).getWidth(text)/2, posY*ratioY, text, Color.white);
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
