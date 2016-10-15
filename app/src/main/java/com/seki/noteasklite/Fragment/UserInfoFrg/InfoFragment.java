package com.seki.noteasklite.Fragment.UserInfoFrg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private String university,school,sex;
    private long id;

    public static InfoFragment newInstance(long id) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putLong("Id",id);
        fragment.setArguments(args);
        return fragment;
    }
//    public static InfoFragment newInstance(){
//
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            id=getArguments().getLong("Id",-1);
        }
    }

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_info, container, false);
        if(id== Long.valueOf(MyApp.userInfo.userId)) {
            university = MyApp.userInfo.userUniversity;
            school = MyApp.userInfo.userSchool;
            ((TextView) view.findViewById(R.id.university)).setText(university);
            ((TextView) view.findViewById(R.id.school)).setText(school);
            ((TextView) view.findViewById(R.id.sex)).setText(sex);
        }
        return view;
    }

    public void update(Bundle bundle) {
        university = bundle.getString("University", "");
        school = bundle.getString("School", "");
        sex = bundle.getString("sex", "");
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.university)).setText(university);
            ((TextView) getView().findViewById(R.id.school)).setText(school);
            switch (sex){
                case "male":
                    ((TextView) getView().findViewById(R.id.sex)).setText("♂");
                    ((TextView) getView().findViewById(R.id.sex)).setTextColor(getResources().getColor(R.color.colorPrimary));
                    break;
                default:
                    ((TextView) getView().findViewById(R.id.sex)).setText("♀");
                    ((TextView) getView().findViewById(R.id.sex)).setTextColor(getResources().getColor(R.color.colorAccent));
                    break;
            }
        }
    }
}
