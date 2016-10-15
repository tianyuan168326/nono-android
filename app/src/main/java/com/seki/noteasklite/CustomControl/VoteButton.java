package com.seki.noteasklite.CustomControl;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seki.noteasklite.R;

import java.util.zip.Inflater;

/**
 * Created by yuan-tian01 on 2016/3/11.
 */
public class VoteButton extends LinearLayout{
    View rootView;
    TintImageView  vote_flag;
    TextView  vote_num;
    int voteNum;
    public enum VoteState{
        VOTE_UP,
        VOTE_NORMAL,
        VOTE_DOWN
    }
    VoteState  voteState;
    public VoteButton(Context context) {
        super(context);
        ini(context);
    }

    public VoteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini(context);
    }

    public VoteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ini(context);
    }

    private void ini(Context context) {
        rootView = inflate(context, R.layout.vote_button,this);
        vote_flag =(TintImageView)rootView.findViewById(R.id.vote_flag);
        vote_num =(TextView)rootView.findViewById(R.id.vote_num);
        voteState = VoteState.VOTE_NORMAL;
        voteNum = 0;
    }
    public VoteState getVoteState(){
        return this.voteState;
    }
    public  void setVoteState(VoteState state){
        voteState = state;
        switch (voteState){
            case VOTE_UP:
                setBackgroundResource(R.drawable.accent_radius_soloid_bg);
                vote_flag.setImageResource(R.mipmap.ic_arrow_drop_up_white_36dp);
                vote_flag.setTiniColor(getResources().getColor(android.R.color.white));
                vote_num.setTextColor(getResources().getColor(R.color.md_text));
                break;
            case VOTE_DOWN:
                setBackgroundResource(R.drawable.accent_radius_soloid_bg);
                vote_flag.setImageResource(R.mipmap.ic_arrow_drop_down_white_36dp);
                vote_flag.setTiniColor(getResources().getColor(android.R.color.white));
                vote_num.setTextColor(getResources().getColor(R.color.md_text));
                break;

            case VOTE_NORMAL:
                setBackgroundResource(R.drawable.accent_radius_bg);
                vote_flag.setImageResource(R.mipmap.ic_arrow_drop_up_white_36dp);
                vote_flag.setTiniColor(getResources().getColor(R.color.colorAccent));
                vote_num.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
        }
    }
    public  void setVoteNum(int num){
        voteNum = num;
        vote_num.setText(String.valueOf(num));
    }
    private void minusVoteNum(){
        voteNum--;
        setVoteNum(voteNum);
    }
    private void plusVoteNum(){
        voteNum++;
        setVoteNum(voteNum);
    }
    public void voteUp(){
        if(voteState.equals(VoteState.VOTE_NORMAL)){
            plusVoteNum();
        }
        if(voteState.equals(VoteState.VOTE_UP)){
            voteNormal();
            return;
        }
        if(voteState.equals(VoteState.VOTE_DOWN)){
            plusVoteNum();
            plusVoteNum();
        }

        setVoteState(VoteState.VOTE_UP);
        return;
    }
    public void voteNormal(){
        if(voteState.equals(VoteState.VOTE_NORMAL)){

            return;
        }
        if(voteState.equals(VoteState.VOTE_UP)){
            minusVoteNum();
        }
        if(voteState.equals(VoteState.VOTE_DOWN)){
            plusVoteNum();
        }
        setVoteState(VoteState.VOTE_NORMAL);
        return;
    }

    public void voteDown(){
        if(voteState.equals(VoteState.VOTE_NORMAL)){
            minusVoteNum();
        }
        if(voteState.equals(VoteState.VOTE_UP)){
            minusVoteNum();
            minusVoteNum();
        }
        if(voteState.equals(VoteState.VOTE_DOWN)){
            voteNormal();
            return;
        }
        setVoteState(VoteState.VOTE_DOWN);
        return;
    }
    public VoteButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ini(context);
    }
}
