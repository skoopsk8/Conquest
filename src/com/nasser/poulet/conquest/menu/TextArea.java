package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class TextArea extends UIElementImage {
	
	private int lines;
	private String[] content;
	private String font = "Arial";
    private String size = Integer.toString(Display.getHeight() / 20 - 20);
    private Image[] corner = new Image[4];

	public TextArea() {
		super("data/img/void.png", "data/img/void.png", "data/img/TextAreaBorder.png");
		for(int i = 0; i < 4; i++) {
			this.corner[i] = com.nasser.poulet.conquest.view.Image.getImage("data/img/TextAreaBorder.png");
			this.corner[i].setCenterOfRotation(this.corner[i].getWidth() / 2, this.corner[i].getHeight() / 2);
			this.corner[i].setRotation(i*90);
		}
		lines = 5;
		content = new String[lines];
		for(int i = 0; i < lines; i++) {
			content[i] = "";
		}
	}
	
	public TextArea(int lines) {
		super("data/img/void.png", "data/img/void.png", "data/img/TextAreaBorder.png");
		for(int i = 0; i < 4; i++) {
			this.corner[i] = com.nasser.poulet.conquest.view.Image.getImage("data/img/TextAreaBorder.png");
			this.corner[i].setCenterOfRotation(this.corner[i].getWidth() / 2, this.corner[i].getHeight() / 2);
			this.corner[i].setRotation(i*90);
		}
		this.lines = lines;
		content = new String[lines];
		for(int i = 0; i < lines; i++) {
			content[i] = "";
		}
	}
	
	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	@Override 
	public void render() {
		int ratioX = Display.getWidth()/30;
        int ratioY = Display.getHeight()/20;
       /*
		img1.draw(posX*ratioX,posY*ratioY,32,this.height*ratioY);
		img1.draw(50,50);
		/*
		img1.draw(posX*ratioX,posY*ratioY,32,this.height*ratioY);
		
	    img2.draw(posX*ratioX + 32, posY*ratioY,this.width*ratioX-64,this.height*ratioY);
	    img2.draw(posX*ratioX + 32, posY*ratioY,this.width*ratioX-64,this.height*ratioY);
	    
	    this.corner[0].draw(posX*ratioX, posY*ratioY - 5);
	    this.corner[1].draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY);/*
		img3.setRotation(180);
		img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);
		img3.setRotation(270);
		img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);*/
		
		for(int i = 0; i < this.lines; i++){
			if(!content[i].equals(""))
				Font.getFont(font+":"+size).drawString(posX*ratioX + ((this.getWidth()*ratioX)-Font.getFont(font+":"+size).getWidth(content[i]))/2, posY*ratioY + 10 + 20 * i, content[i], Color.white);
		}
	}
	
	public void addText(String text) {
		for(int i = 1; i < lines; i++) {
			content[i - 1] = content[i];
		}
		content[lines-1] = text;
	}

}
