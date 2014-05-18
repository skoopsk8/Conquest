package com.nasser.poulet.conquest.model;

/**
 * Created by Lord on 10/12/13.
 */
public enum Loyalty {
    NONE("Nobody"),
    EMPTY("Empty"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow"),
    BARBARIAN("Barbarian")
    ;


    private Loyalty(final String text) {
        this.text = text;
    }

    private final String text;

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
