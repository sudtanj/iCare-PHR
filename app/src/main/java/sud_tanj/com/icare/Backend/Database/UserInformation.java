package sud_tanj.com.icare.Backend.Database;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 12/08/2018 - 20:33.
 * <p>
 * This class last modified by User
 */
@NoArgsConstructor
public class UserInformation {
    public static final String KEY = "https://icare-89c17.firebaseio.com/UserInformation";
    @Getter @Setter
    private String name="",
            email="";
    @Getter @Setter
    private Boolean developer=false;
}
