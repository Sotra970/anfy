package anfy.com.anfy.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Activity.MainActivity;
import anfy.com.anfy.App.AppController;

public class BaseFragment extends Fragment{

    protected void openActivity(Class<?> cls){
        Intent i = new Intent(getContext(), cls);
        startActivity(i);
    }

    protected FragmentSwitchActivity activity(){
        return (FragmentSwitchActivity) getActivity();
    }

    protected void showNoData(boolean show){
        try {
            ((MainActivity) getActivity()).showNoData(show);
        }catch (Exception e){}
    }

    protected void showNoInternet(boolean show, View.OnClickListener retryClickListener){
        try {
            ((MainActivity) getActivity()).showNoInternet(show, retryClickListener);
        }catch (Exception e){}
    }

    protected void showLoading(boolean show){
        try {
            ((MainActivity) getActivity()).showLoading(show);
        }catch (Exception e){}
    }

    protected int getUserId(){
        try {
            return ((BaseActivity) getActivity()).getUser().getId();
        }catch (Exception e){
            return AppController.NO_USER_ID;
        }
    }
}
