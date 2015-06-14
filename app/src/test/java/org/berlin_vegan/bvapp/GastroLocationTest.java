package org.berlin_vegan.bvapp;

import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GastroLocationTest {

    public static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";

    @Test
    public void testParseLocationsWithGSON() throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(GASTRO_LOCATIONS_JSON);
        final List<GastroLocation> locationList = MainListActivity.createList(inputStream);

        assertEquals(3, locationList.size());
        assertEquals("arleo:", locationList.get(0).getName());

        final GastroLocation location = locationList.get(1);// attis
        final List<GastroLocationPicture> pictures = location.getPictures();
        assertEquals(2,pictures.size()); // 2 pictures
        System.out.println(pictures.get(0).getUrl());
        System.out.println(pictures.get(0).getWidth());
    }
}
