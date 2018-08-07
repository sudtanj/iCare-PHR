package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnBindViewHolderListener;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.nightonke.boommenu.Animation.BoomEnum;
import com.nightonke.boommenu.Animation.OrderEnum;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.ramotion.foldingcell.FoldingCell;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import me.shaohui.bottomdialog.BottomDialog;
import me.shaohui.bottomdialog.BottomDialog.ViewListener;
import sud_tanj.com.icare.R;

@EFragment(R.layout.fragment_data_catalogue)
public class DataUi extends Fragment implements OnClickListener<CellCard>,OnBindViewHolderListener {
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

        fastAdapter.withOnBindViewHolderListener(this);
        //numberAdapter.withOnBindViewHolderListener(this);
        final BottomDialog bottomDialog=BottomDialog.create(getFragmentManager());
        bottomDialog.setViewListener(new ViewListener() {
                    @Override
                    public void bindView(View v) {
                        //RecyclerView commentRecyclerView=v.findViewById(R.id.comment_bottomsheet);
                        //commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        //commentRecyclerView.setAdapter(fastAdapter);
                        BoomMenuButton bmb=v.findViewById(R.id.bmb);
                        bmb.setBoomEnum(BoomEnum.RANDOM);
                        bmb.setButtonEnum(ButtonEnum.Ham);
                        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
                        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
                        bmb.setOrderEnum(OrderEnum.DEFAULT);
                        bmb.setOnBoomListener(new OnBoomListener() {
                            @Override
                            public void onClicked(int index, BoomButton boomButton) {

                            }

                            @Override
                            public void onBackgroundClick() {
                            }

                            @Override
                            public void onBoomWillHide() {

                            }

                            @Override
                            public void onBoomDidHide() {

                            }

                            @Override
                            public void onBoomWillShow() {

                            }

                            @Override
                            public void onBoomDidShow() {
                                System.out.println("Button Click");
                                bottomDialog.dismiss();
                            }
                        });
                            HamButton.Builder builder = new HamButton.Builder()
                                    .normalText("Post as Doctor")
                                    .subNormalText("We will tag this comment as from doctor!")
                                    .listener(new OnBMClickListener() {
                                        @Override
                                        public void onBoomButtonClick(int index) {
                                            bottomDialog.show();
                                        }
                                    });
                            HamButton.Builder builder2 = new HamButton.Builder()
                                    .normalText("Post as User")
                                    .subNormalText("We will tag this comment as from you!")
                                    .listener(new OnBMClickListener() {
                                        @Override
                                        public void onBoomButtonClick(int index) {
                                            bottomDialog.show();
                                        }
                                    });
                            bmb.addBuilder(builder);
                            bmb.addBuilder(builder2);
                    }
                })
                .setLayoutRes(R.layout.layout_slider_cell_bottomsheet_comment)      // dialog layout
                ;
        bottomDialog.show();
    }

    @Override
    public boolean onClick(@Nullable View v, IAdapter<CellCard> adapter, CellCard item, int position) {
        ((FoldingCell)v).toggle(false);
        return true;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, List<Object> payloads) {
        /**
        if(position==0) {
            TapTargetView.showFor((Activity)((CellCard.ViewHolder)viewHolder).itemView.getContext(),                 // `this` is an Activity
                    TapTarget.forView(((CellCard.ViewHolder)viewHolder).getNumberUnit(), "Slide Here!", "Slide to the left or right on the circle mark position to view data")
                            // All options below are optional
                            .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                            .titleTextSize(20)                  // Specify the size (in sp) of the title text
                            .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(true)                   // Whether to tint the target view's color
                            .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                            // Specify a custom drawable to draw as the target
                            .targetRadius(60));
        }
         */
    }

    @Override
    public void unBindViewHolder(ViewHolder viewHolder, int position) {

    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder, int position) {

    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder, int position) {

    }

    @Override
    public boolean onFailedToRecycleView(ViewHolder viewHolder, int position) {
        return false;
    }
}
