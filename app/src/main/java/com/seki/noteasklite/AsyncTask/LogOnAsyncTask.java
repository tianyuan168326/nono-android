package com.seki.noteasklite.AsyncTask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.seki.noteasklite.Activity.LogOnActivity;
import com.seki.noteasklite.Controller.AccountController;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.DataUtil.ActivityEnum;
import com.seki.noteasklite.DataUtil.Bean.AuthorBean;
import com.seki.noteasklite.DataUtil.BusEvent.LogOnSuccess;
import com.seki.noteasklite.DataUtil.LogStateEnum;
import com.seki.noteasklite.DataUtil.UserLogInfo;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.NetWorkServices.ServiceFactory;
import com.seki.noteasklite.R;
import com.seki.noteasklite.RetrofitHelper.RequestBody.AuthBody;
import com.seki.noteasklite.Util.FrescoImageloadHelper;
import com.seki.noteasklite.Util.NetWorkUtil;
import com.seki.noteasklite.Util.NotifyHelper;
import com.seki.noteasklite.Util.SeriesLogOnInfo;
import com.seki.noteasklite.Activity.MainActivity;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.seki.noteasklite.Util.EncryptUtils.SHA1;

//import org.apache.http.message.BasicNameValuePair;

//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;
//import io.rong.imlib.model.Message;
//import io.rong.imlib.model.UserInfo;
//
public class LogOnAsyncTask extends AsyncTask<String,Integer,String>
{
	Context mcontext;
	PopupWindow logOnWaitWindow;
	UserLogInfo userLogInfo;
	ActivityEnum activtyEnum;
	String userName;
	String userPassword;
	public LogOnAsyncTask(Context context,UserLogInfo userLogInfo, ActivityEnum activtyEnum, String userName, String userPassword)
	{
		this.mcontext=context;
		this.userLogInfo = userLogInfo;
		this.activtyEnum = activtyEnum;
		this.userName = userName;
		this.userPassword = userPassword;
        Toast.makeText(mcontext,"初始化",Toast.LENGTH_SHORT);
	}
	@Override
	protected String doInBackground(String... data) {
		if(TextUtils.isEmpty(userName) ||TextUtils.isEmpty(userPassword)){
			return "no count";
		}
		return NetWorkUtil.httpHelper("logon.php",
				new BasicNameValuePair("username", userName)
				, new BasicNameValuePair("rawpassword", userPassword)
		);
	}

	@Override
	protected void onPostExecute(String result) {

		/**
		 * 获取json 数据包格式
		 * user_head_img
		 * user_real_name
		 * user_university
		 * user_school
		 *user_token
		 * user_id
		 */
		super.onPostExecute(result);

		if(activtyEnum == ActivityEnum.LogOnActivtyEnum) {
			logOnWaitWindow.dismiss();
		}
		if("no count".equals(result)){
			return ;
		}
		try
		{
			JSONObject jsonObject = new JSONObject(result);
			String stateCodeStr = jsonObject.getString("state_code");
			int state_code = Integer.valueOf(stateCodeStr);

			if(state_code != 0)
			{
				Toast.makeText(mcontext, mcontext.getString(R.string.logon_info_ero), Toast.LENGTH_SHORT).show();
				//mcontext.startActivity(new Intent().setClass(mcontext, LogOnActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				return ;
			}
			JSONObject dataJson = jsonObject.getJSONObject("data");
			{
				MyApp.userInfo.userHeadPicURL =  dataJson.getString("user_head_img");
				MyApp.userInfo.fuckHeadImgae();
				MyApp.userInfo.userRealName =  dataJson.getString("user_real_name");
				MyApp.userInfo.userSchool =  dataJson.getString("user_school");
				MyApp.userInfo.userUniversity =  dataJson.getString("user_university");
				MyApp.userInfo.userToken = dataJson.getString("user_token");
				MyApp.userInfo.userId = dataJson.getString("user_id");
				MyApp.userInfo.userAbstract = dataJson.getString("user_abstract");
				MyApp.userInfo.logStateEnum = LogStateEnum.ONLINE;
				MyApp.userInfo.username = userName;
				MyApp.userInfo.userpassword = userPassword;
				MyApp.userInfo.userSex = dataJson.getString("user_sex");
				MyApp.userInfo.wonderFull = jsonObject.getString("wonderFull");
			}
			AccountController.rightAfterLogOn();
			EventBus.getDefault().post(new LogOnSuccess());
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(activtyEnum == ActivityEnum.LogOnActivtyEnum) {
			logOnWaitWindow = NotifyHelper.popUpWaitingAnimation(mcontext, logOnWaitWindow);
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
}
