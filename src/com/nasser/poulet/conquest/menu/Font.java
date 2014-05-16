package com.nasser.poulet.conquest.menu;

import org.newdawn.slick.*;
import org.newdawn.slick.Image;

import java.awt.*;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Thomas on 12/30/13.
 */
public class Font {
    static public Map<String, TrueTypeFont> fonts = new Hashtable<String, TrueTypeFont>();

    static public org.newdawn.slick.Font getFont(String font){
        String[] fontValue = font.split(":");
        if(!fonts.containsKey(font)){
            System.out.println("Load font: "+font);
            fonts.put(font, new TrueTypeFont(new java.awt.Font(fontValue[0], java.awt.Font.TRUETYPE_FONT, Integer.parseInt(fontValue[1])), true));
        }
        return fonts.get(font);
    }

    static public void destroy(){
        fonts.clear();
    }
}
