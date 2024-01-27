package co.istad.elearningapi.util;

import java.util.Random;

public class RandomUtil {

    public static String random6Digits() {
        Random random = new Random();
        // Generate a random 6-digit number
        int randomNumber = 100000 + random.nextInt(900000);
        return String.valueOf(randomNumber);
    }
}
