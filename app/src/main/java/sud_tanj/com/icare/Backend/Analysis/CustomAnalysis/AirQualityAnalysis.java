package sud_tanj.com.icare.Backend.Analysis.CustomAnalysis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sud_tanj.com.icare.Backend.Analysis.BaseAnalysis;
import sud_tanj.com.icare.Backend.Plugins.CustomPlugins.AirQualitySensor;
@RequiredArgsConstructor
public class AirQualityAnalysis extends BaseAnalysis {
    @Getter @Setter
    private double ppm=-1;
    private int getAnalysis(){
        if(ppm <= 50)
        {
            return BaseAnalysis.EXCELLENT_CONDITION;
        }
        else if (ppm <=100){
            return BaseAnalysis.GOOD_CONDITION;
        }
        else if (ppm <=199)
        {
            return BaseAnalysis.BAD_CONDITION;
        }
        else {
            return BaseAnalysis.WORSE_CONDITION;
        }
    }

    private String getMessage(){
        if(ppm <= 50)
        {
            return "Baik";
        }
        else if (ppm <=100){
            return "Sedang";
        }
        else if (ppm <=199)
        {
            return "Tidak Sehat";
        }
        else {
            return "Sangat Tidak Sehat";
        }
    }
    @Override
    public void run() {
        fireEventListener(getAnalysis(),getMessage());
    }
}
