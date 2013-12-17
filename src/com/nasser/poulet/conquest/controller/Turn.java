package com.nasser.poulet.conquest.controller;

import com.nasser.poulet.conquest.model.Callback;
import com.nasser.poulet.conquest.model.Event;

import java.util.*;

/**
 * Created by Lord on 12/12/13.
 */
public class Turn {
    private int TURN_DURATION = 1500;

    private boolean pause;
    private int turnNumber;

    private Timer timer;

    private int gameSnapshot;
    private int turnSnapshot;
    private int pauseSnapshot;
    private int currentSnapshot;

    private Vector<Event> eventList;

    public Turn(){
        this.timer = new Timer();
        this.gameSnapshot = this.timer.addSnapshot();
        this.turnSnapshot = this.timer.addSnapshot();
        this.pauseSnapshot = this.timer.addSnapshot();
        this.currentSnapshot = this.timer.addSnapshot();

        this.pause = false;
        this.turnNumber = -1;

        this.eventList = new Vector<Event>();
    }

    public void startTurn(){
        turnNumber++;
        System.out.println("Starting turn number: "+this.turnNumber);
        this.timer.updateSnapshot(this.turnSnapshot);
    }

    public long update(){
        if(!pause){
            // TODO : Add the pause in the duration calculation
            if (this.timer.duration(this.turnSnapshot, this.currentSnapshot)>=TURN_DURATION) this.startTurn();  // If more than 1s has elapsed

            this.timer.updateSnapshot(this.currentSnapshot);

    //       for(Event ev: eventList){
            for(int i=0; i < this.eventList.size(); i++){
               if(eventList.get(i).call()) {
                   this.eventList.remove(eventList.get(i));
                   i--;
               }
           }
        }
            return this.timer.duration(this.turnSnapshot, this.currentSnapshot);
    }

    public void addEvent( ){
        this.eventList.add(new Event(1, new Callback() {
            public void methodCallback() {                   // Context loss
                System.out.println("Callbacked!");
            }
        }));
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
        this.timer.updateSnapshot(this.pauseSnapshot);
        System.out.println("Pause : " + pause);
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void stop(){
        this.timer.duration(this.gameSnapshot, this.currentSnapshot);   // Game duration
    }
}
