package com.harshadbirajdar.engineerguru.Branches;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.harshadbirajdar.engineerguru.R;



public class BranchRecyclerViewAdpter extends FirestoreRecyclerAdapter<Branch, BranchRecyclerViewAdpter.BranchViewHolder> {
    private onItemClickListner listner;
        public BranchRecyclerViewAdpter(@NonNull FirestoreRecyclerOptions<Branch> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BranchViewHolder holder, int position, @NonNull Branch model) {
            holder.branchTextView.setText(model.getBranch());

    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.branch_crad_view,viewGroup,false);
        return new BranchViewHolder(view);
    }

    class BranchViewHolder extends RecyclerView.ViewHolder{

            TextView branchTextView;
        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            branchTextView = (TextView)itemView.findViewById(R.id.barncListTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int postion = getAdapterPosition();
                    if(postion!=RecyclerView.NO_POSITION && listner!=null)
                    {
                        listner.onItemClick(getSnapshots().getSnapshot(postion),postion);
                    }
                }
            });
        }
    }
    public interface onItemClickListner {
        void onItemClick(DocumentSnapshot documentSnapshot, int postion);
    }

    public void setOnClickListner(onItemClickListner listner)
    {
        this.listner=listner;
    }
}


