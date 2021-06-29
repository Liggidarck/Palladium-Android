package com.george.vector.сaretaker.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.george.vector.R;
import com.george.vector.root.main.fragments.fragment_bar;
import com.george.vector.root.main.fragments.fragment_ost;
import com.george.vector.сaretaker.main.fragments.fragment_bar_caretaker;
import com.george.vector.сaretaker.main.fragments.fragment_ost_caretaker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CaretakerMainActivity extends AppCompatActivity {

    private static final String TAG = "CaretakerMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainActivity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_main);

        Bundle arguments = getIntent().getExtras();
        String permission = arguments.get("permission").toString();

        if(permission.equals("ost"))
            setUp("ost");

        if(permission.equals("bar"))
            setUp("bar");
    }

    void setUp(@NotNull String permission) {
        Fragment currentFragment = null;
        switch (permission) {
            case "ost":
                Log.i(TAG, "Запуск фрагмента Осафьево");
                currentFragment = new fragment_ost_caretaker();
                break;
            case "bar":
                Log.i(TAG, "Запуск фрагмента Барыши");
                currentFragment = new fragment_bar_caretaker();
                break;
        }
        assert currentFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_caretaker, currentFragment)
                .commit();
    }

}