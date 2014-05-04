package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Created by Thomas on 5/4/14.
 */
public class GameView extends UIElement {

    private int width, height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void render() {
        float ratioX = Display.getWidth()/30;
        float ratioY = Display.getHeight()/20;

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.f);
        GL11.glVertex3f((this.posX*ratioX), (this.posY*ratioY), 0.0f);
        GL11.glVertex3f(((this.posX+this.width)*ratioX), (this.posY*ratioY), 0.0f);
        GL11.glVertex3f(((this.posX+this.width)*ratioX), ((this.posY+this.height)*ratioY), 0.0f);
        GL11.glVertex3f((this.posX*ratioX), ((this.posY+this.height)*ratioY), 0.0f);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_BLEND);
    }

    @Override
    public String click(int posX, int posY) {
        if(inside(posX, posY))
            return "gameView";
        else
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

    }

    public boolean inside( int posX, int posY ){
        int ratioX = Display.getWidth()/30;
        int ratioY = Display.getHeight()/20;
        if(posX>=this.posX*ratioX && posX<=this.posX*ratioX+this.width*ratioX){
            if(posY>=this.posY*ratioY && posY<=this.posY*ratioY+this.height*ratioY){
                return true;
            }
        }
        return false;
    }
}
