package com.george.vector.users.root.main.fragments.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentNotificationsBinding;

public class FragmentNotifications extends Fragment {

    FragmentNotificationsBinding notificationsBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        notificationsBinding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View view = notificationsBinding.getRoot();

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        notificationsBinding = null;
    }
}
