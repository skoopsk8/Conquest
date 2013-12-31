package com.nasser.poulet.conquest.menu;

/**
 * Created by Thomas on 12/31/13.
 */
public class Input extends Button {
    public void keyboard( int keyEvent ){
        switch (keyEvent){
            case 14:
                if (text.length() > 0) {
                    text = text.substring(0, text.length()-1);
                }
                break;
            case 2:
                text+="1";
                break;
            case 3:
                text+="2";
                break;
            case 4:
                text+="3";
                break;
            case 5:
                text+="4";
                break;
            case 6:
                text+="5";
                break;
            case 7:
                text+="6";
                break;
            case 8:
                text+="7";
                break;
            case 9:
                text+="8";
                break;
            case 10:
                text+="9";
                break;
            case 11:
                text+="0";
                break;
            case 52:
                text+=".";
                break;
            case 53:
                text+=":";
                break;
        }
    }
}
