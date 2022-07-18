package com.george.vector.ui.help.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentHelpUserBinding;

public class FragmentUserHelp extends Fragment {

    FragmentHelpUserBinding binding;
    Bundle bundle = new Bundle();
    Fragment data = new FragmentDataHelp();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHelpUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.technicalSupport.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "georgyfilatov@yandex.ru", null));
            intent.putExtra("android.intent.extra.SUBJECT", "Помощь с приложением");
            startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
