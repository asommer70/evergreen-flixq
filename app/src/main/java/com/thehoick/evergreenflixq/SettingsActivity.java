package com.thehoick.evergreenflixq;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;


import java.util.List;


public class SettingsActivity extends PreferenceActivity {

    private static final boolean ALWAYS_SIMPLE_PREFS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setupActionBar();

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        // Theme settings for the Settings page, not the Dialog.
        getListView().setCacheColorHint(Color.TRANSPARENT);
        getListView().setBackgroundColor(getResources().getColor(R.color.white));

        //setTheme(R.style.PreferencesDialogTheme);

        Netflix netflix = new Netflix();
        netflix.setmLibraryUrlProblem(false);


    }
}
