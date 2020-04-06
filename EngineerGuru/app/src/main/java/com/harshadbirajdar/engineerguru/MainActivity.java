package com.harshadbirajdar.engineerguru;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.harshadbirajdar.engineerguru.Fragment.AnsPdfViewer;
import com.harshadbirajdar.engineerguru.Fragment.Home;
import com.harshadbirajdar.engineerguru.Year.FirstYearBranch;
import com.harshadbirajdar.engineerguru.Year.FourthYearBranch;
import com.harshadbirajdar.engineerguru.Fragment.PdfViewerFrgament;
import com.harshadbirajdar.engineerguru.Fragment.QuestionList;
import com.harshadbirajdar.engineerguru.Year.SecondYearBranch;
import com.harshadbirajdar.engineerguru.Fragment.SubjectList;
import com.harshadbirajdar.engineerguru.Year.ThirdYearBranch;

import javax.annotation.Nullable;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String id;
    private String year;
    String text;
    private String pdfString;
    private FirebaseAuth mAuth;
    TextView username;
    TextView useremail;
    String ansPdf;
    String firstYearSubject;
    DocumentReference docRef;

    private Toolbar toolbar;
    UserData userData;
    private SharedPreferences sharedPreferences;

    private NavigationView navigationView;
    FirebaseFirestore db;
    private ProgressDialog progressDialog;
    ListenerRegistration listenerRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.harshadbirajdar.engineerguru", MODE_PRIVATE);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Your Data is Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        View header = navigationView.getHeaderView(0);
        username = (TextView) header.findViewById(R.id.userName);
        useremail = (TextView) header.findViewById(R.id.userEmail);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {


            docRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
            listenerRegistration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            userData = documentSnapshot.toObject(UserData.class);
                            username.setText(userData.getName());
                            useremail.setText(userData.getEmailAddress());
                        }
                        progressDialog.dismiss();
                    }
                }
            });


        }

        if (getIntent().hasExtra("AnsPdf")) {

            ansPdf = getIntent().getStringExtra("AnsPdfId");

            SetFragmentLayout(new AnsPdfViewer());

        } else if (getIntent().hasExtra("pdfId")) {
            pdfString = getIntent().getStringExtra("pdfId");
            ansPdf = getIntent().getStringExtra("AnsPdfId");
            SetFragmentLayout(new PdfViewerFrgament());


        } else if (getIntent().hasExtra("subjectID")) {
            Intent intent = new Intent(MainActivity.this, QuestionList.class);
            intent.putExtra("subject", getIntent().getStringExtra("subjectID"));

            SetFragmentLayout(new QuestionList());
        } else if (getIntent().hasExtra("firstSubjectID")) {

            firstYearSubject = getIntent().getStringExtra("firstSubjectID");
            SetFragmentLayout(new QuestionList());
        } else if (sharedPreferences.getString("shareId", "") != "") {
            setNavigation(sharedPreferences.getString("shareYear", ""));
            sharedPreferences.edit().remove("FirstYear").apply();
            SetFragmentLayout(new SubjectList());

        } else if (sharedPreferences.getString("FirstYear", "").compareTo("") != 0) {
            setNavigation("FirstYear");
            SetFragmentLayout(new FirstYearBranch());

        }else
        {
            SetFragmentLayout(new Home());

        }


        if (getIntent().hasExtra("id") && getIntent().hasExtra("year")) {

            id = getIntent().getStringExtra("id");
            year = getIntent().getStringExtra("year");
            sharedPreferences.edit().putString("shareId", id).apply();
            sharedPreferences.edit().putString("shareYear", year).apply();
            setNavigation(year);

            SetFragmentLayout(new SubjectList());
        }


    }

    private void SetFragmentLayout(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    public void setNavigation(String Year) {
        switch (Year) {
            case "FirstYear":

                navigationView.setCheckedItem(R.id.nav_firstYear);
                break;

            case "SecondYear":

                navigationView.setCheckedItem(R.id.nav_secondYear);
                break;
            case "ThirdYear":

                navigationView.setCheckedItem(R.id.nav_thirdYear);
                break;

            case "FourthYear":
                navigationView.setCheckedItem(R.id.nav_fourthYear);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_firstYear) {
            sharedPreferences.edit().remove("shareId").apply();
            sharedPreferences.edit().remove("shareYear").apply();
            sharedPreferences.edit().putString("FirstYear", "FirstYear").apply();

            SetFragmentLayout(new FirstYearBranch());

        } else if (id == R.id.nav_secondYear) {

            SetFragmentLayout(new SecondYearBranch());


        } else if (id == R.id.nav_thirdYear) {
            SetFragmentLayout(new ThirdYearBranch());


        } else if (id == R.id.nav_fourthYear) {
            SetFragmentLayout(new FourthYearBranch());


        } else if (id == R.id.nav_share) {


               Intent share = new Intent();
               share.setAction(Intent.ACTION_SEND);
               share.putExtra(Intent.EXTRA_TEXT, "Hey there I using the *Engineer Guru* application for previous year question paper it was great application for previous year Question paper \nhttps://play.google.com/store/apps/details?id=com.harshadbirajdar.engineerguru ");
               share.setType("text/plain");
               startActivity(share);



        } else if (id == R.id.nav_send) {
            listenerRegistration.remove();


            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }


    public String getId() {
        return sharedPreferences.getString("shareId", "");
    }

    public String getYear() {
        return sharedPreferences.getString("shareYear", "");
    }

    public String pdfString() {
        return pdfString;
    }

    public String firstYear() {
        return firstYearSubject;
    }


    public String h() {
        return ansPdf;
    }

    public String ansPdf() {
        return getIntent().getStringExtra("AnsPdf");
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {


            db = FirebaseFirestore.getInstance();

            final DocumentReference documentReference = db.collection("Users").document(mAuth.getCurrentUser().getUid());


            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setMessage("Checking Profile");
                    progressDialog.setCanceledOnTouchOutside(false);

                    if (!documentSnapshot.exists()) {
                        progressDialog.show();
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                        finish();
                    }
                    progressDialog.dismiss();

                }
            });
        }
    }


    String inviteText()
    {
        DocumentReference docRef = db.collection("invite").document("text");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                         text=document.getString("text").toString();

                       // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return text;
    }
}
