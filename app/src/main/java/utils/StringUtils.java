package utils;

public class StringUtils {
    private static String createExceptionMessage(String missingPart) {
        return "Invalid " + missingPart + ": cannot be null or empty";
    }

    public static void throwIfNullOrEmpty(String s, String itemName) {
        var itemNameRelatedMsg = createExceptionMessage("itemName");
        var parameterRelatedMsg = createExceptionMessage(itemName);

        if (itemName == null) {
            throw new NullPointerException(itemNameRelatedMsg);
        }

        if (itemName.isEmpty()) {
            throw new IllegalArgumentException(itemNameRelatedMsg);
        }

        if (s == null) {
            throw new NullPointerException(parameterRelatedMsg);
        }

        if (s.isEmpty()) {
            throw new IllegalArgumentException(parameterRelatedMsg);
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
