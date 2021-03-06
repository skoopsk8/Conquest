package com.nasser.poulet.conquest.menu;

import com.nasser.poulet.conquest.controller.Timer;
import com.nasser.poulet.conquest.model.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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
    private GameView gameView;
    private TextArea textarea;
    private UpdatableLabel updatableLabel;

    private boolean keyboard = false;   // Require keyboard listening

    private int wait = 0;     // Timer for auto skip
    public String action = "";   // Return value

    Event event = null;

    private boolean timerCreated = false;
    private boolean onLoad = true;

    public Menu(String filename) {
        this.filename = filename;
        // Parse the menu file
        this.start();
    }

    // Menu Grid 30*20

    public void reload(){
        System.out.println("Reload menu resources");
        for (UIElement uiElement : uiElements) {
            uiElement.reload();
        }
    }

    public void render(){
        renderer(true);
    }

    public void renderNoDisplay(){
        renderer(false);
    }

    public void renderer(boolean display) {
        // Reset action
        action = "";

        // Event for timer escape
        if (!timerCreated) {
            if (wait != 0) {
                event = new Event(1, wait, this, new Callback<Menu>() {
                    public void methodCallback(Menu menu) {
                        menu.action = "continue";
                    }
                });
            }
            timerCreated = true;
        }
        int currentSnapshot = Timer.addSnapshot();
        Timer.updateSnapshot(currentSnapshot);

        // Display close
        if (Display.isCloseRequested())
            action = "quit";

        // Clean the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Render
        for (UIElement uiElement : uiElements) {
        	if(!uiElement.isHidden())
        		uiElement.render();
        }

        // Hover action
        for (UIElement uiElement : uiElements) {
            if (uiElement.getType() != "background" && !uiElement.isHidden())
                uiElement.hover(Mouse.getX(), (-Mouse.getY()) + Display.getHeight());
        }

        // Click action
        while(Mouse.next()){
            if(!Mouse.getEventButtonState()){
                if(Mouse.getEventButton() == 0){
                    for (int i = 0; i < uiElements.size() && action.equals(""); i++) {
                        action = uiElements.get(i).click(Mouse.getX(), (-Mouse.getY() + Display.getHeight()));
                        // Activate Input
                        if(action.equals("input_activate")){
                            selectedInput = (Input)uiElements.get(i);
                        }
                    }
                }
            }
        }

        // reset action
        if(action.equals("input_activate"))
            action = "";
        
        // Select first input
        if(onLoad) {
	        for (int i = 0; i < uiElements.size() && action.equals(""); i++) {
	        	if(uiElements.get(i).getType().equals("input")) {
	        		if(((Input)uiElements.get(i)).isSelected()) {
		        		if(selectedInput != uiElements.get(i)) {
		        			selectedInput = (Input)uiElements.get(i);
			                onLoad = false;
		        		}
	        		}
	        	}
	        }
        }

        // Get keyboard input
        if (keyboard) {
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {   // Only pressed keys
                    if (selectedInput != null) {
                    	switch(selectedInput.keyboard(Keyboard.getEventCharacter(), Keyboard.getEventKey())) {
                    	case Keyboard.KEY_ESCAPE:
                        	action = "quit";
                        break;
                    	case Keyboard.KEY_TAB:
                    		int sip = -1;
                            for (int i = 0; i < uiElements.size() && action.equals(""); i++) {
                            	if(uiElements.get(i).getType().equals("input") || uiElements.get(i).getType().equals("password")) {
                            		if(selectedInput != uiElements.get(i)) {
                            			action = "input_activate";
                            			sip = i;
                            		}
                            	}
                              
                                // Activate Input
                                if(action.equals("input_activate") && sip != -1){
                                    selectedInput = (Input)uiElements.get(sip);
                                }
                            }
                    		break;
                    	}
                    }
                }
            }
        }

        if (wait != 0) if (Timer.duration(event.getLastCall(), currentSnapshot) > event.getInterval())
            event.call(); // Call wait event update

        // Redraw
        if(display)
            Display.update();
    }

    public void updateVariable(int id, String string, String name){
        for (UIElement uiElement : uiElements) {
            if(uiElement.getName() != null){
                if (uiElement.getName().equals(name))
                    if(uiElement.getType().equals("updatableLabel"))
                        ((UpdatableLabel)uiElement).updateVariable(id, string);
            }
        }
    }

    public void updateText(String text, String name) {
        for (UIElement uiElement : uiElements) {
            if(uiElement.getName() != null){
                if (uiElement.getName().equals(name))
                    if(uiElement.getType().equals("textarea"))
                        ((TextArea)uiElement).addText(text);
                    else
                        uiElement.setText(text);
            }
        }
    }

    public UIElement getElement(String name) {
        for (UIElement uiElement : uiElements) {
            if(uiElement.getName() != null){
                if (uiElement.getName().equals(name))
                    return uiElement;
            }
        }
        return null;
    }

    public String getText(String name) {
        for (UIElement uiElement : uiElements) {
            if(uiElement.getName() != null){
                if (uiElement.getName().equals(name))
                    return uiElement.getText();
            }
        }
        return  "";
    }

    private void start() {
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = null;
        try {
            parseur = fabrique.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        File fichier = new File("data/" + filename + "/menu.xml");
        try {
            parseur.parse(fichier, this);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("objects")) {
            uiElements = new LinkedList<UIElement>();
        } else if (qName.equals("background")) {
            element = new Background();
            element.setType("background");
        } else if (qName.equals("label")) {
            element = new Label();
            element.setType("label");
        }  else if (qName.equals("updatableLabel")) {
            element = new UpdatableLabel();
            element.setType("updatableLabel");
        } else if (qName.equals("button")) {
            button = new Button();
            element = button;
            element.setType("button");
        } else if (qName.equals("input")) {
            input = new Input();
            button = input;
            element = button;
            element.setType("input");
        }  else if (qName.equals("password")) {
            input = new Password();
            button = input;
            element = button;
            element.setType("password");
        } else if (qName.equals("textarea")) {
            textarea = new TextArea();
            element = textarea;
            element.setType("textarea");
        }
        else if (qName.equals("gameView")) {
            gameView = new GameView();
            element = gameView;
            element.setType("gameView");
        } else {
            buffer = new StringBuffer();
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("label")) {
            uiElements.add(element);
            element = null;
        } else if (qName.equals("background")) {
            uiElements.add(element);
            element = null;
        }  else if (qName.equals("updatableLabel")) {
            uiElements.add(element);
            element = null;
        } else if (qName.equals("button")) {
            uiElements.add(element);
            element = null;
            button = null;
        } else if (qName.equals("gameView")) {
            uiElements.add(element);
            element = null;
            gameView = null;
        } else if (qName.equals("input")) {
            uiElements.add(element);
            selectedInput = input;
            element = null;
            input = null;
        } else if (qName.equals("password")) {
            uiElements.add(element);
            selectedInput = input;
            element = null;
            input = null;
        } else if (qName.equals("selected")) {
            ((Input)element).setSelected(true);
            buffer = null;
        } else if (qName.equals("hidden")) {
            element.setHidden(true);
            buffer = null;
        }else if (qName.equals("textarea")) {
            uiElements.add(element);
            element = null;
            textarea = null;
        }  else if (qName.equals("lines")) {
            ((TextArea)element).setLines(Integer.getInteger(buffer.toString()));
            element = null;
        } else if (qName.equals("name")) {
            element.setName(buffer.toString());
            buffer = null;
        } else if (qName.equals("image")) {
            ((UIElementImage) element).setImg1(buffer.toString());
            buffer = null;
        } else if (qName.equals("text")) {
            element.setText(buffer.toString());
            buffer = null;
        } else if (qName.equals("posX")) {
            element.setPosX(Integer.parseInt(buffer.toString()));
            buffer = null;
        } else if (qName.equals("posY")) {
            element.setPosY(Integer.parseInt(buffer.toString()));
            buffer = null;
        } else if (qName.equals("width")) {
            if(button!=null)
                button.setWidth(Integer.parseInt(buffer.toString()));
            else if(gameView!=null)
                gameView.setWidth(Integer.parseInt(buffer.toString()));
            else if(textarea!=null)
            	textarea.setWidth(Integer.parseInt(buffer.toString()));
            buffer = null;
        } else if (qName.equals("height")) {
            if(button!=null)
                button.setHeight(Integer.parseInt(buffer.toString()));
            else if(gameView!=null)
                gameView.setHeight(Integer.parseInt(buffer.toString()));
            else if(textarea!=null)
            	textarea.setHeight(Integer.parseInt(buffer.toString()));
            buffer = null;
        } else if (qName.equals("action")) {
            button.setAction(buffer.toString());
            buffer = null;
        } else if (qName.equals("wait")) {
            this.wait = Integer.parseInt(buffer.toString());
            buffer = null;
        } else if (qName.equals("fontName")) {
            element.setFont(buffer.toString());
            buffer = null;
        } else if (qName.equals("size")) {
            element.setSize(buffer.toString());
            buffer = null;
        } else if (qName.equals("keyboard")) {
            this.keyboard = true;
            buffer = null;
        }
    }

    public void characters(char[] ch, int start, int length) {
        String lecture = new String(ch, start, length);
        if (buffer != null) buffer.append(lecture);
    }
}
