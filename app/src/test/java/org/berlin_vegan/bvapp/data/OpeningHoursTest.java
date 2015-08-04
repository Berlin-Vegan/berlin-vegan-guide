package org.berlin_vegan.bvapp.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class OpeningHoursTest {

    public static final int MINUTES_AT_MIDNIGHT = 1440; // 24*60

    @Test
    public void testInvalidData() throws Exception {
        OpeningHours openingHours = new OpeningHours("9 - spät"); // invalid end time, so we assume its open until midnight
        assertEquals(MINUTES_AT_MIDNIGHT,openingHours.getEndMinute());

        openingHours = new OpeningHours("9-"); // missing end time so we assume its open until midnight
        assertEquals(MINUTES_AT_MIDNIGHT,openingHours.getEndMinute());

        openingHours = new OpeningHours("a-b"); // no numbers, so we set everything to close
        assertEquals(0,openingHours.getStartMinute());
        assertEquals(0,openingHours.getEndMinute());

        openingHours = new OpeningHours("9"); // missing -, so we set every to close
        assertEquals(0,openingHours.getStartMinute());
        assertEquals(0,openingHours.getEndMinute());

        openingHours = new OpeningHours(""); // missing -, so we set every to close
        assertEquals(0,openingHours.getStartMinute());
        assertEquals(0,openingHours.getEndMinute());

        openingHours = new OpeningHours("don't know"); // missing -, so we set every to close
        assertEquals(0,openingHours.getStartMinute());
        assertEquals(0,openingHours.getEndMinute());

    }

    @Test
    public void testGetFormattedClosingTime() throws Exception {
        OpeningHours openingHours = new OpeningHours("9-4");
        assertEquals("4",openingHours.getFormattedClosingTime());

        openingHours = new OpeningHours("9-11");
        assertEquals("11",openingHours.getFormattedClosingTime());

        openingHours = new OpeningHours("9-");
        assertEquals("0",openingHours.getFormattedClosingTime());

        openingHours = new OpeningHours("9-12:30");
        assertEquals("12:30",openingHours.getFormattedClosingTime());

        openingHours = new OpeningHours("9-12:05");
        assertEquals("12:05",openingHours.getFormattedClosingTime());

    }
}