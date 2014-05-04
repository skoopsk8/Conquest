package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class TextArea extends UIElementImage {
	
	private int lines;
	private String content;
	private String font = "Arial";
    private String size = Integer.toString(Display.getHeight() / 20 - 20);

	public TextArea() {
		super("data/img/void.png", "data/img/void.png", "data/img/TextAreaBorder.png");
		lines = 5;
		String newLine = System.getProperty("line.separator");
		content = "Hello bro" + newLine + "Hello";
	}
	
	public TextArea(int lines) {
		super("data/img/void.png", "data/img/void.png", "data/img/TextAreaBorder.png");
		this.lines = lines;
		content = "Hello bro";
	}
	
	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override 
	public void render() {
		int ratioX = Display.getWidth()/30;
        int ratioY = Display.getHeight()/20;
       /**/
		img1.draw(posX*ratioX,posY*ratioY,32,this.height*ratioY);
		//img1.draw(50,50);
		/*
		img1.draw(posX*ratioX,posY*ratioY,32,this.height*ratioY);
		
	    img2.draw(posX*ratioX + 32, posY*ratioY,this.width*ratioX-64,this.height*ratioY);
	    img2.draw(posX*ratioX + 32, posY*ratioY,this.width*ratioX-64,this.height*ratioY);
	    
	    img3.setCenterOfRotation(img3.getWidth() / 2, img3.getHeight() / 2);
		img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);
		img3.setRotation(90);
		img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);
		img3.setRotation(180);
		img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);
		img3.setRotation(270);
		img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);
		*/
		Font.getFont(font+":"+size).drawString(posX*ratioX + ((this.getWidth()*ratioX)-Font.getFont(font+":"+size).getWidth(content))/2, posY*ratioY + 10, content, Color.white);
		//Font.getFont(font+":"+size).drawString(posX, posY, content, Color.white);
	}
	
	public void addText(String text) {
		String newLine = System.getProperty("line.separator");
		content+=newLine + text;
	}

}
