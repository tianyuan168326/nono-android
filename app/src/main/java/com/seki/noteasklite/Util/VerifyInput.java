package com.seki.noteasklite.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.seki.noteasklite.Activity.LogOnActivity;
import com.seki.noteasklite.DataUtil.LogStateEnum;
import com.seki.noteasklite.MyApp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyInput {
	 public static boolean isUserName(String username) {
	        Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{3,15}$");
	        Matcher m = p.matcher(username);
	        return m.matches();  
	    }   
	 public static boolean isPassword(String password) {
		 return (!TextUtils.isEmpty(password))&&(password.length()>5)&&(password.length()<13);
	    }
	 public static boolean isRealName(String realname) {

	        return (!TextUtils.isEmpty(realname))&&(realname.length()>1)&&(realname.length()<11);
	    }
	    public static boolean isEmail(String email) {
	        String emailPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	        boolean result = Pattern.matches(emailPattern, email);
	        return result;  
	    }
	public static  boolean isSubject(String subjectString)
	{
		return !subjectString.isEmpty();
	}
	public static  boolean isCategory(String categoryString)
	{
		return !categoryString.isEmpty();
	}
	public static void VerifyLogOn(Activity activity)
	{
			if(MyApp.getInstance().userInfo.logStateEnum.equals(LogStateEnum.OFFLINE))
			{
				activity.getBaseContext().startActivity(new Intent()
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						.setClass(activity.getBaseContext(),LogOnActivity.class));
			}
	}
	public static void VerifyLogOnAndToast(Context context)
	{
		if(MyApp.getInstance().userInfo.logStateEnum.equals(LogStateEnum.OFFLINE))
		{
			Toast.makeText(context, "你还未登录", Toast.LENGTH_SHORT);
		}
	}
}
