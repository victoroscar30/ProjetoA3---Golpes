package ui;

import java.time.LocalTime;

public class Greetings {
    public static String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "Olá, bom dia";
        } else if (hour >= 12 && hour < 18) {
            return "Olá, boa tarde";
        } else {
            return "Olá, boa noite";
        }
    }
}
