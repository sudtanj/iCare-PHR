package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import lombok.Getter;
import sud_tanj.com.icare.Backend.Database.HybridReference;
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
    public static final String MUTE_ENABLE="Pause data flow";
    public static final String MUTE_DISABLED="Resume data flow";
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
        DataFlowListener.listen(holder.dataStatusButton,getRef(position).getKey());
        final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(model.getMuteStatus().get(uid)==null){
            model.getMuteStatus().put(uid,false);
        } else {
            if(model.getMuteStatus().get(uid)==true){
                holder.muteButton.setText(MUTE_DISABLED);
            } else {
                holder.muteButton.setText(MUTE_ENABLE);
            }
        }
        holder.muteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.muteButton.getText().equals(MUTE_DISABLED)) {
                    model.getMuteStatus().put(uid, false);
                    holder.muteButton.setText(MUTE_ENABLE);
                }
                else {
                    model.getMuteStatus().put(uid,true);
                    holder.muteButton.setText(MUTE_DISABLED);
                }
                HybridReference hybridReference=new HybridReference(getRef(position));
                hybridReference.setValue(model);
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
        private TextView titleView;
        private Button dataStatusButton;
        private Button muteButton;
        //TODO: Declare your UI widgets here
        public DataHolder(View itemView) {
            super(itemView);
            titleView=itemView.findViewById(R.id.counter_title);
            dataStatusButton=itemView.findViewById(R.id.data_status);
            muteButton=itemView.findViewById(R.id.data_mute_button);
            //TODO: init UI
        }

    }
}
