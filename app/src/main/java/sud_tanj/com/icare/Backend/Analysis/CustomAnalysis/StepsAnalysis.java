package sud_tanj.com.icare.Backend.Analysis.CustomAnalysis;

import lombok.NoArgsConstructor;
import lombok.Setter;
import sud_tanj.com.icare.Backend.Analysis.BaseAnalysis;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 12/08/2018 - 15:50.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public class StepsAnalysis extends BaseAnalysis {
    @Setter
    private double steps=-1;

    protected int getAnalysisResult(){
        if(steps>-1){
            if(steps>2000){
                return EXCELLENT_CONDITION;
            }
            if(steps>1500){
                return GOOD_CONDITION;
            }
            if(steps>1000){
                return BAD_CONDITION;
            }
        }
        return WORSE_CONDITION;
    }

    protected String getDescription(){
        if(steps>-1){
            if(steps>2000){
                return "You must be feel great!";
            }
            if(steps>1500){
                return "It's still good! keep it up!";
            }
            if(steps>1000){
                return "Not too bad but need more exercise";
            }
        }
        return "Need to exercise alot! steps is below the recommendation!";
    }
    @Override
    public void run() {
        fireEventListener(getAnalysisResult(),getDescription());
    }
}
