package utils;

public class StringUtils {
    public static String requireNonNullOrEmpty(String s) {
        if (s == null) {
            throw new NullPointerException("The parameter cannot be null");
        }

        if (s.isEmpty()) {
            throw new IllegalArgumentException("The parameter cannot be empty");
        }

        return s;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
