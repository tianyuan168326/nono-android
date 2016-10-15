package com.seki.therichedittext;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

/**
 * Created by Seki on 2016/5/3.
 */
public class ColorPanel extends AlertDialog.Builder {
    public ColorPanel(Context context,int color) {
        super(context);
        init(color);
    }
    public ColorPanel(Context context, int theme,int color) {
        super(context,theme);
        init(color);
    }
    ColorPicker picker ;
    SVBar svBar ;
    SaturationBar saturationBar ;
    ValueBar valueBar ;
    private void init(int color){
        View view=View.inflate(getContext(),R.layout.color_panel,null);
        picker = (ColorPicker) view.findViewById(R.id.picker);
        svBar = (SVBar) view.findViewById(R.id.svbar);
        saturationBar = (SaturationBar) view.findViewById(R.id.saturationbar);
        valueBar = (ValueBar) view.findViewById(R.id.valuebar);
        picker.addSVBar(svBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        setView(view);
        picker.setOldCenterColor(color);
        setPositiveButton(getContext().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(colorChoseCallback!=null){
                    colorChoseCallback.onColorChose(ColorPanel.this.picker.getColor());
                }
            }
        });
        show();
    }

    public interface OnColorChoseCallback{
        void onColorChose(@ColorInt int color);
    }

    private OnColorChoseCallback colorChoseCallback=null;

    public void setOnColorChoseCallback(OnColorChoseCallback colorChoseCallback){
       this.colorChoseCallback=colorChoseCallback;
    }
}
