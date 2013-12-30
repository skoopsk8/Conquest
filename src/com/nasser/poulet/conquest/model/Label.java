package com.nasser.poulet.conquest.model;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

/**
 * Created by Thomas on 12/29/13.
 */
public class Label extends UIElement{
    private TrueTypeFont font = new TrueTypeFont(new Font("Impact", Font.BOLD, 24), true);

    public void render(){
        font.drawString(posX, posY, text, Color.white);
    }

    @Override
    public String click(int posX, int posY) {
        return null;
    }

    @Override
    public String hover(int posX, int posY) {
        return null;
    }
}
