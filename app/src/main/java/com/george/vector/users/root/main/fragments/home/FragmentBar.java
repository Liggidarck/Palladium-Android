package com.george.vector.users.root.main.fragments.home;

import static com.george.vector.common.consts.Keys.EMAIL;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentBarRootBinding;

public class FragmentBar extends Fragment {

    private static final String TAG = "FragmentBar";
    FragmentBarRootBinding barRootBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        barRootBinding = FragmentBarRootBinding.inflate(inflater, container, false);
        View view = barRootBinding.getRoot();

        Bundle args = getArguments();
        assert args != null;
        String email = args.getString(EMAIL);
        Log.d(TAG, "email: " + email);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        barRootBinding = null;
    }
}
