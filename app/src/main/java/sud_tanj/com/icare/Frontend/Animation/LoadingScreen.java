package sud_tanj.com.icare.Frontend.Animation;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 16/07/2018 - 20:50.
 * <p>
 * This class last modified by User
 */
public class LoadingScreen {
    private static KProgressHUD hud;
    private static Context context;
    public static void init(Context context){
        LoadingScreen.context=context;
        hud = KProgressHUD.create(context).setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE);
    }
    public static void showLoadingScreen(String messages){
        hud.setLabel(context.getString(R.string.loading_title))
                .setDetailsLabel(messages)
                .setMaxProgress(100)
                .show();
    }


    public static void showLoadingScreen(String messages,int maxProgress){
        hud.setLabel(context.getString(R.string.loading_title))
                .setDetailsLabel(messages)
                .setMaxProgress(maxProgress)
                .show();
    }

    public static void hideLoadingScreen(){
        hud.setProgress(100);
        hud.dismiss();
    }
}
