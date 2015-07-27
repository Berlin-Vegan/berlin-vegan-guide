package org.berlin_vegan.bvapp.data;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * class to store opening hours in intervals, so days with same opening hours are condensed,
 * weekdays are stored as integer, beginning with monday = 0
 */
public class OpeningHoursInterval {
    public static final String CLOSED = "";

    private static int sNoEndDay = -1;
    private int mStartDay = 0; // monday
    private int mEndDay = sNoEndDay;
    private String mOpeningHoursTimes = CLOSED;

    public OpeningHoursInterval(int startDay, int endDay, String openingHours) {
        mStartDay = startDay;
        mEndDay = endDay;
        mOpeningHoursTimes = openingHours;
    }

    public OpeningHoursInterval(int startDay, String openingHours) {
        this.mStartDay = startDay;
        this.mEndDay = sNoEndDay;
        this.mOpeningHoursTimes = openingHours;
    }

    public int getStartDay() {
        return mStartDay;
    }

    public void setStartDay(int startDay) {
        this.mStartDay = startDay;
    }

    public int getEndDay() {
        return mEndDay;
    }

    public void setEndDay(int endDay) {
        this.mEndDay = endDay;
    }

    public String getOpeningHours() {
        return mOpeningHoursTimes;
    }

    public void setOpeningHours(String openingHours) {
        this.mOpeningHoursTimes = openingHours;
    }
    public int getNumberOfDays() {
        if (mEndDay == sNoEndDay) {
            return 1;
        }
        return (mEndDay - mStartDay) + 1;
    }

    /**
     * checks if the date is in open times interval
     * @param date date to check
     * @return true if it is in interval
     */
    public boolean isDateInInterval(Date date) {
        final int dayOfWeek = getDayOfWeek(date);
        boolean isInInterval = false;
        if (getNumberOfDays() == 1) {
            if (mStartDay == dayOfWeek) {
                isInInterval = true;
            }
        } else {
            if (dayOfWeek >= mStartDay && dayOfWeek <= mEndDay) {
                isInInterval = true;
            }
        }
        return isInInterval;
    }

    /**
     * return the current day of week, starting with monday
     */
    private int getDayOfWeek(Date date) {
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) { // set sunday to end
            dayOfWeek = 8;
        }
        dayOfWeek = dayOfWeek - 2;
        return dayOfWeek;
    }
}
