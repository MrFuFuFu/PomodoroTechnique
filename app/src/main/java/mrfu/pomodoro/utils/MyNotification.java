package mrfu.pomodoro.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import mrfu.pomodoro.R;

public class MyNotification {
	
	private Context mContext;
	public MyNotification(Context c) {
		mContext = c;
	}
	
	public	void showSimpleNotification(String title,String text,boolean ongoing,Class<?> cls){
		
		int i = MyUtils.getContinueTimes(mContext);
		
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(mContext)
		        .setSmallIcon(R.drawable.ic_launcher)
//		        .setTicker(title)
		        .setOngoing(ongoing)
		        .setContentTitle(title)
		        .setContentText(text)
		        .setAutoCancel(!ongoing)
		        .setContentInfo(String.valueOf(i));
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER); 
		intent.setClass(mContext, cls);
        intent.putExtra("isGotoBreak", true);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(9527, mBuilder.getNotification());
	}
	
	public void cancelNotification(){
        Trace.debug("cancelNotification");
		NotificationManager manager=(NotificationManager)mContext.
				getSystemService(Context.NOTIFICATION_SERVICE);
		try {
			manager.cancelAll();
		} catch (Exception e) {
		}
	}

}
