package sud_tanj.com.icare.Backend.Sensors.CustomSensor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sud_tanj.com.icare.Backend.Sensors.BaseMicrocontrollerSensor;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 15/12/2018 - 16:44.
 * <p>
 * This class last modified by User
 */
@RequiredArgsConstructor
public class HCSR04Sensor extends BaseMicrocontrollerSensor {
    @Getter @Setter
    private Double duration;
    @Override
    public String getUnitMeasurement() {
        return "cm";
    }

    @Override
    public void run() {
        fireEventListener(calculateResult());
    }

    @Override
    protected Double calculateResult() {
        return (getDuration()/2)/29.1;
    }
}
