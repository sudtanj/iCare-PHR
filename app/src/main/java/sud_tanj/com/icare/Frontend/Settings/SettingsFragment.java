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
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import sud_tanj.com.icare.Backend.Preferences.HybridPreferences;
import sud_tanj.com.icare.R;

@EFragment(R.layout.fragment_settings)
public class SettingsFragment extends Fragment implements OnFormElementValueChangedListener,OnSharedPreferenceChangeListener {
    public static final String AGE_SETTINGS="age_settings";
    @ViewById(R.id.settings_recycler)
    RecyclerView settingsRecycler;
    private FormBuilder mFormBuilder;
    private List<BaseFormElement> formItems;

    @AfterViews
    protected void initUi(){
        //Init HybridPreferences
        HybridPreferences.getFirebaseInstance().registerOnSharedPreferenceChangeListener(this);
        //Init setTitle
        getActivity().setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+getString(R.string.settings_personal_information_headings));
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
        //Load to form
        formItems = new ArrayList<>();
        //formItems.add(header);
        formItems.add(ageElement);

        mFormBuilder.addFormElements(formItems);
    }

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        if(baseFormElement.getTitle().equals(getString(R.string.age_settings_menu_title))){
            HybridPreferences.getFirebaseInstance().edit().putString(AGE_SETTINGS,baseFormElement.getValue()).commit();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        for(BaseFormElement temp:formItems){
            if(temp.getTitle().equals(getString(R.string.age_settings_menu_title))){
                temp.setValue(sharedPreferences.getString(s,null));
                break;
            }
        }
        settingsRecycler.getAdapter().notifyDataSetChanged();
    }
}
