package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

import lombok.Getter;
import sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.FirebaseCounterAdapter.CounterHolder;
import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 12/08/2018 - 21:00.
 * <p>
 * This class last modified by User
 */
public class FirebaseCounterAdapter extends FirebaseRecyclerAdapter<Integer,CounterHolder> {

    private List<String> unit=null;
    private View itemParentView;
    /**
     * Initialize a {@link Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseCounterAdapter(@NonNull FirebaseRecyclerOptions<Integer> options,List<String> unit,View itemView) {
        super(options);
        this.unit=unit;
        this.itemParentView=itemView;
    }

    @Override
    protected void onBindViewHolder(@NonNull CounterHolder holder, int position, @NonNull Integer model) {
        //holder.itemView.setBackgroundResource(R.color.colorPrimary);
        holder.number.setText(model.toString());
        holder.unit.setText(this.unit.get(position));
        holder.itemView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            private boolean isAlreadyRun=false;
            @Override
            public void onGlobalLayout() {
                if(!isAlreadyRun){
                    ((FoldingCell) itemParentView).toggle(true);
                    ((FoldingCell) itemParentView).toggle(true);
                    isAlreadyRun=true;
                }
            }
        });
    }

    @NonNull
    @Override
    public CounterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_slider_data_counter, parent, false);

        return new CounterHolder(view);
    }

    protected static class CounterHolder extends RecyclerView.ViewHolder{
        @Getter
        private TextView number;
        @Getter
        private TextView unit;
        //TODO: Declare your UI widgets here
        public CounterHolder(View itemView) {
            super(itemView);
            number=(TextView)itemView.findViewById(R.id.number_data_counter);
            unit=(TextView)itemView.findViewById(R.id.unit_data_counter);;
            //imageView=(ImageView) itemView.findViewById(R.id.image);
            //TODO: init UI
        }
    }
}
