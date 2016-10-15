package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seki.noteasklite.DataUtil.SubjectArray;
import com.seki.noteasklite.R;

import java.util.List;

/**
 * Created by ???? on 2015/7/17.
 */
public class SubjectAdapter extends BaseAdapter{
    private List<SubjectArray> mList;
    private Context mContext;

    public SubjectAdapter(Context pContext, List<SubjectArray> pList) {
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
        convertView=_LayoutInflater.inflate(R.layout.list_subject, null);
        if(convertView!=null)
        {
            TextView textView=(TextView)convertView.findViewById(R.id.subject_list_item);
            if(mList.get(position) != null)
            textView.setText(mList.get(position).getSubjectName());
        }
        return convertView;
    }
}
