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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.harshadbirajdar.engineerguru.Branches.Branch;
import com.harshadbirajdar.engineerguru.Branches.BranchRecyclerViewAdpter;
import com.harshadbirajdar.engineerguru.MainActivity;
import com.harshadbirajdar.engineerguru.R;

public class ThirdYearBranch extends Fragment {
    private FirebaseFirestore firestore =FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("ThirdYear");
    private BranchRecyclerViewAdpter branchRecyclerViewAdpter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_year_branch, container, false);
        Query query =collectionReference.orderBy("position",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Branch> options =new FirestoreRecyclerOptions.Builder<Branch>().setQuery(query,Branch.class).build();
        branchRecyclerViewAdpter = new BranchRecyclerViewAdpter(options);
        RecyclerView recyclerView = view.findViewById(R.id.thirdYearRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(branchRecyclerViewAdpter);

        branchRecyclerViewAdpter.setOnClickListner(new BranchRecyclerViewAdpter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int postion) {
                Branch branch = documentSnapshot.toObject(Branch.class);
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("year","ThirdYear");
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        branchRecyclerViewAdpter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        branchRecyclerViewAdpter.stopListening();
    }
}
