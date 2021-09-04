package com.george.vector.common.announcements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class fragment_news extends Fragment {

    CardView card_news;
    TextView title_news_text_view, subtitle_news_text_view;

    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        card_news = view.findViewById(R.id.card_news);
        title_news_text_view = view.findViewById(R.id.title_news_text_view);
        subtitle_news_text_view = view.findViewById(R.id.subtitle_news_text_view);

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firebaseFirestore.collection("news").document("news_fragment");
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            String title = value.getString("title");
            String subtitle = value.getString("subtitle");

            title_news_text_view.setText(title);
            subtitle_news_text_view.setText(subtitle);
        });

        card_news.setOnClickListener(v -> {

        });

        return view;
    }
}
