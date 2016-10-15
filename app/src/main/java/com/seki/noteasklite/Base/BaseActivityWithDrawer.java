package com.seki.noteasklite.Base;

import android.content.res.ColorStateList;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.OnGetImagePolicy.ImageProcessor;
import com.seki.noteasklite.R;



/**
 * Created by yuan on 2015/9/17.
 */
public abstract class BaseActivityWithDrawer extends BaseActivity implements
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{
    protected NavigationView navigationView;
    protected FrameLayout headerLayout;
    private IDrawerClosedCallBack iDrawerClosedCallBack = null;
    public void setContentView(int layoutResID,String title) {
        super.setContentView(layoutResID, title);
        registerDrawer();
        registerWidgetWithDrawer();
    }
    @Deprecated
    protected abstract void registerWidgetWithDrawer();
    protected void registerDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(iDrawerClosedCallBack != null){
                    iDrawerClosedCallBack.onDrawerClosed();
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tintNavView(ThemeController.getCurrentColor().mainColor);
        headerLayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        ImageView imageView=(ImageView)headerLayout.findViewById(R.id.nav_header_back);
        imageView.setImageDrawable(ImageProcessor.zoomImageMin(
                ContextCompat.getDrawable(this, R.drawable.navigation_header)
                , getResources().getDisplayMetrics().widthPixels
                , getResources().getDisplayMetrics().widthPixels));
        navigationView.addHeaderView(headerLayout);
    }
    protected  void tintNavView(int color){
        navigationView.setItemIconTintList(ColorStateList.valueOf(color));
        navigationView.setItemTextColor(ColorStateList.valueOf(color));
        navigationView.invalidate();
    }

    @Override
    protected void themePatch() {
        super.themePatch();
        tintNavView(ThemeController.getCurrentColor().mainColor);
    }
 }
