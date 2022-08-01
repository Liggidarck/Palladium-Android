package com.george.vector.ui.users.root.main.fragments.my_tasks;

import static com.george.vector.common.utils.consts.Keys.EMAIL;
import static com.george.vector.common.utils.consts.Keys.OST;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES;
import static com.george.vector.common.utils.consts.Keys.USER_PREFERENCES_EMAIL;
import static com.george.vector.common.utils.consts.Logs.TAG_TASK_ROOT_FRAGMENT;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.george.vector.data.preferences.UserPreferencesViewModel;
import com.george.vector.databinding.FragmentRootTasksBinding;

import org.jetbrains.annotations.NotNull;

public class FragmentRootMyTasks extends Fragment {

    String zone, email;
    FragmentRootTasksBinding rootTasksBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootTasksBinding = FragmentRootTasksBinding.inflate(inflater, container, false);
        View view = rootTasksBinding.getRoot();

        UserPreferencesViewModel userPrefViewModel = new ViewModelProvider(this).get(UserPreferencesViewModel.class);
        email = userPrefViewModel.getUser().getEmail();

        zone = PreferenceManager.getDefaultSharedPreferences(FragmentRootMyTasks.this.getContext()).getString("default_root_location", OST);

        if(zone.equals("ost"))
            rootTasksBinding.chipOstTasks.setChecked(true);

        if(zone.equals("bar"))
            rootTasksBinding.chipBarTasks.setChecked(true);

        rootTasksBinding.chipOstTasks.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG_TASK_ROOT_FRAGMENT, "Остафьево checked");
                zone = "ost";
                updateZones(zone);
            }

        });

        rootTasksBinding.chipBarTasks.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Log.i(TAG_TASK_ROOT_FRAGMENT, "Барыши checked");
                zone = "bar";
                updateZones(zone);
            }
        });

        updateZones(zone);
        return view;
    }

    void updateZones(@NotNull String zone_update) {
        Fragment currentFragment = null;
        switch (zone_update) {
            case "ost":
                Log.i(TAG_TASK_ROOT_FRAGMENT, "Запуск фрагмента Осафьево");
                currentFragment = new FragmentRootMyTasksOst();

                Bundle email = new Bundle();
                email.putString(EMAIL, this.email);
                currentFragment.setArguments(email);

                break;
            case "bar":
                Log.i(TAG_TASK_ROOT_FRAGMENT, "Запуск фрагмента Барыши");
                currentFragment = new FragmentRootMyTasksBar();
                break;
        }
        assert currentFragment != null;
        getParentFragmentManager().beginTransaction().replace(R.id.task_frame_root, currentFragment).commit();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootTasksBinding = null;
    }
}