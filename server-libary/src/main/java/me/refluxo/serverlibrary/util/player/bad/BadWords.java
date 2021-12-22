package me.refluxo.serverlibrary.util.player.bad;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BadWords {

    public static ArrayList<String> badWords;

    public static void init() {
        badWords = new ArrayList<>();
        badWords.add("arsch");
        badWords.add("arschloch");
        badWords.add("hure");
        badWords.add("hurensohn");
        badWords.add("hurentochter");
        badWords.add("schlampe");
        badWords.add("nude");
        badWords.add("idiot");
        badWords.add("vollidiot");
        badWords.add("itiot");
        badWords.add("missgeburt");
        badWords.add("ich töte dein");
        badWords.add("fuck");
        badWords.add("fick");
        badWords.add("dick");
        badWords.add("penis");
        badWords.add("fuck");
        badWords.add("pornhub");
        badWords.add("kacke");
        badWords.add("scheiße");
        badWords.add("*");
        badWords.add("porn");
        badWords.add("beschissen");
        badWords.add("download server");
    }

    public static boolean contains(String string) {
        AtomicBoolean b = new AtomicBoolean(false);
        badWords.forEach(s -> {
            if(string.toLowerCase().contains(s.toLowerCase())) {
                b.set(true);
            }
        });
        return b.get();
    }

}
