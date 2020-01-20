package com.gonchar.project.reminder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.gonchar.project.reminder.R;
import com.gonchar.project.reminder.service.ReminderService;

import static com.gonchar.project.reminder.utils.Constants.KEY_FOR_CHANGE_MAIN;
import static com.gonchar.project.reminder.utils.Constants.KEY_FOT_CHANGE_SETTING;
import static com.gonchar.project.reminder.utils.Constants.ON_SERVICE_KEY;
import static com.gonchar.project.reminder.utils.Constants.SWITCH_THEME_SETTING_KEY;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(KEY_FOT_CHANGE_SETTING, false)) {
            this.setTheme(R.style.AppTheme_Dark);
            setContentView(R.layout.settings_activity_dark);
        } else {
            this.setTheme(R.style.AppTheme);
            setContentView(R.layout.settings_activity);

        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                // this part will change theme of setting activity
                if (key.equals(SWITCH_THEME_SETTING_KEY)) {
                    if (sharedPreferences.getBoolean(SWITCH_THEME_SETTING_KEY, false)) {
                        sharedPreferences.edit().putBoolean(KEY_FOR_CHANGE_MAIN, true).putBoolean(KEY_FOT_CHANGE_SETTING, true).apply();
                        recreate();
                    } else {
                        sharedPreferences.edit().putBoolean(KEY_FOR_CHANGE_MAIN, false).putBoolean(KEY_FOT_CHANGE_SETTING, false).apply();
                        recreate();
                    }
                }
                // this part will stop reminder service
                if (key.equals(ON_SERVICE_KEY)) {
                    if (!sharedPreferences.getBoolean(ON_SERVICE_KEY, false)) {
                        stopService(new Intent(getApplicationContext(), ReminderService.class));
                    }
                }
            }
        });

    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}