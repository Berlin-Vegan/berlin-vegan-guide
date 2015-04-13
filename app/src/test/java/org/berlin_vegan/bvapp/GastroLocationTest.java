package org.berlin_vegan.bvapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class GastroLocationTest {

    public static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";

    @Test
    public void testParseLocationsWithGSON() throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(GASTRO_LOCATIONS_JSON);
        final InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<ArrayList<GastroLocation>>(){}.getType();
        final ArrayList<GastroLocation> locationList = new Gson().fromJson(reader, listType);

        assertEquals(3,locationList.size());
        assertEquals("arleo:",locationList.get(0).getName());
    }
}