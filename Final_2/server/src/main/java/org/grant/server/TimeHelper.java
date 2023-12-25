package org.grant.server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeHelper {

    public static String getCurrentTimeFormatted() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
