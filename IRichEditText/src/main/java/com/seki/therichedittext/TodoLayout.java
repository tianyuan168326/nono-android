package com.seki.therichedittext;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

/**
 * Created by Seki on 2016/5/2.
 */
public class TodoLayout extends BaseContainer {

    AppCompatCheckBox checkBox;
    BaseRichEditText editText;
    boolean isChecked;

    public TodoLayout(Context context) {
        super(context);
    }

    public TodoLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TodoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initUI() {
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        checkBox=new AppCompatCheckBox(getContext());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TodoLayout.this.isChecked=isChecked;
            }
        });
        editText=new BaseRichEditText(getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
        linearLayout.addView(checkBox);
        linearLayout.addView(editText);
        this.addView(linearLayout);
    }

    public void setChecked(boolean isChecked){
        checkBox.setChecked(isChecked);
    }

    @Override
    protected String toHtml() {
        return "<section id=\""+type+"\">"+(type==TYPE_TODO?"<input type=\"checkbox\""+  (isChecked?"checked=\"checked\"":"") +"/>"+
                        "<label>":"")+editText.getHtmlText().trim()+(type==TYPE_TODO?"</label>":"")+"</section>";
    }

    @Override
    protected void setHtml(String html) {
        editText.setHtmlText(html.substring(html.indexOf("<label>")+"<label>".length(),html.lastIndexOf("</label>")));
        html=html.substring(html.indexOf("checked=\"")+"checked=\"".length(),html.lastIndexOf("<"));
        setChecked(html.substring(0,html.indexOf("\"")).trim().equalsIgnoreCase("checked"));
    }

    @Override
    protected boolean isEmpty() {
        return editText.getText().length()<=0;
    }

    @Override
    protected void setType() {
        setType(TYPE_TODO);
    }

    @Override
    protected boolean reqFocus() {
        return editText.requestFocus();
    }

    @Override
    protected void setEditable(final boolean editable) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                if(editable){
                    editText.setFocusableInTouchMode(true);
                }
                editText.setFocusable(editable);
                if(!editable){
                    editText.clearFocus();
                }
            }
        });

        checkBox.post(new Runnable() {
            @Override
            public void run() {
                checkBox.setEnabled(editable);
            }
        });
    }

    @Override
    protected BaseRichEditText returnEdit() {
        return editText;
    }
}
