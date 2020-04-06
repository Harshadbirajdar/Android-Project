package com.harshadbirajdar.engineerguru;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
  ProgressBar progressBar;
    private TextInputLayout nameText, emailText, phoneText;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        phoneText = (TextInputLayout) findViewById(R.id.phoneTextInputLayout);
        nameText = (TextInputLayout) findViewById(R.id.textInputName);
        emailText = (TextInputLayout) findViewById(R.id.textInputEmail);
        progressBar = (ProgressBar)findViewById(R.id.profileProgress);


        phoneText.getEditText().setText(mAuth.getCurrentUser().getPhoneNumber());
        phoneText.getEditText().setEnabled(false);

        send = (Button) findViewById(R.id.profileBtn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = nameText.getEditText().getText().toString();
                String emailStr = emailText.getEditText().getText().toString();
                String phoneStr = phoneText.getEditText().getText().toString();
                if (nameStr.equals("")) {
                    nameText.setErrorEnabled(true);
                    nameText.setError("Please Enter the Name");
                } else if (emailStr.equals("")) {
                    nameText.setErrorEnabled(false);
                    emailText.setError("Please Enter the Email Address");
                }else if(!isValid(emailStr))
                {
                    emailText.setError("Please Enter the Valid Email Address");
                }else

                {   emailText.setErrorEnabled(false);
                    send.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    UserData userData = new UserData(nameStr, phoneStr, emailStr);
                    db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finish();
                        }
                    });

                }



            }
        });


    }
    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
