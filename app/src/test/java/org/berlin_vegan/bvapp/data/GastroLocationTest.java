package org.berlin_vegan.bvapp.data;

import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GastroLocationTest {

    private static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";

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
    public void testCondensedOpenTimes() throws Exception {
        final GastroLocation location = new GastroLocation();
        location.setOtMon("9 - 18");
        location.setOtTue("9 - 18");
        location.setOtWed("");
        location.setOtThu("7 - 19");
        location.setOtFri("10 - 21");
        location.setOtSat("10 - 22");
        location.setOtSun("10 - 22");
        final List<OpenTimesInterval> openTimes = location.getCondensedOpenTimes();
        assertEquals(5,openTimes.size());

    }
    @Test
    public void testCondensedOpenTimesCompleteClosed() throws Exception {
        final GastroLocation location = new GastroLocation();
        location.setOtMon("");
        location.setOtTue("");
        location.setOtWed("");
        location.setOtThu("");
        location.setOtFri("");
        location.setOtSat("");
        location.setOtSun("");
        final List<OpenTimesInterval> openTimes = location.getCondensedOpenTimes();
        assertEquals(1, openTimes.size());
    }
}
