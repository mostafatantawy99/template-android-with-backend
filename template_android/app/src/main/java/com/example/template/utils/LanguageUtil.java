package com.example.template.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.example.template.R;
import com.example.template.model.shareddata.Prefs;

import java.util.Locale;

/**
 * Created by omar on 6/15/2016.
 */
public class LanguageUtil {


    public static void setAppLanguage(Context context, String localeLang) {


        Prefs.putString(context.getString(R.string.language_prefs), localeLang);


        setDeviceLocale(context, localeLang);
    }

    public static String getAppLanguage(Context context)

    {
        String appLanguage = Prefs.getString(context.getString(R.string.language_prefs), context.getString(R.string.arabiclanguage_prefs));
        setDeviceLocale(context, appLanguage);

        return appLanguage;

    }

    public static String getDeviceLocale()

    {

        Log.e("getDeviceLocale", Locale.getDefault().getISO3Language());
        return Locale.getDefault().getISO3Language();


    }


    private static void setDeviceLocale(Context context, String localeLang) {
        Locale locale = new Locale(localeLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


}
