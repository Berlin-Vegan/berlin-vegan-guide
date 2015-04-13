package org.berlin_vegan.bvapp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        GastroLocationAdapter gastroLocationAdapter = new GastroLocationAdapter(createList(30));
        recyclerView.setAdapter(gastroLocationAdapter);
    }

    // TODO: remove if actual data is available
    private List<GastroLocation> createList(int size) {
        List<GastroLocation> result = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            GastroLocation gastroLocation = new GastroLocation();
            gastroLocation.name = GastroLocation.NAME_PREFIX + i;
            gastroLocation.street = GastroLocation.ADDRESS_PREFIX + i;
            result.add(gastroLocation);
        }
        return result;
    }
}
