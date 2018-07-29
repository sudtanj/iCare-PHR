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
import me.riddhimanadib.formmaster.model.BaseFormElement;
import sud_tanj.com.icare.R;

@EFragment(R.layout.fragment_settings)
public class SettingsFragment extends Fragment {
    @ViewById(R.id.settings_recycler)
    RecyclerView settingsRecycler;
    private FormBuilder mFormBuilder;

    @AfterViews
    protected void initUi(){
        //Init setTitle
        getActivity().setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+getString(R.string.settings_personal_information_headings));
        //init Form
        mFormBuilder = new FormBuilder(getContext(), settingsRecycler);
        // declare form elements
        //FormHeader header = FormHeader.createInstance(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+getString(R.string.settings_personal_information_headings));

        //Load to form
        List<BaseFormElement> formItems = new ArrayList<>();
        //formItems.add(header);

        mFormBuilder.addFormElements(formItems);
    }
}
