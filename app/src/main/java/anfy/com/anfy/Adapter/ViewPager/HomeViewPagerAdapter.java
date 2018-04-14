package anfy.com.anfy.Adapter.ViewPager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anfy.com.anfy.Model.DepartmentItem;
import anfy.com.anfy.R;
import anfy.com.anfy.Util.Utils;

public class HomeViewPagerAdapter extends BaseFragmentPagerAdapter {

    private Context context;

    public HomeViewPagerAdapter(ArrayList<Fragment> fragments,
                                @NonNull FragmentManager fragmentManager,
                                Context context) {
        super(fragments, fragmentManager);
        this.context = context;
    }

    public View getTabView(DepartmentItem departmentItem, Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout_item_white, null);
        try {
            ((TextView) view.findViewById(R.id.title)).setText(departmentItem.getName());
            Glide.with(context).load(Utils.getImageUrl(departmentItem.getIcon()))
                    .into(((ImageView) view.findViewById(R.id.image)));
        }catch (Exception e){
            Log.e("getTabView()", e.getMessage());
        }
        return view;
    }
}
