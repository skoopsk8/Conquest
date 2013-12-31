package com.nasser.poulet.conquest.menu;

import com.nasser.poulet.conquest.controller.Timer;
import com.nasser.poulet.conquest.model.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas on 12/29/13.
 */
public class Menu extends DefaultHandler{
    private String filename;    // folder name

    private List<UIElement> uiElements; // Contains all the active elements of the menu

    private StringBuffer buffer;    // String buffer for parsing

    private UIElement element;
    private Button button;
    private Input input;
    private Input selectedInput = null;

    private boolean keyboard = false;   // Require keyboard listening

    private int wait=0;     // Timer for auto skip
    String action = null;   // Return value

    public Menu(String filename){
        this.filename = filename;

        // Parse the menu file
        this.start();
    }

    public String[] render(){
        // Optimize the display for text
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Event for "wait" escape
        Event event = null;
        if(wait!=0){
            event = new Event(1, wait, this, new Callback<Menu>() {
                public void methodCallback(Menu menu) {
                    menu.action = "continue";
                }
            });
        }

        int currentSnapshot = Timer.addSnapshot();

        while(action==null){
            Timer.updateSnapshot(currentSnapshot);

            if(Display.isCloseRequested())
                action = "quit";

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            for(UIElement uiElement : uiElements){
                uiElement.render();
            }

            // Hover action
            for(UIElement uiElement : uiElements){
                uiElement.hover(Mouse.getX(), (-Mouse.getY()+600));
            }

            // Click action
            if(Mouse.isButtonDown(0)){
                for(int i=0; i<uiElements.size() && action==null ; i++){
                    action = uiElements.get(i).click(Mouse.getX(), (-Mouse.getY()+600));
                }
            }

            // Get selected input
            if(action!=null){
                for(UIElement uiElement : uiElements){
                    if(uiElement.getType().equals("input")){
                        if(uiElement.getAction().equals(action)){
                            selectedInput = (Input)uiElement;
                            action = null;
                            break;
                        }
                    }
                }
            }

            // Get keyboard input
            if(keyboard){
                while(Keyboard.next()){
                    if(Keyboard.getEventKeyState()) {   // Only pressed keys
                        if(selectedInput!=null)
                            selectedInput.keyboard(Keyboard.getEventKey());
                    }
                }
            }

            if(wait!=0)if(Timer.duration(event.getLastCall(), currentSnapshot)>event.getInterval())event.call(); // Call wait event update

            Display.update();
        }

        GL11.glDisable(GL11.GL_BLEND);  // Disable Blending otherwise the game won't work

        String[] returnValue = new String[2];
        returnValue[0] = action;

        if(action.equals("join")){
            returnValue[1] = selectedInput.getText();
        }

        return returnValue;
    }

    private void start(){
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = null;
        try {
            parseur = fabrique.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        File fichier = new File("data/"+filename+"/menu.xml");
        try {
            parseur.parse(fichier, this);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes){
        if(qName.equals("objects")){
            uiElements = new LinkedList<UIElement>();
        }
        else if(qName.equals("label")){
            element = new Label();
            element.setType("label");
        }
        else if(qName.equals("button")){
            button = new Button();
            element = button;
            element.setType("button");
        }
        else if(qName.equals("input")){
            input = new Input();
            button = input;
            element = button;
            element.setType("input");
        }
        else {
            buffer = new StringBuffer();
        }
    }

    public void endElement(String uri, String localName, String qName){
        if(qName.equals("label")){
            uiElements.add(element);
            element = null;
        }
        else if(qName.equals("button")){
            uiElements.add(element);
            element = null;
        }
        else if(qName.equals("input")){
            uiElements.add(element);
            selectedInput = input;
            element = null;
        }
        else if(qName.equals("name")){
            element.setName(buffer.toString());
            buffer = null;
        }
        else if(qName.equals("text")){
            element.setText(buffer.toString());
            buffer = null;
        }
        else if(qName.equals("posX")){
            element.setPosX(Integer.parseInt(buffer.toString()));
            buffer = null;
        }
        else if(qName.equals("posY")){
            element.setPosY(Integer.parseInt(buffer.toString()));
            buffer = null;
        }
        else if(qName.equals("width")){
            button.setWidth(Integer.parseInt(buffer.toString()));
            buffer = null;
        }
        else if(qName.equals("height")){
            button.setHeight(Integer.parseInt(buffer.toString()));
            buffer = null;
        }
        else if(qName.equals("action")){
            button.setAction(buffer.toString());
            buffer = null;
        }
        else if(qName.equals("wait")){
            this.wait = Integer.parseInt(buffer.toString());
            buffer = null;
        }
        else if(qName.equals("fontName")){
            element.getFont().setFontName(buffer.toString());
            buffer = null;
        }
        else if(qName.equals("size")){
            element.getFont().setSize(Integer.parseInt(buffer.toString()));
            buffer = null;
        }
        else if(qName.equals("keyboard")){
            this.keyboard = true;
            buffer = null;
        }
    }

    public void characters(char[] ch,int start, int length){
        String lecture = new String(ch,start,length);
        if(buffer != null) buffer.append(lecture);
    }

}
