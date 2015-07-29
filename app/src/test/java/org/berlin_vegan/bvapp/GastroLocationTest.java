package org.berlin_vegan.bvapp;

import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.berlin_vegan.bvapp.data.GastroLocation;
import org.berlin_vegan.bvapp.data.OpeningHoursInterval;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GastroLocationTest {

    private static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";
    private GastroLocation location;
    private GastroLocation locationAlwaysClosed;

    @Before
    public void setUp() throws Exception {
        location = new GastroLocation();
        location.setOtMon("9 - 1");
        location.setOtTue("9 - 1");
        location.setOtWed("");
        location.setOtThu("7 - 19");
        location.setOtFri("10 - 14");
        location.setOtSat("10 - 22");
        location.setOtSun("10 - 22");

        locationAlwaysClosed = new GastroLocation();
        locationAlwaysClosed.setOtMon("");
        locationAlwaysClosed.setOtTue("");
        locationAlwaysClosed.setOtWed("");
        locationAlwaysClosed.setOtThu("");
        locationAlwaysClosed.setOtFri("");
        locationAlwaysClosed.setOtSat("");
        locationAlwaysClosed.setOtSun("");
    }

    @Test
    public void testParseLocationsWithGSON() throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(GASTRO_LOCATIONS_JSON);
        final List<GastroLocation> locationList = MainListActivity.createList(inputStream);
        final int numLocations = locationList.size();

        assertEquals(276, numLocations);

        final GastroLocation location = locationList.get(0);
        final String name = location.getName();
        final int numPictures = location.getPictures().size();

        assertEquals("Amaranth", name);
        assertEquals(2, numPictures);
    }

    @Test
    public void testCondensedOpeningHours() throws Exception {
        final List<OpeningHoursInterval> openingHours = location.getCondensedOpeningHours();
        assertEquals(5, openingHours.size());

    }

    @Test
    public void testCondensedOpeningHoursCompleteClosed() throws Exception {

        final List<OpeningHoursInterval> openingHours = locationAlwaysClosed.getCondensedOpeningHours();
        assertEquals(1, openingHours.size());
    }

    @Test
    public void testIsOpenLocationAlwaysClosed() throws Exception {
        assertFalse(locationAlwaysClosed.isOpen(Calendar.getInstance().getTime()));
    }

    @Test
    public void testIsOpen() throws Exception {
        Date date = new GregorianCalendar(2015, 6, 27, 20, 32).getTime();// monday 20:32, 27 July 2015
        assertTrue(location.isOpen(date));

        date = new GregorianCalendar(2015, 6, 28, 0, 32).getTime();// tuesday 0:32, after midnight, so take the opening hours from "monday"
        assertTrue(location.isOpen(date));

        date = new GregorianCalendar(2015, 6, 29, 1, 32).getTime();// wednesday
        assertFalse(location.isOpen(date));

        date = new GregorianCalendar(2015, 4, 1, 21, 0).getTime();// friday, but as 1 may is public holiday, take opening hours from sunday
        assertTrue(location.isOpen(date));

    }
}