package com.helloscala.web.utils;

import java.util.Random;


public class RandomUtil {
    private static final String LETTER = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final  Random RANDOM = new Random();

    public static String generationCapital(Integer number){
        StringBuilder stringBuilder = new StringBuilder();
        char[] c = LETTER.toCharArray();
        for( int i = 0; i < number; i ++) {
            stringBuilder.append(c[RANDOM.nextInt(c.length)]);
        }
        return stringBuilder.toString();
    }

    public static String generationNumberChar(Integer number){
        StringBuilder stringBuilder = new StringBuilder();
        for( int i = 0; i < number; i ++) {
            stringBuilder.append(RANDOM.nextInt(10));
        }
        return stringBuilder.toString();
    }

    public static Integer generationNumber(int number){
        return RANDOM.nextInt(number);
    }
}
