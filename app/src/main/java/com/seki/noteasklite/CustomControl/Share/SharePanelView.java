package com.seki.noteasklite.CustomControl.Share;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

/**
 * Created by yuan on 2016/6/12.
 */
public class SharePanelView extends RelativeLayout implements View.OnClickListener{
    ViewGroup root;
    private static final String WECHAT_APP_ID = "wxcfa72cf1b8233b20";
    public  static  final String QQ_API_ID="1105234183";
    private static IWXAPI iwxapi;
    private static Tencent tencentQQApi;
    public SharePanelView(Context context) {
        super(context);
        ini();
    }
    public static IWXAPI getIWXAPI(){
        if(iwxapi == null){
            iwxapi = WXAPIFactory.createWXAPI(MyApp.getInstance().getApplicationContext(),WECHAT_APP_ID,true);
            iwxapi.registerApp(WECHAT_APP_ID);
        }
        return iwxapi;
    }
    public static Tencent getTencentQQApi(){
        if(tencentQQApi == null){
            tencentQQApi = Tencent.createInstance(QQ_API_ID,MyApp.getInstance().getApplicationContext());
        }
        return tencentQQApi;
    }
    public SharePanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini();
    }

    private void ini() {

        root =(ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.view_share_panel,this,true);
        root.findViewById(R.id.share_image).setOnClickListener(this);
        root.findViewById(R.id.share_wechat).setOnClickListener(this);
        root.findViewById(R.id.share_wechat_circle).setOnClickListener(this);
        root.findViewById(R.id.share_weibo).setOnClickListener(this);
        root.findViewById(R.id.share_nono).setOnClickListener(this);
        root.findViewById(R.id.share_qq).setOnClickListener(this);
        root.findViewById(R.id.share_qq_zone).setOnClickListener(this);
        root.findViewById(R.id.share_copy_text).setOnClickListener(this);
        root.findViewById(R.id.copy_link).setOnClickListener(this);
    }
    public SharePanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ini();
    }

    public SharePanelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ini();
    }
    static   FrameLayout container;
    public static  SharePanelView openPanel(final Context c){
        isPanelOpen = true;
        WindowManager wm  =(WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.format = PixelFormat.TRANSLUCENT;
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
//        if(container == null){
            container = new FrameLayout(c){
                @Override
                public boolean dispatchKeyEvent(KeyEvent event) {
                    if (event.getKeyCode()==KeyEvent.KEYCODE_BACK) {
                        SharePanelView.removePanel(c);
                        return true;
                    }
                    return super.dispatchKeyEvent(event);
                }
            };
            container.setBackgroundColor(c.getResources().getColor(R.color.dialog_shadow_color));
            container.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    removePanel(c);
                }
            });
        wm.addView(container,params);
        FrameLayout.LayoutParams panelParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        panelParams.gravity=Gravity.BOTTOM;
        container.addView(new SharePanelView(c),panelParams);

        final SharePanelView sharePanel  =(SharePanelView) container.getChildAt(0);
        final int parentHeight = wm.getDefaultDisplay().getHeight();
        container.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if((bottom  - top)>parentHeight - 20 ){
                    sharePanel.layout(sharePanel.getLeft(),parentHeight,sharePanel.getRight(),sharePanel.getBottom());
                    ObjectAnimator animator =   ObjectAnimator
                            .ofInt(sharePanel,"top",parentHeight,parentHeight-sharePanel.getHeight())
                            .setDuration(200);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator .start();
                }
            }
        });

        return sharePanel;
    }
    public static void removePanel(final Context c){
        isPanelOpen = false;
        View sharePanel  = container.getChildAt(0);
        int parentHeight = ((View)sharePanel.getParent()).getHeight();
        sharePanel.layout(sharePanel.getLeft(),parentHeight-sharePanel.getHeight(),sharePanel.getRight(),sharePanel.getBottom());
        ObjectAnimator animator =  ObjectAnimator
                .ofInt(sharePanel,"top",parentHeight-sharePanel.getHeight(),parentHeight-1)
                .setDuration(200);
        animator  .addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        try{
                            WindowManager wm  =(WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
                            wm.removeView(container);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

    }

    ShareInterface shareInterface = null;
    public void setShareInterface(ShareInterface l){
        shareInterface = l;
    }
    public static interface ShareInterface{
        public void shareImage();
        public void sharePlainText();
        public void shareWeChat();
        public void shareWeChatCircle();
        public void shareWeibo();
        public void shareNONo();
        public void shareQQ();
        public void shareQQZone();
        public void copyLink();
    }
    @Override
    public void onClick(View v) {

        if(shareInterface == null){
            return ;
        }
        SharePanelView.removePanel(getContext());
        switch (v.getId()){
            case R.id.share_image:
                shareInterface.shareImage();
                break;
            case R.id.share_copy_text:
                shareInterface.sharePlainText();
                break;
            case R.id.share_wechat:
                shareInterface.shareWeChat();
                break;
            case R.id.share_wechat_circle:
                shareInterface.shareWeChatCircle();
                break;
            case R.id.share_weibo:
                shareInterface.shareWeibo();
                break;
            case R.id.share_nono:
                shareInterface.shareNONo();
                break;
            case R.id.share_qq:
                shareInterface.shareQQ();
                break;
            case R.id.share_qq_zone:
                shareInterface.shareQQZone();
                break;
            case R.id.copy_link:
                shareInterface.copyLink();
                break;

        }
    }
    public static  boolean isPanelOpen = false;

}
