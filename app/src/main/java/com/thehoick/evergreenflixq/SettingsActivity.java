package com.thehoick.evergreenflixq;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;


public class SettingsActivity extends PreferenceActivity {

    private static final boolean ALWAYS_SIMPLE_PREFS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Need to fix this later:
        // http://developer.android.com/reference/android/preference/PreferenceActivity.html
        addPreferencesFromResource(R.xml.settings);

        // Theme settings for the Settings page, not the Dialog.
        getListView().setCacheColorHint(Color.TRANSPARENT);
        getListView().setBackgroundColor(getResources().getColor(R.color.white));


        Netflix netflix = new Netflix();
        netflix.setmLibraryUrlProblem(false);


    }
}
