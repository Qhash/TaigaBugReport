package de.techfunder.taigabugreport.items;

import com.nguyenhoanglam.imagepicker.model.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.List;

import de.techfunder.taigabugreport.R;
import de.techfunder.taigabugreport.adapter.flexible.ClickableFlexibleItem;
import de.techfunder.taigabugreport.adapter.flexible.ClickableFlexibleViewHolder;
import de.techfunder.taigabugreport.adapter.flexible.FlexibleViewClickListener;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by Denis Dyakov on 11.10.2018.
 */
public class AttachmentItem extends ClickableFlexibleItem<AttachmentItem.AttachmentItemHolder> {

    private Image image;
    private FlexibleViewClickListener flexibleViewClickListener;

    public AttachmentItem(Image image, FlexibleViewClickListener flexibleViewClickListener) {
        super(flexibleViewClickListener);
        this.image = image;
        this.flexibleViewClickListener = flexibleViewClickListener;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, final AttachmentItemHolder holder, int position, List<Object> payloads) {
            holder.tvName.setText(image.getName());
            holder.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flexibleViewClickListener.onFlexibleViewClick(view, holder.getAdapterPosition());
                }
            });
    }

    @Override
    public boolean onEquals(Object o) {
        return false;
    }

    @Override
    public int onGetLayoutRes() {
        return R.layout.taigabugreport_list_item_attachment;
    }

    @Override
    public AttachmentItemHolder onCreateViewHolder(View view, FlexibleAdapter adapter) {
        return new AttachmentItemHolder(view, adapter);
    }

    @Override
    public void onBindViewHolder(FlexibleAdapter adapter, AttachmentItemHolder holder, int position, List<Object> payloads) {

    }

    class AttachmentItemHolder extends ClickableFlexibleViewHolder {

        TextView tvName;
        TextView tvSize;
        ImageButton ibDelete;

        public AttachmentItemHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            tvName = view.findViewById(R.id.tvName);
            tvSize = view.findViewById(R.id.tvSize);
            ibDelete = view.findViewById(R.id.ibDelete);
        }
    }
}
