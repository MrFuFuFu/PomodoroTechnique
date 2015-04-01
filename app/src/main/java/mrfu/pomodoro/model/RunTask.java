package mrfu.pomodoro.model;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import mrfu.pomodoro.service.CDService;
import mrfu.pomodoro.utils.SetMyAlarmManager;
import mrfu.pomodoro.utils.SettingUtility;

/**
 * Created by MrFu on 15/3/21.
 */
public class RunTask {
    private static CountDownTimer timer;

    public static void doIt(Context context, TextView tv_Time) {
        final int leftTimeInSec;
        long finishTime = SettingUtility.getFinishTimeInMills();
        long nowTime = System.currentTimeMillis();
        int runningType = SettingUtility.getRunningType();

        if ((finishTime > nowTime) && (runningType == SettingUtility.POMO_RUNNING)) {
            leftTimeInSec = (int) ((finishTime - nowTime + 1000) / 1000);
        } else {
            SettingUtility.setRunningType(SettingUtility.POMO_RUNNING);
            SetMyAlarmManager.schedulService(context, SettingUtility.getPomodoroDuration(), CDService.class);
            leftTimeInSec = SettingUtility.getPomodoroDuration() * 60;
        }
        startCountDown(tv_Time, leftTimeInSec);
    }

    private static void startCountDown(final TextView tv_Time, final int leftTimeInSec) {
        SettingUtility.setRunningType(SettingUtility.POMO_RUNNING);
        timer = new CountDownTimer((long) leftTimeInSec * 1000, 1000) {
            int min, sec;
            String secStr;
            @Override
            public void onTick(long millisUntilFinished) {
                min = (int) (millisUntilFinished / 60000);
                sec = (int) ((millisUntilFinished - min * 60000) / 1000);
                if (sec < 10) {
                    secStr = "0" + String.valueOf(sec);
                } else {
                    secStr = String.valueOf(sec);
                }
                tv_Time.setText(String.valueOf(min) + " " + secStr);
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    public static void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
