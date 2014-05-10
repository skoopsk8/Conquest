package com.nasser.poulet.conquest.view;

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.State;
import com.nasser.poulet.conquest.model.Unit;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Created by Lord on 10/12/13.
 */
public class RenderBoard implements Render {
    private int TILE_SIZE = 31;
    private int BORDER_SIZE = 8;
    private boolean firstRender = true;

    private int offsetX, offsetY, width, height;

    public RenderBoard(int offsetX, int offsetY, int width, int height) {
        this.offsetX = offsetX*(Display.getWidth()/30);
        this.offsetY = offsetY*(Display.getHeight()/20);
        this.width = width*(Display.getWidth()/30);
        this.height = height*(Display.getHeight()/20);
    }

    public void render(){
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void render( Board board ) {
        // Clear the display
        //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Adjust state size
        if(firstRender){
            firstRender = false;
            if(width>height){
                TILE_SIZE = width/board.getBoardWidth()-((width/board.getBoardWidth())/4);
                BORDER_SIZE = (width/board.getBoardWidth())/4;

                this.offsetX = this.offsetX + ((this.width-(board.getBoardWidth()*(TILE_SIZE+BORDER_SIZE)))/2);
                this.offsetY = this.offsetY + ((this.height-(board.getBoardHeight()*(TILE_SIZE+BORDER_SIZE)))/2);
            }
        }

        // Render the Board
        if(!board.lock){
            for(int i=0;i<board.getBoardWidth();i++) {
                for (int j = 0; j < board.getBoardHeight(); j++) {
                    if(!board.lock)
                        renderState(board.getState(i, j));
                }
            }
        }
    }

    public int getTILE_SIZE() {
        return TILE_SIZE + BORDER_SIZE;
    }

    private void renderState( State state ){
        switch (state.getLoyalty()){
            case NONE:
                GL11.glColor4f(0.1f, 0.1f, 0.1f, 1.f);
                break;
            case EMPTY:
                GL11.glColor4f(0.3f, 0.3f, 0.3f, 1.f);
                break;
            case BLUE:
                GL11.glColor4f(0.1f, 0.1f, 0.5f, 1.f);
                break;
            case GREEN:
                GL11.glColor4f(0.1f, 0.5f, 0.1f, 1.f);
                break;
            case YELLOW:
                GL11.glColor4f(0.5f, 0.5f, 0.1f, 1.f);
                break;
            case BARBARIAN:
                GL11.glColor4f(0.5f, 0.1f, 0.1f, 1.f);
                break;
        }

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f((state.getPosX()*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+offsetX,(state.getPosY()*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+offsetY);
        GL11.glVertex2f((state.getPosX()*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2)+offsetX,(state.getPosY()*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+offsetY);
        GL11.glVertex2f((state.getPosX()*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2)+offsetX,(state.getPosY()*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2)+offsetY);
        GL11.glVertex2f((state.getPosX()*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+offsetX,(state.getPosY()*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2)+offsetY);
        GL11.glEnd();

        // Render units
        for(int i=0; i<2; i++){
            if(state.getUnit(i) != null)
                this.renderUnit(state.getUnit(i), state.getPosX(), state.getPosY(), i);
        }
    }

    private void renderUnit( Unit unit, int posX, int posY, int level ){
        switch (unit.getLoyalty()){
            case BLUE:
                GL11.glColor3f(0.068f, 0.568f, 0.836f);
                break;
            case GREEN:
                GL11.glColor3f(0.384f, 0.88f, 0.072f);
                break;
            case YELLOW:
                GL11.glColor3f(0.98f, 0.876f, 0.1f);
                break;
            case BARBARIAN:
                GL11.glColor3f(0.7f, 0.1f, 0.1f);
                break;
        }
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f((posX*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+offsetX,(posY*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+TILE_SIZE-(TILE_SIZE*level)+offsetY);
        GL11.glVertex2f((posX*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2)+offsetX,(posY*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+TILE_SIZE-(TILE_SIZE*level)+offsetY);
        GL11.glVertex2f((posX * (TILE_SIZE + BORDER_SIZE)) + TILE_SIZE + (BORDER_SIZE / 2)+offsetX, (posY * (TILE_SIZE + BORDER_SIZE)) - TILE_SIZE / 5 + (BORDER_SIZE / 2) + TILE_SIZE-((TILE_SIZE-(2*TILE_SIZE/5))*level)+offsetY);
        GL11.glVertex2f((posX*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+offsetX,(posY*(TILE_SIZE+BORDER_SIZE))-TILE_SIZE/5+(BORDER_SIZE/2)+TILE_SIZE-((TILE_SIZE-(2*TILE_SIZE/5))*level)+offsetY);
        GL11.glEnd();
    }
}