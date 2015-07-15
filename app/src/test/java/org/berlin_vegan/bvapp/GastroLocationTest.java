package org.berlin_vegan.bvapp;

import org.berlin_vegan.bvapp.activities.MainListActivity;
import org.berlin_vegan.bvapp.data.GastroLocation;
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
}
