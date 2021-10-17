package com.george.vector.users.root.main.fragments.tasks;

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
import com.google.android.material.chip.Chip;

import org.jetbrains.annotations.NotNull;

public class FragmentTasks extends Fragment {

    private static final String TAG = "FragmentTasksRoot";
    Chip chip_root_future_ost_tasks, chip_root_future_bar_tasks;

    String zone, email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_tasks, container, false);

        chip_root_future_ost_tasks = view.findViewById(R.id.chip_root_future_ost_tasks);
        chip_root_future_bar_tasks = view.findViewById(R.id.chip_root_future_bar_tasks);

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(EMAIL);
        Log.d(TAG, "email: " + email);

        zone = PreferenceManager.getDefaultSharedPreferences(FragmentTasks.this.getContext()).getString("default_root_location", OST);

        if(zone.equals("ost"))
            chip_root_future_ost_tasks.setChecked(true);

        if(zone.equals("bar"))
            chip_root_future_bar_tasks.setChecked(true);

        chip_root_future_ost_tasks.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.i(TAG, "Остафьево checked");
                zone = "ost";
                updateZones(zone);
            }

        });

        chip_root_future_bar_tasks.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
                currentFragment = new FragmentOstWork();

                Bundle email = new Bundle();
                email.putString(EMAIL, this.email);
                currentFragment.setArguments(email);

                break;
            case "bar":
                Log.i(TAG, "Запуск фрагмента Барыши");
                currentFragment = new FragmentBarWork();
                break;
        }
        assert currentFragment != null;
        getParentFragmentManager().beginTransaction().replace(R.id.task_frame_root, currentFragment).commit();

    }

}