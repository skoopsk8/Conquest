package com.nasser.poulet.conquest.model;

/**
 * Created by Lord on 11/12/13.
 */
public class Unit {
    private Loyalty loyalty;
    private State state;

    public Loyalty getLoyalty() {
        return loyalty;
    }

    public Unit( Loyalty loyalty ) {
        this.loyalty = loyalty;

    }
}
