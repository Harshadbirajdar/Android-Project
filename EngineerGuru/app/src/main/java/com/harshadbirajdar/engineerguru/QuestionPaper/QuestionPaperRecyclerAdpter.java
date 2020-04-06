package com.harshadbirajdar.engineerguru.QuestionPaper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.harshadbirajdar.engineerguru.Branches.BranchRecyclerViewAdpter;
import com.harshadbirajdar.engineerguru.R;

public class QuestionPaperRecyclerAdpter extends FirestoreRecyclerAdapter<QuestionPaper, QuestionPaperRecyclerAdpter.QuestionViewHolder> {
    private BranchRecyclerViewAdpter.onItemClickListner listner;

    public QuestionPaperRecyclerAdpter(@NonNull FirestoreRecyclerOptions<QuestionPaper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionViewHolder holder, int position, @NonNull QuestionPaper model) {
        holder.month.setText(model.getMonth());
        holder.pattern.setText(model.getPattern());
        holder.year.setText(String.valueOf(model.getYear()));
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pdf_card_layout,viewGroup,false);

        return new QuestionViewHolder(view);
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder{
        TextView month,pattern,year;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            month = (TextView)itemView.findViewById(R.id.pdfMonth);
            pattern = (TextView)itemView.findViewById(R.id.pdfPattern);
            year = (TextView)itemView.findViewById(R.id.pdfYear);
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

    public void setOnClickListner(BranchRecyclerViewAdpter.onItemClickListner listner)
    {
        this.listner=listner;
    }
}
