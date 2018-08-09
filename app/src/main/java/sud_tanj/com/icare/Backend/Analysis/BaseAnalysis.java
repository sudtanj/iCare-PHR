package sud_tanj.com.icare.Backend.Analysis;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 09/08/2018 - 17:16.
 * <p>
 * This class last modified by User
 */
public abstract class BaseAnalysis implements Runnable{
    public static final int EXCELLENT_CONDITION=0;
    public static final int GOOD_CONDITION=1;
    public static final int BAD_CONDITION=2;
    public static final int WORSE_CONDITION=3;
    @Getter
    protected static List<BaseAnalysis> baseAnalysisList=new ArrayList<>();
    protected List<AnalysisListener> analysisListeners;

    public void addanalysisListener(AnalysisListener analysisListener){
        if(analysisListeners.indexOf(analysisListener)==-1){
            analysisListeners.add(analysisListener);
        }
    }

    public void removeAnalysisListener(AnalysisListener analysisListener){
        if(analysisListeners.indexOf(analysisListener)>-1){
            analysisListeners.remove(analysisListener);
        }
    }

    public void onDispose(){
        if(BaseAnalysis.baseAnalysisList.indexOf(this)>-1) {
            BaseAnalysis.baseAnalysisList.remove(this);
        }
        this.analysisListeners.clear();
    }

}
