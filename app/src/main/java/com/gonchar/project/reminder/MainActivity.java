package com.gonchar.project.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gonchar.project.reminder.utils.Tools;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static com.gonchar.project.reminder.utils.Constants.*;

public class MainActivity extends AppCompatActivity {


    TextInputLayout reminderMessage;
    TextInputLayout timeValue;
    Button offButton;
    Button onButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        offButton = findViewById(R.id.off_button);
        onButton = findViewById(R.id.on_button);

        reminderMessage = findViewById(R.id.reminderMessage);
        timeValue = findViewById(R.id.timeValue);
        context = this;

    }


    private void writeMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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

        if(Tools.checkServiceRunning(ReminderService.class.getName(), context)){
            stopService(new Intent(view.getContext(), ReminderService.class));
            writeMessage(getString(R.string.MainActivity_onClickForOffButton_method_serviceIsStopped));

        } else {
            writeMessage(getString(R.string.MainActivity_onClick_method_forOffButton_noActiveService));
        }
    }


    public void onCLickForOnButton(View view) {

        if(!Tools.isEmptyMessage(reminderMessage.getEditText().getText().toString())
                && !Tools.isEmptyMessage(timeValue.getEditText().getText().toString())
                && !Tools.checkServiceRunning(ReminderService.class.getName(), context)){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(view.getContext(), ReminderService.class)
                        .putExtra(EXTRAS_MESSAGE_KEY, Objects.requireNonNull(reminderMessage.getEditText()).getText())
                        .putExtra(EXTRAS_TIME_VALUE_KEY, Integer.parseInt(Objects.requireNonNull(timeValue.getEditText()).getText().toString())));
                writeMessage(getString(R.string.MainActivity_onClickForOnButton_method_serviceIsStarted));

            } else {
                startService(new Intent(view.getContext(), ReminderService.class)
                        .putExtra(EXTRAS_MESSAGE_KEY, reminderMessage.getEditText().getText())
                        .putExtra(EXTRAS_TIME_VALUE_KEY, Integer.parseInt(timeValue.getEditText().getText().toString())));
                writeMessage(getString(R.string.MainActivity_onClickForOnButton_method_serviceIsStarted));
            }

        }else{
            writeMessage(getString(R.string.MainActivity_onCLickForOnButton_method_notAllFieldsAreFilled));
        }
    }

}
