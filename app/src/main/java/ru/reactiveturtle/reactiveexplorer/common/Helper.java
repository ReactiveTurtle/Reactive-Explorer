package ru.reactiveturtle.reactiveexplorer.common;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

public class Helper {
    public static GradientDrawable getRoundDrawable(int color, float radius) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(radius);
        return gd;
    }

    public static int getThemeColor(Context context, int attribute) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{attribute});
        int color = a.getColor(0, 0);

        a.recycle();
        return color;
    }

    public static void editDrawableToWhite(Drawable drawable) {
        drawable.clearColorFilter();
        drawable.setColorFilter(new LightingColorFilter(Color.BLACK, Color.WHITE));
    }

    public static void editDrawableToOther(Drawable drawable, int otherColor) {
        drawable.clearColorFilter();
        drawable.setColorFilter(new LightingColorFilter(Color.BLACK, otherColor));
    }

    public static float unitToPixels(int dp, int dimenType, Resources resources) {
        return TypedValue.applyDimension(dimenType, dp, resources.getDisplayMetrics());
    }
}
