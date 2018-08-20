package sud_tanj.com.icare.Frontend.Fragment.SensorCatalogue;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.listener.OnFormElementValueChangedListener;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementSwitch;
import me.riddhimanadib.formmaster.model.FormElementTextMultiLine;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;
import me.riddhimanadib.formmaster.model.FormHeader;
import sud_tanj.com.icare.R;

public abstract class AddModifyMonitor extends Fragment implements OnFormElementValueChangedListener {

    public static final int ELEMENT_IMAGE=0;
    public static final int ELEMENT_NAME=1;
    public static final int ELEMENT_DESCRIPTION=2;
    public static final int ELEMENT_GRAPHLEGEND=3;
    public static final int ELEMENT_STATUS=4;

    public static final String STATUS_ON="On";
    public static final String STATUS_OFF="Off";

    protected FormBuilder mFormBuilder;
    protected List<BaseFormElement> formItems;
    public void initUI(){
        //init Form
        mFormBuilder = new FormBuilder(getContext(), getRecyclerView(),this);
        // declare form elements
        //FormHeader header = FormHeader.createInstance(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+getString(R.string.settings_personal_information_headings));
        // age number input
        FormElementTextSingleLine nameElement= FormElementTextSingleLine
                .createInstance()
                .setTag(ELEMENT_NAME)
                .setTitle(getString(R.string.name_monitor_title))
                .setHint(getString(R.string.name_monitor_hint))
                .setRequired(true);
        FormElementTextSingleLine imageElement= FormElementTextSingleLine
                .createInstance()
                .setTag(ELEMENT_IMAGE)
                .setTitle(getString(R.string.image_monitor_title))
                .setHint(getString(R.string.image_monitor_hint))
                .setRequired(true);
        FormElementTextMultiLine descriptionElement= FormElementTextMultiLine
                .createInstance()
                .setTag(ELEMENT_DESCRIPTION)
                .setTitle(getString(R.string.description_monitor_title))
                .setHint(getString(R.string.description_monitor_hint))
                .setRequired(true);
        FormElementTextMultiLine graphLegendElement= FormElementTextMultiLine
                .createInstance()
                .setTag(ELEMENT_GRAPHLEGEND)
                .setTitle(getString(R.string.graphLegend_monitor_title))
                .setHint(getString(R.string.graphLegend_monitor_hint))
                .setRequired(true);
        FormElementSwitch statusElement= FormElementSwitch
                .createInstance()
                .setTag(ELEMENT_STATUS)
                .setSwitchTexts(STATUS_ON, STATUS_OFF)
                .setTitle(getString(R.string.status_monitor_title))
                .setHint(getString(R.string.status_monitor_hint))
                .setRequired(true);

        //Load to form
        formItems = new ArrayList<>();
        if(getFormHeader()!=null){
            formItems.add(getFormHeader());
        }
        //formItems.add(header);
        formItems.add(imageElement);
        formItems.add(nameElement);
        formItems.add(descriptionElement);
        formItems.add(statusElement);
        formItems.add(graphLegendElement);

        mFormBuilder.addFormElements(formItems);
    }

    public abstract RecyclerView getRecyclerView();

    public abstract void formValueChanged(BaseFormElement baseFormElement);

    public abstract FormHeader getFormHeader();

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        formValueChanged(baseFormElement);
    }
}
