package mrfu.pomodoro.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import mrfu.pomodoro.MainActivity;
import mrfu.pomodoro.R;
import mrfu.pomodoro.utils.MyNotification;
import mrfu.pomodoro.utils.MyUtils;
import mrfu.pomodoro.utils.SettingUtility;

public class CDService extends Service {

	
	public Handler mHandler=new Handler()  
    {  
        @Override
		public void handleMessage(Message msg)  
        {  
            switch(msg.what)  
            {
            case 1:  
            	doService();
                //TODO add count
      		  	//for long break
      		  	MyUtils.addContinueTimes(CDService.this);
      		  	//notification
      	        MyNotification mn = new MyNotification(CDService.this);
      	        mn.showSimpleNotification(getString(R.string.pomodoro_time_up), getString(R.string.tap_to_break), false, MainActivity.class);
                break;  
            default:  
                break;        
            }  
            super.handleMessage(msg);  
        }  
    };  

  @Override
  public void onCreate() {
	  Thread thread=new Thread(new Runnable()  
        {  
            @Override  
            public void run()  
            {
                //when timer over, go here
                Message message=new Message();
                message.what=1;  
                mHandler.sendMessage(message);  
                 
            }  
        });  
        thread.start();  
     
  }
  
  private void doService(){
	  SettingUtility.setRunningType(SettingUtility.POMO_FINISHED);
	  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String strRingtonePreference = sp.getString("pref_ringtone", "");
		if(!strRingtonePreference.equals("")){
            Uri ringtoneUri = Uri.parse(strRingtonePreference);
            Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
            ringtone.play();
		}
		
		
		Boolean isVibrator = sp.getBoolean("startShake", false);
		if(isVibrator){
			Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			mVibrator.vibrate(new long[]{50,100,50,100}, -1);
		}

		MyUtils.ScreenState screenState = MyUtils.getScreenState(this);
		Intent intent = new Intent(CDService.this,MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(MyUtils.INTENT_IS_FULLSCREEN, false);
//        intent.putExtra("isGotoBreak", true);
        SettingUtility.setIsGotobreak(true);
		switch (screenState) {
		case LOCK:
//			intent.putExtra(MyUtils.INTENT_IS_FULLSCREEN, true);
			startActivity(intent);
			break;
		case MYAPP:
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			break;
		case OTHERAPP:
			showAlertDialog();
			break;
		default:
			break;
		}
		CDService.this.stopSelf();//停止服务
	  
  }
	private void showAlertDialog(){
		AlertDialog d = new AlertDialog.Builder(this)
        .setTitle(getString(R.string.pomodoro_time_up))
        .setMessage(getString(R.string.now_you_can_take_a_break))
        .setPositiveButton(R.string.take_a_break, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(CDService.this,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("isGotoBreak", true);
				startActivity(intent);
				
				
			}
		})
        .setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
      	.create();
		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		d.show();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
