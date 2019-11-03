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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch ternOn = null;
    EditText reminderMessage = null;
    private EditText timeValue = null;
    Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ternOn = findViewById(R.id.switch1);
        Button off_Button = findViewById(R.id.off_button);
        Button on_Button = findViewById(R.id.on_button);

        reminderMessage = findViewById(R.id.reminderMessage);
        timeValue = findViewById(R.id.timeValue);
        //final Context pageContext = this;
        /*final Intent intent =new Intent (this, MyService.class)
                .putExtra("message", reminderMessage.getText())
                .putExtra("time", Integer.parseInt(timeValue.getText().toString()))
                .putExtra("flag", flag);*/



        off_Button.setOnClickListener(new View.OnClickListener() {

            /**
             * this method check active service and stop it
             * if no active service - will be write message for user
             * used for off_button only
             * @param v
             */
            @Override
            public void onClick(View v) {

                if (flag) {
                    // this block execute if will be find active service
                    flag = false;

                    stopService(new Intent(getApplicationContext(), MyService.class)
                            .putExtra("message", reminderMessage.getText())
                            .putExtra("time", Integer.parseInt(timeValue.getText().toString()))
                            .putExtra("flag", flag));

                } else {
                    // this block execute if no active service
                    writeMessage(getString(R.string.MainActivity_onClick_method_forOffButton_noActiveService));
                }
            }
        });



        on_Button.setOnClickListener(new View.OnClickListener() {

            /**
             * This method make start service, check text input fields
             * used for on_Button only
             *
             * @param v
             */
            @Override
            public void onClick(View v) {

                //this block check reminder input fields
                if (!flag && (reminderMessage.getText().toString().equals(""))) {
                    writeMessage(getString(R.string.MainActivity_checkUserValue_method_fillReminderMessage));
                    ternOn.setChecked(false);

                } else if (!flag && timeValue.getText().toString().equals("")) {
                    //this block check time input fields
                    writeMessage(getString(R.string.MainActivity_checkUserValue_method_fillTimeValue));
                    ternOn.setChecked(false);

                } else {
                    //this block will execute if all text fields is fill

                    if (!flag) {
                        //this block used if API laval > API 26
                        flag = true;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(new Intent(getApplicationContext(), MyService.class)
                                    .putExtra("message", reminderMessage.getText())
                                    .putExtra("time", Integer.parseInt(timeValue.getText().toString()))
                                    .putExtra("flag", flag));

                        } else {
                            //this block used if API laval < API 26
                            startService(new Intent(getApplicationContext(), MyService.class)
                                    .putExtra("message", reminderMessage.getText())
                                    .putExtra("time", Integer.parseInt(timeValue.getText().toString()))
                                    .putExtra("flag", flag));
                        }
                    }
                }
            }
        });

        /*ternOn.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && (reminderMessage.getText().toString().equals(""))) {
                    writeMessage(getString(R.string.MainActivity_checkUserValue_method_fillReminderMessage));
                    ternOn.setChecked(false);
                } else if (isChecked && timeValue.getText().toString().equals("")) {
                    writeMessage(getString(R.string.MainActivity_checkUserValue_method_fillTimeValue));
                    ternOn.setChecked(false);
                } else {
                    if (isChecked && !flag) {
                        startService(new Intent(pageContext, MyService.class)
                                .putExtra("message", reminderMessage.getText())
                                .putExtra("time", Integer.parseInt(timeValue.getText().toString())));
                        flag = true;
                    } else if (!isChecked && flag) {
                        stopService(new Intent(pageContext, MyService.class)
                                .putExtra("message", reminderMessage.getText())
                                .putExtra("time", Integer.parseInt(timeValue.getText().toString())));
                        flag = false;
                    }
                }
            }
        });*/
    }

    private void writeMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("ReminderMessage", reminderMessage.getText().toString());
        outState.putString("timeValue", timeValue.getText().toString());
        outState.putBoolean("flagKey", flag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        reminderMessage.setText(savedInstanceState.getString("ReminderMessage"));
        timeValue.setText(savedInstanceState.getString("timeValue"));
        flag = savedInstanceState.getBoolean("flagKey");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
