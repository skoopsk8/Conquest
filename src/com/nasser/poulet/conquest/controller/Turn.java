package com.nasser.poulet.conquest.controller;

import com.nasser.poulet.conquest.model.Callback;
import com.nasser.poulet.conquest.model.Event;

import java.util.*;

/**
 * Created by Lord on 12/12/13.
 */
public class Turn{
    public static final int TURN_DURATION = 1500;

    private boolean pause;
    private int turnNumber;

    private int gameSnapshot, turnSnapshot, pauseSnapshot, currentSnapshot;
    static private ArrayList<Event> eventList = new ArrayList<Event>();

    public Turn(){
        this.gameSnapshot = Timer.addSnapshot();
        this.turnSnapshot = Timer.addSnapshot();
        this.pauseSnapshot = Timer.addSnapshot();
        this.currentSnapshot = Timer.addSnapshot();

        this.pause = false;
        this.turnNumber = -1;
    }

    public void startTurn(){
        turnNumber++;
        System.out.println("Starting turn number: "+this.turnNumber);
        Timer.updateSnapshot(this.turnSnapshot);
    }

    public long update(){
        if(!pause){
            // TODO : Add the pause in the duration calculation
            if (Timer.duration(this.turnSnapshot, this.currentSnapshot)>=TURN_DURATION) this.startTurn();  // If more than 1s has elapsed

            Timer.updateSnapshot(this.currentSnapshot);

    //       for(Event ev: eventList){
            for(int i=0; i < eventList.size(); i++){
               if(Timer.duration(eventList.get(i).getLastCall(), this.currentSnapshot)>eventList.get(i).getInterval()){
                   if(eventList.get(i).call()) {
                       eventList.remove(eventList.get(i));
                       i--;
                   }
               }
           }
        }
            return Timer.duration(this.turnSnapshot, this.currentSnapshot);
    }

    static public int addEvent(Event event){
        eventList.add(event);
        return eventList.size()-1;
    }

    static public void removeEvent( int index ){
        System.out.println("Remove event: "+index);
        //if(eventList.size()>index)
            eventList.remove(index);
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
        Timer.updateSnapshot(this.pauseSnapshot);
        System.out.println("Pause : " + pause);
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void stop(){
        Timer.duration(this.gameSnapshot, this.currentSnapshot);   // Game duration
        eventList.clear();
    }

    public static void clearEvent(){
        eventList.clear();
    }
}
