package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class UIElementImage extends UIElement {
	
	protected int width, height;
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

	protected Image img1=null, img2=null, img3=null;

    private String img1Name=null, img2Name=null, img3Name=null;
	
	public UIElementImage() {
		try {
			img1 = new Image("data/img/void.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		img2 = null;
		img3 = null;
	}
	
	public UIElementImage(String img) {
		try {
			img1 = new Image(img);
            img1Name = img;
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		img2 = null;
		img3 = null;
	}
	
	public UIElementImage(String img1, String img2, String img3) {
        this.img1 = com.nasser.poulet.conquest.view.Image.getImage(img1);
        this.img1Name = img1;
        this.img2 = com.nasser.poulet.conquest.view.Image.getImage(img2);
        this.img2Name = img2;
        this.img3 = com.nasser.poulet.conquest.view.Image.getImage(img3);
        this.img3Name = img3;
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
			img3.draw(posX*ratioX + this.width*ratioX-32, posY*ratioY,32,this.height*ratioY);
		}
	}

	public Image getImg1() {
		return img1;
	}

	public Image getImg2() {
		return img2;
	}

	public Image getImg3() {
		return img3;
	}

	public void setImg1(String img1) {
        this.img1 = com.nasser.poulet.conquest.view.Image.getImage(img1);
        this.img1Name = img1;
	}

	@Override
	public String click(int posX, int posY) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
    public boolean hover(int posX, int posY) {
        boolean returnValue = false;
        if(inside(posX, posY)){
            returnValue = true;
        }

        render();
        return returnValue;
    }

	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public void reload() {
        System.out.println("Reload Images");
        if(img1Name != null )
            this.img1 = com.nasser.poulet.conquest.view.Image.getImage(img1Name);
        if(img2Name != null )
            this.img2 = com.nasser.poulet.conquest.view.Image.getImage(img2Name);
        if(img3Name != null )
            this.img3 = com.nasser.poulet.conquest.view.Image.getImage(img3Name);

        // Update Font size
        size = Integer.toString(Display.getHeight()/20-20);
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
