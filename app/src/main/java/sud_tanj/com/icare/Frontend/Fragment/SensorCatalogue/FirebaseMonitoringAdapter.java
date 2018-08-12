package sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import lombok.Getter;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue.FirebaseMonitoringAdapter.MonitoringHolder;
import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 12/08/2018 - 16:54.
 * <p>
 * This class last modified by User
 */
public class FirebaseMonitoringAdapter extends FirebaseRecyclerAdapter<MonitoringInformation, MonitoringHolder> {

    private SensorUi sensorUi;
    /**
     * Initialize a {@link Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseMonitoringAdapter(@NonNull FirebaseRecyclerOptions<MonitoringInformation> options,SensorUi sensorUi) {
        super(options);
        this.sensorUi=sensorUi;
    }

    @Override
    protected void onBindViewHolder(@NonNull MonitoringHolder holder, int position, @NonNull MonitoringInformation model) {
        Glide.with(holder.itemView).asBitmap()
                .load(model.getImage())
                .apply(new RequestOptions()
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(holder.imageView);
        if(position==0){
            this.sensorUi.initMonitorTitle(model.getName());
            this.sensorUi.initMonitorDescription(model.getDescription());
        }
    }

    @NonNull
    @Override
    public MonitoringHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_slider_card, parent, false);

        return new MonitoringHolder(view);
    }

    protected static class MonitoringHolder extends RecyclerView.ViewHolder{
        @Getter
        ImageView imageView;

        //TODO: Declare your UI widgets here
        public MonitoringHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.image);
            //TODO: init UI
        }
    }
}
