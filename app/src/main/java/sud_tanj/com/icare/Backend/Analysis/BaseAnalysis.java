package sud_tanj.com.icare.Backend.Analysis;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import sud_tanj.com.icare.Backend.BaseAbstractComponent;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 09/08/2018 - 17:16.
 * <p>
 * This class last modified by User
 */
public abstract class BaseAnalysis extends BaseAbstractComponent<AnalysisListener,Object> {
    public static final int EXCELLENT_CONDITION=0;
    public static final int GOOD_CONDITION=1;
    public static final int BAD_CONDITION=2;
    public static final int WORSE_CONDITION=3;
    @Getter
    protected static List<BaseAnalysis> baseAnalysisList=new ArrayList<>();
    protected List<AnalysisListener> analysisListeners=new ArrayList<>();

    public BaseAnalysis(){
        if(BaseAnalysis.baseAnalysisList.indexOf(this)==-1){
            BaseAnalysis.baseAnalysisList.add(this);
        }
    }
    @Override
    protected void onEventListenerFired(AnalysisListener listener, Object... valuePassed) {
        listener.onAnalysisDone((Integer)valuePassed[0],(String)valuePassed[1]);
    }

    @Override
    public void onDispose(){
        if(BaseAnalysis.baseAnalysisList.indexOf(this)>-1) {
            BaseAnalysis.baseAnalysisList.remove(this);
        }
        this.analysisListeners.clear();
    }

}
