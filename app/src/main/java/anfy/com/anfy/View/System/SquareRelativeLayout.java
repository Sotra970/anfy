package anfy.com.anfy.View.System;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Ahmed on 5/22/2017.
 */

public class SquareRelativeLayout extends LinearLayout {

    public SquareRelativeLayout(Context context) {
        super(context);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

                if (widthSize == 0 && heightSize == 0) {
                        // If there are no constraints on size, let FrameLayout measure
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

                        // Now use the smallest of the measured dimensions for both dimensions
                        final int minSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
                        setMeasuredDimension(minSize, minSize);
                        return;
                   }

                final int size;
                if (widthSize == 0 || heightSize == 0) {
                        // If one of the dimensions has no restriction on size, set both dimensions to be the
                        // on that does
                        size = Math.max(widthSize, heightSize);
                    } else {
                        // Both dimensions have restrictions on size, set both dimensions to be the
                        // smallest of the two
                        size = Math.min(widthSize, heightSize);
                    }

                final int newMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
                super.onMeasure(newMeasureSpec, newMeasureSpec);
    }

}
