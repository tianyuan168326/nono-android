package com.yuantian.com.easeuitransplant;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 聊天页面底部的聊天输入菜单栏 <br/>
 * 主要包含3个控件:EaseChatPrimaryMenu(主菜单栏，包含文字输入、发送等功能), <br/>
 * EaseChatExtendMenu(扩展栏，点击加号按钮出来的小宫格的菜单栏), <br/>
 * 以及EaseEmojiconMenu(表情栏)
 */
public class EaseChatInputMenu extends LinearLayout {
    FrameLayout primaryMenuContainer;
    protected EaseChatPrimaryMenuBase chatPrimaryMenu;
    protected LayoutInflater layoutInflater;

    private ChatInputMenuListener listener;
    private Context context;
    private boolean inited;

    public EaseChatInputMenu(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EaseChatInputMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseChatInputMenu(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.ease_widget_chat_input_menu, this);
        primaryMenuContainer = (FrameLayout) findViewById(R.id.primary_menu_container);

        

    }

    /**
     * init view 此方法需放在registerExtendMenuItem后面及setCustomEmojiconMenu，
     * setCustomPrimaryMenu(如果需要自定义这两个menu)后面
     * @param emojiconGroupList 表情组类别，传null使用easeui默认的表情
     */
    public void init(List<EaseEmojiconGroupEntity> emojiconGroupList) {
        if(inited){
            return;
        }
        // 主按钮菜单栏,没有自定义的用默认的
        if(chatPrimaryMenu == null){

            chatPrimaryMenu = (EaseChatPrimaryMenu) layoutInflater.inflate(R.layout.ease_layout_chat_primary_menu, null);
        }
        primaryMenuContainer.addView(chatPrimaryMenu);


        processChatMenu();
        
        inited = true;
    }
    
    public void init(){
        init(null);
    }
    







    protected void processChatMenu() {
        // 发送消息栏
        chatPrimaryMenu.setChatPrimaryMenuListener(new EaseChatPrimaryMenuBase.EaseChatPrimaryMenuListener() {

            @Override
            public void onSendBtnClicked(String content) {
                if (listener != null)
                    listener.onSendMessage(content);
            }

            @Override
            public void onToggleVoiceBtnClicked() {

            }

            @Override
            public void onToggleExtendClicked() {

            }

            @Override
            public void onToggleEmojiconClicked() {
            }

            @Override
            public void onEditTextClicked() {

            }


            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                if(listener != null){
                    return listener.onPressToSpeakBtnTouch(v, event);
                }
                return false;
            }
        });

    }






    /**
     * 系统返回键被按时调用此方法
     * 
     * @return 返回false表示返回键时扩展菜单栏时打开状态，true则表示按返回键时扩展栏是关闭状态<br/>
     *         如果返回时打开状态状态，会先关闭扩展栏再返回值
     */
    public boolean onBackPressed() {
            return true;

    }
    

    public void setChatInputMenuListener(ChatInputMenuListener listener) {
        this.listener = listener;
    }

    public interface ChatInputMenuListener {
        /**
         * 发送消息按钮点击
         * 
         * @param content
         *            文本内容
         */
        void onSendMessage(String content);


        /**
         * 长按说话按钮touch事件
         * @param v
         * @param event
         * @return
         */
        boolean onPressToSpeakBtnTouch(View v, MotionEvent event);
    }
    
}
