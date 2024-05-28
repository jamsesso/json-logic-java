package io.github.jamsesso.jsonlogic.utils;

import java.time.OffsetDateTime;

public class DateOperations {
    public static OffsetDateTime fromString(String date) {
        return OffsetDateTime.parse(date);
    }
    public static boolean equals(OffsetDateTime a, OffsetDateTime b) {
        return a.isEqual(b);
    }
}
