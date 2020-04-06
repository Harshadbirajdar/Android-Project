package com.harshadbirajdar.engineerguru.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;

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

import com.harshadbirajdar.engineerguru.Branches.BranchRecyclerViewAdpter;
import com.harshadbirajdar.engineerguru.MainActivity;
import com.harshadbirajdar.engineerguru.QuestionPaper.QuestionPaper;
import com.harshadbirajdar.engineerguru.QuestionPaper.QuestionPaperRecyclerAdpter;
import com.harshadbirajdar.engineerguru.R;

public class QuestionList extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    QuestionPaperRecyclerAdpter adpter;
    QuestionPaper questionPaper;
    public String h;
    Query query;


    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.list_question, container, false);


        MainActivity activity = (MainActivity) getActivity();


        if (getActivity().getIntent().hasExtra("subjectID")) {
            collectionReference = firestore.collection(activity.getYear())
                    .document(activity.getId())
                    .collection("subjects")
                    .document(getActivity().getIntent()
                            .getStringExtra("subjectID"))
                    .collection("Paper");
            activity.setNavigation(activity.getYear());
        } else {
            activity.setNavigation("FirstYear");
            collectionReference = firestore.collection("FirstYear").document(activity.firstYear()).collection("Paper");
        }



        query = collectionReference.orderBy("pattern", Query.Direction.DESCENDING);
       FirestoreRecyclerOptions<QuestionPaper> options = new FirestoreRecyclerOptions.Builder<QuestionPaper>().setQuery(query, QuestionPaper.class).build();
        adpter = new QuestionPaperRecyclerAdpter(options);
       RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adpter);


        adpter.setOnClickListner(new BranchRecyclerViewAdpter.onItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int postion) {
                questionPaper = documentSnapshot.toObject(QuestionPaper.class);
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("pdfId", questionPaper.getPdf());
                intent.putExtra("AnsPdfId", questionPaper.getAns());

                if(netIsConnected())
                {
                    startActivity(intent);
                }else
                {

                    Toast.makeText(getActivity(), "Please Check The Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });




        return view;
    }




    boolean netIsConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
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

    public String getAnsPdf() {
        return questionPaper.getAns();
    }
}
