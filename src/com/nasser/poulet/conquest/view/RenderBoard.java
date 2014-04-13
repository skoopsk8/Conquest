package com.nasser.poulet.conquest.view;

import com.nasser.poulet.conquest.model.Board;
import com.nasser.poulet.conquest.model.State;
import com.nasser.poulet.conquest.model.Unit;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by Lord on 10/12/13.
 */
public class RenderBoard implements Render {
    private final int TILE_SIZE = 64;
    private final int BORDER_SIZE = 8;
    private Image[] images = new Image[5];

    public RenderBoard() {
        try {
            images[0] = new Image("data/img/map/0.png");
            images[1] = new Image("data/img/map/1.png");
            images[2] = new Image("data/img/map/2.png");
            images[3] = new Image("data/img/map/3.png");
            images[4] = new Image("data/img/map/4.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void render(){
    }

    public void render( Board board ) {
        // Clear the display
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Render Background
        for(int i=0;i<board.getBoardWidth();i++) {
            for (int j = 0; j < board.getBoardHeight(); j++) {
                renderBackground(i,
                                 j,
                                 board.getStateLoyalty(i,j),
                                 board.getStateLoyalty(i-1, j-1),
                                 board.getStateLoyalty(i, j-1),
                                 board.getStateLoyalty(i+1, j-1),
                                 board.getStateLoyalty(i+1, j),
                                 board.getStateLoyalty(i+1, j+1),
                                 board.getStateLoyalty(i, j+1),
                                 board.getStateLoyalty(i-1, j+1),
                                 board.getStateLoyalty(i-1, j));
            }
        }

        // Render the Board
        /*for(int i=0;i<board.getBoardWidth();i++) {
            for (int j = 0; j < board.getBoardHeight(); j++) {
                renderState(board.getState(i, j));
            }
        }*/
    }

    private void renderBackground(int x, int y, int state, int NO, int N, int NE, int E, int SE, int S, int SO, int O){
        Image tempImage = images[1].copy();

        if(O == 0 && NO == 0 && N == 0 && state == 1 && S == 1 && E == 1){
            tempImage = images[3].copy();
            tempImage.rotate(-90);
        }
        else if(O == 0 && SO == 0 && S == 0 && state == 1 && N == 1 && E == 1){
            tempImage = images[3].copy();
            tempImage.rotate(180);
        }
        else if(S == 0 && SE == 0 && E == 0 && state == 1 && O == 1 && N == 1){
            tempImage = images[3].copy();
            tempImage.rotate(90);
        }
        else if(N == 0 && NE == 0 && E == 0 && state == 1 && O == 1 && S == 1){
            tempImage = images[3].copy();
        }
        else if(NO == 0 && state == 1 && N == 1 && O == 1){
            tempImage = images[4].copy();
            tempImage.rotate(90);
        }
        else if(SO == 0 && state == 1 && S == 1 && O == 1){
            tempImage = images[4].copy();
        }
        else if(SE == 0 && state == 1 && S == 1 && E == 1){
            tempImage = images[4].copy();
            tempImage.rotate(-90);
        }
        else if(NE == 0 && state == 1 && E == 1 && N == 1){
            tempImage = images[4].copy();
            tempImage.rotate(180);
        }
        else if(O == 0 && N == 1 && state == 1 && S ==1){
            tempImage = images[2].copy();
            tempImage.rotate(180);
        }
        else if(E == 0 && N == 1 && state == 1 && S ==1){
            tempImage = images[2].copy();
        }
        else if(N == 0 && O == 1 && state == 1 && E ==1){
            tempImage = images[2].copy();
            tempImage.rotate(-90);
        }
        else if(S == 0 && O == 1 && state == 1 && E ==1){
            tempImage = images[2].copy();
            tempImage.rotate(90);
        }
        else if(state == 1){
            tempImage = images[0].copy();
        }
        else if(state == 0){
            tempImage = images[1].copy();
        }

        tempImage.draw(x*(64+8),y*(64+8),64+8,64+8);
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
            GL11.glVertex2f((state.getPosX()*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2),(state.getPosY()*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2));
            GL11.glVertex2f((state.getPosX()*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2),(state.getPosY()*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2));
            GL11.glVertex2f((state.getPosX()*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2),(state.getPosY()*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2));
            GL11.glVertex2f((state.getPosX()*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2),(state.getPosY()*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2));
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
            GL11.glVertex2f((posX*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2),(posY*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+TILE_SIZE-(TILE_SIZE*level));
            GL11.glVertex2f((posX*(TILE_SIZE+BORDER_SIZE))+TILE_SIZE+(BORDER_SIZE/2),(posY*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2)+TILE_SIZE-(TILE_SIZE*level));
            GL11.glVertex2f((posX * (TILE_SIZE + BORDER_SIZE)) + TILE_SIZE + (BORDER_SIZE / 2), (posY * (TILE_SIZE + BORDER_SIZE)) - TILE_SIZE / 5 + (BORDER_SIZE / 2) + TILE_SIZE-((TILE_SIZE-(2*TILE_SIZE/5))*level));
            GL11.glVertex2f((posX*(TILE_SIZE+BORDER_SIZE))+(BORDER_SIZE/2),(posY*(TILE_SIZE+BORDER_SIZE))-TILE_SIZE/5+(BORDER_SIZE/2)+TILE_SIZE-((TILE_SIZE-(2*TILE_SIZE/5))*level));
        GL11.glEnd();
    }
}
