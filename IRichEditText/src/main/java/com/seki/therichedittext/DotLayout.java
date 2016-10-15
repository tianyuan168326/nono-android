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
public class DotLayout extends BaseContainer {

    AppCompatTextView textView;
    BaseRichEditText editText;

    public DotLayout(Context context) {
        super(context);
    }

    public DotLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initUI() {
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        textView=new AppCompatTextView(getContext());
        int size=(int)(getResources().getDisplayMetrics().density*32);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(size,size);
        //lp.gravity=Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(lp);
        textView.setText("·");
        textView.setTextColor(0xff000000);
        textView.setTextAppearance(getContext(),R.style.DotTextAppearance);
        textView.setGravity(Gravity.CENTER);
        editText=new BaseRichEditText(getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        this.addView(linearLayout);
    }

    @Override
    protected String toHtml() {
        return "<section id=\""+type+"\">"+(type==TYPE_DOT?"<ul><li>":"")+editText.getHtmlText().trim()+(type==TYPE_DOT?"</li></ul>":"")+"</section>";
    }

    @Override
    protected void setHtml(String html) {
        editText.setHtmlText(html.substring(html.indexOf("<li>")+"<li>".length(),html.lastIndexOf("</li>")));
    }

    @Override
    protected boolean isEmpty() {
        return editText.getText().length()<=0;
    }

    @Override
    protected void setType() {
        setType(TYPE_DOT);
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
