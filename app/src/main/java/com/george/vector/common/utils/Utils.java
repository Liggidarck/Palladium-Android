package com.george.vector.common.utils;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public boolean validate_field(@NonNull String text, TextInputLayout text_input_layout) {
        if (text.isEmpty()) {
            text_input_layout.setError("Это поле не может быть пустым");
            text_input_layout.setErrorEnabled(true);
            return false;
        } else {
            text_input_layout.setErrorEnabled(false);
            return true;
        }
    }

    public void validateNumberField(String text, TextInputLayout text_input_layout, ExtendedFloatingActionButton floating_action_button, int length) {
        if (!validateNumberText(text)) {
            text_input_layout.setError("Ошибка! Проверьте правильность написания");
            floating_action_button.setClickable(false);
            text_input_layout.setErrorEnabled(true);
        } else if (text.length() > length) {
            floating_action_button.setClickable(false);
            text_input_layout.setErrorEnabled(true);
        } else {
            floating_action_button.setClickable(true);
            text_input_layout.setErrorEnabled(false);
        }
    }

    public void clear_error(@NonNull TextInputLayout textInputLayout) {
        Objects.requireNonNull(textInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textInputLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public String getDate() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public static final Pattern VALID_NUMBER =
            Pattern.compile("^[1-9]", Pattern.CASE_INSENSITIVE);

    public static boolean validateNumberText(String emailStr) {
        Matcher matcher = VALID_NUMBER.matcher(emailStr);
        return matcher.find();
    }

}
