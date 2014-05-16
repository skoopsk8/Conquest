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
        for(int i=0;i<board.getBoardWidth();i++) {
            for (int j = 0; j < board.getBoardHeight(); j++) {
                if(!board.lock)
                    renderState(board.getState(i, j));
            }
        }
    }

    public int getTILE_SIZE() {
        return TILE_SIZE + BORDER_SIZE;
    }

    private void renderState( State state ){
        switch (state.getLoyalty()){
            case NONE:
                GL11.glColor3f(0.1f, 0.1f, 0.1f);
                break;
            case EMPTY:
                GL11.glColor3f(0.3f, 0.3f, 0.3f);
                break;
            case BLUE:
                GL11.glColor3f(142/255.f, 68/255.f, 173/255.f);
                break;
            case GREEN:
                GL11.glColor3f(0/255.f, 151/255.f, 64/255.f);
                break;
            case YELLOW:
                GL11.glColor3f(231/255.f, 85/255.f, 34/255.f);
                break;
            case BARBARIAN:
                GL11.glColor3f(0.752f, 0.223f, 0.168f);
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
                GL11.glColor3f(217/255.f, 149/255.f, 245/255.f);
                break;
            case GREEN:
                GL11.glColor3f(133/255.f, 246/255.f, 152/255.f);
                break;
            case YELLOW:
                GL11.glColor3f(255/255.f, 174/255.f, 121/255.f);
                break;
            case BARBARIAN:
                GL11.glColor3f(0.905f, 0.298f, 0.235f);
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