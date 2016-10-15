package com.seki.noteasklite.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.seki.noteasklite.DataUtil.InfoArray;
import com.seki.noteasklite.DataUtil.SubjectArray;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by 七升 on 2015/8/1.
 */
public class EditInfoRecycleListAdapter extends RecyclerView.Adapter {

    private Context context;
    private int TYPE_SHOW=0;
    private int TYPE_DELETE=1;
    private int TYPE_NEW=2;

    public static interface OnRecyclerViewListener {
        void onItemClick(View v, int position);
        boolean onItemLongClick(View v, int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private List<InfoArray> list;

    public EditInfoRecycleListAdapter(Context context, List<InfoArray> list) {
        this.list = list;
        this.context=context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if(viewHolder instanceof InfoShowHolder){
            bindInfoShowHolder(viewHolder,i);
        }else if(viewHolder instanceof InfoDeleteHolder){
            bindInfoDeleteHolder(viewHolder, i);
        }else if(viewHolder instanceof InfoNewHolder){
            bindInfoNewHolder(viewHolder, i);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=new View(context);
       // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       // lp.setMargins(0,(int) (4 * context.getResources().getDisplayMetrics().density),0,(int) (4 * context.getResources().getDisplayMetrics().density));
        if(i==TYPE_SHOW) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_info, viewGroup,false);
            //view.setLayoutParams(lp);
            return new InfoShowHolder(view);
        }else if(i==TYPE_DELETE){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_info_delete, viewGroup,false);
          //  view.setLayoutParams(lp);
            return new InfoDeleteHolder(view);
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_info_new, viewGroup,false);
         //   view.setLayoutParams(lp);
            return new InfoNewHolder(view);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    class InfoShowHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public int position;
        public TextView textView;

        public InfoShowHolder (View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.edit_info_category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(v,position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(v,position);
            }
            return false;
        }
    }

    class InfoDeleteHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public int position;
        public TextView textView;
        public ImageView imageView;

        public InfoDeleteHolder (View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.edit_info_delete);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
            textView=(TextView)itemView.findViewById(R.id.edit_info_delete_category);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(v,position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(v,position);
            }
            return false;
        }
    }

    class InfoNewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public int position;
        public ImageView imageView;
        Spinner subFst;
        Spinner subSec;
        public InfoNewHolder (View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.edit_info_new);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fstSub=((TextView)subFst.getSelectedView()).getText().toString();
                    String secSub=((TextView)subSec.getSelectedView()).getText().toString();
                    for(int i=1;i<list.size();i++){
                        if(secSub.compareTo((list.get(i)).getCategory()[1])==0){
                            Toast.makeText(context,"所选项已经存在",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    list.add(1,new InfoArray(TYPE_DELETE,fstSub,secSub));
                    notifyDataSetChanged();
                }
            });
            subFst = (Spinner) itemView.findViewById(R.id.edit_info_outer_category);
            subSec = (Spinner) itemView.findViewById(R.id.edit_info_inner_category);
            final List<SubjectArray> outerSubjectList=new ArrayList<>();

            Set<String> outerCategoryKeys = MyApp.getInstance().subjectCategory.keySet();
            for (String key : outerCategoryKeys) {
                outerSubjectList.add(new SubjectArray(key));
            }

            SubjectAdapter fstAdapter=new SubjectAdapter(context,outerSubjectList);
            subFst.setAdapter(fstAdapter);
            subFst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    List<String> tempInnerSubjectList =
                            MyApp.getInstance().subjectCategory.get(outerSubjectList.get(position).getSubjectName());
                    List<SubjectArray> innerSubjectList = new ArrayList<SubjectArray>();
                    for (String s : tempInnerSubjectList) {
                        innerSubjectList.add(new SubjectArray(s));
                    }
                    SubjectAdapter subAdapter = new SubjectAdapter(context, innerSubjectList);
                    subSec.setAdapter(subAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(v,position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(v,position);
            }
            return false;
        }
    }

    private void bindInfoShowHolder(RecyclerView.ViewHolder viewHolder,int i){
        InfoShowHolder holder=(InfoShowHolder)viewHolder;
        holder.position=i;
        holder.textView.setText(list.get(i).getCategory()[0]+" - "+list.get(i).getCategory()[1]);
    }

    private void bindInfoDeleteHolder(RecyclerView.ViewHolder viewHolder,int i){
        InfoDeleteHolder holder=(InfoDeleteHolder)viewHolder;
        holder.position=i;
        holder.textView.setText(list.get(i).getCategory()[0]+" - "+list.get(i).getCategory()[1]);

    }

    private void bindInfoNewHolder(RecyclerView.ViewHolder viewHolder,int i){
        InfoNewHolder holder=(InfoNewHolder)viewHolder;
        holder.position=i;
    }
}

