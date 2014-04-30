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
            fonts.put(font, new TrueTypeFont(new java.awt.Font(fontValue[0], java.awt.Font.BOLD, Integer.parseInt(fontValue[1])), true));
        }
        else
            System.out.println("Access image: "+font);
        return fonts.get(font);
    }

    static public void destroy(){
        fonts.clear();
    }
    /*
    private String fontName;
    private int size;
    private java.awt.Font font;
    private TrueTypeFont trueTypeFont;

    public Font(){
        this.fontName = "Arial";
        this.size = 25;

        this.generateFont();
    }

    public Font(String fontName, int size){
        this.fontName = fontName;
        this.size = size;

        this.generateFont();
    }

    private void generateFont(){
        font = null;
        trueTypeFont = null;

        font = new java.awt.Font(this.fontName, java.awt.Font.BOLD, this.size);
        trueTypeFont = new TrueTypeFont(font, true);
    }

    public TrueTypeFont getFont(){
        return trueTypeFont;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;

        generateFont();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;

        generateFont();
    }*/
}
