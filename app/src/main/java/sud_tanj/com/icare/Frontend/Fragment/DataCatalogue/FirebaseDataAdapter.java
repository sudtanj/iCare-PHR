package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ramotion.cardslider.CardSliderLayoutManager;

import lombok.Getter;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Backend.Database.PersonalData.HealthData;
import sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.FirebaseDataAdapter.DataHolder;
import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 12/08/2018 - 20:35.
 * <p>
 * This class last modified by User
 */
public class FirebaseDataAdapter extends FirebaseRecyclerAdapter<MonitoringInformation,DataHolder> {

    private FirebaseCounterAdapter firebaseCounterAdapter;
    /**
     * Initialize a {@link Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseDataAdapter(@NonNull FirebaseRecyclerOptions<MonitoringInformation> options) {
        super(options);
    }

    @Override
    public void stopListening() {
        super.stopListening();
        firebaseCounterAdapter.stopListening();
    }

    @Override
    protected void onBindViewHolder(@NonNull DataHolder holder, int position, @NonNull MonitoringInformation model) {
        holder.titleView.setText(model.getName());
        Query query = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(HealthData.KEY)
                .child(model.getHealthDatas().get(model.getHealthDatas().size()-1)).child("dataList");
        FirebaseRecyclerOptions<Integer> options =
                new FirebaseRecyclerOptions.Builder<Integer>()
                        .setQuery(query, Integer.class)
                        .build();
        firebaseCounterAdapter=new FirebaseCounterAdapter(options,model.getGraphLegend());
        firebaseCounterAdapter.startListening();
        holder.getCounterRecyclerView().setAdapter(firebaseCounterAdapter);
        holder.getCounterRecyclerView().setLayoutManager(new CardSliderLayoutManager(holder.itemView.getContext()));
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_slider_cell, parent, false);

        return new DataHolder(view);
    }

    protected static class DataHolder extends RecyclerView.ViewHolder{
        @Getter
        TextView titleView;
        @Getter
        RecyclerView counterRecyclerView;

        //TODO: Declare your UI widgets here
        public DataHolder(View itemView) {
            super(itemView);
            titleView=itemView.findViewById(R.id.counter_title);
            counterRecyclerView=itemView.findViewById(R.id.number_unit_data_catalogue);
            //TODO: init UI
        }
    }
}
