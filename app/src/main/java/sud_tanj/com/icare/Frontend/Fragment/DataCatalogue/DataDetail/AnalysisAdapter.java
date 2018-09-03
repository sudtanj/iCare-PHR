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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sud_tanj.com.icare.Backend.Database.PersonalData.DataAnalysis;
import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 03/09/2018 - 10:12.
 * <p>
 * This class last modified by User
 */
public class AnalysisAdapter extends FirebaseRecyclerAdapter<DataAnalysis,AnalysisAdapter.DataHolder> {

    /**
     * Initialize a {@link Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AnalysisAdapter(@NonNull FirebaseRecyclerOptions<DataAnalysis> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DataHolder holder, int position, @NonNull DataAnalysis model) {
        holder.analysisView.setText(model.getAnalysisMessage());
        Calendar c=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        c.setTimeInMillis(model.getTimeStamp());
        holder.dateView.setText(sdf.format(c.getTime()));

    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_analysis_layout, parent, false);

        return new AnalysisAdapter.DataHolder(view);
    }

    protected static class DataHolder extends RecyclerView.ViewHolder{
        protected TextView analysisView;
        protected TextView dateView;
        //TODO: Declare your UI widgets here
        public DataHolder(View itemView) {
            super(itemView);
            analysisView=itemView.findViewById(R.id.analysis_text);
            dateView=itemView.findViewById(R.id.analysis_date);
            //TODO: init UI
        }

    }
}
