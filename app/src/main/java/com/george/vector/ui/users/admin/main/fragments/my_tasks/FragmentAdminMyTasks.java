package com.george.vector.ui.users.admin.main.fragments.my_tasks;

import static com.george.vector.common.utils.consts.Keys.OST;

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
import com.george.vector.databinding.FragmentRootTasksBinding;

import org.jetbrains.annotations.NotNull;

public class FragmentAdminMyTasks extends Fragment {

    private String zone;
    private FragmentRootTasksBinding rootTasksBinding;

    public static final String TAG = FragmentAdminMyTasks.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootTasksBinding = FragmentRootTasksBinding.inflate(inflater, container, false);
        View view = rootTasksBinding.getRoot();

        zone = PreferenceManager.getDefaultSharedPreferences(FragmentAdminMyTasks.this.getContext()).getString("default_root_location", OST);

        if(zone.equals("ost"))
            rootTasksBinding.chipOstTasks.setChecked(true);

        if(zone.equals("bar"))
            rootTasksBinding.chipBarTasks.setChecked(true);

        rootTasksBinding.chipOstTasks.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "Остафьево checked");
                zone = "ost";
                updateZones(zone);
            }

        });

        rootTasksBinding.chipBarTasks.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Log.i(TAG, "Барыши checked");
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
                Log.i(TAG, "Запуск фрагмента Осафьево");
                currentFragment = new FragmentAdminMyTasksOst();
                break;
            case "bar":
                Log.i(TAG, "Запуск фрагмента Барыши");
                currentFragment = new FragmentAdminMyTasksBar();
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