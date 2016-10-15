package com.seki.noteasklite.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.seki.noteasklite.R;

public class NotifyHelper {
	public static void popNotifyInfo(Context context,String title,String info)
	{
		new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setTitle(title)
		.setMessage(info)
		.setPositiveButton(context.getString(R.string.i_know), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		})
		.show();
	}
	public static void makeToastwithTextAndPic(Context context,String info,int imageResId)
	{
		Toast toast = Toast.makeText(context,info,Toast.LENGTH_SHORT);
//		Toast toast= Toast.makeText(context, info, Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		ViewGroup layout=(ViewGroup)toast.getView();
//		ImageView notifyPic=new ImageView(context);
//		notifyPic.setImageResource(imageResId);
//		layout.addView(notifyPic);
		toast.show();
	}
	public static PopupWindow popUpWaitingAnimation(Context mContext,PopupWindow p)
	{
		Activity activity;
		try
		{
			activity = (Activity)mContext;
		}
		catch (ClassCastException classCastE)
		{
			Toast.makeText(mContext.getApplicationContext(), "生存期错误，看到请反馈", Toast.LENGTH_SHORT)
					.show();
			classCastE.printStackTrace();
		}
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View popupView = layoutInflater.inflate(R.layout.wait_pop_window, null);
		PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		popupWindow.showAsDropDown(popupView);
		return popupWindow;
	}
	public  static void popUpWaitingAnimationFinished(PopupWindow popupWindow)
	{
		if(popupWindow !=null){
			popupWindow.dismiss();
		}
	}
}
