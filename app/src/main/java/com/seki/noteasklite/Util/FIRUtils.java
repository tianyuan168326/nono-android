package com.seki.noteasklite.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seki.noteasklite.BuildConfig;
import com.seki.noteasklite.DataUtil.Bean.UpdateInfo;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by leon on 15/5/16.
 * Check for update via Fir.im or wandoujia.You need to use a Theme.AppCompat theme (or descendant)
 * with this activity
 */
public class FIRUtils {

    static String GENERAL_KEY="38bd3638cfbb282b1772dbf6a86ad797";
    public final static void checkForUpdate(Context context, boolean isShowToast) {
        VersionCheckCallback versionCheckCallback = callback(context, isShowToast);
            FIR.checkForUpdateInFIR(GENERAL_KEY, versionCheckCallback);
            //FIR.checkForUpdateInAppStore( versionCheckCallback);

    }
    public class VersionInfo{
        int firVersionCode=0;
        String firVersionName="0";
    }
    static VersionCheckCallback callback(final Context context, final boolean isShowToast) {

        return new VersionCheckCallback() {
            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                final UpdateInfo updateInfo;
                try{
                    updateInfo = new Gson().fromJson(s,new TypeToken<UpdateInfo>(){}.getType());
                }catch (Exception e){
                    return;
                }

                double version = 0;
                try{
                    version = Double.valueOf(updateInfo.getVersion());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(version <= Double.valueOf(BuildConfig.VERSION_CODE)){
                    if(isShowToast){
                        Toast.makeText(context,"你已经是最新的内测版本啦！", Toast.LENGTH_SHORT).show();
                    }

                    return ;
                }
                try {
                    if(isShowToast){
                        Toast.makeText(context,"检测到新版本", Toast.LENGTH_SHORT).show();
                    }

                    new AlertDialog.Builder(context).setTitle("有新版本可用")
                            .setMessage(updateInfo.getChangelog())
                            .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //when download complete, broadcast will be sent to receiver
                                    DownloadUtils.DownloadApkWithProgress(context.getApplicationContext(),
                                            updateInfo.getDirect_install_url());
                                }
                            })
                            .setNegativeButton("明天再说", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AppPreferenceUtil.setCheckUpdateInterval(AppPreferenceUtil.CHECK_UPDATE_INTERVAL_DAY);
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNeutralButton("近期不提醒", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AppPreferenceUtil.setCheckUpdateInterval(AppPreferenceUtil.CHECK_UPDATE_INTERVAL_WEEK);
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                    //IllegalStateException when using appcompAlertDialog or NullException when windows leak
                } catch (Exception e) {
                    if(isShowToast){
                        Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }
}
