package com.harshadbirajdar.engineerguru.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;


import com.harshadbirajdar.engineerguru.MainActivity;
import com.harshadbirajdar.engineerguru.R;

import java.io.BufferedInputStream;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PdfViewerFrgament extends Fragment {
    private PDFView pdfView;

    private ProgressDialog progressDialog;
    private MainActivity activity;
    private FloatingActionButton floatingActionButton;


    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pdf_viewer, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        pdfView = (PDFView) view.findViewById(R.id.pdfView);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);

        activity = (MainActivity) getActivity();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);



        progressDialog.show();


        new RetrivepdfFromStream().execute(activity.pdfString());

        if (activity.h()!=null) {

            floatingActionButton.setVisibility(View.VISIBLE);

        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


        return view;
    }


    class RetrivepdfFromStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;
            try {
                URL uRi = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) uRi.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {


            pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    progressDialog.dismiss();
                }
            }).load();


        }
    }

}



