package ru.practicum.util;

import java.time.format.DateTimeFormatter;

public class Util {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String EVENTS_URI = "/events/";
    public static final String APP_NAME = "main-service";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
}
