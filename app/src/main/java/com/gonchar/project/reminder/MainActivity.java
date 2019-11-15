package com.gonchar.project.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reminderMessage = findViewById(R.id.reminderMessage);
        timeValue = findViewById(R.id.timeValue);

        Tools.makeCustomToolBar(Objects.requireNonNull(getSupportActionBar()));

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
            stopService(new Intent(view.getContext(), ReminderService.class));
        }
    }


    public void onCLickForOnButton(View view) {
        if (!Tools.isEmptyMessage(timeValue.getEditText().getText().toString()) ||
                !Tools.shouldShowError(MIN_TIME_VALUE, Integer.parseInt(timeValue.getEditText().getText().toString()))) {
            timeValue.getEditText().setText("1");

        }
        if (Tools.shouldShowError(reminderMessage.getEditText().length(), MIN_MESSAGE_LENGTH)) {
            Tools.showError(getText(R.string.MainActivity_showError_method_Error).toString(),reminderMessage);

        } else if (Tools.checkServiceRunning(ReminderService.class.getName(), view.getContext())) {
            stopService(new Intent(view.getContext(), ReminderService.class));
            startReminderService(view);

        } else {
            startReminderService(view);
        }
    }

    public void startReminderService(View view) {
        Tools.showError(null, reminderMessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(view.getContext(), ReminderService.class)
                    .putExtra(EXTRAS_MESSAGE_KEY, Objects.requireNonNull(reminderMessage.getEditText()).getText())
                    .putExtra(EXTRAS_TIME_VALUE_KEY, Integer.parseInt(Objects.requireNonNull(timeValue.getEditText()).getText().toString())));

        } else {
            startService(new Intent(view.getContext(), ReminderService.class)
                    .putExtra(EXTRAS_MESSAGE_KEY, reminderMessage.getEditText().getText())
                    .putExtra(EXTRAS_TIME_VALUE_KEY, Integer.parseInt(timeValue.getEditText().getText().toString())));
        }

    }

}
