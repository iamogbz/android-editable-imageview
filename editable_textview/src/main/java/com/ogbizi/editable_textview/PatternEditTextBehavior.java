package com.ogbizi.editable_textview;

import android.text.Editable;

public class PatternEditTextBehavior implements EditTextView.Behavior {

    private String mPattern;
    private EditTextView mView;

    public PatternEditTextBehavior(String pattern) {
        mPattern = pattern;
    }

    private String format(String text) {
        // TODO
        return String.format(mPattern, text);
    }

    @Override
    public void onAttach(EditTextView view) {
        mView = view;
    }

    @Override
    public String onFocusIn(int direction, String value, String text) {
        return text.equals(value) ? null : value;
    }

    @Override
    public String onTextChanged(Editable s) {
        return null;
    }

    @Override
    public String onFocusOut(int direction, String value, String text) {
        boolean isFalsy = value == null || value.isEmpty();
        String finalValue = isFalsy ? value : format(value);
        return text.equals(finalValue) ? null : finalValue;
    }
}
