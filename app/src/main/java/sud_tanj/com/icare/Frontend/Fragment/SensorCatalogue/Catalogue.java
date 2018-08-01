package sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue.Cards.SliderCard;
import sud_tanj.com.icare.Frontend.Icon.IconBuilder;
import sud_tanj.com.icare.R;

@EFragment(R.layout.fragment_sensor_catalogue)
public class Catalogue extends Fragment {
    private final int[] pics = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5};
   // private final int[] maps = {R.drawable.map_paris, R.drawable.map_seoul, R.drawable.map_london, R.drawable.map_beijing, R.drawable.map_greece};
    private final int[] descriptions = {R.string.text1, R.string.text2, R.string.text3, R.string.text4, R.string.text5};
    private final String[] countries = {"Sensor A", "Sensor B", "Sensor C", "Sensor D", "Sensor E"};
    private final String[] places = {"Description", "Description", "Description", "Description","Description"};
    private final String[] temperatures = {"Available", "Unavailable", "17°C", "23°C", "20°C"};
    private final String[] times = {"Jane Doe", "John Doe", "Jane Doe"};

   // private final SliderAdapter sliderAdapter = new SliderAdapter(pics, 20, new OnCardClickListener());
    private FastAdapter fastAdapter;

    private CardSliderLayoutManager layoutManger;
    private TextSwitcher temperatureSwitcher;
    private TextSwitcher placeSwitcher;
    private TextSwitcher clockSwitcher;
    private TextSwitcher descriptionsSwitcher;

    private TextView country1TextView;
    private TextView country2TextView;
    private int countryOffset1;
    private int countryOffset2;
    private long countryAnimDuration;
    private int currentPosition;

    RecyclerView sensorCatalogue;
    @ViewById(R.id.catalogue_description_icon)
    ImageView catalogueDescriptionIcon;
    @ViewById(R.id.sensor_author)
    ImageView sensorAuthorIcon;

    @AfterViews
    protected void init(){
        catalogueDescriptionIcon.setImageDrawable(IconBuilder.get(IconValue.CLIPBOARD_TEXT));
        sensorAuthorIcon.setImageDrawable(IconBuilder.get(IconValue.ARTIST));
        initRecyclerView();
        initCountryText();
        initSwitchers();
    }


    private void initRecyclerView() {
        ItemAdapter itemAdapter = new ItemAdapter();
        fastAdapter=FastAdapter.with(itemAdapter);
        itemAdapter.add(new SliderCard(getResources().getDrawable(R.drawable.ic_arduino)));
        itemAdapter.add(new SliderCard(getResources().getDrawable(R.drawable.ic_arduino)));
        itemAdapter.add(new SliderCard(getResources().getDrawable(R.drawable.ic_arduino)));
        itemAdapter.add(new SliderCard(getResources().getDrawable(R.drawable.ic_arduino)));
        itemAdapter.add(new SliderCard(getResources().getDrawable(R.drawable.ic_arduino)));

        sensorCatalogue = (RecyclerView) getActivity().findViewById(R.id.sensor_catalogue);
        sensorCatalogue.setAdapter(fastAdapter);
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

        new CardSnapHelper().attachToRecyclerView(sensorCatalogue);
    }

    private void initSwitchers() {
        temperatureSwitcher = (TextSwitcher) getActivity().findViewById(R.id.ts_temperature);
        temperatureSwitcher.setFactory(new TextViewFactory(R.style.TemperatureTextView, true));
        temperatureSwitcher.setCurrentText(temperatures[0]);

        placeSwitcher = (TextSwitcher) getActivity().findViewById(R.id.ts_place);
        placeSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
        placeSwitcher.setCurrentText(places[0]);

        clockSwitcher = (TextSwitcher) getActivity().findViewById(R.id.ts_clock);
        clockSwitcher.setFactory(new TextViewFactory(R.style.ClockTextView, false));
        clockSwitcher.setCurrentText(times[0]);

        descriptionsSwitcher = (TextSwitcher) getActivity().findViewById(R.id.ts_description);
        descriptionsSwitcher.setInAnimation(getContext(), android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(getContext(), android.R.anim.fade_out);
        descriptionsSwitcher.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));
        descriptionsSwitcher.setCurrentText(getString(descriptions[0]));
    }

    private void initCountryText() {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
        country1TextView = (TextView) getActivity().findViewById(R.id.tv_country_1);
        country2TextView = (TextView) getActivity().findViewById(R.id.tv_country_2);

        country1TextView.setX(countryOffset1);
        country2TextView.setX(countryOffset2);
        country1TextView.setText(countries[0]);
        country2TextView.setAlpha(0f);

    }

    private void setCountryText(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (country1TextView.getAlpha() > country2TextView.getAlpha()) {
            visibleText = country1TextView;
            invisibleText = country2TextView;
        } else {
            visibleText = country2TextView;
            invisibleText = country1TextView;
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

        setCountryText(countries[pos % countries.length], left2right);

        temperatureSwitcher.setInAnimation(getContext(), animH[0]);
        temperatureSwitcher.setOutAnimation(getContext(), animH[1]);
        temperatureSwitcher.setText(temperatures[pos % temperatures.length]);

        placeSwitcher.setInAnimation(getContext(), animV[0]);
        placeSwitcher.setOutAnimation(getContext(), animV[1]);
        placeSwitcher.setText(places[pos % places.length]);

        clockSwitcher.setInAnimation(getContext(), animV[0]);
        clockSwitcher.setOutAnimation(getContext(), animV[1]);
        clockSwitcher.setText(times[pos % times.length]);

        descriptionsSwitcher.setText(getString(descriptions[pos % descriptions.length]));

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

    private class ImageViewFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            final ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            final LayoutParams lp = new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);

            return imageView;
        }
    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm =  (CardSliderLayoutManager) sensorCatalogue.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = sensorCatalogue.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                final Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, pics[activeCardPosition % pics.length]);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    //startActivity(intent);
                } else {
                    final CardView cardView = (CardView) view;
                    final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
                    final ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(getActivity(), sharedView, "shared");
                    //startActivity(intent, options.toBundle());
                }
            } else if (clickedPosition > activeCardPosition) {
                sensorCatalogue.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }
}
