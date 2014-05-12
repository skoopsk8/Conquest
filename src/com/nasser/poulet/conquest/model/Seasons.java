package com.nasser.poulet.conquest.model;

/**
 * Created by Thomas on 5/12/14.
 */
public enum Seasons {
    WINTER("Winter"),
    SPRING("Spring"),
    SUMMER("Summer"),
    AUTUMN("Autumn")
    ;

    private Seasons(final String text) {
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
