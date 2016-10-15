package com.seki.noteasklite.CustomControl.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.seki.noteasklite.R;
import com.seki.therichedittext.EditView;

/**
 * Created by yuan on 2016/5/12.
 */
public class EditDialog {
    public interface OnOkListener{
        public boolean onOk(String s);
    }
    Context context;
    EditText editView;
    public EditDialog(Context c, String title, final OnOkListener l) {
        context = c;
        View edit = LayoutInflater.from(c).inflate(R.layout.edit,null,false);
        editView = (EditText) edit.findViewById(R.id.edit_view);
        AlertDialog.Builder b=  new AlertDialog.Builder(c)
                .setTitle(title)
                .setView(edit)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean state = l.onOk(editView.getText().toString());
                        if(state){
                            dialogInterface.dismiss();
                        }
                    }
                });
        b.show();
    }
    public void setHintString(String hint){
        editView.setHint(hint);
        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
    }
}
