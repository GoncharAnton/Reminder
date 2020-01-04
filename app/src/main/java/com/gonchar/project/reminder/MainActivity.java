package com.gonchar.project.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.gonchar.project.reminder.service.ReminderService;
import com.gonchar.project.reminder.utils.PreferencesManager;
import com.gonchar.project.reminder.utils.Tools;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static com.gonchar.project.reminder.utils.Constants.*;


public class MainActivity extends AppCompatActivity {


    private PreferencesManager manager = PreferencesManager.init(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCustomToolBar(Objects.requireNonNull(getSupportActionBar()));
        checkUserSetting();
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.putBooleanPreferences(ACTIVITY_STATE, false);
        if (!manager.getStringPreference(SHARED_PREFERENCES_REMINDER_KEY)
                .equals(((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText().getText().toString())) {
            ((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText().setText(manager.getStringPreference(SHARED_PREFERENCES_REMINDER_KEY));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        manager.putBooleanPreferences(ACTIVITY_STATE, true);
        ((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText().setText(manager.getStringPreference(SHARED_PREFERENCES_REMINDER_KEY));

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(EXTRAS_MESSAGE_KEY, ((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText().getText().toString());
        outState.putString(EXTRAS_TIME_VALUE_KEY, ((TextInputLayout)findViewById(R.id.timeValue)).getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        ((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText().setText(savedInstanceState.getString(EXTRAS_MESSAGE_KEY));
        ((TextInputLayout)findViewById(R.id.timeValue)).getEditText().setText(savedInstanceState.getString(EXTRAS_TIME_VALUE_KEY));
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * this method change default toolbar
     * in this version print label (app name) in the middle of toolbar
     *
     * @param actionBar it is default toolbar
     */
    public static void setCustomToolBar(ActionBar actionBar) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.toolbar);
    }

    /**
     * this method rewrite text field in TextInputLayouts reminderMessage and timeValue if application
     * wos closet but service wos`t stop
     */
    private void checkUserSetting() {

        if (manager.contains(SHARED_PREFERENCES_REMINDER_KEY)) {
            ((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText().setText(manager.getStringPreference(SHARED_PREFERENCES_REMINDER_KEY));
        }
        if (manager.contains(SHARED_PREFERENCES_TIME_VALUE_KEY)) {
            ((TextInputLayout)findViewById(R.id.timeValue)).getEditText().setText(manager.getStringPreference(SHARED_PREFERENCES_TIME_VALUE_KEY));
        }
    }

    public void onClickForOffButton(View view) {
        if (Tools.checkServiceRunning(ReminderService.class.getName(), view.getContext())) {
            manager.putStringPreferences(SHARED_PREFERENCES_REMINDER_KEY, EMPTY_STRING);
            manager.putStringPreferences(SHARED_PREFERENCES_TIME_VALUE_KEY, EMPTY_STRING);
            manager.putBooleanPreferences(SHARED_PREFERENCES_AUTO_START_KEY, false);
            stopService(new Intent(view.getContext(), ReminderService.class));
        }
    }

    public void onCLickForOnButton(View view) {

        if (Tools.shouldShowError(MIN_MESSAGE_LENGTH, ((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText().length())) {
            Tools.showError(getText(R.string.MainActivity_showError_method_Error_ReminderMessageError).toString(), ((TextInputLayout)findViewById(R.id.reminderMessage)));
        } else {
            Tools.showError(null, ((TextInputLayout)findViewById(R.id.reminderMessage)));
        }
        if (Tools.isEmptyMessage(((TextInputLayout)findViewById(R.id.timeValue)).getEditText().getText().toString()) ||
                Tools.shouldShowError(MIN_TIME_VALUE, Integer.parseInt(((TextInputLayout)findViewById(R.id.timeValue)).getEditText().getText().toString()))) {
            Tools.showError(getText(R.string.MainActivity_onClickForOnButton_argumentInShowErrorMethod_timeValueError).toString(), ((TextInputLayout)findViewById(R.id.timeValue)));
        } else if (Tools.shouldShowError(MIN_MESSAGE_LENGTH, ((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText().length())) {
            Tools.showError(null, ((TextInputLayout)findViewById(R.id.timeValue)));
        } else {
            serviceCheck(view);
        }
    }


    /**
     * this method stat or restart reminder service (check service, is it work in this moment or not)
     * if service is working - calls the stopService method (stop service) then startReminderService
     * method (start with new parameters), if service ist work - calls the tartReminderService method
     * (start with new parameters)
     *
     * @param view object with user interface
     */
    private void serviceCheck(View view) {

        manager.putStringPreferences(SHARED_PREFERENCES_REMINDER_KEY, ((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText().getText().toString());
        manager.putStringPreferences(SHARED_PREFERENCES_TIME_VALUE_KEY, ((TextInputLayout)findViewById(R.id.timeValue)).getEditText().getText().toString());
        manager.putBooleanPreferences(SHARED_PREFERENCES_AUTO_START_KEY, true);
        if (Tools.checkServiceRunning(ReminderService.class.getName(), view.getContext())) {
            stopService(new Intent(view.getContext(), ReminderService.class));
        }
        startReminderService(view);
    }

    /**
     * this method delete all error message, create new intent object and start reminder service
     *
     * @param view object with user interface
     */
    public void startReminderService(View view) {

        Tools.showError(null, ((TextInputLayout)findViewById(R.id.timeValue)));
        Intent intent = new Intent(view.getContext(), ReminderService.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(EXTRAS_MESSAGE_KEY, Objects.requireNonNull(((TextInputLayout)findViewById(R.id.reminderMessage)).getEditText()).getText())
                .putExtra(EXTRAS_TIME_VALUE_KEY, Integer.parseInt(Objects.requireNonNull(
                        ((TextInputLayout)findViewById(R.id.timeValue)).getEditText()).getText().toString()));
        intent.setAction(ACTION_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}
