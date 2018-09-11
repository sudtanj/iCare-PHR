package sud_tanj.com.icare.Frontend.Settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.listener.OnFormElementValueChangedListener;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementSwitch;
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import sud_tanj.com.icare.Backend.Preferences.HybridPreferences;
import sud_tanj.com.icare.R;

@EFragment(R.layout.fragment_settings)
public class SettingsFragment extends Fragment implements OnFormElementValueChangedListener,OnSharedPreferenceChangeListener {
    public static final int AGE_SETTING=0;
    public static final int BACKGROUND_JOB_SETTING=1;
    public static final String AGE_SETTINGS="age_settings";
    public static final String BACKGROUND_JOB_SETTINGS="background_job_settings";
    @ViewById(R.id.settings_recycler)
    RecyclerView settingsRecycler;
    private FormBuilder mFormBuilder;
    private List<BaseFormElement> formItems;

    @AfterViews
    protected void initUi(){
        //init Form
        mFormBuilder = new FormBuilder(getContext(), settingsRecycler,this);
        // declare form elements
        //FormHeader header = FormHeader.createInstance(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+getString(R.string.settings_personal_information_headings));
        // age number input
        FormElementTextNumber ageElement = FormElementTextNumber
                .createInstance()
                .setTitle(getString(R.string.age_settings_menu_title))
                .setHint(getString(R.string.age_settings_menu_hint))
                .setRequired(Boolean.TRUE)
                .setValue(HybridPreferences.getFirebaseInstance().getString(AGE_SETTINGS,""));
        FormElementSwitch backgroundElement = FormElementSwitch
                .createInstance()
                .setTitle(getString(R.string.background_job_title))
                .setHint(getString(R.string.background_job_hint))
                .setSwitchTexts("On","Off")
                .setRequired(true)
                .setValue(HybridPreferences.getFirebaseInstance().getString(BACKGROUND_JOB_SETTINGS,"On"));
        FormElementTextNumber uidElement = FormElementTextNumber
                .createInstance()
                .setTitle(getString(R.string.uid_settings_menu_title))
                .setHint(getString(R.string.uid_settings_menu_hint))
                .setRequired(Boolean.TRUE)
                .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //Load to form
        formItems = new ArrayList<>();
        //formItems.add(header);
        formItems.add(ageElement);
        formItems.add(backgroundElement);
        formItems.add(uidElement);

        mFormBuilder.addFormElements(formItems);

        //Init HybridPreferences
        HybridPreferences.getFirebaseInstance().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        if(baseFormElement.getTitle().equals(getString(R.string.age_settings_menu_title))){
            HybridPreferences.getFirebaseInstance().edit().putString(AGE_SETTINGS,baseFormElement.getValue()).commit();
        }
        if(baseFormElement.getTitle().equals(getString(R.string.background_job_title))){
            HybridPreferences.getFirebaseInstance().edit().putString(BACKGROUND_JOB_SETTINGS,baseFormElement.getValue()).commit();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        formItems.get(AGE_SETTING).setValue(sharedPreferences.getString(AGE_SETTINGS, ""));
        formItems.get(BACKGROUND_JOB_SETTING).setValue(sharedPreferences.getString(BACKGROUND_JOB_SETTINGS,"On"));
        settingsRecycler.getAdapter().notifyDataSetChanged();
    }
}
