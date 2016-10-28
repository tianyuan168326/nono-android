package com.seki.noteasklite.Base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.seki.noteasklite.Activity.MainActivity;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.DataUtil.BusEvent.CloseWaitingDialogEvent;
import com.seki.noteasklite.DataUtil.BusEvent.OpenWaitingDialogEvent;
import com.seki.noteasklite.DataUtil.BusEvent.ThemeColorPairChangedEvent;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.NotifyHelper;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ActivityOptionsICS.ActivityCompatICS;
import ActivityOptionsICS.ActivityOptionsCompatICS;
import ActivityOptionsICS.transition.TransitionCompat;
import de.greenrobot.event.EventBus;

/**
 * Created by yuan on 2015/9/17.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    public interface BaseActivityOption
    {
        public static  int OPTION_MENU_ON = 1;
    }
    protected Toolbar toolBar;
    protected int menuResId = R.menu.menu_empty;
    private  int stateMachineCode = 0;
    private boolean isContentSet = false;
    public int currentState = 0;
    private boolean hasStateMachine = false;
    private StateMachineCodeProcessHandle stateMachineCodeProcessHandle;
    public String toolBarTitle = new String();
    public static AppCompatActivity instance;
    List<Integer> menuItemIdList = new ArrayList<>();
    List<Method> menuItemInvokingMethodList = new ArrayList<>();
    protected SparseArray<MenuItem> menuItems = new SparseArray<>();
    public void setMenuResId(int menuResId) {
        this.menuResId = menuResId;
    }

    @Deprecated
    //(don not override this method, try in setMenuResId())
    public boolean onCreateOptionsMenu(Menu menu) {

        int menuCounter = menu.size();
        for(int itemIndex= 0;itemIndex <menuCounter;itemIndex++){
            MenuItem item = menu.getItem(itemIndex);
            menuItems.append(item.getItemId(),item);
        }
        if(menuResId != 0) {
            getMenuInflater().inflate(menuResId, menu);
            return true;
        }
        return false;
    }

    public void bindMenuOptionInvokeMethod(int menuItemId,String menuItemInvokingMethodName)
    {
            menuItemIdList.add(menuItemId);
            Class currentClass = getClass();
            while(!currentClass.equals(AppCompatActivity.class.getSuperclass().getSuperclass())) {
                Method[] methods = currentClass.getDeclaredMethods();
                int methodNum = methods.length;

                for (int methodIndex = 0; methodIndex < methodNum; methodIndex++) {
                    String methodName = methods[methodIndex].getName();
                    if (methodName.equals(menuItemInvokingMethodName)) {
                        menuItemInvokingMethodList.add(methods[methodIndex]);
                        return;
                    }
                }
                methods = currentClass.getMethods();
                methodNum = methods.length;
                for (int methodIndex = 0; methodIndex < methodNum; methodIndex++) {
                    String methodName = methods[methodIndex].getName();
                    if (methodName.equals(menuItemInvokingMethodName)) {
                        menuItemInvokingMethodList.add(methods[methodIndex]);
                        return;
                    }
                }
                currentClass = currentClass.getSuperclass();
            }


    }
    @Deprecated
    //(don not override this method, try in bindMenuOptionInvokeMethod())

    public boolean onOptionsItemSelected(MenuItem item) {
        HashMap<Integer,String> methodNameList = setUpOptionMenu();
        if(methodNameList == null){
            methodNameList = new HashMap<>();
        }
        if(!methodNameList.containsKey(android.R.id.home)){
            methodNameList.put(android.R.id.home,"onBackPressed");
        }

        Set<Map.Entry<Integer,String>> methodNameListEntrySet = methodNameList.entrySet();
        for (Map.Entry<Integer,String> methodNamePair: methodNameListEntrySet) {
            bindMenuOptionInvokeMethod(methodNamePair.getKey(),methodNamePair.getValue());
        }
        int menuItemId = item.getItemId();
        int menuItemIndex ;
        if(menuItemIdList.contains(menuItemId))
        {
            menuItemIndex = menuItemIdList.indexOf(menuItemId);
            try {
                menuItemInvokingMethodList.get(menuItemIndex).setAccessible(true);
                menuItemInvokingMethodList.get(menuItemIndex).invoke(this);
            }catch (InvocationTargetException ex){
                ex.printStackTrace();
                Toast.makeText(this, getString(R.string.getMethodError), Toast.LENGTH_SHORT).show();
            }catch (IllegalAccessException e){
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.getMethodError), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void bindViewsToOnClickListenerById(int... viewIds) {
        for (int viewId:viewIds)
        {
            findView(viewId).setOnClickListener(this);
        }
    }

    protected void bindTextColors(int[] textViewResIdArray,int[] colorResIdArray) {
        int bindIndex = 0;
        for(int textViewResId:textViewResIdArray)
        {
            setTextColor(textViewResId,colorResIdArray[bindIndex]);
            bindIndex ++;
        }
    }

    protected void changeSingleImageView(int imageViewResId,int imageResourcesId,int color,boolean isColorId) {
        ImageView imageView =  findView(imageViewResId);
        imageView.setImageResource(imageResourcesId);
        if(color ==0)
        {
            imageView.getDrawable().clearColorFilter();
        }else{
            if(isColorId)
            {
                imageView.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }else{
                imageView.getDrawable().setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    private void setTextColor(int textViewResId,int colorResId) {
        ((TextView)findView(textViewResId)).setTextColor(getResources().getColor(colorResId));
    }

    protected final <T extends View> T findView(int resId) {
        try {
            return (T)(this.findViewById(resId));
        }catch (ClassCastException e){
            Toast.makeText(this, "����ת������", Toast.LENGTH_SHORT);
        throw e;
        }
    }
    protected final <T extends View> T $(int resId) {
        try {
            return (T)(this.findViewById(resId));
        }catch (ClassCastException e){
            Toast.makeText(this, "find view error!", Toast.LENGTH_SHORT);
            throw e;
        }
    }

    protected void setUpToolBar(String title) {
        toolBar = findView(R.id.toolbar);
        if(toolBar == null){
            return ;
        }
        setSupportActionBar(toolBar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.md_text), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolBarTitle =title;
        toolBar.setTitle(title);
        toolBar.setTitleTextColor(getResources().getColor(R.color.md_text));
        if( getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(title);
        setTitle(title);
    }

    /**
     *  initial widgets or get the pointers of widget
     *
     */
    @Deprecated
    protected abstract void registerWidget();

    /**
     * register adapters
     * @param
     * @param
     */
    @Deprecated
    protected abstract void registerAdapters();
    /**
     * when you set up option menu, override it! or just Return otherwise
     */
    @Deprecated
    protected abstract HashMap<Integer,String> setUpOptionMenu();

    void processStateMachine(){
        if(hasStateMachine){
            int stateMachineCode = getIntent().getIntExtra("state",0);
            if(stateMachineCode != 0 && stateMachineCodeProcessHandle !=null){
                currentState = stateMachineCode;
                stateMachineCodeProcessHandle.onStateMachineCode(stateMachineCode);
            }
        }
    }

    public void registerStateMachineCodeProcessHandle(StateMachineCodeProcessHandle handle){
        hasStateMachine = true;
        stateMachineCodeProcessHandle =handle;
    }
    String clazz;
    //make sure to register state machine handle before this;
    public void setContentView(int layoutResID,String title) {
        super.setContentView(layoutResID);
        openEventBus();
        clazz = this.getClass().getName();
        TransitionCompat.startTransition(this, layoutResID);
        instance = this;
        setUpToolBar(title);
        registerWidget();
        registerAdapters();
        setUpOptionMenu();
        processStateMachine();
    }

    public void screenTransitAnimByPair(Intent intent,Pair<View, Integer>... views) {
        // isSceneAnim = true;
        ActivityOptionsCompatICS options = ActivityOptionsCompatICS.makeSceneTransitionAnimation(
                this, views);
        ActivityCompatICS.startActivity(this, intent, options.toBundle());
    }
    public static Activity getSingleInstance(){
        if(instance == null){
            throw new NullPointerException("you can not get null activity ");
        }else{
            return instance;
        }
    }
    boolean isFragmentProcessed = false;
    @Override
    public void onBackPressed() {

        List<android.support.v4.app.Fragment>  frags=  getSupportFragmentManager().getFragments();
        if(frags !=null){
            for (Fragment frag :
                    frags
                    ) {
                if(frag instanceof  BaseFragment){
                    boolean processed = ((BaseFragment)frag).onBackPressed();
                    if(processed)
                        isFragmentProcessed  = true;
                }
            }
        }
        if(!isFragmentProcessed){
            super.onBackPressed();
            TransitionCompat.finishAfterTransition(this);
        }
    }
    boolean bus_key = false;
    //should be used in onCreate function
    public void openEventBus(){
        if(!bus_key){
            bus_key = true;
            EventBus.getDefault().register(this);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bus_key){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(clazz);
        MobclickAgent.onResume(this);
        themePatch();
    }

    protected  void themePatch(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setBackgroundDrawable(new ColorDrawable(ThemeController.getCurrentColor().getMainColor()));
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            if ((this instanceof MainActivity)) {
                try {
                    final CoordinatorLayout layout=((CoordinatorLayout) findViewById(R.id.coordinator));
                    final View view= findViewById(R.id.fragment_container);
                    if(view !=null){
                        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                int coordHeight =layout.getHeight();
                                int viewHeight =view.getHeight();
                                if((coordHeight-viewHeight)>returnStatusHeight()*2){
                                    return;
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                                View statusView=new View(BaseActivity.this);
                                statusView.setBackgroundColor(ThemeController.getCurrentColor().getDarkColor());
                                statusView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        coordHeight-viewHeight));
                                ((FrameLayout)findViewById(R.id.content_root)).addView(statusView);

                            }
                        });
                    }

                }catch (NullPointerException e){
                    getWindow().setStatusBarColor(ThemeController.getCurrentColor().getDarkColor());
                    e.printStackTrace();
                }
            } else {
                getWindow().setStatusBarColor(ThemeController.getCurrentColor().getDarkColor());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(clazz);
        MobclickAgent.onPause(this);
    }
    public void onEventMainThread(ThemeColorPairChangedEvent e){
        //themePatch();
    }
//    public void traversalView(ViewGroup viewGroup,int accentColor) {
//        int count = viewGroup.getChildCount();
//        for (int i = 0; i < count; i++) {
//            View view = viewGroup.getChildAt(i);
//            if (view instanceof ViewGroup) {
//                traversalView((ViewGroup) view,accentColor);
//            } else if (view instanceof FloatingActionButton){
//                fuckFAB((FloatingActionButton)view,accentColor);
//            } else if (view instanceof RapidFloatingActionButton){
//                fuckRFAB((RapidFloatingActionButton)view,accentColor);
//            }
//        }
//    }
    private void fuckFAB(FloatingActionButton fab, int accentColor) {
        fab.setBackgroundColor(accentColor);
    }

    protected int returnStatusHeight(){
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    PopupWindow popupWindow = null;
    public void onEventMainThread(OpenWaitingDialogEvent e){
        if(popupWindow == null){
            popupWindow = NotifyHelper.popUpWaitingAnimation(BaseActivity.this, popupWindow);
        }
    }
    public void onEventMainThread(CloseWaitingDialogEvent e){
        if(popupWindow != null){
            NotifyHelper.popUpWaitingAnimationFinished(popupWindow);
            popupWindow = null;
        }
    }

}
