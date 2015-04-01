package mrfu.pomodoro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import mrfu.pomodoro.service.BreakFinishService;
import mrfu.pomodoro.service.CDService;
import mrfu.pomodoro.utils.MyNotification;
import mrfu.pomodoro.utils.SetMyAlarmManager;
import mrfu.pomodoro.utils.SettingUtility;

/**
 * Created by MrFu on 15/3/21.
 */
public class MainActivity extends FragmentActivity implements TomatoFragment.ChangeColorListener {


    private ViewPager mViewPager;
    private PagerTabStrip mPagerTabShip;
    private TomatoFragment tomatoFragment;
    private TaskFragment taskFragment;
    private ArrayList<Fragment> fragmentList;
    ArrayList<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PomodoroApplication.setActivity(this);
        initView();
    }

    private void initView(){
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mPagerTabShip = (PagerTabStrip)findViewById(R.id.pagertab);
        //设置下划线的颜色
        mPagerTabShip.setTabIndicatorColor(getResources().getColor(android.R.color.white));
        //设置背景的颜色
        mPagerTabShip.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        mPagerTabShip.setTextColor(getResources().getColor(android.R.color.white));
        tomatoFragment = new TomatoFragment();
        taskFragment = new TaskFragment();
        fragmentList = new ArrayList<Fragment>();
        titleList = new ArrayList<String>();
        fragmentList.add(tomatoFragment);
        fragmentList.add(taskFragment);
        titleList.add(getResources().getString(R.string.pomodoro));
        titleList.add(getResources().getString(R.string.task));
        mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        tomatoFragment.setChangeColorListener(this);
    }

    @Override
    protected void onResume() {
        initResume();
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        initResume();
        super.onNewIntent(intent);
    }

    private void initResume() {
        boolean isNext = SettingUtility.getIsNext();
        boolean isGotoBreak = SettingUtility.getIsGotoBreak();
        SettingUtility.setIsNext(false);
        SettingUtility.setIsGotobreak(false);
        if (isGotoBreak){
            initWindow();
            ((TomatoFragment)fragmentList.get(0)).doBreakTask();
            return;
        }
        if (isNext){
            initWindow();
            ((TomatoFragment)fragmentList.get(0)).doNextTask();
            return;
        }
    }

    private void initWindow(){
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//		    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

//        if(SettingUtility.isLightsOn()){
//            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        }

        //nofitication
        MyNotification mn = new MyNotification(this);
        mn.cancelNotification();
    }

    @Override
    public void changeColor(TomatoFragment.CLICK_TYPE click_type) {
        if (click_type == TomatoFragment.CLICK_TYPE.BREAK){
            mPagerTabShip.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }else if (click_type == TomatoFragment.CLICK_TYPE.RUN){
            mPagerTabShip.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }


    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            return true;
        }else if (id == R.id.action_about){

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((SettingUtility.isPomoRunning() || SettingUtility.isBreakRunning())
                && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog d = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.stop_pomodoro))
                    .setMessage(getString(R.string.do_you_wish_to_stop))
                    .setPositiveButton(R.string.running_in_background, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            MainActivity.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.stop, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SetMyAlarmManager.stopschedulService(MainActivity.this, CDService.class);
                            SetMyAlarmManager.stopschedulService(MainActivity.this, BreakFinishService.class);
//                            //退出时复原
//                            if(SettingUtility.isSilentMode()){
//                                MyUtils.controlNetwork(true, getApplicationContext());
//                            }
                            ((TomatoFragment)fragmentList.get(0)).reset();
                        }
                    })
                    .create();
            d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            d.show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
