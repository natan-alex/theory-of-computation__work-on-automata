package utils;

import java.util.Collection;

public class CollectionsUtils {
    public static <T> void throwIfNullOrEmpty(Collection<T> c) {
        if (c == null) {
            throw new NullPointerException("The collection can not be null");
        }

        if (c.isEmpty()) {
            throw new IllegalArgumentException("The collection can not be empty");
        }
    }
}
