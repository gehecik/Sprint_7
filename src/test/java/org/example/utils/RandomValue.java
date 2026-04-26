package org.example.utils;

import java.util.Random;

public class RandomValue {
    public static String random(String name) {
        int randomNum = new Random().nextInt();

        return String.format(name + "%d", randomNum);
    }


    public static String randomNameWithDigit() {
         return random("user");
    }

    public static String randomNumberAsString() {
        return random("");
    }

    public static int randomNumber() {
        int randomNum = new Random().nextInt();

        return randomNum;
    }
}
