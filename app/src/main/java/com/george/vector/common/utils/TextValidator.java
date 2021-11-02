package com.george.vector.common.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class TextValidator implements TextWatcher {

    private final TextView text_view;

    public TextValidator(TextView textView) {
        this.text_view = textView;
    }

    public abstract void validate(TextView text_view, String text);

    @Override
    final public void afterTextChanged(Editable s) {
        String text = text_view.getText().toString();
        validate(text_view, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }


}
