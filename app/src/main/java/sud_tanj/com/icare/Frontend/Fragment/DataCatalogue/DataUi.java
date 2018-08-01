package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.ramotion.foldingcell.FoldingCell;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import sud_tanj.com.icare.R;

@EFragment(R.layout.fragment_data_catalogue)
public class DataUi extends Fragment implements OnClickListener<CellCard> {
    @ViewById(R.id.data_recycler_view)
    protected RecyclerView dataRecyclerView;
    private FastAdapter fastAdapter;
    protected RecyclerView numberUnit;

    @AfterViews
    public void init(){
        numberUnit=(RecyclerView)((Activity)dataRecyclerView.getContext()).findViewById(R.id.number_unit_data_catalogue);
        //numberUnit.setHasFixedSize(true);
        ItemAdapter itemAdapterNumberunit=new ItemAdapter();
        FastAdapter numberAdapter=FastAdapter.with(itemAdapterNumberunit);
        itemAdapterNumberunit.add(new NumberSlider("20","cm"));
        itemAdapterNumberunit.add(new NumberSlider("20","cm"));
        itemAdapterNumberunit.add(new NumberSlider("20","cm"));


        ItemAdapter itemAdapter = new ItemAdapter();
        fastAdapter= FastAdapter.with(itemAdapter);
        itemAdapter.add(new CellCard(numberAdapter));
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataRecyclerView.setAdapter(fastAdapter);
        dataRecyclerView.setHasFixedSize(true);
        fastAdapter.withOnClickListener(this);
    }

    @Override
    public boolean onClick(@Nullable View v, IAdapter<CellCard> adapter, CellCard item, int position) {
        ((FoldingCell)v).toggle(false);
        return true;
    }
}
