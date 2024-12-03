package ru.assist.migrate.slack2pachka.v1.commands.test.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.IntStream;

public class Utils {
    public static String generateRandomString(int length) {
        int leftLimit = 65; // letter 'A'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    //generate /limit/ random number between /from/ to /to/
    public static int[] getRandomIntsArray(int from, int to, int limit) {
        return IntStream.generate(() -> new Random().nextInt(from) + (to - from)).limit(limit).toArray();
    }

    public static String inputStreamAsString(InputStream is) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            for (int length; (length = is.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }

            // StandardCharsets.UTF_8.name() > JDK 7
            return result.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

}
