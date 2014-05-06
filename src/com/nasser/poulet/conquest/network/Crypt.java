package com.nasser.poulet.conquest.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Thomas on 5/3/14.
 */
public class Crypt {
    private static String salt="roflcopterlol";
    private static MessageDigest digester;

    static {
        try {
            digester = MessageDigest.getInstance("SHA-512");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String value){
        String salted = value + salt;

        digester.update(salted.getBytes());
        byte[] hash = digester.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            }
            else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }
}
