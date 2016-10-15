package com.seki.noteasklite.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.seki.noteasklite.Base.BaseFragment;
import com.seki.noteasklite.Base.BaseMultiChoiceRecycleViewAdapter;
import com.seki.noteasklite.Base.BaseRecycleViewAdapter;
import com.seki.noteasklite.DataUtil.BusEvent.ToogleGroup2ListEvent;
import com.seki.noteasklite.DataUtil.BusEvent.ToogleGroup2TableEvent;
import com.seki.noteasklite.DataUtil.Enums.GroupListStyle;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.noteasklite.Util.DisplayUtil;

/**
 * Created by yuan on 2016/5/12.
 */
public abstract class  FluidBaseFragment extends BaseFragment {
    protected  StaggeredGridLayoutManager layoutManager ;
    protected  GroupListStyle style;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        style = AppPreferenceUtil.getGroupStyle();
        if(style == GroupListStyle.LIST){
            layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        }else if(style == GroupListStyle.TABLE){
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getRecycleViewAdapter() == null){
            return ;
        }
        if(style == GroupListStyle.LIST){
            getRecycleViewAdapter().clearItemMargin();
            getRecycleViewAdapter().notifyDataSetChanged();
        }else if(style == GroupListStyle.TABLE){
            getRecycleViewAdapter().setItemMargin(-1, DisplayUtil.dip2px(getActivity(),4),-1,DisplayUtil.dip2px(getActivity(),4));
            getRecycleViewAdapter().notifyDataSetChanged();
        }
    }

    public abstract BaseRecycleViewAdapter getRecycleViewAdapter();
    public abstract RecyclerView getRecycleView();
    @SuppressWarnings("unused")
    public final void onEvent(ToogleGroup2TableEvent event){
        layoutManager  = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        getRecycleView().setLayoutManager(layoutManager);
        getRecycleViewAdapter().setItemMargin(-1, DisplayUtil.dip2px(getActivity(),4),-1,DisplayUtil.dip2px(getActivity(),4));
        getRecycleViewAdapter().notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public final void onEvent(ToogleGroup2ListEvent event){
        layoutManager  = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        getRecycleView().setLayoutManager(layoutManager);
        getRecycleViewAdapter().clearItemMargin();
        getRecycleViewAdapter().notifyDataSetChanged();
    }
    @Override
    public final boolean onBackPressed() {
        if(!  (getRecycleViewAdapter() instanceof BaseMultiChoiceRecycleViewAdapter)){
            return false;
        }
        if(((BaseMultiChoiceRecycleViewAdapter)getRecycleViewAdapter()).getSelectedPositionList().size() != 0){
            ((BaseMultiChoiceRecycleViewAdapter)getRecycleViewAdapter()).unSelectAll();
            return true;
        }else{
            return false;
        }

    }
}
