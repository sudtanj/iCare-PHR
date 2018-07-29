package sud_tanj.com.icare.Frontend.Settings;

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
public class SettingsFragment extends Fragment implements OnFormElementValueChangedListener {
    public static final String AGE_SETTINGS="age_settings";
    @ViewById(R.id.settings_recycler)
    RecyclerView settingsRecycler;
    private FormBuilder mFormBuilder;

    @AfterViews
    protected void initUi(){
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
                .setValue(HybridPreferences.getInstance().getObject(AGE_SETTINGS,String.class));
        //Load to form
        List<BaseFormElement> formItems = new ArrayList<>();
        //formItems.add(header);
        formItems.add(ageElement);

        mFormBuilder.addFormElements(formItems);

    }

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        if(baseFormElement.getTitle().equals(getString(R.string.age_settings_menu_title))){
            HybridPreferences.getInstance().addObject(AGE_SETTINGS,baseFormElement.getValue()).save();
        }
    }
}
