package sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue.Cards;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.fastadapter.items.AbstractItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 01/08/2018 - 7:43.
 * <p>
 * This class last modified by User
 */
@AllArgsConstructor
public class SliderCard extends AbstractItem<SliderCard, SliderCard.ViewHolder>{

    private static int viewWidth = 0;
    private static int viewHeight = 0;

    private Drawable image;

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        ViewHolder viewHolder=new ViewHolder(v);
        viewHolder.getImageView().setImageDrawable(image);
        return viewHolder;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_slider_card;
    }

    // Manually create the ViewHolder class
    protected static class ViewHolder extends RecyclerView.ViewHolder{
        @Getter
        ImageView imageView;

        //TODO: Declare your UI widgets here
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.image);
            //TODO: init UI
        }
    }

}