package sud_tanj.com.icare.Backend.Analysis.CustomAnalysis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sud_tanj.com.icare.Backend.Analysis.BaseAnalysis;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 15/12/2018 - 16:55.
 * <p>
 * This class last modified by User
 */
@RequiredArgsConstructor
public class DistanceAnalysis extends BaseAnalysis {
    @Getter @Setter
    private Double distance;

    protected int getAnalysisResult(){
        if(distance>5){
            return EXCELLENT_CONDITION;
        }
        return WORSE_CONDITION;
    }

    protected String getDescription(){
        if(distance>5){
                return "You are so far away!";
        }
        return "Your are too close!";
    }

    @Override
    public void run() {
        fireEventListener(getAnalysisResult(),getDescription());
    }
}
