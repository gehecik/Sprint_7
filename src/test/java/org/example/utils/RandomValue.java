package org.example.utils;

import java.util.Random;

public class RandomValue {
    public static String randomLogin() {
        int randomNum = new Random().nextInt();

        return String.format("user%d", randomNum);
    }

    public static String randomPassword() {
        int randomNum = new Random().nextInt();

        return String.format("%d", randomNum);
    }

    public static int randomId() {
        int randomNum = new Random().nextInt();

        return randomNum;
    }
}
