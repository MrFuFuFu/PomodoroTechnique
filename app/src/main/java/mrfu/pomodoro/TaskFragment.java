package mrfu.pomodoro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MrFu on 15/3/21.
 */
public class TaskFragment extends Fragment {

    private View mMainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mMainView = inflater.inflate(R.layout.task_layout, (ViewGroup)getActivity().findViewById(R.id.viewpager),false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)mMainView.getParent();
        if (viewGroup != null){
            viewGroup.removeAllViewsInLayout();
        }
        return mMainView;
    }
}
