package anfy.com.anfy.Fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import anfy.com.anfy.Activity.Base.BaseActivity;
import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Activity.MainActivity;
import anfy.com.anfy.App.AppController;
import anfy.com.anfy.App.MyPreferenceManager;
import anfy.com.anfy.Model.UserModel;
import anfy.com.anfy.R;
import butterknife.BindView;

public class BaseFragment extends Fragment{

    @Nullable
    @BindView(R.id.loading)
    View loading;
    @Nullable
    @BindView(R.id.no_internet_layout)
    View noInternet;
    @Nullable
    @BindView(R.id.retry)
    View retry;
    @Nullable
    @BindView(R.id.no_data)
    View noData;

    protected void openActivity(Class<?> cls){
        Intent i = new Intent(getContext(), cls);
        startActivity(i);
    }

    protected void openActivityForRes(Class<?> cls, int reuqestCode){
        Intent i = new Intent(getContext(), cls);
        startActivityForResult(i, reuqestCode);
    }

    protected FragmentSwitchActivity activity(){
        return (FragmentSwitchActivity) getActivity();
    }

    protected void showNoData(boolean show){
        if(noData != null){
            noData.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    protected int getUserId(){
        try {
            return ((BaseActivity) getActivity()).getUser().getId();
        }catch (Exception e){
            return AppController.NO_USER_ID;
        }
    }

    public void showLoading(boolean show){
        if(loading != null){
            loading.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void showNoInternet(boolean show, View.OnClickListener retryClickListener){
        if(noInternet != null && retry != null){
            noInternet.setVisibility(show ? View.VISIBLE : View.GONE);
            retry.setOnClickListener(retryClickListener);
        }
    }
}
