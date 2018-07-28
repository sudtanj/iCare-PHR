package sud_tanj.com.icare.Frontend.Icon;

import android.content.Context;
import android.graphics.drawable.Drawable;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue;

import sud_tanj.com.icare.R;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 27/07/2018 - 16:13.
 * <p>
 * This class last modified by BasicUser
 */
public class IconBuilder {
    private static Context context;
    public static void init(Context context){
        IconBuilder.context=context;
    }
    public static Drawable get(IconValue iconValue){
        Drawable drawable = MaterialDrawableBuilder.with(context) // provide a context
                .setIcon(iconValue) // provide an icon
                //.setColor(R.color.theme_default_primary) // set the icon color
                .setColorResource(R.color.theme_default_primary)
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build
        return drawable;
    }
}
