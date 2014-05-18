package com.nasser.poulet.conquest.menu;

/**
 * Created by Thomas on 12/30/13.
 */
public abstract class UIElement {
    protected String name;
    protected String text;
    protected int posX, posY;
    protected String font = "Arial";
    protected String size = "20";
    protected String type;
    public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	protected boolean hidden = false;

    public abstract void render();

    public abstract String click( int posX, int posY );

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

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

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
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
