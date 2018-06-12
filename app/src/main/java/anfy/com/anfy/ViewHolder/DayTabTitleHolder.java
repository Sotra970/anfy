package anfy.com.anfy.ViewHolder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import anfy.com.anfy.R;


/**
 * Created by Sotraa on 6/13/2016.
 */
public class DayTabTitleHolder {
    private final View view;
    LayoutInflater inflater ;
    TextView tab_day;
    TextView tab_day_name;
    View tab_day_bg;
    Context context ;
    public long child  ;
    public int day ;
    public DayTabTitleHolder(Long child , Context context){
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.day_tab_title_holder, null);
        this.context = context ;
        this.child = child ;
        tab_day = (TextView) view.findViewById(R.id.tab_day);
        tab_day_name = (TextView) view.findViewById(R.id.tab_day_name);
        tab_day_bg =  view.findViewById(R.id.tab_day_bg);

        Calendar calendar = Calendar.getInstance() ;
        calendar.setTimeInMillis(child);
        String dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK , Calendar.LONG , new Locale("ar")) ;
        day  = calendar.get(Calendar.DAY_OF_MONTH) ;
        tab_day.setText(day+"");
        tab_day_name.setText(dayName);
    }
    public View getView (){
        return view;
    }

    public  void select(){
        tab_day_name.setTextColor(ContextCompat.getColor(context , R.color.colorAccent));
        tab_day_bg.setBackgroundResource(R.drawable.accent_circle);

    }


    public  void unSelect(){
        tab_day_name.setTextColor(ContextCompat.getColor(context , R.color.grey_700));
        tab_day_bg.setBackgroundResource(R.drawable.grey_circle);
    }
}
