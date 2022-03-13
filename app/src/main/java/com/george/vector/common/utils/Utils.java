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

    public boolean validateField(@NonNull String text, TextInputLayout inputLayout) {
        if (text.isEmpty()) {
            inputLayout.setError("Это поле не может быть пустым");
            inputLayout.setErrorEnabled(true);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void validateNumberField(String text, TextInputLayout inputLayout, ExtendedFloatingActionButton actionButton, int length) {
        if (!validateNumberText(text)) {
            inputLayout.setError("Ошибка! Проверьте правильность написания");
            actionButton.setClickable(false);
            inputLayout.setErrorEnabled(true);
        } else if (text.length() > length) {
            actionButton.setClickable(false);
            inputLayout.setErrorEnabled(true);
        } else {
            actionButton.setClickable(true);
            inputLayout.setErrorEnabled(false);
        }
    }

    public void clearError(@NonNull TextInputLayout inputLayout) {
        Objects.requireNonNull(inputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
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
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
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
