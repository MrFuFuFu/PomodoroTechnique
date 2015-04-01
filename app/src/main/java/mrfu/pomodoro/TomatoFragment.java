package mrfu.pomodoro;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mrfu.pomodoro.model.BreakTask;
import mrfu.pomodoro.model.RunTask;
import mrfu.pomodoro.progressbar.HoloCircularProgressBar;
import mrfu.pomodoro.utils.SettingUtility;

/**
 * Created by MrFu on 15/3/21.
 */
public class TomatoFragment extends Fragment implements View.OnClickListener{

    private ChangeColorListener listener;

    public enum CLICK_TYPE{
        RUN, BREAK, NEXT_RUN
    }
    private View mMainView;
    private HoloCircularProgressBar mProgressbar;
    private TextView tv_time;
    private CLICK_TYPE click_type;
    private static ObjectAnimator mProgressBarAnimator;

    interface ChangeColorListener{
        void changeColor(CLICK_TYPE click_type1);
    }

    public void setChangeColorListener(ChangeColorListener listener){
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mMainView = inflater.inflate(R.layout.tomato_layout, (ViewGroup) getActivity().findViewById(R.id.viewpager), false);
        mProgressbar = (HoloCircularProgressBar)mMainView.findViewById(R.id.progressbar);
        tv_time = (TextView)mMainView.findViewById(R.id.tv_time);
        click_type = CLICK_TYPE.RUN;
        tv_time.setOnClickListener(this);
        tv_time.setText(getActivity().getResources().getString(R.string.start));
    }

    @Override
    public void onClick(View v) {
        if (SettingUtility.isPomoRunning() || SettingUtility.isBreakRunning()) return;
        switch (click_type){
            case RUN:
                listener.changeColor(CLICK_TYPE.RUN);
                tv_time.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                mProgressbar.setProgressColor(getResources().getColor(android.R.color.holo_red_light));
                RunTask.doIt(getActivity(), tv_time);
                dealProgress(mProgressbar, SettingUtility.getPomodoroDuration());
                break;
            case BREAK:
                listener.changeColor(CLICK_TYPE.BREAK);
                tv_time.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                mProgressbar.setProgressColor(getResources().getColor(android.R.color.holo_green_light));
                BreakTask.doIt(getActivity(), tv_time);
                dealProgress(mProgressbar, SettingUtility.getBreakDuration());
                break;
//            case NEXT_RUN:
//                tv_time.setTextColor(getResources().getColor(android.R.color.holo_red_light));
//                mProgressbar.setProgressColor(getResources().getColor(android.R.color.holo_red_light));
//                RunTask.doIt(getActivity(), tv_time);
//                dealProgress(mProgressbar, SettingUtility.getPomodoroDuration());
//                break;
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) mMainView.getParent();
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
        }
        return mMainView;
    }

    public void doBreakTask(){
        click_type = CLICK_TYPE.BREAK;
        tv_time.setText(getActivity().getResources().getString(R.string.tobreak));
    }

    public void doNextTask() {
        click_type = CLICK_TYPE.RUN;
        tv_time.setText(getActivity().getResources().getString(R.string.next_pomodro));
    }

    public void reset() {
        RunTask.cancel();
        BreakTask.cancel();
        if (mProgressBarAnimator != null) {
            mProgressBarAnimator.cancel();
        }
        tv_time.setText(getActivity().getResources().getString(R.string.start));
        tv_time.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        mProgressbar.setProgressColor(getResources().getColor(android.R.color.holo_red_light));
        listener.changeColor(CLICK_TYPE.RUN);
    }




    private void dealProgress(HoloCircularProgressBar progressBar, int totalTimeInMill) {
        if (mProgressBarAnimator != null) {
            mProgressBarAnimator.cancel();
        }
        mProgressbar.setProgress(0);
        animate(progressBar, null, 1, totalTimeInMill * 60 * 1000 + 1000);
    }

    private void animate(final HoloCircularProgressBar progressBar, final Animator.AnimatorListener listener,
                                final float progress, final int duration) {

        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);

        mProgressBarAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
            }

            @Override
            public void onAnimationStart(final Animator animation) {
            }
        });
        if (listener != null) {
            mProgressBarAnimator.addListener(listener);
        }
        mProgressBarAnimator.reverse();
        mProgressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        progressBar.setMarkerProgress(progress);
        mProgressBarAnimator.start();
    }
}
