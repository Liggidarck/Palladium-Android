package com.george.vector.ui.users.root.main.fragments.home;

import static com.george.vector.common.consts.Keys.OST;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_LAST_NAME;
import static com.george.vector.common.consts.Keys.USER_PREFERENCES_NAME;
import static com.george.vector.common.consts.Logs.TAG_HOME_ROOT_FRAGMENT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.george.vector.ui.users.root.profile.ProfileRootActivity;
import com.george.vector.ui.users.root.tasks.BottomSheetAddTask;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class FragmentRootHome extends Fragment {

    String zone, email;

    FirebaseFirestore firebaseFirestore;

    SharedPreferences sharedPreferences;
    FragmentRootHomeBinding homeBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeBinding = FragmentRootHomeBinding.inflate(inflater, container, false);
        View view = homeBinding.getRoot();

        firebaseFirestore = FirebaseFirestore.getInstance();

        zone = PreferenceManager
                .getDefaultSharedPreferences(FragmentRootHome.this.getContext())
                .getString("default_root_location", OST);

        sharedPreferences = getActivity().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        String nameUser = sharedPreferences.getString(USER_PREFERENCES_NAME, "");
        String lastnameUser = sharedPreferences.getString(USER_PREFERENCES_LAST_NAME, "");
        email = sharedPreferences.getString(USER_PREFERENCES_EMAIL, "");

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
                Log.i(TAG_HOME_ROOT_FRAGMENT, "Остафьево checked");
                zone = "ost";
                updateZones(zone);
            }

        });
        homeBinding.chipRootBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i(TAG_HOME_ROOT_FRAGMENT, "Барыши checked");
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
        Log.d(TAG_HOME_ROOT_FRAGMENT, "Zone: " + zone);
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
                Log.i(TAG_HOME_ROOT_FRAGMENT, "Запуск фрагмента Осафьево");
                currentFragment = new FragmentRootOst();

                break;
            case "bar":
                Log.i(TAG_HOME_ROOT_FRAGMENT, "Запуск фрагмента Барыши");
                currentFragment = new FragmentRootBar();

                break;
        }
        assert currentFragment != null;
        getParentFragmentManager().beginTransaction().replace(R.id.home_frame_root, currentFragment).commit();

    }
}
