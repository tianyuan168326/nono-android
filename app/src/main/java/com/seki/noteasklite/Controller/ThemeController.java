package com.seki.noteasklite.Controller;

import android.graphics.Color;

import com.seki.noteasklite.DataUtil.BusEvent.ThemeColorPairChangedEvent;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.ColorUtils;
import com.seki.noteasklite.Util.PreferenceUtils;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by yuan on 2016/4/28.
 */
public class ThemeController {
    public static void setColorPolicy(ColorPair currentColorPair) {
        current = currentColorPair;//getColor(PreferenceUtils.getPrefString(MyApp.getInstance().getApplicationContext(), "app_theme", "light_purple"));
        EventBus.getDefault().post(new ThemeColorPairChangedEvent(currentColorPair));
    }

    public static class ColorPair {
        public int mainColor;
        public int darkColor;
        public int accentColor = -1;

        public int getMainColor() {
            return mainColor;
        }

        public int getLightColor() {
            int color = ColorUtils.getLighterColor(getMainColor(), 0.7f);
            int r = Color.red(color);
            int a = Color.alpha(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            return Color.argb(a / 2, r, g, b);
        }

        public void setMainColor(int mainColor) {
            this.mainColor = mainColor;
        }

        public int getDarkColor() {
            return darkColor;
        }

        public void setDarkColor(int darkColor) {
            this.darkColor = darkColor;
        }

        public ColorPair(int mainColorId, int darkColorId) {
            this.mainColor = MyApp.getInstance().getApplicationContext().getResources().getColor(mainColorId);
            this.darkColor = MyApp.getInstance().getApplicationContext().getResources().getColor(darkColorId);
            ;
        }

        public ColorPair(int mainColor, int darkColor, int accentColor) {
            this.mainColor = MyApp.getInstance().getApplicationContext().getResources().getColor(mainColor);
            this.darkColor = MyApp.getInstance().getApplicationContext().getResources().getColor(darkColor);
            this.accentColor = MyApp.getInstance().getApplicationContext().getResources().getColor(accentColor);
        }

        public int getAccentColor() {
            return MyApp.getInstance().getApplicationContext().getResources().getColor(R.color.colorAccent);
        }
    }

    private static HashMap<String, ColorPair> colorPairs = new HashMap<>();

    public static ColorPair getColor(String policyName) {
        if (colorPairs == null || colorPairs.size() == 0) {
            colorPairs.put("light_purple", new ColorPair(R.color.skin_colorPrimary_light_purple, R.color.skin_colorPrimaryDark_light_purple));
            colorPairs.put("red", new ColorPair(R.color.skin_colorPrimary_red, R.color.skin_colorPrimaryDark_red, R.color.colorAccent));
            colorPairs.put("pink", new ColorPair(R.color.skin_colorPrimary_pink, R.color.skin_colorPrimaryDark_pink, R.color.colorAccent));
            colorPairs.put("purple", new ColorPair(R.color.skin_colorPrimary_purple, R.color.skin_colorPrimaryDark_purple, R.color.colorAccent));
            colorPairs.put("deep_purple", new ColorPair(R.color.skin_colorPrimary_deep_purple, R.color.skin_colorPrimaryDark_deep_purple, R.color.colorAccent));
            colorPairs.put("indigo", new ColorPair(R.color.skin_colorPrimary_indigo, R.color.skin_colorPrimaryDark_indigo, R.color.colorAccent));
            colorPairs.put("blue", new ColorPair(R.color.skin_colorPrimary_blue, R.color.skin_colorPrimaryDark_blue, R.color.colorAccent));
            colorPairs.put("cyan", new ColorPair(R.color.skin_colorPrimary_cyan, R.color.skin_colorPrimaryDark_cyan, R.color.colorAccent));
            colorPairs.put("teal", new ColorPair(R.color.skin_colorPrimary_teal, R.color.skin_colorPrimaryDark_teal, R.color.colorAccent));
            colorPairs.put("green", new ColorPair(R.color.skin_colorPrimary_green, R.color.skin_colorPrimaryDark_green, R.color.colorAccent));
            colorPairs.put("lime", new ColorPair(R.color.skin_colorPrimary_lime, R.color.skin_colorPrimaryDark_lime, R.color.colorAccent));
            colorPairs.put("deep_orange", new ColorPair(R.color.skin_colorPrimary_deep_orange, R.color.skin_colorPrimaryDark_deep_orange, R.color.colorAccent));
            colorPairs.put("brown", new ColorPair(R.color.skin_colorPrimary_brown, R.color.skin_colorPrimaryDark_brown, R.color.colorAccent));
            colorPairs.put("blue_grey", new ColorPair(R.color.skin_colorPrimary_blue_grey, R.color.skin_colorPrimaryDark_blue_grey, R.color.colorAccent));
        }
        ColorPair colorPair = colorPairs.get(policyName);
        return colorPair;
    }

    public static ColorPair current = null;

    public static ColorPair getCurrentColor() {
        return current == null ?
                getColor(PreferenceUtils.getPrefString(MyApp.getInstance().getApplicationContext(), "app_theme", "light_purple"))
                : current;
    }
}
