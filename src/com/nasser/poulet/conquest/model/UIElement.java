package com.nasser.poulet.conquest.model;

/**
 * Created by Thomas on 12/30/13.
 */
public abstract class UIElement {
    protected String name;
    protected String text;
    protected int posX, posY;

    public abstract void render();

    public abstract String click( int posX, int posY );

    public abstract String hover( int posX, int posY );

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
