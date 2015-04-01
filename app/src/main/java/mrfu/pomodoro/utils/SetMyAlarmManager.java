package mrfu.pomodoro.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import mrfu.pomodoro.MainActivity;
import mrfu.pomodoro.R;

public class SetMyAlarmManager {


    //Set a alarm to start service at the time after "min" minute.
    public static void schedulService(Context mContext, int min, Class<?> cls) {
        PendingIntent mAlarmSender = PendingIntent.getService(mContext, 0, new Intent(mContext, cls), 0);
        long finishTimeInMills = System.currentTimeMillis() + min * 60000;
        // Schedule the alarm!  
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, finishTimeInMills, mAlarmSender);
        MyNotification mn = new MyNotification(mContext);
        mn.showSimpleNotification(mContext.getString(R.string.sp_is_running), mContext.getString(R.string.click_to_return), true, MainActivity.class);
        SettingUtility.setFinishTimeInMills(finishTimeInMills);

    }

    public static void stopschedulService(Context mContext, Class<?> cls) {
        MyNotification n = new MyNotification(mContext);
        n.cancelNotification();
        PendingIntent mAlarmSender = PendingIntent.getService(mContext, 0, new Intent(mContext, cls), 0);
        // cancel the alarm.
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(mAlarmSender);
        SettingUtility.setRunningType(SettingUtility.NONE_RUNNING);
        SettingUtility.setFinishTimeInMills(0);
    }
}
