package com.nasser.poulet.conquest;

import com.nasser.poulet.conquest.model.Button;
import com.nasser.poulet.conquest.model.Label;
import com.nasser.poulet.conquest.model.UIElement;
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
    private String filename;

    private List<UIElement> uiElements;

    private StringBuffer buffer;

    private UIElement element;
    private Label label;
    private Button button;

    public Menu(String filename){
        this.filename = filename;

        // Parse the menu file
        this.start();

        // Render the board and the menu
        this.render();
    }

    private void render(){
        // prepare the display
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        while(!Display.isCloseRequested()){
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            for(UIElement uiElement : uiElements){
                uiElement.render();
            }
            Display.update();
        }

        GL11.glDisable(GL11.GL_BLEND);  // Disable Blend otherwise game won't work
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
        }
        else if(qName.equals("button")){
            element = new Button();
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
    }

    public void characters(char[] ch,int start, int length){
        String lecture = new String(ch,start,length);
        if(buffer != null) buffer.append(lecture);
    }

}
