package com.harshadbirajdar.engineerguru.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import com.harshadbirajdar.engineerguru.MainActivity;

import com.harshadbirajdar.engineerguru.R;
import com.harshadbirajdar.engineerguru.Subject.Subject;
import com.harshadbirajdar.engineerguru.Subject.SubjectAdpter;

public class SubjectList extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private SubjectAdpter adpter;

    CollectionReference v;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subject_fragment, container, false);
        MainActivity activity = (MainActivity) getActivity();


        collectionReference = firestore.collection(activity.getYear());
        v = collectionReference.document(activity.getId()).collection("subjects");

        Query query = v.orderBy("position", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Subject> options = new FirestoreRecyclerOptions.Builder<Subject>().setQuery(query, Subject.class).build();
        adpter = new SubjectAdpter(options);
        RecyclerView recyclerView = view.findViewById(R.id.subjectRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adpter);

        adpter.setOnClickListner(new SubjectAdpter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int postion) {
                Subject subject = documentSnapshot.toObject(Subject.class);
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("subjectID", id);

                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        adpter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();

        adpter.startListening();
    }


}
