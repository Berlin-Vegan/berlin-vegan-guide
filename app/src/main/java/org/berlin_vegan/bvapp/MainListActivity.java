package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainListActivity extends BaseActivity {

    private static final String GASTRO_LOCATIONS_JSON = "GastroLocations.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        GastroLocationAdapter gastroLocationAdapter = new GastroLocationAdapter(createList());
        recyclerView.setAdapter(gastroLocationAdapter);
    }

    private List<GastroLocation> createList() {
        final InputStream inputStream = getClass().getResourceAsStream(GASTRO_LOCATIONS_JSON);
        final InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<ArrayList<GastroLocation>>() {
        }.getType();
        final List<GastroLocation> locationList = new Gson().fromJson(reader, listType);
        return locationList;
    }
}
