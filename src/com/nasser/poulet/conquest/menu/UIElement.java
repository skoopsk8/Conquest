package com.nasser.poulet.conquest.menu;

/**
 * Created by Thomas on 12/30/13.
 */
public abstract class UIElement {
    protected String name;
    protected String text;
    protected int posX, posY;
    protected Font font = new Font();
    protected String type;

    public abstract void render();

    public abstract String click( int posX, int posY );

    public abstract boolean hover( int posX, int posY );

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

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public abstract String getAction();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract void reload();
}
