package com.example.logisticstracking.infrastructure.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

import static com.example.logisticstracking.domain.constants.PackageConstants.LOG_PARSE_DATE_ERROR_TEMPLATE;
import static com.example.logisticstracking.domain.constants.PackageConstants.LOG_RECEIVED_TRACKING_EVENT_REQUEST_TEMPLATE;

@Slf4j
public class DateUtils {
    public static LocalDate parseDate(String dateStr, LocalDate defaultDate) {
        try {
            if (dateStr != null) {
                String[] parts = dateStr.split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                return LocalDate.of(year, month, day);
            }
        } catch (Exception e) {
            log.warn(LOG_PARSE_DATE_ERROR_TEMPLATE, dateStr, defaultDate);
        }
        return defaultDate;
    }

    public static LocalDateTime parseDateTime(String dateStr, LocalDateTime defaultDate) {
        if (dateStr != null && !dateStr.isBlank()) {
            try {
                return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
            } catch (Exception e) {
                log.warn(LOG_RECEIVED_TRACKING_EVENT_REQUEST_TEMPLATE, dateStr);
            }
        }
        return defaultDate;
    }
}
