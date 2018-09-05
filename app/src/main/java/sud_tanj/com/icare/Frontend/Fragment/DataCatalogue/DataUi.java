package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mikepenz.fastadapter.FastAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import co.intentservice.chatui.models.ChatMessage;
import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.R;

@EFragment(R.layout.fragment_data_catalogue)
public class DataUi extends Fragment {
    @ViewById(R.id.data_recycler_view)
    protected SuperRecyclerView dataRecyclerView;
    private FastAdapter fastAdapter;
    protected RecyclerView numberUnit;
    private FirebaseDataAdapter firebaseDataAdapter;
    private ChatMessage tempMessage;

    @AfterViews
    public void init(){
        Query query = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(MonitoringInformation.KEY)
                .limitToLast(10);

        FirebaseRecyclerOptions<MonitoringInformation> options =
                new FirebaseRecyclerOptions.Builder<MonitoringInformation>()
                        .setQuery(query, MonitoringInformation.class)
                        .setLifecycleOwner(this)
                        .build();
        firebaseDataAdapter=new FirebaseDataAdapter(options,this);
        dataRecyclerView.setAdapter(firebaseDataAdapter);
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}