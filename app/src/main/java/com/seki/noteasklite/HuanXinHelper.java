package com.seki.noteasklite;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.util.EMLog;
import com.seki.noteasklite.Activity.Ask.NotifyActivity;
import com.seki.noteasklite.Activity.ChatActivity;
import com.seki.noteasklite.DataUtil.Bean.NotificationBean;
import com.seki.noteasklite.DataUtil.Bean.NotificationDataModel;
import com.seki.noteasklite.Util.Constant;
import com.seki.noteasklite.Activity.MainActivity;
import com.yuantian.com.easeuitransplant.EaseCommonUtils;
import com.yuantian.com.easeuitransplant.EaseEmojicon;
import com.yuantian.com.easeuitransplant.EaseNotifier;
import com.yuantian.com.easeuitransplant.EaseUI;
import com.yuantian.com.easeuitransplant.EaseUser;
import com.yuantian.com.easeuitransplant.PreferenceManager;

import java.util.List;
import java.util.Map;

public class HuanXinHelper {
    /**
     * 数据同步listener
     */
    static public interface DataSyncListener {
        /**
         * 同步完毕
         * @param success true：成功同步到数据，false失败
         */
        public void onSyncComplete(boolean success);
    }

    protected static final String TAG = "DemoHelper";
    
	private EaseUI easeUI;
	
    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

	private UserProfileManager userProManager;

	private static HuanXinHelper instance = null;
	
	private DemoModel demoModel = null;

    
    private boolean alreadyNotified = false;
	
	private String username;

    private Context appContext;

    private EMConnectionListener connectionListener;


	private HuanXinHelper() {
	}

	public synchronized static HuanXinHelper getInstance() {
		if (instance == null) {
			instance = new HuanXinHelper();
		}
		return instance;
	}

	/**
	 * init helper
	 * 
	 * @param context
	 *            application context
	 */
	public void init(Context context) {
	    demoModel = new DemoModel(context);
	    EMOptions options = initChatOptions();
	    //options传null则使用默认的
		if (EaseUI.getInstance().init(context, options)) {
		    appContext = context;
		    
		    //设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
		    EMClient.getInstance().setDebugMode(false);
		    //get easeui instance
		    easeUI = EaseUI.getInstance();
		    //调用easeui的api设置providers
		    setEaseUIProviders();
			//初始化PreferenceManager
			PreferenceManager.init(context);
			//初始化用户管理类
			getUserProfileManager().init(context);
			
			//设置全局监听
			setGlobalListeners();
	        initDbDao();
		}
	}

	
	private EMOptions initChatOptions(){
        Log.d(TAG, "init HuanXin Options");
        
        // 获取到EMChatOptions对象
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 设置是否需要已读回执
        options.setRequireAck(true);
        // 设置是否需要已送达回执
        options.setRequireDeliveryAck(false);
        // 设置从db初始化加载时, 每个conversation需要加载msg的个数
        //options.setNumberOfMessagesLoaded(1);
        
        //使用gcm和mipush时，把里面的参数替换成自己app申请的
        //设置google推送，需要的GCM的app可以设置此参数
        options.setGCMNumber("324169311137");
        //在小米手机上当app被kill时使用小米推送进行消息提示，同GCM一样不是必须的
        options.setMipushConfig("2882303761517426801", "5381742660801");
        
        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
        options.setDeleteMessagesAsExitGroup(getModel().isDeleteMessagesAsExitGroup());
        options.setAutoAcceptGroupInvitation(getModel().isAutoAcceptGroupInvitation());
        
        return options;
//        notifier.setNotificationInfoProvider(getNotificationListener());
    }

    protected void setEaseUIProviders() {
        //需要easeui库显示用户头像和昵称设置此provider
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
        
        //不设置，则使用easeui默认的
        easeUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {
            
            @Override
            public boolean isSpeakerOpened() {
                return demoModel.getSettingMsgSpeaker();
            }
            
            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return demoModel.getSettingMsgVibrate();
            }
            
            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return demoModel.getSettingMsgSound();
            }
            
            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if(message == null){
                    return demoModel.getSettingMsgNotification();
                }
                if(!demoModel.getSettingMsgNotification()){
                    return false;
                }else{
                    return true;
                }
            }
        });
        //设置表情provider
        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {

                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                //返回文字表情emoji文本和图片(resource id或者本地路径)的映射map
                return null;
            }
        });
        
        //不设置，则使用easeui默认的
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //修改标题,这里使用默认
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //设置小图标，这里为默认
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    return getUserInfo(message.getFrom()).getNick() + ": " + ticker;
                } else {
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
                // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                if(message.getBody() instanceof EMCmdMessageBody){

                    Intent intent = new Intent().setClass(MyApp.getInstance().getApplicationContext(), NotifyActivity.class);
                    return intent;
                }
                //设置点击通知栏跳转事件
                Intent intent = new Intent(appContext, ChatActivity.class);
                //有电话时优先跳转到通话页面
                {
                    ChatType chatType = message.getChatType();
                    if (chatType == ChatType.Chat) { // 单聊信息
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    } else { // 群聊信息
                        // message.getTo()为群聊id
                        intent.putExtra("userId", message.getTo());
                        if (chatType == ChatType.GroupChat) {
                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                return intent;
            }
        });
    }
    
    /**
     * 设置全局事件监听
     */
    protected void setGlobalListeners(){
        
        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                if (error == EMError.USER_REMOVED) {
                    onCurrentAccountRemoved();
                }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    onConnectionConflict();
                }
            }

            @Override
            public void onConnected() {
                
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
                if(true){
                    new Thread(){
                        @Override
                        public void run(){
                            HuanXinHelper.getInstance().notifyForRecevingEvents();
                        }
                    }.start();
                }else{
                }
            }
        };
        
        //注册连接监听
        EMClient.getInstance().addConnectionListener(connectionListener);
        //注册消息事件监听
        registerEventListener();
        
    }
    
    private void initDbDao() {
    }
    /**
     * 账号在别的设备登录
     */
    protected void onConnectionConflict(){
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACCOUNT_CONFLICT, true);
        appContext.startActivity(intent);
    }
    
    /**
     * 账号被移除
     */
    protected void onCurrentAccountRemoved(){
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACCOUNT_REMOVED, true);
        appContext.startActivity(intent);
    }
	
	private EaseUser getUserInfo(String username){
	    //获取user信息，demo是从内存的好友列表里获取，
        //实际开发中，可能还需要从服务器获取用户信息,
        //从服务器获取的数据，最好缓存起来，避免频繁的网络请求
        EaseUser user = HuanXinUserManager.get(username);
        if(user == null){
            user = new EaseUser(username);
            HuanXinUserManager.put(username,user);
        }
        if(TextUtils.isEmpty(user.getNick())){
            user.setNick("不知名的小同学");
        }
        return user;
	}

	 /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void registerEventListener() {
    	messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;
			
			@Override
			public void onMessageReceived(List<EMMessage> messages) {
			    for (EMMessage message : messages) {
			        EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
			        //应用在后台，不需要刷新UI,通知栏提示新消息
			        if(!easeUI.hasForegroundActivies()){
			            getNotifier().onNewMsg(message);
			        }
			    }
			}
			
			@Override
			public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    try{
                        String message_title = message.getStringAttribute("message_title");
                        String message_abstract = message.getStringAttribute("message_abstract");
                        String logo = message.getStringAttribute("logo");
                        String message_detail = message.getStringAttribute("message_detail");
                        NotificationBean notificationBean = new Gson().fromJson(message_detail,
                                new TypeToken<NotificationBean>(){}.getType());
                        NotificationDataModel notificationDataModel = new NotificationDataModel();
                        notificationDataModel.hasRead = false;
                         notificationDataModel.dataTime = notificationBean.notification_data;
                        notificationDataModel.questionAbstract = notificationBean.question_abstract;
                        notificationDataModel.answerAbstract = notificationBean.key_abstract;
                        notificationDataModel.otherSideUserRealName = notificationBean.other_side_user_real_name;
                        notificationDataModel.answerId = notificationBean.answer_id;
                        notificationDataModel.questionId =notificationBean.question_id;
                        notificationDataModel.otherSideUserId = notificationBean.other_side_user_id;
                        notificationDataModel.notificationHistoryType = Integer.valueOf( notificationBean.notify_history_type);
                        notificationDataModel.commentId = notificationBean.comment_id;
                        notificationDataModel.commentAbstract = notificationBean.comment_abstract;
                        MyApp.getInstance().getNotificationDataModelList().add(0,notificationDataModel);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    NotificationDataModel.broadAddNotification();
                    //应用在后台，不需要刷新UI,通知栏提示新消息
                    if(!easeUI.hasForegroundActivies()){
                        getNotifier().onNewCmd(message);
                    }
                }
			}

			@Override
			public void onMessageReadAckReceived(List<EMMessage> messages) {
			}
			
			@Override
			public void onMessageDeliveryAckReceived(List<EMMessage> message) {
			}
			
			@Override
			public void onMessageChanged(EMMessage message, Object change) {
				
			}
		};
		
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

	/**
	 * 是否登录成功过
	 * 
	 * @return
	 */
	public boolean isLoggedIn() {
		return EMClient.getInstance().isLoggedInBefore();
	}

	/**
	 * 退出登录
	 * 
	 * @param unbindDeviceToken
	 *            是否解绑设备token(使用GCM才有)
	 * @param callback
	 *            callback
	 */
	public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
		Log.d(TAG, "logout: " + unbindDeviceToken);
		EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "logout: onSuccess");
			    reset();
				if (callback != null) {
					callback.onSuccess();
				}

			}

			@Override
			public void onProgress(int progress, String status) {
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}

			@Override
			public void onError(int code, String error) {
				Log.d(TAG, "logout: onSuccess");
                reset();
				if (callback != null) {
					callback.onError(code, error);
				}
			}
		});
	}
	
	/**
	 * 获取消息通知类
	 * @return
	 */
	public EaseNotifier getNotifier(){
	    return easeUI.getNotifier();
	}
	
	public DemoModel getModel(){
        return (DemoModel) demoModel;
    }


    
    /**
     * 设置当前用户的环信id
     * @param username
     */
    public void setCurrentUserName(String username){
    	this.username = username;
    	demoModel.setCurrentUserName(username);
    }
    
    /**
     * 获取当前用户的环信id
     */
    public String getCurrentUsernName(){
    	if(username == null){
    		username = demoModel.getCurrentUsernName();
    	}
    	return username;
    }



	public UserProfileManager getUserProfileManager() {
		if (userProManager == null) {
			userProManager = new UserProfileManager();
		}
		return userProManager;
	}



	
	public synchronized void notifyForRecevingEvents(){
        if(alreadyNotified){
            return;
        }
        
        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        alreadyNotified = true;
    }
	
    synchronized void reset(){
        
        demoModel.setGroupsSynced(false);
        demoModel.setContactSynced(false);
        demoModel.setBlacklistSynced(false);

        
        alreadyNotified = false;
        

        getUserProfileManager().reset();
        DemoDBManager.getInstance().closeDB();
    }

    public void pushActivity(Activity activity) {
        easeUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.popActivity(activity);
    }

}
