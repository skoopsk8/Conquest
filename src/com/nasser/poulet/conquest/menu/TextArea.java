package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class TextArea extends UIElementImage {
	
	private int lines, car;
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
		lines = 0;
		car = 0;
	}

	
	@Override
	public void setWidth(int width) {
		this.width = width;
		
		String line = "Azerty";
		int ratioX = Display.getWidth()/30;
		
		this.car = (this.width * ratioX) /  Font.getFont(font+":"+size).getWidth(line);
	}
	
	@Override
	public void setHeight(int height) {
		this.height = height;
		
		String line = "Azerty";
		int ratioY = Display.getHeight()/20;
		this.lines = (this.height * ratioY) /  Font.getFont(font+":"+size).getHeight(line);
		
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
		img1.draw(posX*ratioX,posY*ratioY - 1, this.width*ratioX, 1);
		img1.draw(posX*ratioX,posY*ratioY + this.height*ratioY, this.width*ratioX, 1);
		
	    img2.draw(posX*ratioX, posY*ratioY, 1,this.height*ratioY);
	    img2.draw(posX*ratioX + this.width*ratioX, posY*ratioY, 1,this.height*ratioY);
	    
	    /* BORDS RONDS NE FONCTIONNE PAS pour l'instant a cause de rotation
	    this.corner[0].draw(posX*ratioX, posY*ratioY - 5);
	    this.corner[1].draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY);/*
		img3.setRotation(180);
		img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);
		img3.setRotation(270);
		img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);*/
		
		for(int i = 0; i < this.lines; i++){
			if(!content[i].equals(""))
				Font.getFont(font+":"+size).drawString(posX*ratioX, posY*ratioY + 10 + Font.getFont(font+":"+size).getHeight(content[i]) * i, content[i], Color.white);
		}
	}
	
	public void addText(String text) {
		/*if(text.length() > this.car) {
			char[] temp = new char[this.car];
			text.getChars(0, 39, temp, 0);
			String word = new String(temp);
			addText(word);
			
		}
		else {*/
			for(int i = 1; i < lines; i++) {
				content[i - 1] = content[i];
			}
			content[lines-1] = text;
		//}
	}
}
