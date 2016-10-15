package com.seki.noteasklite.CustomControl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seki.noteasklite.R;

/**
 * Created by yuan-tian01 on 2016/3/12.
 */
public class NoticeButton extends LinearLayout {
    public NoticeButton(Context context) {
        super(context);
    }

    public NoticeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini(context, attrs);
    }

    public NoticeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ini(context, attrs);
    }

    public NoticeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ini(context, attrs);
    }
    public enum NoticeState{
        NOTICED,
        UN_NOTICED
    };
    View rootView;
    NoticeState state;
    int notice_num = 0;

    public int getNotice_num() {
        return notice_num;
    }

    public void setNotice_num(int notice_num) {
        this.notice_num = notice_num;
        refrsh();
    }
    public void setNoticeType(String  notice_type) {
        switch (notice_type){
            case "0":
                this.setState(NoticeButton.NoticeState.UN_NOTICED);
                break;
            case "1":
                this.setState(NoticeButton.NoticeState.NOTICED);
                break;
        }
    }
    public void setState(NoticeState state) {
        this.state = state;
        refrsh();
    }

    public NoticeState getState() {
        return state;
    }

    TintImageView notice_type_view;
    TextView notice_num_view;
    private void ini(Context context, AttributeSet attrs) {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_notice_btn,this);
        notice_type_view = (TintImageView)rootView.findViewById(R.id.notice_type_view);
        notice_num_view = (TextView)rootView.findViewById(R.id.notice_num_view);
        state = NoticeState.UN_NOTICED;
    }

    private void refrsh(){
        switch (state){
            case NOTICED:
                rootView.setBackgroundResource(R.drawable.accent_radius_soloid_bg);
                notice_type_view.setTiniColor(getResources().getColor(R.color.md_text));
                notice_num_view.setTextColor(getResources().getColor(R.color.md_text));

                break;

            case UN_NOTICED:
                rootView.setBackgroundResource(R.drawable.accent_radius_bg);
                notice_type_view.setTiniColor(getResources().getColor(R.color.colorAccent));
                notice_num_view.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
        }
        notice_num_view.setText(String.valueOf(notice_num));
    }


}
