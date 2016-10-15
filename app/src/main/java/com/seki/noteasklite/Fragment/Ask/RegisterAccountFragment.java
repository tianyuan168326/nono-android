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
public class RegisterAccountFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_account,container,false);
    }
    public static RegisterAccountFragment newInstance(){
        return new RegisterAccountFragment();
    }
    public AccountInfo getAccountInfo(){
        String username=((TextView)getView().findViewById(R.id.register_username))
                .getText().toString();
        String useremail=((TextView)getView().findViewById(R.id.register_email))
                .getText().toString();
        String password=((TextView)getView().findViewById(R.id.register_password))
                .getText().toString();
        String passwordagin=((TextView)getView().findViewById(R.id.register_password_again))
                .getText().toString();
        AccountInfo accountInfo = new AccountInfo();
        if(!VerifyInput.isUserName(username))
        {
            accountInfo.errorInfo = "用户名要为3-15的数字/字母组合哦~";
            return accountInfo;
        }
        if(!VerifyInput.isEmail(useremail))
        {
            accountInfo.errorInfo = "邮箱格式不正确哦~";
            return accountInfo;
        }
        if(!VerifyInput.isPassword(password))
        {
            accountInfo.errorInfo = "密码要为6-12位哦~";
            return accountInfo;
        }
        if(!(password.equals(passwordagin) ))
        {
            accountInfo.errorInfo = "两个密码不一致哦~";
            return accountInfo;
        }
        accountInfo =  new AccountInfo(useremail,password,passwordagin,username);
        return accountInfo;
    }
    public static class BaseInfo{
        public String errorInfo = null;
    }
   public static class AccountInfo extends BaseInfo{
       public String register_username;
       public String register_email;
       public String register_password;
       public String register_password_again;

       public AccountInfo(String register_email, String register_password, String register_password_again, String register_username) {
           this.register_email = register_email;
           this.register_password = register_password;
           this.register_password_again = register_password_again;
           this.register_username = register_username;

       }
        public  AccountInfo(){

        }
   }
}
