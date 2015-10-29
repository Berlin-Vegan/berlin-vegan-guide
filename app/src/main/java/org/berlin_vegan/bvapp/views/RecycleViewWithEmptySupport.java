package org.berlin_vegan.bvapp.views;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecycleViewWithEmptySupport extends RecyclerView {
    @Nullable
    View emptyView;

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

    public RecycleViewWithEmptySupport(Context context) {
        super(context);
    }

    public RecycleViewWithEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleViewWithEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    void checkIfEmpty() {
        if (emptyView != null) {
            final boolean empty = getAdapter().getItemCount() == 0;
            if (empty) {
                emptyView.setVisibility(VISIBLE);
                setVisibility(GONE);
            } else {
                emptyView.setVisibility(GONE);
                setVisibility(VISIBLE);
            }
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

    public void setEmptyView(@Nullable View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

}
