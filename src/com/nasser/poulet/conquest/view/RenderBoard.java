package com.nasser.poulet.conquest.view;

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.State;
import org.lwjgl.opengl.GL11;

/**
 * Created by Lord on 10/12/13.
 */
public class RenderBoard implements Render {
    private Board board;

    public RenderBoard( Board board ){
        this.board = board;
    }

    @Override
    public void render() {
        // Clear the display
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        int tileSize = 32;
        int borderSize = 8;

        // Render the Board
        for(int i=0;i<20;i++){
            for(int j=0;j<15;j++){
                switch (board.stateArray[i][j].val){
                    case 0:
                        GL11.glColor3f(0.1f, 0.1f, 0.1f);
                        break;
                    case 1:
                        GL11.glColor3f(0.3f, 0.3f, 0.3f);
                        break;
                    case 2:
                        GL11.glColor3f(0.1f, 0.5f, 0.1f);
                        break;
                    case 3:
                        GL11.glColor3f(0.1f, 0.1f, 0.5f);
                        break;
                    case 4:
                        GL11.glColor3f(0.5f, 0.5f, 0.1f);
                        break;
                    case 5:
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
        }
    }
}
