package mrfu.pomodoro.model;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import mrfu.pomodoro.service.BreakFinishService;
import mrfu.pomodoro.utils.MyUtils;
import mrfu.pomodoro.utils.SetMyAlarmManager;
import mrfu.pomodoro.utils.SettingUtility;

/**
 * Created by MrFu on 15/3/21.
 */
public class BreakTask {
    private static int maxBreakDuration;
    private static CountDownTimer timer;

    public static void doIt(final Context context, final TextView tv_Time){

        maxBreakDuration = SettingUtility.getBreakDuration();
        final long leftTimeInMills;

        long finishTime = SettingUtility.getFinishTimeInMills();
        long nowTime = MyUtils.getCurrentUTCInMIlls();
        int runningType = SettingUtility.getRunningType();
        if((finishTime > nowTime) &&(runningType == SettingUtility.BREAK_RUNNING)){
            leftTimeInMills = finishTime - nowTime;
        }else {
            SettingUtility.setRunningType(SettingUtility.BREAK_RUNNING);
            SetMyAlarmManager.schedulService(context,
                    maxBreakDuration,
                    BreakFinishService.class);
            leftTimeInMills = (long)maxBreakDuration*60*1000;
        }

        SettingUtility.setRunningType(SettingUtility.BREAK_RUNNING);
        timer = new CountDownTimer(leftTimeInMills, 1000) {
            int min, sec;
            String secStr;
            @Override
            public void onTick(long millisUntilFinished) {
                min = (int) (millisUntilFinished / 60000);
                sec = (int) ((millisUntilFinished - min *60000)/1000);
                if (sec < 10){
                    secStr = "0"+String.valueOf(sec);
                }else{
                    secStr = String.valueOf(sec);
                }
                tv_Time.setText(min+" "+secStr);
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    public static void cancel() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
