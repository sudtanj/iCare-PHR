package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 01/08/2018 - 17:50.
 * <p>
 * This class last modified by User
 */
@AllArgsConstructor
public class NumberSlider extends AbstractItem<NumberSlider, NumberSlider.ViewHolder> {
    private String number;
    private String unit;

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.getNumber().setText(number);
        viewHolder.getUnit().setText(unit);
        return viewHolder;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_slider_data_counter;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        //@Getter
        //ImageView imageView;
        @Getter
        private TextView number;
        @Getter
        private TextView unit;
        //TODO: Declare your UI widgets here
        public ViewHolder(View itemView) {
            super(itemView);
            number=(TextView)itemView.findViewById(R.id.number_data_counter);
            unit=(TextView)itemView.findViewById(R.id.unit_data_counter);;
            //imageView=(ImageView) itemView.findViewById(R.id.image);
            //TODO: init UI
        }
    }
}
