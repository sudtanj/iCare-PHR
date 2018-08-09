package sud_tanj.com.icare.Backend.Microcontrollers;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 09/08/2018 - 10:22.
 * <p>
 * This class last modified by User
 */
public class MicrocontrollerBackgroundJob extends JobService {
    List<BaseMicrocontroller> runnableMicrocontrollers=BaseMicrocontroller.getBaseMicrocontrollerList();
    @Override
    public boolean onStartJob(JobParameters job) {
        System.out.println("Freibase jobs running");
        for(BaseMicrocontroller runnableMicrocontroller:runnableMicrocontrollers){
            runnableMicrocontroller.run();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        System.out.println("Firebase jbos stop");
        for(BaseMicrocontroller runnableMicrocontroller:runnableMicrocontrollers){
            runnableMicrocontroller.onDispose();
        }
        return true;
    }
}
