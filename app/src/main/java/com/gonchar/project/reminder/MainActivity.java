package com.gonchar.project.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.gonchar.project.reminder.utils.Tools;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static com.gonchar.project.reminder.utils.Constants.*;


public class MainActivity extends AppCompatActivity {


    private TextInputLayout reminderMessage;
    private TextInputLayout timeValue;
    private SharedPreferences userVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tools.makeCustomToolBar(Objects.requireNonNull(getSupportActionBar()));

        reminderMessage = findViewById(R.id.reminderMessage);
        timeValue = findViewById(R.id.timeValue);
        checkUserSetting();


    }

    /**
     *  this method rewrite text field in TextInputLayouts reminderMessage and timeValue if application
     *  wos closet but service wos`t stop
     */
    private void checkUserSetting() {
        userVariable = getSharedPreferences("userVar", MODE_PRIVATE);
        if(userVariable.contains(SHARED_PREFERENCES_REMINDER_KEY)){
            reminderMessage.getEditText().setText(userVariable.getString(SHARED_PREFERENCES_REMINDER_KEY,""));
        }
        if (userVariable.contains(USER_SETTING_TIME_VALUE_KEY)) {
            timeValue.getEditText().setText(userVariable.getString(USER_SETTING_TIME_VALUE_KEY,""));
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
            changeUserSetting("","");
            stopService(new Intent(view.getContext(), ReminderService.class));
        }
    }

    /**
     * this method save user settings (text  rom text fields in reminderMassage and timeValue)
     * @param newReminder last variant from user or empty string if method coll in onClickForOffButton method
     * @param newTimeValue last variant from user or empty string if method coll in onClickForOffButton method
     */
    private void changeUserSetting(String newReminder, String newTimeValue) {
        SharedPreferences.Editor editor = userVariable.edit();
        editor.putString(SHARED_PREFERENCES_REMINDER_KEY,newReminder);
        editor.putString(USER_SETTING_TIME_VALUE_KEY,newTimeValue);
        editor.apply();
    }


    public void onCLickForOnButton(View view) {

        if (Tools.isEmptyMessage(timeValue.getEditText().getText().toString()) ||
                Tools.shouldShowError(MIN_TIME_VALUE, Integer.parseInt(timeValue.getEditText().getText().toString()))) {
            Tools.showError(getText(R.string.MainActivity_onClickForOnButton_argumentInShowErrorMethod_timeValueError).toString(), timeValue);
            Tools.showError(null, reminderMessage);
        } else if (Tools.shouldShowError(MIN_MESSAGE_LENGTH, reminderMessage.getEditText().length())) {
            Tools.showError(getText(R.string.MainActivity_showError_method_Error).toString(), reminderMessage);
            Tools.showError(null, timeValue);
        } else {
            serviceCheck(view);
        }
    }

    /**
     * this method start or restart reminder service (check service, is it work in this moment or not)
     * if service is working - calls the stopService method (stop service) then startReminderService
     * method (start with new parameters), if service ist work - calls the tartReminderService method
     * (start with new parameters)
     *
     * @param view object with user interface
     */
    private void serviceCheck(View view) {

        if (Tools.checkServiceRunning(ReminderService.class.getName(), view.getContext())) {
            stopService(new Intent(view.getContext(), ReminderService.class));
            startReminderService(view);
        } else {
            changeUserSetting(reminderMessage.getEditText().getText().toString(),
                    timeValue.getEditText().getText().toString());
            startReminderService(view);
        }
    }

    /**
     * this method delete all error message, create new intent object and start reminder service
     * @param view object with user interface
     */
    public void startReminderService(View view) {

        Tools.showError(null, reminderMessage);
        Tools.showError(null, timeValue);
        Intent intent = new Intent(view.getContext(), ReminderService.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(EXTRAS_MESSAGE_KEY, Objects.requireNonNull(reminderMessage.getEditText()).getText())
                .putExtra(EXTRAS_TIME_VALUE_KEY, Integer.parseInt(Objects.requireNonNull(timeValue.getEditText()).getText().toString()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);

        } else {
            startService(intent);
        }
    }

}
