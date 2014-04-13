package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

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

	private Image img1=null, img2=null, img3=null;
	
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
		if(img3 == null && img2 == null) {
			img1.draw(posX, posY,Display.getWidth(), Display.getHeight());
		}
		else {
            int ratioX = Display.getWidth()/30;
            int ratioY = Display.getHeight()/20;
			img1.draw(posX*ratioX,posY*ratioY,32,this.height*ratioY);
		    img2.draw(posX*ratioX + 32, posY*ratioY,this.width*ratioX-64,this.height*ratioY);
			img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);
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
		return "";
	}

	@Override
	public boolean hover(int posX, int posY) {
		// TODO Auto-generated method stub
		 if(inside(posX, posY)){
			 img1.setAlpha(0.8f);
			 render();
             return true;
		 }
		 img1.setAlpha(1f);
		 render();
		return false;
	}

	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return null;
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
