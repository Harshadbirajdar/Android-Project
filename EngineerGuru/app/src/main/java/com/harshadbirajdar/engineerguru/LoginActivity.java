package com.harshadbirajdar.engineerguru;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneNumber, verificationCode;
     Button sendVerify;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private int btnTypr = 0;
    FirebaseFirestore db;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        phoneNumber = (EditText) findViewById(R.id.phonEditText);
        verificationCode = (EditText) findViewById(R.id.verificationEditText);
        sendVerify = (Button) findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        verificationCode.setVisibility(View.INVISIBLE);

        sendVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnTypr == 0) {
                    String phNumber = phoneNumber.getText().toString();
                    if (phNumber.equals("")) {
                        phoneNumber.setError("Please Enter the phone number");
                    } else if (phNumber.length() != 10) {
                        phoneNumber.setError("Please Enter the Vaild phone number");

                    } else {
                        phoneNumber.setEnabled(false);

                        String contact = "+91".concat(phNumber);
                        sendVerify.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                contact,
                                60,
                                TimeUnit.SECONDS,
                                LoginActivity.this,
                                mCallbacks
                        );

                    }


                } else {
                    String code = verificationCode.getText().toString().trim();
                    if(code.equals(""))
                    {
                        verificationCode.setError("Please Enter the verification code");

                    }else
                    {
                        sendVerify.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                        signInWithPhoneAuthCredential(credential);
                    }

                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("Harshad",e.toString());
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                verificationCode.setVisibility(View.VISIBLE);
                sendVerify.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                sendVerify.setText("Verify");

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                btnTypr = 1;

                // ...
            }
        };


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();
                            final DocumentReference documentReference = db.collection("Users").document(mAuth.getCurrentUser().getUid());
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    UserData userData = documentSnapshot.toObject(UserData.class);
                                    if (documentSnapshot.exists()) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });


                            // ...
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                sendVerify.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                verificationCode.setError("Please Ente the valid code");
                            }
                        }
                    }
                });
    }



}
