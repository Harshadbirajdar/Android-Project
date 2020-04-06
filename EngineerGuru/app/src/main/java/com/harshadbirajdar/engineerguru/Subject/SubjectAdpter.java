package com.harshadbirajdar.engineerguru.Subject;

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

public class SubjectAdpter extends FirestoreRecyclerAdapter<Subject, SubjectAdpter.SubjectViewHolder> {
    private onItemClickListner listner;
    public SubjectAdpter(@NonNull FirestoreRecyclerOptions<Subject> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SubjectViewHolder holder, int position, @NonNull Subject model) {
        holder.subjectTextView.setText(model.getSubject());
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject_list_card,viewGroup,false);

        return new SubjectViewHolder(view);
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder
    {
        TextView subjectTextView;
        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = (TextView)itemView.findViewById(R.id.subjectText);
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
