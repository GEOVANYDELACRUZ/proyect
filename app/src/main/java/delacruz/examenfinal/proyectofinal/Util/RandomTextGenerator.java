package delacruz.examenfinal.proyectofinal.Util;

import java.util.Random;

public class RandomTextGenerator {
    public static String generateRandomText() {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$%^+";
        Integer length = 8;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            char randomChar = chars.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
