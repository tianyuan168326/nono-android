package com.seki.noteasklite.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.seki.noteasklite.Activity.Note.AuthActivity;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FIRUtils;
import com.seki.noteasklite.Util.PreferenceUtils;

/**
 * Created by yuan-tian01 on 2016/4/3.
 */
public class NONoPreferenceFragment extends PreferenceFragment {
    public static final int RESULT_GRAPHICS_PASSWORD = 1;
    public static final int RESULT_PIN_PASSWORD = 2;

    private static int card_line_num = -1;
    public static int getCardLineNum(){
        if(card_line_num == -1){
            card_line_num  =Integer.valueOf(PreferenceUtils.getPrefString(MyApp.getInstance(),"card_line_num","4") );
        }
        return card_line_num;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_general);
        findPreference("checkUpdate").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        //config About Library
                        FIRUtils.checkForUpdate(getActivity(),true);
                        return false;
                    }
                });
        ( findPreference("app_theme")).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //SkinManager.getInstance().changeSkin((String)newValue)
                ThemeController.ColorPair currentColorPair  = ThemeController.getColor((String)newValue);
                if(currentColorPair == null){
                    Toast.makeText(MyApp.getInstance().getApplicationContext(),"没有这种颜色策略",Toast.LENGTH_LONG).show();
                    return false;
                }
                ThemeController.setColorPolicy(currentColorPair);
                return true;
            }
        });
        //为了图形解锁
        findPreference("is_password").setPersistent(false);
        findPreference("is_password").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(isSetPinPassWord()){
                    Toast.makeText(getActivity(),"请先清除PIN码再设置手势密码！",Toast.LENGTH_SHORT).show();
                }else{
                    AuthActivity.startForResult(NONoPreferenceFragment.this,RESULT_GRAPHICS_PASSWORD);
                }
                return true;
            }
        });
        //为了手势解锁
        findPreference("is_pin_password").setPersistent(false);
        findPreference("is_pin_password").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(isSetGraphicsPassWord()){
                    Toast.makeText(getActivity(),"请先清除手势密码再设置PIN码！",Toast.LENGTH_SHORT).show();
                }else{
                    AuthActivity.startForResult(NONoPreferenceFragment.this,RESULT_PIN_PASSWORD);
                }
                return true;
            }
        });
        findPreference("card_line_num").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                card_line_num = Integer.valueOf((String) newValue) ;
                return true;
            }
        });

    }

    private boolean isSetGraphicsPassWord() {
        return PreferenceUtils.getPrefBoolean(getActivity(),"is_password",false);
    }

    private boolean isSetPinPassWord() {
        return PreferenceUtils.getPrefBoolean(getActivity(),"is_pin_password",false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_GRAPHICS_PASSWORD:
                    if(PreferenceUtils.getPrefBoolean(getActivity(),"is_password",false)== false){
                        ((CheckBoxPreference)findPreference("is_password")).setChecked(false);
                    }else{
                        ((CheckBoxPreference)findPreference("is_password")).setChecked(true);
                    }
            case RESULT_PIN_PASSWORD:
                if(PreferenceUtils.getPrefBoolean(getActivity(),"is_pin_password",false)== false){
                    ((CheckBoxPreference)findPreference("is_pin_password")).setChecked(false);
                }else{
                    ((CheckBoxPreference)findPreference("is_pin_password")).setChecked(true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
