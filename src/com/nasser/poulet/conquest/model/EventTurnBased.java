package com.nasser.poulet.conquest.model;

import com.nasser.poulet.conquest.controller.Timer;

/**
 * Created by Lord on 15/12/13.
 */

public class EventTurnBased {
    private int occurrence;     // Occurrence before auto-destroy MI style
    private Callback callback;
    private int life;
    private int age;
    private Object context; // For callback re context

    public EventTurnBased(int occurrence, int life, Object context, Callback callback){
        this.occurrence = occurrence;
        this.callback = callback;
        this.life = life;
        this.age = 0;
        this.context = context;
    }

    public boolean call(){
        age++;
        if(age==life){
            this.callback.methodCallback(this.context);
            if(this.occurrence!=-1){    // -1 = infinity
                this.occurrence--;

                if(this.occurrence <= 0) return true;  // Auto destroy
            }
            age = 0;
        }
        return false;
    }
}
