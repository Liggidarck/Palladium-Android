package com.george.vector.develop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.common.utils.TextValidator;
import com.george.vector.common.utils.Utils;
import com.george.vector.develop.notifications.NotificationActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

public class DevelopActivity extends AppCompatActivity {

    private static final String TAG = "DevelopActivity";
    TextInputLayout develop_text_field, develop_text_field2;
    ExtendedFloatingActionButton button_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop);

        Button button_notifications = findViewById(R.id.button_notifications);
        Button button_camera = findViewById(R.id.button_camera);
        button_send = findViewById(R.id.button_send);

        develop_text_field = findViewById(R.id.develop_text_field);
        develop_text_field2 = findViewById(R.id.develop_text_field2);

        button_notifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationActivity.class)));


        develop_text_field.getEditText().addTextChangedListener(new TextValidator(develop_text_field.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {

                Utils utils = new Utils();
                utils.validateNumberField(text, develop_text_field, button_send, 3);

            }
        });

        develop_text_field2.getEditText().addTextChangedListener(new TextValidator(develop_text_field2.getEditText()) {
            @Override
            public void validate(TextView textView, String text) {


            }
        });


    }

}
