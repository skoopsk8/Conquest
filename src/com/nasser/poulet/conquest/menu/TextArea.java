package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class TextArea extends UIElementImage {
	
	private int lines;
	private String[] content;
	private String font = "Arial";
    private String size = Integer.toString(Display.getHeight() / 20 - 20);

	public TextArea() {
		super("data/img/void.png", "data/img/void.png", "data/img/bg.png");
		lines = 0;
	}

    @Override
    public boolean hover(int posX, int posY) {
        return true;
    }
	
	@Override
	public void setWidth(int width) {
		this.width = width;
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
		super("data/img/void.png", "data/img/void.png", "data/img/bg.png");
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

        img3.draw(posX*ratioX, posY*ratioY, this.width*ratioX,this.height*ratioY);

		img1.draw(posX*ratioX,posY*ratioY - 1, this.width*ratioX, 1);
		img1.draw(posX*ratioX,posY*ratioY + this.height*ratioY, this.width*ratioX, 1);
		
	    img2.draw(posX*ratioX, posY*ratioY, 1,this.height*ratioY);
	    img2.draw(posX*ratioX + this.width*ratioX, posY*ratioY, 1,this.height*ratioY);

		for(int i = 0; i < this.lines; i++){
			if(!content[i].equals("")) {
				Font.getFont(font+":"+size).drawString(posX*ratioX, posY*ratioY + 10 + Font.getFont(font+":"+size).getHeight(content[i]) * i, content[i], Color.white);	
			}
		}
	}
	
	public void addText(String text) {
		int ratioX = Display.getWidth()/30;
		
		if(Font.getFont(font+":"+size).getWidth(text) < this.width*ratioX) {
			for(int i = 1; i < lines; i++) {
				content[i - 1] = content[i];
			}
			content[lines-1] = text;
		}
		else {
			int start = 0;
			
			while(start<text.length()) {
				int end = text.length();
				while(Font.getFont(font+":"+size).getWidth(text.substring(start, end)) > this.width*ratioX) {
					if (end>0) end--;
					System.out.println("Start : " + start + " end : " + end + " textlength : " + text.length());
				}
				for(int i = 1; i < lines; i++) {
					content[i - 1] = content[i];
				}
				content[lines-1] = text.substring(start, end);
				start = end;
				System.out.println("Start : " + start + " end : " + end + " textlength : " + text.length());
			}
		}
	}

    @Override
    public void reload() {
        size = Integer.toString(Display.getHeight() / 20 - 20);
        super.reload();
    }
	
}
