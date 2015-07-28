package org.berlin_vegan.bvapp.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class OpeningHoursTest {

    public static final int MINUTES_AT_MIDNIGHT = 1440; // 24*60

    @Test
    public void testInvalidData() throws Exception {
        OpeningHours openingHours = new OpeningHours("9 - spät"); // invalid end time, so we assume its open until midnight
        assertEquals(openingHours.getEndMinute(), MINUTES_AT_MIDNIGHT);

        openingHours = new OpeningHours("9-"); // missing end time so we assume its open until midnight
        assertEquals(openingHours.getEndMinute(),MINUTES_AT_MIDNIGHT);

        openingHours = new OpeningHours("a-b"); // no numbers, so we set everything to close
        assertEquals(openingHours.getStartMinute(),0);
        assertEquals(openingHours.getEndMinute(),0);

        openingHours = new OpeningHours("9"); // missing -, so we set every to close
        assertEquals(openingHours.getStartMinute(),0);
        assertEquals(openingHours.getEndMinute(),0);

        openingHours = new OpeningHours(""); // missing -, so we set every to close
        assertEquals(openingHours.getStartMinute(),0);
        assertEquals(openingHours.getEndMinute(),0);

        openingHours = new OpeningHours("don't know"); // missing -, so we set every to close
        assertEquals(openingHours.getStartMinute(),0);
        assertEquals(openingHours.getEndMinute(),0);

    }
}