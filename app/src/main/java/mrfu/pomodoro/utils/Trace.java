package mrfu.pomodoro.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by MrFu on 15/3/21.
 */
public class Trace {
    private static String TAG = "MrFu";
    public static void debug(String content){
        if(!TextUtils.isEmpty(content))
            Log.i(TAG, content);
    }

    public static void debug(int content) {
        Log.i(TAG, "" + content);
    }
    public static void debug(long content) {
        Log.i(TAG, "" + content);
    }
}
