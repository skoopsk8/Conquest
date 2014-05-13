package com.nasser.poulet.conquest.menu;

import org.lwjgl.input.Keyboard;

/**
 * Created by Thomas on 12/31/13.
 */
public class Input extends Button {
    public void keyboard( char keyChar, int keyEvent ){    	
    	switch(keyEvent) {
	    	case Keyboard.KEY_BACK:
	            if (text.length() > 0) {
	                text = text.substring(0, text.length()-1);
	            }
	         break;
	    	case Keyboard.KEY_SPACE:
	    		text+=" ";
	    		break;
	    	case Keyboard.KEY_DELETE:
	    		text = "";
	    		break;
	    	case Keyboard.KEY_RETURN:
	    		text+=Keyboard.KEY_INSERT;
	    	break;
	    	case Keyboard.KEY_ESCAPE:
	    		
	    		break;
	         default:
	        	 boolean isCorrect = Character.isAlphabetic(keyChar) || Character.isDigit(keyChar) ||keyChar == ':' || keyChar == '/' || keyChar == ',' || keyChar == '!' || keyChar == '?' || keyChar == '\'' || keyChar == '.' || keyChar == '_' || keyChar == '^';
	        	 if(isCorrect){
	        		 text+=keyChar;	 
	        	 }
	        	 
    	}
    }
    public String click( int posX, int posY){
        if(super.inside(posX, posY)){
            return "input_activate";
        }
        return "";
    }
}
