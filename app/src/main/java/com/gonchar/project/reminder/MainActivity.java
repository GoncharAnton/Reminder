package com.gonchar.project.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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


    private TextInputLayout reminderMessage; // -
    private TextInputLayout timeValue; // -
    PreferencesManager manager = new PreferencesManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setCustomToolBar(Objects.requireNonNull(getSupportActionBar()));
        initView();
        checkUserSetting();
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
     * this method initialize value
     */
    private void initView(){
        reminderMessage = findViewById(R.id.reminderMessage);
        timeValue = findViewById(R.id.timeValue);
    }

    /**
     * this method rewrite text field in TextInputLayouts reminderMessage and timeValue if application
     * wos closet but service wos`t stop
     */
    private void checkUserSetting() {

        if (manager.contains(SHARED_PREFERENCES_REMINDER_KEY)) {
            reminderMessage.getEditText().setText(manager.getPreference(SHARED_PREFERENCES_REMINDER_KEY));
        }
        if (manager.contains(SHARED_PREFERENCES_TIME_VALUE_KEY)) {
            timeValue.getEditText().setText(manager.getPreference(SHARED_PREFERENCES_TIME_VALUE_KEY));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(EXTRAS_MESSAGE_KEY, reminderMessage.getEditText().getText().toString());
        outState.putString(EXTRAS_TIME_VALUE_KEY, timeValue.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        reminderMessage.getEditText().setText(savedInstanceState.getString(EXTRAS_MESSAGE_KEY));
        timeValue.getEditText().setText(savedInstanceState.getString(EXTRAS_TIME_VALUE_KEY));
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onClickForOffButton(View view) {
        if (Tools.checkServiceRunning(ReminderService.class.getName(), view.getContext())) {
            manager.putPreferences(SHARED_PREFERENCES_REMINDER_KEY,EMPTY_STRING);
            manager.putPreferences(SHARED_PREFERENCES_TIME_VALUE_KEY,EMPTY_STRING);
            stopService(new Intent(view.getContext(), ReminderService.class));
        }
    }

    public void onCLickForOnButton(View view) {

        if(Tools.shouldShowError(MIN_MESSAGE_LENGTH, reminderMessage.getEditText().length())){
            Tools.showError(getText(R.string.MainActivity_showError_method_Error_ReminderMessageError).toString(),reminderMessage);
        }else{
            Tools.showError(null, reminderMessage);
        }
        if (Tools.isEmptyMessage(timeValue.getEditText().getText().toString()) ||
                Tools.shouldShowError(MIN_TIME_VALUE, Integer.parseInt(timeValue.getEditText().getText().toString()))) {
            Tools.showError(getText(R.string.MainActivity_onClickForOnButton_argumentInShowErrorMethod_timeValueError).toString(), timeValue);
        } else if (Tools.shouldShowError(MIN_MESSAGE_LENGTH, reminderMessage.getEditText().length())) {
            Tools.showError(null, timeValue);
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

        manager.putPreferences(SHARED_PREFERENCES_REMINDER_KEY, reminderMessage.getEditText().getText().toString());
        manager.putPreferences(SHARED_PREFERENCES_TIME_VALUE_KEY,timeValue.getEditText().getText().toString());
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

        Tools.showError(null, timeValue);
        Intent intent = new Intent(view.getContext(), ReminderService.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(EXTRAS_MESSAGE_KEY, Objects.requireNonNull(reminderMessage.getEditText()).getText())
                .putExtra(EXTRAS_TIME_VALUE_KEY, Integer.parseInt(Objects.requireNonNull(timeValue.getEditText()).getText().toString()));
        intent.setAction(ACTION_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}
