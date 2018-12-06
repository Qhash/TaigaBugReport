package de.techfunder.taigabugreport.adapter.flexible;

import android.view.View;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by Denis Dyakov on 11.04.2018.
 */

public abstract class ClickableFlexibleItem<VH extends ClickableFlexibleViewHolder> extends AbstractFlexibleItem<VH> {

    private FlexibleViewClickListener viewClickListener;

    public ClickableFlexibleItem(FlexibleViewClickListener flexibleViewClickListener){
        viewClickListener = flexibleViewClickListener;
    }

    @Override
    public boolean equals(Object o) {
        return onEquals(o);
    }

    @Override
    public int getLayoutRes() {
        return onGetLayoutRes();
    }

    @Override
    public VH createViewHolder(View view, FlexibleAdapter adapter) {
        VH holder = onCreateViewHolder(view, adapter);
        holder.setFlexibleClickListener(viewClickListener);
        return holder;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, VH holder, int position, List<Object> payloads) {
        onBindViewHolder(adapter, holder, position, payloads);
    }

    public abstract boolean onEquals(Object o);

    public abstract int onGetLayoutRes();

    public abstract VH onCreateViewHolder(View view, FlexibleAdapter adapter);

    public abstract void onBindViewHolder(FlexibleAdapter adapter, VH holder, int position, List<Object> payloads);
}
