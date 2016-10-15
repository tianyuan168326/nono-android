package com.seki.noteasklite.CustomControl.TagSelector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuan on 2016/2/16.
 */
public class AutoTagCompleteAdapter<T> extends BaseAdapter implements Filterable{
    private List<T> mList;
    private Context context;
    public AutoTagCompleteAdapter(Context context,List<T> mList) {
        this.mList = mList;
        this.context = context;
    }

    public void addListString(T s){
        mList.add(s);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new myFilter();
    }

    @Override
    public int getCount() {
        return mList == null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null?null:mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList==null?0:mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            View rootView = LayoutInflater.from(context).inflate( R.layout.layout_tag_completion, parent,false);
            convertView = rootView;
        }
        TextView txt = (TextView) convertView.findViewById(R.id.text);
        txt.setText((String)mList.get(position));
        return txt;
    }

    private class myFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(mList ==null){
                mList = new ArrayList<>();
            }
            results.values = mList;
            results.count = mList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
