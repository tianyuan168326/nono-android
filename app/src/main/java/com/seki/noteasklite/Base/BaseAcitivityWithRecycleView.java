package com.seki.noteasklite.Base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;

/**
 * Created by yuan on 2015/9/17.
 */
public abstract class BaseAcitivityWithRecycleView extends BaseActivity{

    protected  StaggeredGridLayoutManager layoutManager ;
    protected GroupListStyle style;

    public interface LayoutType
    {
        public static int LINEARLAYOUT = 0;
        public static int STAGGEREDGRIDLAYOUT = 1;
    }
    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter recycleViewAdapter;
    protected void setUpRecycleView()
    {
        setUpRecycleView(LayoutType.STAGGEREDGRIDLAYOUT);
    }
    protected void setUpRecycleView(int layoutType)
    {
        recycleViewAdapter = setRecyclerViewAdapter();
        switch (layoutType) {
            //case LayoutType.LINEARLAYOUT:
            //    recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //    recyclerView.setAdapter(recycleViewAdapter);
            //    break;
            case LayoutType.STAGGEREDGRIDLAYOUT:
                style = AppPreferenceUtil.getGroupStyle();
                if(style == GroupListStyle.LIST){
                    layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                }else if(style == GroupListStyle.TABLE){
                    layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                }
                recyclerView.setLayoutManager(layoutManager);
//                Configuration mConfiguration = this.getResources().getConfiguration();
//                if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//                    recyclerView.setLayoutManager(layoutManager);
//                } else {
//                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//                    recyclerView.setLayoutManager(layoutManager);
//                }
                recyclerView.setAdapter(recycleViewAdapter);
                break;
        }
    }
    protected void afterSetUpRecycleView(){

    }
    protected abstract RecyclerView.Adapter setRecyclerViewAdapter();
    public void setContentView(int layoutResID,String title){
        super.setContentView(layoutResID,title);
        recyclerView = findView(R.id.recycle_view);
        setUpRecycleView();
        afterSetUpRecycleView();
    }
}
