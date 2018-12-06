package com.kipdev.taigabugreport.adapter.flexible;

import android.view.View;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by Denis Dyakov on 11.04.2018.
 */

public class ClickableFlexibleViewHolder extends FlexibleViewHolder {

    private FlexibleViewClickListener viewClickListener;

    public ClickableFlexibleViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
    }

    public ClickableFlexibleViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
        super(view, adapter, stickyHeader);
    }

    protected void setClickable(View clickable) {
        clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewClickListener != null) {
                    viewClickListener.onFlexibleViewClick(view, getAdapterPosition());
                }
            }
        });
    }

    public void setFlexibleClickListener(FlexibleViewClickListener viewClickListener) {
        this.viewClickListener = viewClickListener;
    }
}
