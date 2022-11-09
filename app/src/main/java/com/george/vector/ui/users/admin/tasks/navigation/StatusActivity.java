package com.george.vector.ui.users.admin.tasks.navigation;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.BAR_RUCHEEK;
import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.BAR_ZVEZDOCHKA;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.IS_EXECUTE;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;
import static com.george.vector.common.utils.consts.Keys.OST_AIST;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.OST_YAGODKA;
import static com.george.vector.common.utils.consts.Keys.STATUS;
import static com.george.vector.common.utils.consts.Keys.ZONE;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.george.vector.R;
import com.george.vector.databinding.ActivityStatusBinding;

public class StatusActivity extends AppCompatActivity {

    private ActivityStatusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Palladium);
        super.onCreate(savedInstanceState);
        binding = ActivityStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        String zone = arguments.getString(ZONE);
        boolean executed = arguments.getBoolean(IS_EXECUTE);

        if (zone.equals(OST_SCHOOL))
            binding.toolbarLocationFolderRoot.setTitle(getString(R.string.ost_text));

        if (zone.equals(OST_AIST))
            binding.toolbarLocationFolderRoot.setTitle(getString(R.string.ost_stork_text));

        if (zone.equals(OST_YAGODKA))
            binding.toolbarLocationFolderRoot.setTitle(getString(R.string.ost_berry_text));

        if (zone.equals(BAR_SCHOOL))
            binding.toolbarLocationFolderRoot.setTitle(getString(R.string.bar_text));

        if (zone.equals(BAR_RUCHEEK))
            binding.toolbarLocationFolderRoot.setTitle(getString(R.string.bar_stream_text));

        if (zone.equals(BAR_ZVEZDOCHKA))
            binding.toolbarLocationFolderRoot.setTitle(getString(R.string.bar_star_text));

        binding.toolbarLocationFolderRoot.setNavigationOnClickListener(v -> onBackPressed());

        binding.newTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllTasksAdminActivity.class);
            intent.putExtra(STATUS, NEW_TASKS);
            intent.putExtra(ZONE, zone);
            intent.putExtra(IS_EXECUTE, executed);
            startActivity(intent);
        });

        binding.inProgressTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllTasksAdminActivity.class);
            intent.putExtra(STATUS, IN_PROGRESS_TASKS);
            intent.putExtra(ZONE, zone);
            intent.putExtra(IS_EXECUTE, executed);
            startActivity(intent);
        });

        binding.completedTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllTasksAdminActivity.class);
            intent.putExtra(STATUS, COMPLETED_TASKS);
            intent.putExtra(ZONE, zone);
            intent.putExtra(IS_EXECUTE, executed);
            startActivity(intent);
        });

        binding.archiveTasksCardRoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllTasksAdminActivity.class);
            intent.putExtra(STATUS, ARCHIVE_TASKS);
            intent.putExtra(ZONE, zone);
            intent.putExtra(IS_EXECUTE, executed);
            startActivity(intent);
        });

    }
}