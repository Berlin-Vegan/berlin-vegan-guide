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

        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(createList(30));
        recyclerView.setAdapter(restaurantAdapter);
    }

    // TODO: remove if actual data is available
    private List<RestaurantInfo> createList(int size) {
        List<RestaurantInfo> result = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            RestaurantInfo restaurantInfo = new RestaurantInfo();
            restaurantInfo.name = RestaurantInfo.NAME_PREFIX + i;
            restaurantInfo.address = RestaurantInfo.ADDRESS_PREFIX + i;
            result.add(restaurantInfo);
        }
        return result;
    }
}
