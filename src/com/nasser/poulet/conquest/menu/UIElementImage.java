package com.nasser.poulet.conquest.menu;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class UIElementImage extends UIElement {
	
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

	private Image img1, img2, img3;
	
	public UIElementImage() {
		img1 = null;
		img2 = null;
		img3 = null;
	}
	
	public UIElementImage(String img) {
		try {
			img1 = new Image(img);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		img2 = null;
		img3 = null;
	}
	
	public UIElementImage(String img1, String img2, String img3) {
		try {
			this.img1 = new Image(img1);
			this.img2 = new Image(img2);
			this.img3 = new Image(img3);
		} catch (SlickException e) {
		

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void render() {
		// TODO Auto-generated method stub
		// afire le render des images mais que button (enfant) appel le render de son parent pour faire le render des images
		if(img3 == null && img2 == null) {
			img1.draw(posX, posY);
		}
		else {
			img1.draw(posX,posY);
			for(int x = 1; x < 20; x++) {
				img2.draw(posX + 7*x, posY);
			}
			img3.draw(posX + 140, posY);
		}
	}

	public Image getImg1() {
		return img1;
	}

	public void setImg1(Image img1) {
		this.img1 = img1;
	}

	@Override
	public String click(int posX, int posY) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String hover(int posX, int posY) {
		// TODO Auto-generated method stub
		 if(inside(posX, posY)){
			 img1.setAlpha(0.8f);
			 render();
		 }
		 img1.setAlpha(1f);
		 render();
		return null;
	}

	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
    public boolean inside( int posX, int posY ){
        if(posX>=this.posX && posX<=this.posX+this.width){
            if(posY>=this.posY && posY<=this.posY+this.height){
                return true;
            }
        }
        return false;
    }

}
