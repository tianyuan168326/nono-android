package com.seki.noteasklite.CustomControl.TagSelector;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.inputmethod.CompletionInfo;
import android.widget.AutoCompleteTextView;

/**
 * Created by yuan on 2016/2/16.
 */
public class TagAutoCompleteTextView extends AutoCompleteTextView {

    public TagAutoCompleteTextView(Context context) {
        super(context);
    }

    public TagAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TagAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private OnItemSelectedListener listener;
    public void setOnItemSelectedListener(OnItemSelectedListener l){
        this.listener = l;
    }
    @Override
    public void onCommitCompletion(CompletionInfo completion) {
        //super.onCommitCompletion(completion);
        setText("");
    }

    @Override
    protected void replaceText(CharSequence text) {
        clearComposingText();
        listener.onSelect(text);
        setText("");
        // make sure we keep the caret at the end of the text view
        Editable spannable = getText();
        Selection.setSelection(spannable, spannable.length());
    }
    public interface OnItemSelectedListener{
        public void onSelect(CharSequence s);
    }
}
