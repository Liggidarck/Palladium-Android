package com.george.vector.users.root.main.fragments.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.common.announcements.fragment_news;
import com.george.vector.users.root.tasks.BottomSheetAddTask;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FragmentHome extends Fragment {

    private static final String TAG = "FragmentHomeRoot";
    String zone, email;

    Chip chip_root_future_ost, chip_root_future_bar;
    ExtendedFloatingActionButton create_task_root;

    FirebaseFirestore firebaseFirestore;

    boolean show_news_fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_home, container, false);

        chip_root_future_ost = view.findViewById(R.id.chip_root_future_ost);
        chip_root_future_bar = view.findViewById(R.id.chip_root_future_bar);
        create_task_root = view.findViewById(R.id.create_task_root);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(getString(R.string.email));

        zone = PreferenceManager
                .getDefaultSharedPreferences(FragmentHome.this.getContext())
                .getString("default_root_location", getString(R.string.ost));

        DocumentReference documentReference = firebaseFirestore.collection("news").document("news_fragment");
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            show_news_fragment = Objects.requireNonNull(value.getBoolean("show"));
            Log.d(TAG, String.format("show_news_fragment: %s", show_news_fragment));

            if (show_news_fragment)
                show_news_fragment();
        });

        create_task_root.setOnClickListener(v -> {
            BottomSheetAddTask bottomSheet = new BottomSheetAddTask();

            Bundle email = new Bundle();
            email.putString(getString(R.string.email), this.email);
            bottomSheet.setArguments(email);

            bottomSheet.show(getParentFragmentManager(), "BottomSheetAddTask");
        });

        if (zone.equals("ost"))
            chip_root_future_ost.setChecked(true);

        if (zone.equals("bar"))
            chip_root_future_bar.setChecked(true);

        chip_root_future_ost.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Log.i(TAG, "Остафьево checked");
                zone = "ost";
                updateZones(zone);
            }

        });
        chip_root_future_bar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "Барыши checked");
                zone = "bar";
                updateZones(zone);
            }
        });

        updateZones(zone);
        return view;
    }

    void show_news_fragment() {
        Fragment fragment_news = new fragment_news();
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.root_announcement, fragment_news)
                .commit();
    }


    void updateZones(@NotNull String zone_update) {
        Fragment currentFragment = null;
        switch (zone_update) {
            case "ost":
                Log.i(TAG, "Запуск фрагмента Осафьево");
                currentFragment = new FragmentOst();

                Bundle email = new Bundle();
                email.putString(getString(R.string.email), this.email);
                currentFragment.setArguments(email);

                break;
            case "bar":
                Log.i(TAG, "Запуск фрагмента Барыши");
                currentFragment = new FragmentBar();

                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.email), this.email);
                currentFragment.setArguments(bundle);

                break;
        }
        assert currentFragment != null;
        getParentFragmentManager().beginTransaction().replace(R.id.home_frame_root, currentFragment).commit();

    }
}
