package com.nasser.poulet.conquest.menu;

/**
 * Created by Thomas on 12/31/13.
 */
public class Input extends Button {
    public void keyboard( char keyChar, int keyEvent ){    	
    	switch(keyEvent) {
	    	case 14:
	            if (text.length() > 0) {
	                text = text.substring(0, text.length()-1);
	            }
	         break;
	    	case 57:
	    		text+=" ";
	    		break;
	    	case 211:
	    		text = "";
	    	case 28:
	    		// On appuie sur Retour
	    		break;
	         default:
	        	 boolean isCorrect = Character.isAlphabetic(keyChar) || Character.isDigit(keyChar) || keyChar == ':' || keyChar == '/' || keyChar == ',' || keyChar == '!' || keyChar == '\'' || keyChar == '.' || keyChar == '_';
	        	 if(isCorrect)
	        		 text+=keyChar;
    	}
    }
}
