package com.example.pointview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cdc4512 on 2017/12/14.
 */

public class Utils {

    public static final int COLOR_PRIMARY = 1;
    public static final int COLOR_PRIMARY_DARK = 2;
    public static final int COLOR_ACCENT = 3;

    @IntDef({COLOR_PRIMARY, COLOR_PRIMARY_DARK, COLOR_ACCENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorType {

    }


    public static int dp2px(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) Math.ceil(dp * dm.density);
    }

    public static int sp2px(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) Math.ceil(dp * dm.scaledDensity);
    }

    /**
     * 获取APP主题颜色
     * @param ctx
     * @param type 应用主题色类型
     * */
    public static int getThemeColor(Context ctx, @ColorType int type) {
        TypedValue value = new TypedValue();
        if (type == COLOR_PRIMARY) {
            ctx.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        } else if (type == COLOR_PRIMARY_DARK) {
            ctx.getTheme().resolveAttribute(R.attr.colorPrimaryDark, value, true);
        } else if (type == COLOR_ACCENT) {
            ctx.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        }
        return value.data;
    }

}
