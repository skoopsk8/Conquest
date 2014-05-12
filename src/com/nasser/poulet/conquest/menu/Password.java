package com.nasser.poulet.conquest.menu;

import org.lwjgl.opengl.Display;

public class Password extends Input {

	public Password() {
		
	}
	
	@Override
	public void render() {
		int ratioX = Display.getWidth()/30;
        int ratioY = Display.getHeight()/20;
		img1.draw(posX*ratioX,posY*ratioY,32,this.height*ratioY);
	    img2.draw(posX*ratioX + 32, posY*ratioY,this.width*ratioX-64,this.height*ratioY);
		img3.draw(posX*ratioX + ((this.width*ratioX/32)*32)-32, posY*ratioY,32,this.height*ratioY);

		String line = "*";
		
		int car = (this.width * ratioX) /  Font.getFont(font+":"+size).getWidth(line);
		String hidden = "";
		for(int i = 0; i < text.length(); i++) hidden+="*";

        if(text.length() < car) {
        	Font.getFont(font+":"+size).drawString(posX*ratioX + ((this.getWidth()*ratioX)-Font.getFont(font+":"+size).getWidth(hidden))/2, posY*ratioY + 10, hidden, color);
        }
        else {
        	Font.getFont(font+":"+size).drawString(posX*ratioX + ((this.getWidth()*ratioX)-Font.getFont(font+":"+size).getWidth(hidden.substring(text.length() - car)))/2, posY*ratioY + 10, hidden.substring(text.length() - car), color);
        }
	}

}
