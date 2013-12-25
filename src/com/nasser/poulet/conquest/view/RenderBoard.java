package com.nasser.poulet.conquest.view;

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.State;
import com.nasser.poulet.conquest.model.Unit;
import com.nasser.poulet.conquest.model.UnitContainer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Lord on 10/12/13.
 */
public class RenderBoard implements Render {
    private Board board;

    private int tileSize = 32;
    private int borderSize = 8;

    public RenderBoard( Board board ){
        this.board = board;
    }

    @Override
    public void render() {
        // Clear the display
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Render the Board
        for(int i=0;i<20;i++) for(int j=0;j<15;j++) renderState(i, j);

        for(int i=0;i<20;i++){
            for(int j=0;j<15;j++){
                for(int k=0;k<2;k++){
                    if(UnitContainer.unitBoard[i][j][k] != null){
                        renderUnit(UnitContainer.unitBoard[i][j][k], k);
                    }
                }
            }
        }
    }

    private void renderState( int i, int j ){
        switch (board.stateArray[i][j].getLoyalty()){
            case NONE:
                GL11.glColor3f(0.1f, 0.1f, 0.1f);
                break;
            case EMPTY:
                GL11.glColor3f(0.3f, 0.3f, 0.3f);
                break;
            case BLUE:
                GL11.glColor3f(0.1f, 0.1f, 0.5f);
                break;
            case GREEN:
                GL11.glColor3f(0.1f, 0.5f, 0.1f);
                break;
            case YELLOW:
                GL11.glColor3f(0.5f, 0.5f, 0.1f);
                break;
            case BARBARIAN:
                GL11.glColor3f(0.5f, 0.1f, 0.1f);
                break;
        }

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f((board.stateArray[i][j].getPosX()*(tileSize+borderSize))+(borderSize/2),(board.stateArray[i][j].getPosY()*(tileSize+borderSize))+(borderSize/2));
        GL11.glVertex2f((board.stateArray[i][j].getPosX()*(tileSize+borderSize))+tileSize+(borderSize/2),(board.stateArray[i][j].getPosY()*(tileSize+borderSize))+(borderSize/2));
        GL11.glVertex2f((board.stateArray[i][j].getPosX()*(tileSize+borderSize))+tileSize+(borderSize/2),(board.stateArray[i][j].getPosY()*(tileSize+borderSize))+tileSize+(borderSize/2));
        GL11.glVertex2f((board.stateArray[i][j].getPosX()*(tileSize+borderSize))+(borderSize/2),(board.stateArray[i][j].getPosY()*(tileSize+borderSize))+tileSize+(borderSize/2));
        GL11.glEnd();
    }

    private void renderUnit( Unit unit, int level ){
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
        if(level == 0){
            GL11.glVertex2f((unit.getPosX()*(tileSize+borderSize))+(borderSize/2),(unit.getPosY()*(tileSize+borderSize))+(borderSize/2));
            GL11.glVertex2f((unit.getPosX()*(tileSize+borderSize))+tileSize+(borderSize/2),(unit.getPosY()*(tileSize+borderSize))+(borderSize/2));
            GL11.glVertex2f((unit.getPosX()*(tileSize+borderSize))+tileSize+(borderSize/2),(unit.getPosY()*(tileSize+borderSize))+tileSize/5+(borderSize/2));
            GL11.glVertex2f((unit.getPosX()*(tileSize+borderSize))+(borderSize/2),(unit.getPosY()*(tileSize+borderSize))+tileSize/5+(borderSize/2));
        }
        if(level == 1){
            GL11.glVertex2f((unit.getPosX()*(tileSize+borderSize))+(borderSize/2),(unit.getPosY()*(tileSize+borderSize))+(borderSize/2)+tileSize);
            GL11.glVertex2f((unit.getPosX()*(tileSize+borderSize))+tileSize+(borderSize/2),(unit.getPosY()*(tileSize+borderSize))+(borderSize/2)+tileSize);
            GL11.glVertex2f((unit.getPosX()*(tileSize+borderSize))+tileSize+(borderSize/2),(unit.getPosY()*(tileSize+borderSize))-tileSize/5+(borderSize/2)+tileSize);
            GL11.glVertex2f((unit.getPosX()*(tileSize+borderSize))+(borderSize/2),(unit.getPosY()*(tileSize+borderSize))-tileSize/5+(borderSize/2)+tileSize);
        }
        GL11.glEnd();
    }
}
