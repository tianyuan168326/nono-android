package com.seki.noteasklite.CustomControl.Prefer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.R;

import java.util.List;

/**
 * Created by yuan on 2016/6/17.
 */
public class ColorPreferAdapter extends BaseAdapter{

    private List<MaterialListPreference.ColorPreferPair> dataSrc;

    public ColorPreferAdapter(List<MaterialListPreference.ColorPreferPair> dataSrc) {
        this.dataSrc = dataSrc;
    }

    @Override
    public int getCount() {
        return dataSrc.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSrc.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View root =  LayoutInflater.from(parent.getContext()).inflate(R.layout.color_prefer,null,false);
        TextView txt = (TextView)root.findViewById(R.id.color_name);
        View color = (View)root.findViewById(R.id.color_block);
        txt.setText(dataSrc.get(position).colorEntry);
        color.setBackgroundColor(ThemeController.getColor(dataSrc.get(position).colorValue).mainColor);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(onItemClickListener!=null){
                       onItemClickListener.onClick(position,v);
                   }
            }
        });
        return root;
    }

    OnItemClickListener onItemClickListener=null;

    public interface  OnItemClickListener{
        void onClick(int position,View v);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}