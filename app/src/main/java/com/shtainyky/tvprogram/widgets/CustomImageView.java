package com.shtainyky.tvprogram.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.shtainyky.tvprogram.R;


public class CustomImageView extends android.support.v7.widget.AppCompatImageView{
    private static final int[] STATE_MARKED = {R.attr.state_marked};
    private boolean isPreferred;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPreferred(boolean isPreferred)
    {
        this.isPreferred = isPreferred;
        refreshDrawableState();
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace)
    {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isPreferred)
        {
            mergeDrawableStates(drawableState, STATE_MARKED);
        }
        return drawableState;
    }




}
