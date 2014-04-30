package com.nasser.poulet.conquest.view;

import org.newdawn.slick.SlickException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Thomas on 4/30/14.
 */
public class Image {
    static public Map<String, org.newdawn.slick.Image> images = new Hashtable<String, org.newdawn.slick.Image>();

    static public org.newdawn.slick.Image getImage(String filename){
        if(!images.containsKey(filename)){
            System.out.println("Load image: "+filename);
            try {
                images.put(filename, new org.newdawn.slick.Image(filename));
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
        return images.get(filename);
    }

    static public void destroy(){
        for(Map.Entry<String, org.newdawn.slick.Image> entry : images.entrySet()){
            try {
                entry.getValue().destroy();
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
        images.clear();
    }
}
