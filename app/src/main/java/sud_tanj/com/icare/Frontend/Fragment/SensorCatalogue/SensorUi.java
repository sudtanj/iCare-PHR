package sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.cardslider.CardSliderLayoutManager;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import sud_tanj.com.icare.Backend.Database.Monitoring.MonitoringInformation;
import sud_tanj.com.icare.Backend.Database.UserInformation;
import sud_tanj.com.icare.Frontend.Fragment.FragmentBuilder;
import sud_tanj.com.icare.Frontend.Icon.IconBuilder;
import sud_tanj.com.icare.R;
import sumimakito.android.advtextswitcher.AdvTextSwitcher;

@EFragment(R.layout.fragment_sensor_catalogue)
public class SensorUi extends Fragment {
    public static final int ACTIVITY_ADDING_MODIFY=365;
    //private final int[] pics = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5};
    // private final int[] maps = {R.drawable.map_paris, R.drawable.map_seoul, R.drawable.map_london, R.drawable.map_beijing, R.drawable.map_greece};
    //private final int[] descriptions = {R.string.text1, R.string.text2, R.string.text3, R.string.text4, R.string.text5};
    //private final String[] countries = {"Heart Rate", "Blood Pressure", "Sensor C", "Sensor D", "Sensor E"};
    private final String[] temperatures = {"Available", "Unavailable", "17°C", "23°C", "20°C"};
    private final String[] times = {"Jane Doe", "John Doe", "Jane Doe"};

    // private final SliderAdapter sliderAdapter = new SliderAdapter(pics, 20, new OnCardClickListener());
    //private FastAdapter fastAdapter;
    private FirebaseMonitoringAdapter firebaseMonitoringAdapter;

    private CardSliderLayoutManager layoutManger;
    @ViewById(R.id.ts_temperature)
    protected AdvTextSwitcher temperatureSwitcher;
    @ViewById(R.id.ts_place)
    protected AdvTextSwitcher placeSwitcher;
    @ViewById(R.id.ts_clock)
    protected AdvTextSwitcher clockSwitcher;
    @ViewById(R.id.ts_description)
    protected AdvTextSwitcher descriptionsSwitcher;

    @ViewById(R.id.monitor_name)
    protected AdvTextSwitcher monitoringTitle;
    @ViewById(R.id.tv_country_2)
    protected AdvTextSwitcher monitoringTitleHelper;
    private int countryOffset1;
    private int countryOffset2;
    private long countryAnimDuration;
    private int currentPosition;

    @ViewById(R.id.sensor_catalogue)
    protected RecyclerView sensorCatalogue;
    @ViewById(R.id.catalogue_description_icon)
    protected ImageView catalogueDescriptionIcon;
    @ViewById(R.id.sensor_author)
    protected ImageView sensorAuthorIcon;
    @ViewById(R.id.adding_button)
    protected FloatingActionButton addingButton;

    private Query query=null;

    @AfterViews
    protected void init(){
        catalogueDescriptionIcon.setImageDrawable(IconBuilder.get(IconValue.CLIPBOARD_TEXT));
        sensorAuthorIcon.setImageDrawable(IconBuilder.get(IconValue.ARTIST));
        initFAB();
        initRecyclerView();
        initSwitchers();
    }

    private void initFAB(){
        FirebaseDatabase.getInstance().getReferenceFromUrl(UserInformation.KEY)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserInformation userInformation=dataSnapshot.getValue(UserInformation.class);
                        if(userInformation.getDeveloper()) {
                            addingButton.setVisibility(View.VISIBLE);
                            addingButton.setImageDrawable(IconBuilder.get(IconValue.PLUS, R.color.temperature_text));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Click(R.id.adding_button)
    protected void addingButtonClicked(){
        AddMonitor_.intent(this.getContext()).startForResult(ACTIVITY_ADDING_MODIFY);
        // /FragmentBuilder.changeFragment(AddMonitor_.builder().build());
    }

    @OnActivityResult(ACTIVITY_ADDING_MODIFY)
    protected void onResult(Integer resultCode, Intent data) {
        FragmentBuilder.changeFragment(SensorUi_.builder().build());
    }

    private void initRecyclerView() {
            query = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(MonitoringInformation.KEY)
                    .limitToLast(10);

            FirebaseRecyclerOptions<MonitoringInformation> options =
                    new FirebaseRecyclerOptions.Builder<MonitoringInformation>()
                            .setQuery(query, MonitoringInformation.class)
                            .setLifecycleOwner(this)
                            .build();

            firebaseMonitoringAdapter = new FirebaseMonitoringAdapter(options, this);


            sensorCatalogue.setAdapter(firebaseMonitoringAdapter);
            sensorCatalogue.setHasFixedSize(true);

            sensorCatalogue.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        onActiveCardChange();
                    }
                }
            });

            layoutManger = (CardSliderLayoutManager) sensorCatalogue.getLayoutManager();
        //new CardSnapHelper().attachToRecyclerView(sensorCatalogue);
    }

    private void initSwitchers() {
        //temperatureSwitcher.setTexts(temperatures);
        placeSwitcher.setCurrentText(getString(R.string.monitoring_Descirption_subtitle));
        //clockSwitcher.setTexts(times);
    }

    public void initSwitcher(String text){
        temperatureSwitcher.setText(text);
        ((TextView)temperatureSwitcher.getCurrentView()).setTextColor(getResources().getColor(R.color.temperature_text));
    }

    public void initMonitorTitle(String text) {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);

        monitoringTitle.setX(countryOffset1);
        monitoringTitleHelper.setX(countryOffset2);
        //country1TextView.setText(countries[0]);
        //monitoringTitle.setCurrentText(text);
        setCountryText(text,true);
        monitoringTitleHelper.setAlpha(0f);
    }

    public void initMonitorDeveloper(String text){
        clockSwitcher.setText(text);
    }

    public void initMonitorDescription(String text){
        descriptionsSwitcher.setInAnimation(getContext(), android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(getContext(), android.R.anim.fade_out);
        descriptionsSwitcher.setCurrentText(text);
    }

    public void setCountryText(String text, boolean left2right) {
        final AdvTextSwitcher invisibleText;
        final AdvTextSwitcher visibleText;
        if (monitoringTitle.getAlpha() > monitoringTitleHelper.getAlpha()) {
            visibleText = monitoringTitle;
            invisibleText = monitoringTitleHelper;
        } else {
            visibleText = monitoringTitleHelper;
            invisibleText = monitoringTitle;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = countryOffset2;
        } else {
            invisibleText.setX(countryOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);
        ((TextView)invisibleText.getCurrentView()).setTextSize(20);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", countryOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(countryAnimDuration);
        animSet.start();
    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        //setCountryText(countries[pos % countries.length], left2right);
        setCountryText(firebaseMonitoringAdapter
                .getItem(pos % firebaseMonitoringAdapter.getItemCount()).getName()
                ,left2right);

        temperatureSwitcher.setInAnimation(getContext(), animH[0]);
        temperatureSwitcher.setOutAnimation(getContext(), animH[1]);
        initSwitcher(firebaseMonitoringAdapter
                .getItem(pos % firebaseMonitoringAdapter.getItemCount()).getMonitoring()?
                "Available" : "Unavailable");

        placeSwitcher.setInAnimation(getContext(), animV[0]);
        placeSwitcher.setOutAnimation(getContext(), animV[1]);
        //placeSwitcher.setText(places[pos % places.length]);

        clockSwitcher.setInAnimation(getContext(), animV[0]);
        clockSwitcher.setOutAnimation(getContext(), animV[1]);
        //clockSwitcher.setText(times[pos % times.length]);
        final List<String> developers=firebaseMonitoringAdapter
                .getItem(pos % firebaseMonitoringAdapter.getItemCount()).getDeveloper();
        FirebaseDatabase.getInstance().getReferenceFromUrl(UserInformation.KEY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String developer="";
                        for(DataSnapshot temp:dataSnapshot.getChildren()){
                            if(!developer.isEmpty()){
                                developer+=",";
                            }
                            if(developers.indexOf(temp.getKey())>-1) {
                                UserInformation userInformation = temp.getValue(UserInformation.class);
                                developer += userInformation.getName();
                            }
                        }
                        initMonitorDeveloper(developer);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        //descriptionsSwitcher.setText(getString(descriptions[pos % descriptions.length]));
        descriptionsSwitcher.setText(firebaseMonitoringAdapter
                        .getItem(pos % firebaseMonitoringAdapter.getItemCount()).getDescription()
                );
        currentPosition = pos;
    }

    private class TextViewFactory implements  ViewSwitcher.ViewFactory {

        @StyleRes final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(getContext());

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(getContext(), styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }

    }
}