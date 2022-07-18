package com.george.vector.ui.help.root;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.databinding.FragmentRootHelpBinding;

public class FragmentRootHelp extends Fragment {

    FragmentRootHelpBinding helpBinding;
    Fragment data = new FragmentDataHelp();
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        helpBinding = FragmentRootHelpBinding.inflate(inflater, container, false);
        View view = helpBinding.getRoot();

        helpBinding.technicalSupport.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "georgyfilatov@yandex.ru", null));
            intent.putExtra("android.intent.extra.SUBJECT", "Помощь с приложением");
            startActivity(Intent.createChooser(intent, "Выберите приложение для отправки электронного письма разработчику приложения"));
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        helpBinding = null;
    }
}
