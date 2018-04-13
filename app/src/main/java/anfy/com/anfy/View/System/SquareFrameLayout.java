package anfy.com.anfy.View.System;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Ahmed on 9/11/2017.
 */

public class SquareFrameLayout extends FrameLayout {

    public SquareFrameLayout(@NonNull Context context) {
        super(context);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
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
