package com.seki.noteasklite.Fragment.Ask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.VerifyInput;

/**
 * Created by yuan-tian01 on 2016/2/27.
 */
public class RegisterCompulsoryFragment extends Fragment {
    private CompulsoryInfo compulsoryInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_compulsory,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public static RegisterCompulsoryFragment newInstance(){
        return new RegisterCompulsoryFragment();
    }

    public CompulsoryInfo getCompulsoryInfo() {

        String userrealname=
                ((TextView)getView().findViewById(R.id.register_realname))
                        .getText().toString();
        String usersubjet=
                ((TextView)getView().findViewById(R.id.register_subject))
                        .getText().toString();
        String useruniversity=
                ((TextView)getView().findViewById(R.id.register_university))
                        .getText().toString();
        CompulsoryInfo compulsoryInfo = new CompulsoryInfo();
        if(!VerifyInput.isRealName(userrealname))
        {
            compulsoryInfo.errorInfo = "名字格式(需要2-10个字符)不正确哦~";
            return compulsoryInfo;
        }
        if(usersubjet == null){
            usersubjet = "";
        }
        if(useruniversity == null){
            useruniversity = "";
        }
        compulsoryInfo = new CompulsoryInfo(userrealname,usersubjet,useruniversity);
        return compulsoryInfo;
    }

    public static class CompulsoryInfo extends RegisterAccountFragment.BaseInfo{
        public String register_realname;
        public String register_university;
        public String register_subject;

        public CompulsoryInfo(String register_realname, String register_subject, String register_university) {
            this.register_realname = register_realname;
            this.register_subject = register_subject;
            this.register_university = register_university;
        }

        public CompulsoryInfo() {

        }
    }
}
