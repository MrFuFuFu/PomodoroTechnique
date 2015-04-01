package mrfu.pomodoro.utils;

import android.content.Context;

import mrfu.pomodoro.PomodoroApplication;

public class SettingUtility {
	public static final String POMODORO_DURATION = "TomatoTime_value";
	public static final String BREAK_DURATION = "BreakTime_value";
    private static final String ENABLE_TICKING = "pref_enable_ticking";
    private static final String DEBUG_MODE = "developer_Mode";
    private static final String ENABLE_VIBRATIONS = "startShake";
    private static final String FINSH_MUSIC = "pref_ringtone";

	//Anti be killed Test
	private static final String FINISH_TIME = "finish_time";
	private static final String NOW_RUNNING_TYPE = "now_running_type";
	public static final int NONE_RUNNING = 0;
	public static final int POMO_RUNNING = 1;
	public static final int BREAK_RUNNING = 2;
	public static final int POMO_FINISHED = 3;
	public static final int BREAK_FINISHED = 4;

    private static final String IS_NEXT = "isNext";
    private static final String IS_GOTOBREAK = "isGotoBreak";

    private SettingUtility() {

    }

    private static Context getContext() {
        return PomodoroApplication.getInstance();
    }

    public static boolean isTick(){
        boolean type = SettingHelper.
                getSharedPreferences(getContext(), ENABLE_TICKING, false);
        return type;
    }

    public static void setTick(boolean isTick){
        SettingHelper.setEditor(getContext(), ENABLE_TICKING, isTick);
    }

    public static long getFinishTimeInMills() {
    	long type = SettingHelper.
    			getSharedPreferences(getContext(), FINISH_TIME, 0L);
    	return type;
    }
    
    public static void setFinishTimeInMills(long inMill){
    	SettingHelper.setEditor(getContext(), FINISH_TIME, inMill);
    }
    
    public static int getRunningType() {
    	int type = SettingHelper.
    			getSharedPreferences(getContext(), NOW_RUNNING_TYPE, 0);
    	return type;
    }
    
    public static boolean isPomoRunning(){
    	return getRunningType()==POMO_RUNNING;
    }
    
    public static boolean isBreakRunning(){
    	return getRunningType()==BREAK_RUNNING;
    }
    
    public static void setRunningType(int runningType) {
    	SettingHelper.setEditor(getContext(), NOW_RUNNING_TYPE, runningType);
    }

    public static int getPomodoroDuration(){
    	int value =  Integer.parseInt(SettingHelper.getSharedPreferences(getContext(), POMODORO_DURATION, "1"));
        if(SettingHelper.getSharedPreferences(getContext(), DEBUG_MODE, false)){
            value = 1;
        }
        return value;
    }
    
    public static void setPomodoroDuration(int duration){
    	SettingHelper.setEditor(getContext(), POMODORO_DURATION, duration);
    }
    
    public static int getBreakDuration(){
        int value =  Integer.parseInt(SettingHelper.getSharedPreferences(getContext(), POMODORO_DURATION, "1"));
		if(SettingHelper.getSharedPreferences(getContext(), DEBUG_MODE, false)){
    		value = 1;
    	}
		return value;
	}

    public static String getFinshMusic(){
        return  SettingHelper.getSharedPreferences(getContext(), FINSH_MUSIC, "");
    }
    
    public static void setBreakDuration(int duration){
    	SettingHelper.setEditor(getContext(), BREAK_DURATION, duration);
    }

    public static boolean isVibrator() {
        boolean value = SettingHelper.getSharedPreferences(getContext(), ENABLE_VIBRATIONS, false);
        return value;
    }

    public static boolean getIsNext(){
        return  SettingHelper.getSharedPreferences(getContext(), IS_NEXT, false);
    }

    public static void setIsNext(boolean isNext){
        SettingHelper.setEditor(getContext(), IS_NEXT, isNext);
    }

    public static boolean getIsGotoBreak(){
        return  SettingHelper.getSharedPreferences(getContext(), IS_GOTOBREAK, false);
    }

    public static void setIsGotobreak(boolean isGotobreak){
        SettingHelper.setEditor(getContext(), IS_GOTOBREAK, isGotobreak);
    }
}
