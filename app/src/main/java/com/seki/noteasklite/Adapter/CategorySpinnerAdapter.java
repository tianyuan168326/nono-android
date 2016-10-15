package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seki.noteasklite.R;

import java.util.List;

/**
 * Created by yuan on 2015/7/26.
 */
public class CategorySpinnerAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;

    public CategorySpinnerAdapter(Context pContext, List<String> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater= LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.category_list, null);
        if(convertView!=null){
            TextView textView=(TextView)convertView.findViewById(R.id.category_list_item);
            textView.setText(mList.get(position));
        }
        return convertView;
    }
}