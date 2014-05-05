package com.nasser.poulet.conquest.server.chat;

/**
 * Created by Thomas on 4/4/14.
 */
public class ChatMessage {
    //String
    private String original;
    private boolean isCommand = false;
    private String message;
    private String command;
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public ChatMessage( String message ){
        this.original = message;
        decode();
    }

    public void decode(){
        if(original.startsWith("/")){
            isCommand = true;
            String[] temp = (original.substring(1)).split(" ");
            this.command = temp[0];
            if(2+this.command.length()<original.length())
                this.message = original.substring(2+this.command.length());
            else
                this.message = "";
            generateCommandAnswer();
        }
        else{
            this.message = this.original;
        }
    }

    public boolean isCommand() {
        return isCommand;
    }

    public String getMessage() {
        return message;
    }

    public String getCommand(){
        return command;
    }

    private void generateCommandAnswer(){
        if(command.equals("help")){
            answer = "Help coming soon!";
        }
        else if(command.equals("join")){
            answer = "Change channel";
        }
        else if(command.equals("list")){
            answer = "Listing the current channels: ";
        }
        else if(command.equals("who")){
            answer = "Listing the current users in channel: ";
        }
        else{
            answer = "";
        }
    }
}
