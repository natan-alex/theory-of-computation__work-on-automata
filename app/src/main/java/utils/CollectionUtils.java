package utils;

import java.util.Collection;

public class CollectionUtils {
    public static <T> void throwIfNullOrEmpty(Collection<T> c, String itemName) {
        StringUtils.throwIfNullOrEmpty(itemName, "itemName");

        var exceptionMsg = "The " + itemName + " cannot be null or empty";

        if (c == null) {
            throw new NullPointerException(exceptionMsg);
        }

        if (c.isEmpty()) {
            throw new IllegalArgumentException(exceptionMsg);
        }
    }
}
