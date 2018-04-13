package anfy.com.anfy.View.System;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Ahmed on 5/28/2017.
 */

public class SquareImageView extends AppCompatImageView {
    public SquareImageView(@NonNull Context context) {
        super(context);
    }

    public SquareImageView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
