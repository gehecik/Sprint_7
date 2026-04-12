package org.example.utils;

import java.util.Random;

public class RandomLogin {
    public static String randomLogin() {
        Random random = new Random();
        int randomNum = random.nextInt();

        return String.format("user%d", randomNum);
    }
}
