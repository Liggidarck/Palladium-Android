package com.george.vector.common.utils;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import com.george.vector.R;
import com.george.vector.users.admin.main.MainAdminActivity;
import com.george.vector.users.caretaker.main.MainCaretakerActivity;
import com.george.vector.users.executor.main.MainExecutorActivity;
import com.george.vector.users.root.main.RootMainActivity;
import com.george.vector.users.user.main.MainUserActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Utils {

    public boolean validate_field(@NonNull String check, TextInputLayout textInputLayout) {
        if(check.isEmpty()) {
            textInputLayout.setError("Это поле не может быть пустым");
            return false;
        } else
            return true;
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

}
