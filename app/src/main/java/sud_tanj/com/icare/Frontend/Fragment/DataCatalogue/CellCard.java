package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.ramotion.foldingcell.FoldingCell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 01/08/2018 - 16:08.
 * <p>
 * This class last modified by User
 */
@AllArgsConstructor
public class CellCard extends AbstractItem<CellCard, CellCard.ViewHolder> {

    private FastAdapter numberAdapter;

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        ViewHolder viewHolder=new ViewHolder(v);
        viewHolder.setAdapter(numberAdapter);
        return viewHolder;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_slider_cell;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        @Getter
        private RecyclerView numberUnit;
        //@Getter
        //ImageView imageView;
        @Getter
        private TextView textView;
        @Getter
        private FoldingCell foldingCell;
        //TODO: Declare your UI widgets here
        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.counter_title);
            numberUnit=(RecyclerView)itemView.findViewById(R.id.number_unit_data_catalogue);
            foldingCell=itemView.findViewById(R.id.folding_cell);
            //imageView=(ImageView) itemView.findViewById(R.id.image);
            //TODO: init UI
        }

        public void setAdapter(FastAdapter fastAdapter){
            numberUnit.setAdapter(fastAdapter);
        }

    }
}