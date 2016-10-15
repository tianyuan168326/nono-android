package com.seki.therichedittext;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Seki on 2016/5/2.
 */
public class NumericLayout extends BaseContainer {

    AppCompatTextView textView;
    BaseRichEditText editText;
    private int index;

    public NumericLayout(Context context) {
        super(context);
    }

    public NumericLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumericLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initUI() {
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        textView=new AppCompatTextView(getContext());
        int size=(int)(getResources().getDisplayMetrics().density*32);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(size,size);
        //lp.gravity= Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(lp);
        textView.setTextColor(0xff000000);
        textView.setGravity(Gravity.CENTER);
        editText=new BaseRichEditText(getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        this.addView(linearLayout);
    }

    public void setNumeric(int index) {
        this.index=index;
        textView.setText(index + ".");
    }

    public int getNumeric(){
        return index;
    }

    @Override
    protected String toHtml() {
        return "<section id=\""+type+"\">"+(type==TYPE_NUMERIC?"<ol start=\""+index+"\"><li>":"")+editText.getHtmlText().trim()+(type==TYPE_NUMERIC?"</li></ol>":"")+"</section>";
    }

    @Override
    protected void setHtml(String html) {
        editText.setHtmlText(html.substring(html.indexOf("<li>")+"<li>".length(),html.lastIndexOf("</li>")));
        html=html.substring(html.indexOf("start=\"")+"start=\"".length(),html.lastIndexOf("<"));
        setNumeric(Integer.valueOf(html.substring(0,html.indexOf("\"")).trim()));
    }

    @Override
    protected boolean isEmpty() {
        return editText.getText().length()<=0;
    }

    @Override
    protected void setType() {
        setType(TYPE_NUMERIC);
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

    }

    @Override
    protected BaseRichEditText returnEdit() {
        return editText;
    }
}
