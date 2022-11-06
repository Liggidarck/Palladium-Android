package com.george.vector.ui.users.root.main.fragments.home;

import static com.george.vector.common.utils.consts.Keys.OST;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.george.vector.R;
import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.FragmentRootHomeBinding;
import com.george.vector.ui.users.root.profile.ProfileRootActivity;
import com.george.vector.ui.users.root.tasks.BottomSheetAddTask;

import org.jetbrains.annotations.NotNull;

public class FragmentRootHome extends Fragment {

    private String zone;

    private FragmentRootHomeBinding homeBinding;

    public static final String TAG = FragmentRootHome.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeBinding = FragmentRootHomeBinding.inflate(inflater, container, false);
        View view = homeBinding.getRoot();

        zone = PreferenceManager
                .getDefaultSharedPreferences(FragmentRootHome.this.getContext())
                .getString("default_root_location", OST);

        UserDataViewModel userPrefViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        String nameUser = userPrefViewModel.getUser().getName();
        String lastnameUser = userPrefViewModel.getUser().getLastName();
        String email = userPrefViewModel.getUser().getEmail();

        String _name = Character.toString(nameUser.charAt(0));
        String _last_name = Character.toString(lastnameUser.charAt(0));
        String ava = String.format("%s%s", _name, _last_name);

        homeBinding.textNameRoot.setText(String.format("%s %s", nameUser, lastnameUser));
        homeBinding.textEmailRoot.setText(email);
        homeBinding.textViewNameAva.setText(ava);

        homeBinding.createTaskRoot.setOnClickListener(v -> {
            BottomSheetAddTask bottomSheet = new BottomSheetAddTask();
            bottomSheet.show(getParentFragmentManager(), "BottomSheetAddTask");
        });

        if (zone.equals("ost"))
            homeBinding.chipRootOst.setChecked(true);

        if (zone.equals("bar"))
            homeBinding.chipRootBar.setChecked(true);

        homeBinding.chipRootOst.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                zone = "ost";
                updateZones(zone);
            }

        });
        homeBinding.chipRootBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                zone = "bar";
                updateZones(zone);
            }
        });

        homeBinding.profileLayout.setOnClickListener(v ->
                startActivity(new Intent(FragmentRootHome.this.getActivity(),
                        ProfileRootActivity.class)));

        updateZones(zone);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        zone = PreferenceManager
                .getDefaultSharedPreferences(FragmentRootHome.this.getContext())
                .getString("default_root_location", OST);
        Log.d(TAG, "Zone: " + zone);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }

    void updateZones(@NotNull String zoneUpdate) {
        Fragment currentFragment = null;
        switch (zoneUpdate) {
            case "ost":
                Log.i(TAG, "Запуск фрагмента Осафьево");
                currentFragment = new FragmentRootZoneOst();

                break;
            case "bar":
                Log.i(TAG, "Запуск фрагмента Барыши");
                currentFragment = new FragmentRootZoneBar();

                break;
        }
        assert currentFragment != null;
        getParentFragmentManager().beginTransaction().replace(R.id.home_frame_root, currentFragment).commit();

    }
}
