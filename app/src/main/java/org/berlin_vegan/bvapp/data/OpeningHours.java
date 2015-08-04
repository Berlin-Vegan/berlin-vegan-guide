package org.berlin_vegan.bvapp.data;


import org.berlin_vegan.bvapp.helpers.DateUtil;

import static org.berlin_vegan.bvapp.helpers.DateUtil.MINUTES_PER_DAY;
import static org.berlin_vegan.bvapp.helpers.DateUtil.MINUTES_PER_HOUR;

/**
 * this class parsed the open hours format and convert it to a minute range,
 * if the opening hours are after midnight the day get extended
 * you can check with isInRange() if your "minute" is in the opening hours range
 * supported formats are:
 * "5 - "
 * "5 - 10"
 * "5:30 - 11"
 * "10 - 3"
 * "10 - 0"
 * "10 - 24"
 */
public class OpeningHours {

    int startMinute=0;
    int endMinute=0;

    public OpeningHours(String openingHours) {
        if (openingHours.contains("-")) {
            final String[] parts = openingHours.split("-");
            String startTime = parts[0];
            startMinute = getMinute(startTime);
            if (parts.length > 1) {
                String endTime = parts[1];
                endMinute = getMinute(endTime);
                if (startMinute != 0 && endMinute == 0) {
                    endMinute = MINUTES_PER_DAY;
                }
            }else if (startMinute != 0) {
                endMinute = MINUTES_PER_DAY;
            }
        }
    }

    public boolean isInRange(int minute) {
        int endMinutes = endMinute;
        if (endMinute < startMinute) { // closing time is after midnight
            endMinutes = endMinutes + MINUTES_PER_DAY; // add a whole day in minutes, so we can easily calculate range
        }
        return minute >= startMinute && minute <= endMinutes;
    }

    public String getFormattedClosingTime() {
        return DateUtil.formatTimeFromMinutes(endMinute);
    }

    public int getStartMinute() {
        return startMinute;
    }
    public int getEndMinute() {
        return endMinute;
    }


    /**
     * parse the minute of day for the given string
     * "5:30" returns 5*60 + 30
     */
    private int getMinute(String time) {
        if (time == null || time.isEmpty()) {
            return 0;
        }
        int hour = 0;
        int minute = 0;
        try {
            if (time.contains(":")) {
                final String[] parts = time.split(":");
                hour = Integer.parseInt(parts[0].trim());
                minute = Integer.parseInt(parts[1].trim());
            } else {
                hour = Integer.parseInt(time.trim());
                if (hour == 0) { // 0 is the same as 24 in hour format
                    hour = 24;
                }
            }
        } catch (Exception ignored) {
        }

        return hour * MINUTES_PER_HOUR + minute;
    }
}
