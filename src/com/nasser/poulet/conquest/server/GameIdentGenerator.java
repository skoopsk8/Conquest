package com.nasser.poulet.conquest.server;

/**
 * Created by Thomas on 5/6/14.
 */
import java.math.BigInteger;
import java.security.SecureRandom;

public final class GameIdentGenerator {
    private SecureRandom random = new SecureRandom();

    public String generateGameIdent() {
        return new BigInteger(32, random).toString(32);
    }
}