package com.george.vector.users.root.main.fragments.home;

import static com.george.vector.common.consts.Keys.EMAIL;
import static com.george.vector.common.consts.Keys.OST;

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
import com.george.vector.databinding.FragmentRootHomeBinding;
import com.george.vector.users.root.tasks.BottomSheetAddTask;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class FragmentHome extends Fragment {

    private static final String TAG = "FragmentHomeRoot";
    String zone, email;

    FirebaseFirestore firebase_firestore;

    FragmentRootHomeBinding homeBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeBinding = FragmentRootHomeBinding.inflate(inflater, container, false);
        View view = homeBinding.getRoot();

        firebase_firestore = FirebaseFirestore.getInstance();

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(EMAIL);

        zone = PreferenceManager
                .getDefaultSharedPreferences(FragmentHome.this.getContext())
                .getString("default_root_location", OST);
        Log.d(TAG, "Zone: " + zone);

        homeBinding.createTaskRoot.setOnClickListener(v -> {
            BottomSheetAddTask bottomSheet = new BottomSheetAddTask();

            Bundle email = new Bundle();
            email.putString(EMAIL, this.email);
            bottomSheet.setArguments(email);

            bottomSheet.show(getParentFragmentManager(), "BottomSheetAddTask");
        });

        if (zone.equals("ost"))
            homeBinding.chipRootOst.setChecked(true);

        if (zone.equals("bar"))
            homeBinding.chipRootBar.setChecked(true);

        homeBinding.chipRootOst.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Log.i(TAG, "Остафьево checked");
                zone = "ost";
                updateZones(zone);
            }

        });
        homeBinding.chipRootBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "Барыши checked");
                zone = "bar";
                updateZones(zone);
            }
        });

        updateZones(zone);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        zone = PreferenceManager
                .getDefaultSharedPreferences(FragmentHome.this.getContext())
                .getString("default_root_location", OST);
        Log.d(TAG, "Zone: " + zone);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }

    void updateZones(@NotNull String zone_update) {
        Fragment currentFragment = null;
        switch (zone_update) {
            case "ost":
                Log.i(TAG, "Запуск фрагмента Осафьево");
                currentFragment = new FragmentOst();

                Bundle email = new Bundle();
                email.putString(EMAIL, this.email);
                currentFragment.setArguments(email);

                break;
            case "bar":
                Log.i(TAG, "Запуск фрагмента Барыши");
                currentFragment = new FragmentBar();

                Bundle bundle = new Bundle();
                bundle.putString(EMAIL, this.email);
                currentFragment.setArguments(bundle);

                break;
        }
        assert currentFragment != null;
        getParentFragmentManager().beginTransaction().replace(R.id.home_frame_root, currentFragment).commit();

    }
}
