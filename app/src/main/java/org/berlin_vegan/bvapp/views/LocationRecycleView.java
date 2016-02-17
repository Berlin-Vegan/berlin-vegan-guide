package org.berlin_vegan.bvapp.views;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import org.berlin_vegan.bvapp.data.Locations;

public class LocationRecycleView extends RecyclerView {


    private View mEmptySearch;
    private View mEmptyFavorite;

    private Locations mLocations;

    public void setLocations(Locations locations) {
        mLocations = locations;
    }


    final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkIfEmpty();
        }
    };




    public LocationRecycleView(Context context) {
        super(context);
    }

    public LocationRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocationRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    void checkIfEmpty() {
        final boolean empty = getAdapter().getItemCount() == 0;
        setVisibility(GONE);
        mEmptySearch.setVisibility(GONE);
        mEmptyFavorite.setVisibility(GONE);

        if (empty) {
            if (mLocations.getDataType() == Locations.DATA_TYPE.FAVORITE) {
                mEmptyFavorite.setVisibility(VISIBLE);
            } else if (mLocations.getSearchState()) {
                mEmptySearch.setVisibility(VISIBLE);
            }
        } else {
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    public void setEmptyViews(View emptyFavorite, View emptySearch) {
        this.mEmptyFavorite = emptyFavorite;
        this.mEmptySearch = emptySearch;
    }



}
