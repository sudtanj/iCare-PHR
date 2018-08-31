package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import lombok.Getter;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataDetail.DataDetails_;
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

    private DataUi dataUi;
    /**
     * Initialize a {@link Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseDataAdapter(@NonNull FirebaseRecyclerOptions<MonitoringInformation> options,DataUi dataUi) {
        super(options);
        this.dataUi=dataUi;
    }

    @Override
    protected void onBindViewHolder(@NonNull final DataHolder holder, final int position, @NonNull final MonitoringInformation model) {
        holder.titleView.setText(model.getName());
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DataDetails_.intent(holder.itemView.getContext()).id(getRef(position).getKey()).start();
            }
        });
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
        //TODO: Declare your UI widgets here
        public DataHolder(View itemView) {
            super(itemView);
            titleView=itemView.findViewById(R.id.counter_title);
            //TODO: init UI
        }

    }
}
