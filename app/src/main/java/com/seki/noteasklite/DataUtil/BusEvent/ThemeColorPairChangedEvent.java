package com.seki.noteasklite.DataUtil.BusEvent;

import com.seki.noteasklite.Controller.ThemeController;

/**
 * Created by yuan on 2016/4/28.
 */
public class ThemeColorPairChangedEvent {
    ThemeController.ColorPair currentColorPair;

    public ThemeColorPairChangedEvent(ThemeController.ColorPair currentColorPair) {
        this.currentColorPair = currentColorPair;
    }

    public ThemeController.ColorPair getCurrentColorPair() {
        return currentColorPair;
    }

    public void setCurrentColorPair(ThemeController.ColorPair currentColorPair) {
        this.currentColorPair = currentColorPair;
    }
}
