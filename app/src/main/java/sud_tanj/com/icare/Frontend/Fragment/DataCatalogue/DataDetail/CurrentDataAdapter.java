package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataDetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

import lombok.Getter;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 31/08/2018 - 8:51.
 * <p>
 * This class last modified by User
 */
public class CurrentDataAdapter extends FirebaseRecyclerAdapter<HealthData,CurrentDataAdapter.DataHolder> {


    private List<String> units;

    /**
     * Initialize a {@link Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CurrentDataAdapter(@NonNull FirebaseRecyclerOptions<HealthData> options, List<String> units) {
        super(options);
        this.units = units;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_current_data_layout, parent, false);

        return new CurrentDataAdapter.DataHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull DataHolder holder, int position, @NonNull HealthData model) {
        holder.valueView.setText(model.getDataList().get(position).toString());
        if(!units.isEmpty() && units.size()>position)
            holder.unitView.setText(units.get(position));
    }

    protected static class DataHolder extends RecyclerView.ViewHolder{
        @Getter
        protected TextView valueView;
        protected TextView unitView;
        //TODO: Declare your UI widgets here
        public DataHolder(View itemView) {
            super(itemView);
            valueView=itemView.findViewById(R.id.value);
            unitView=itemView.findViewById(R.id.units);
            //TODO: init UI
        }

    }
}
