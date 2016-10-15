package com.seki.noteasklite.CustomControl.Dialog;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.seki.noteasklite.CustomControl.TintHelper;
import com.seki.noteasklite.CustomControl.VoteButton;
import com.seki.noteasklite.R;

/**
 * Created by yuan-tian01 on 2016/3/11.
 */
public class VoteDialog {
    AlertDialog.Builder builder;
    View rootView;
    ImageView vote_up;
    ImageView vote_down;
    VoteButton voteButton;
    OnVoteListener onVoteListener;
    public interface OnVoteListener{
        public void onVoteUp();
        public void onVoteDown();
    }
    public VoteDialog(AppCompatActivity activity,View view) {
        if (!(view instanceof VoteButton)) {
            throw new IllegalArgumentException("Vote Dialog only worked with Vote Button!");
        }
        voteButton =(VoteButton) view;
        builder = new AlertDialog.Builder(activity);
        rootView = activity.getLayoutInflater().inflate( R.layout.vote_dialog, null);
        builder.setView(rootView);
        vote_up = (ImageView)rootView.findViewById(R.id.vote_up);
        vote_down = (ImageView)rootView.findViewById(R.id.vote_down);
        TintHelper.tintView(vote_up, activity.getResources().getColorStateList(R.color.md_second_color));
        TintHelper.tintView(vote_down, activity.getResources().getColorStateList(R.color.md_second_color));
        switch (voteButton.getVoteState()){
            case VOTE_UP:
                TintHelper.tintView(vote_up, activity.getResources().getColorStateList(R.color.colorAccent));
                TintHelper.tintView(vote_down, activity.getResources().getColorStateList(R.color.md_second_color));
                break;
            case VOTE_NORMAL:
                TintHelper.tintView(vote_up, activity.getResources().getColorStateList(R.color.md_second_color));
                TintHelper.tintView(vote_down, activity.getResources().getColorStateList(R.color.md_second_color));
                break;
            case VOTE_DOWN:
                TintHelper.tintView(vote_up, activity.getResources().getColorStateList(R.color.md_second_color));
                TintHelper.tintView(vote_down, activity.getResources().getColorStateList(R.color.colorAccent));
                break;
        }
        vote_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVoteListener.onVoteUp();
                voteButton.voteUp();

            }
        });
        vote_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVoteListener.onVoteDown();
                voteButton.voteDown();
            }
        });
    }
    public AlertDialog show(){
        return builder.show();
    }
    public void setOnVoteListener(OnVoteListener l){
        onVoteListener = l;
    }
}
