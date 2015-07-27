package org.berlin_vegan.bvapp.helpers;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;


public class DateUtilTest {

    @Test
    public void testIsPublicHoliday() throws Exception {
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(2015, 0, 1);
        assertTrue(DateUtil.isPublicHoliday(calendar.getTime()));

        calendar.set(2016, 2, 28); //28 March is "Ostermontag"
        assertTrue(DateUtil.isPublicHoliday(calendar.getTime()));

        calendar.set(2015, 5, 5); // 5 June
        assertFalse(DateUtil.isPublicHoliday(calendar.getTime()));

    }
}