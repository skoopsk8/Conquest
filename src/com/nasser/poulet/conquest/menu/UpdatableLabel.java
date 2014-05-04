package com.nasser.poulet.conquest.menu;

import com.sun.deploy.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thomas on 5/4/14.
 */
public class UpdatableLabel extends Label {
    private String[] variables;
    private String[] decomposedText;
    private String originalText;

    public void render(){
        this.text = recomposeText();
        super.render(); // render the text
    }

    public void updateVariable(int id, String value){
        variables[id] = value;
    }

    @Override
    public void setText(String text){
        this.originalText = text;
        this.text = originalText;
        parseText(text);
    }

    private void parseText(String text){
        decomposedText = text.split(" ");
        int counter = 0; // Count the number of variables
        for(String word: decomposedText){
            if(word.matches("\\$[0-9]*"))
                counter++;
        }

        variables = new String[counter];
    }

    private String recomposeText(){
        StringBuilder builder = new StringBuilder();

        for (String string : decomposedText) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            if(string.matches("\\$[0-9]*"))
                builder.append(variables[Integer.parseInt(string.substring(1))]);
            else
                builder.append(string);
        }

        return builder.toString();
    }
}
