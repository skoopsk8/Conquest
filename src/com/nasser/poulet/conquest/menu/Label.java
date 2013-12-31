package com.nasser.poulet.conquest.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

/**
 * Created by Thomas on 12/29/13.
 */
public class Label extends UIElement{

    public void render(){
        font.getFont().drawString(posX, posY, text, Color.white);
    }

    @Override
    public String click(int posX, int posY) {
        return null;
    }

    @Override
    public String hover(int posX, int posY) {
        return null;
    }

    @Override
    public String getAction() {
        return null;
    }
}
