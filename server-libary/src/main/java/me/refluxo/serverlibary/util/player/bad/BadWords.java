package me.refluxo.serverlibary.util.player.bad;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BadWords {

    private static ArrayList<String> badwords;

    public static void init() {
        badwords = new ArrayList<>();
        badwords.add("arsch");
        badwords.add("arschloch");
        badwords.add("hure");
        badwords.add("hurensohn");
        badwords.add("hurentochter");
        badwords.add("schlampe");
        badwords.add("nude");
        badwords.add("idiot");
        badwords.add("vollidiot");
        badwords.add("itiot");
        badwords.add("missgeburt");
        badwords.add("ich töte dein");
        badwords.add("fuck");
        badwords.add("fuck you");
        badwords.add("pornhub");
        badwords.add("kacke");
        badwords.add("scheiße");
        badwords.add("porn");
        badwords.add("beschissen");
        badwords.add("download server");
    }

    public static boolean contains(String string) {
        AtomicBoolean b = new AtomicBoolean(false);
        badwords.forEach(s -> {
            if(string.toLowerCase().contains(s.toLowerCase())) {
                b.set(true);
            }
        });
        return b.get();
    }

}
