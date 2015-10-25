package com.sdr.asynctaskcancel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private AsyncTask myAsyncTask;
    private int taskCounter = 0;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAsyncTask();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTaskIfRunning();
    }

    public void cancelTaskIfRunning(){
        if(myAsyncTask != null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
            myAsyncTask.cancel(true);
            myAsyncTask = null;
        }
    }

    public void launchAsyncTask(){
        cancelTaskIfRunning();

        myAsyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if(isCancelled()) return null;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(isCancelled()) return;

                super.onPostExecute(aVoid);
                Log.d(TAG, "onPost!");
                textView.setText("We finished! " + System.currentTimeMillis());
                Toast.makeText(MainActivity.this, "onPost", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
}
