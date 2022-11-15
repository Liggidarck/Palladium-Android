package com.george.vector.ui.users.admin.main.fragments.statistic;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;
import static com.george.vector.common.utils.consts.Keys.OST_AIST;
import static com.george.vector.common.utils.consts.Keys.OST_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.OST_YAGODKA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.FragmentStatisticOstBinding;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

public class FragmentStatisticOst extends Fragment {

    private FragmentStatisticOstBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatisticOstBinding.inflate(inflater, container, false);

        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.requireActivity().getApplication(),
                userDataViewModel.getToken()
        )).get(TaskViewModel.class);

        taskViewModel.countByZoneLikeAndStatusLike(OST_SCHOOL, NEW_TASKS)
                .observe(this.requireActivity(), count ->
                        binding.ostSchoolCountNew.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(OST_SCHOOL, IN_PROGRESS_TASKS)
                .observe(this.requireActivity(), count ->
                        binding.ostSchoolCountProgress.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(OST_SCHOOL, COMPLETED_TASKS)
                .observe(this.requireActivity(), count ->
                        binding.ostSchoolCountCompleted.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(OST_SCHOOL, ARCHIVE_TASKS)
                .observe(this.requireActivity(), count -> binding.ostSchoolCountArchive.setText(count.getMessage()));


        taskViewModel.countByZoneLikeAndStatusLike(OST_AIST, NEW_TASKS)
                .observe(this.requireActivity(), count -> binding.ostAistCountNew.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(OST_AIST, IN_PROGRESS_TASKS)
                .observe(this.requireActivity(), count -> binding.ostAistCountProgress.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(OST_AIST, COMPLETED_TASKS)
                .observe(this.requireActivity(), count -> binding.ostAistCountCompleted.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(OST_AIST, ARCHIVE_TASKS)
                .observe(this.requireActivity(), count -> binding.ostAistCountArchive.setText(count.getMessage()));


        taskViewModel.countByZoneLikeAndStatusLike(OST_YAGODKA, NEW_TASKS)
                .observe(this.requireActivity(), count -> binding.ostYagodkaCountNew.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(OST_YAGODKA, IN_PROGRESS_TASKS)
                .observe(this.requireActivity(), count -> binding.ostYagodkaCountProgress.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(OST_YAGODKA, COMPLETED_TASKS)
                .observe(this.requireActivity(), count -> binding.ostYagodkaCountCompleted.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(OST_YAGODKA, ARCHIVE_TASKS)
                .observe(this.requireActivity(), count -> binding.ostYagodkaCountArchive.setText(count.getMessage()));


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
