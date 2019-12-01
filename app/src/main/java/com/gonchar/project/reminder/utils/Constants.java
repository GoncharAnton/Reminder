package com.gonchar.project.reminder.utils;

public class Constants {

    public static final String EXTRAS_MESSAGE_KEY = "message";

    public static final String EXTRAS_TIME_VALUE_KEY = "time";

    public static final int DEFAULT_TIME_VALUE = 1000;

    public static final int COEFFICIENT_FOR_CONVERT_MS_IN_SEC = 1000;

    public static final String SERVICE_CHANNEL_ID = "channel_id_first";

    public static final String SERVICE_CHANNEL_NAME = "delivery service reminder";

    public static final String SERVICE_CHANNEL_DESCRIPTION = "service channel (delivery message about background service)";

    public static final String NOTIFICATION_CHANNEL_ID = "channel_id_second";

    public static final String NOTIFICATION_CHANNEL_NAME = "delivery user reminder";

    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "notification channel (delivery user reminder)";

    public static  final int MIN_MESSAGE_LENGTH = 4;

    public static final int MIN_TIME_VALUE = 1;

    public static final String QUICK_NOTIFICATION_CHANGE = "quickChange";

    public static final String REMOTE_KEY = "remoteInputKey";

    public static final String SHARED_PREFERENCES_REMINDER_KEY = "sharedPreferencesReminderKey";

    public static final String USER_SETTING_TIME_VALUE_KEY = "sharedPreferencesTimeValueKey";

}
