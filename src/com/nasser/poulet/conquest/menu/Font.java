package com.nasser.poulet.conquest.menu;

import org.newdawn.slick.TrueTypeFont;
import java.awt.*;

/**
 * Created by Thomas on 12/30/13.
 */
public class Font {
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
    }
}
