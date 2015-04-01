package mrfu.pomodoro;

import android.app.Activity;
import android.app.Application;

/**
 * @author Mr.傅
 * 2015-3-21 下午12:22:39
 */
public class PomodoroApplication extends Application {

    private static PomodoroApplication globalContext = null;
	public static Activity activity = null;
	
	@Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
    }
	
	public static PomodoroApplication getInstance() {
        return globalContext;
    }
	
	public static Activity getActivity(){
		return activity;
	}
	
	public static void setActivity(Activity a){
		activity = a;
	}
}
