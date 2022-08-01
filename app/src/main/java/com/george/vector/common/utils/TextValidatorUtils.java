package com.george.vector.common.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextValidatorUtils {

    public static final Pattern NUMBER_REGEX =
            Pattern.compile("^[1-9]", Pattern.CASE_INSENSITIVE);

    public static final Pattern EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static boolean validateEmail(String email) {
        Matcher matcher = EMAIL_ADDRESS_REGEX.matcher(email);
        return !matcher.find();
    }

    public static boolean validateNumberText(String text) {
        Matcher matcher = NUMBER_REGEX.matcher(text);
        return matcher.find();
    }

    public boolean isEmptyField(@NonNull String text, TextInputLayout inputLayout) {
        inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                inputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (text.isEmpty()) {
            inputLayout.setError("Это поле не может быть пустым");
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }

    public boolean validateNumberField(String text, TextInputLayout inputLayout, int length) {

        if (!validateNumberText(text)) {
            inputLayout.setError("Ошибка! Поле не может быть пустым");
            return false;
        }

        if(text.length() > length) {
            inputLayout.setError("Ошибка!");
            return false;
        }

        inputLayout.setError(null);
        return true;
    }

}
