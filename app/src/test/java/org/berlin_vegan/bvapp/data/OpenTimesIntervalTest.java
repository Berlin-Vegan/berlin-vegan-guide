package org.berlin_vegan.bvapp.data;

import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class OpenTimesIntervalTest {

    @Test
    public void testIsDateInInterval() throws Exception {
        final Date date = new GregorianCalendar(2015, GregorianCalendar.JULY, 26).getTime(); // sunday

        OpenTimesInterval interval = new OpenTimesInterval(5, 6, "9 - 10");// saturday - sunday
        assertTrue(interval.isDateInInterval(date));

        interval = new OpenTimesInterval(6, "9 - 10");// only sunday
        assertTrue(interval.isDateInInterval(date));

        interval = new OpenTimesInterval(0, 2, "9 - 10");// monday - wednesday
        assertFalse(interval.isDateInInterval(date));
    }

    @Test
    public void testNumberOfDays() throws Exception {
        OpenTimesInterval interval = new OpenTimesInterval(5, 6, "9 - 10");// saturday - sunday
        assertEquals(2,interval.getNumberOfDays());

        interval = new OpenTimesInterval(0, 6, "9 - 10");// monday - sunday
        assertEquals(7,interval.getNumberOfDays());

        interval = new OpenTimesInterval(0, "9 - 10");// monday
        assertEquals(1,interval.getNumberOfDays());
    }
}