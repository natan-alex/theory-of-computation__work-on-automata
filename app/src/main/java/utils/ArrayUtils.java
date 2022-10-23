package utils;

import java.util.Arrays;

public class ArrayUtils {
    public static <T> void throwIfNullOrEmpty(T[] array, String itemName) {
        StringUtils.throwIfNullOrEmpty(itemName, "itemName");

        var exceptionMsg = "The " + itemName + " cannot be null or empty";

        if (array == null) {
            throw new NullPointerException(exceptionMsg);
        }

        if (array.length == 0) {
            throw new IllegalArgumentException(exceptionMsg);
        }
    }

    public static void throwIfAnyElementIsNullOrEmpty(String[] array, String itemName) {
        StringUtils.throwIfNullOrEmpty(itemName, "itemName");

        if (Arrays.stream(array).anyMatch(e -> StringUtils.isNullOrEmpty(e))) {
            throw new IllegalArgumentException("The " + itemName + " cannot contain any element that is null or empty");
        }
    }
}
