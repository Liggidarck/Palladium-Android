package com.george.vector.ui.users.root.main.fragments.tasks;

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
import com.george.vector.databinding.FragmentRootTasksBinding;

import org.jetbrains.annotations.NotNull;

public class FragmentTasks extends Fragment {

    private static final String TAG = "FragmentTasksRoot";
    String zone, email;
    FragmentRootTasksBinding rootTasksBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootTasksBinding = FragmentRootTasksBinding.inflate(inflater, container, false);
        View view = rootTasksBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        email = args.getString(EMAIL);
        Log.d(TAG, "email: " + email);

        zone = PreferenceManager.getDefaultSharedPreferences(FragmentTasks.this.getContext()).getString("default_root_location", OST);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootTasksBinding = null;
    }
}