package com.george.vector.ui.users.admin.main.fragments.statistic;

import static com.george.vector.common.utils.consts.Keys.ARCHIVE_TASKS;
import static com.george.vector.common.utils.consts.Keys.BAR_RUCHEEK;
import static com.george.vector.common.utils.consts.Keys.BAR_SCHOOL;
import static com.george.vector.common.utils.consts.Keys.BAR_ZVEZDOCHKA;
import static com.george.vector.common.utils.consts.Keys.COMPLETED_TASKS;
import static com.george.vector.common.utils.consts.Keys.IN_PROGRESS_TASKS;
import static com.george.vector.common.utils.consts.Keys.NEW_TASKS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.george.vector.data.user.UserDataViewModel;
import com.george.vector.databinding.FragmentStatisticBarBinding;
import com.george.vector.ui.viewmodel.TaskViewModel;
import com.george.vector.ui.viewmodel.ViewModelFactory;

public class FragmentStatisticBar extends Fragment {

    private FragmentStatisticBarBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatisticBarBinding.inflate(inflater, container, false);



        UserDataViewModel userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        TaskViewModel taskViewModel = new ViewModelProvider(this, new ViewModelFactory(
                this.requireActivity().getApplication(),
                userDataViewModel.getToken()
        )).get(TaskViewModel.class);

        taskViewModel.countByZoneLikeAndStatusLike(BAR_SCHOOL, NEW_TASKS)
                .observe(this.requireActivity(), count ->
                        binding.barSchoolCountNew.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(BAR_SCHOOL, IN_PROGRESS_TASKS)
                .observe(this.requireActivity(), count ->
                        binding.barSchoolCountProgress.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(BAR_SCHOOL, COMPLETED_TASKS)
                .observe(this.requireActivity(), count ->
                        binding.barSchoolCountCompleted.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(BAR_SCHOOL, ARCHIVE_TASKS)
                .observe(this.requireActivity(), count -> binding.barSchoolCountArchive.setText(count.getMessage()));


        taskViewModel.countByZoneLikeAndStatusLike(BAR_RUCHEEK, NEW_TASKS)
                .observe(this.requireActivity(), count -> binding.barRucheekCountNew.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(BAR_RUCHEEK, IN_PROGRESS_TASKS)
                .observe(this.requireActivity(), count -> binding.barRucheekCountProgress.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(BAR_RUCHEEK, COMPLETED_TASKS)
                .observe(this.requireActivity(), count -> binding.barRucheekCountCompleted.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(BAR_RUCHEEK, ARCHIVE_TASKS)
                .observe(this.requireActivity(), count -> binding.barRucheekCountArchive.setText(count.getMessage()));


        taskViewModel.countByZoneLikeAndStatusLike(BAR_ZVEZDOCHKA, NEW_TASKS)
                .observe(this.requireActivity(), count -> binding.barStarCountNew.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(BAR_ZVEZDOCHKA, IN_PROGRESS_TASKS)
                .observe(this.requireActivity(), count -> binding.barStarCountProgress.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(BAR_ZVEZDOCHKA, COMPLETED_TASKS)
                .observe(this.requireActivity(), count -> binding.barStarCountCompleted.setText(count.getMessage()));

        taskViewModel.countByZoneLikeAndStatusLike(BAR_ZVEZDOCHKA, ARCHIVE_TASKS)
                .observe(this.requireActivity(), count -> binding.barStarCountArchive.setText(count.getMessage()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
