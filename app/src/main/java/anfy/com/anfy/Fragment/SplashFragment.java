package anfy.com.anfy.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import anfy.com.anfy.Activity.Base.FragmentSwitchActivity;
import anfy.com.anfy.Activity.LoginActivity;
import anfy.com.anfy.Activity.MainActivity;
import anfy.com.anfy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashFragment extends BaseFragment {


    private View mView;

    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.logo)
    ImageView logo;

    public static SplashFragment getInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_splash, container, false);
            ButterKnife.bind(this, mView);
            Glide.with(this).load(R.drawable.splash).into(bg);
            Glide.with(this).load(R.drawable.logo_no_bg).into(logo);

        }
        return mView;
    }

}
