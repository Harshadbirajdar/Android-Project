package com.harshadbirajdar.engineerguru.Year;


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

public class FirstYearBranch extends Fragment {

    private FirebaseFirestore firestore =FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("FirstYear");
    private SubjectAdpter adpter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_year_branch, container, false);
        Query query =collectionReference.orderBy("position",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Subject> options =new FirestoreRecyclerOptions.Builder<Subject>().setQuery(query,Subject.class).build();
        adpter = new SubjectAdpter(options);
        RecyclerView recyclerView = view.findViewById(R.id.firstYearRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adpter);

        adpter.setOnClickListner(new SubjectAdpter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int postion) {
                Subject subject = documentSnapshot.toObject(Subject.class);
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("firstSubjectID",id);
                //intent.putExtra("year","ThirdYear");
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adpter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adpter.stopListening();

    }
}
