package sud_tanj.com.icare.Backend.Database;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 15/07/2018 - 14:20.
 * <p>
 * This class last modified by User
 */

public class HealthData {
    @Getter @Setter
    private String healthDataId = null;
    @Getter @Setter
    private Long timeStamp = null;

    public HealthData(String healthDataId, Date timeStamp) {
        this.healthDataId = healthDataId;
        this.timeStamp = timeStamp.getTime();
    }
}
