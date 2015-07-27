package org.berlin_vegan.bvapp.helpers;


import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtil {

    public static final List<String> FIXED_HOLIDAYS = Arrays.asList( "1.1", "1.5", "3.10", "25.12", "26.12");
    public static final List<String> DYNAMIC_HOLIDAYS = Arrays.asList( "25.3.2016", "28.3.2016", "5.5.2016", "16.5.2016", "14.4.2017","17.4.2017","25.5.2017","05.6.2017");
    public static final int ONE_MINUTE_IN_MILLISECONDS = 60000;
    /**
     * return the current day of week, starting with monday
     */
    static public int getDayOfWeek(Date date) {
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) { // set sunday to end
            dayOfWeek = 8;
        }
        dayOfWeek = dayOfWeek - 2;
        return dayOfWeek;
    }

    static public boolean isPublicHoliday(Date date) {
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH) +1;
        final int year = calendar.get(Calendar.YEAR);

        String dateStr = String.valueOf(day) + "." + String.valueOf(month);
        String dateStrWithYear = dateStr + "." + String.valueOf(year);
        return FIXED_HOLIDAYS.contains(dateStr) || DYNAMIC_HOLIDAYS.contains(dateStrWithYear);
    }

    public static int inMinutes(int hours, int minutes) {
        return (hours * 60) + minutes;
    }
    public static Date addMinutesToDate(Date date,int minutes){
        final long currentTime = date.getTime();
        return new Date(currentTime + (minutes * ONE_MINUTE_IN_MILLISECONDS));
    }
}
