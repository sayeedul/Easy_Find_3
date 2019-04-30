package com.sayeedul.easy_find_3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    ProgressBar prog;
    TextView BY;

    String[] permissionsString = new String[]{Manifest.permission.READ_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CONTACTS};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prog = (ProgressBar)findViewById(R.id.progressBar);
        BY = findViewById(R.id.byTV);

        if(!hasPermissions(getApplicationContext(),permissionsString))
        {
            //WE HAVE TO ASK FROM USER FOR PERMISSION AT RUNTIME.
            ActivityCompat.requestPermissions(SplashActivity.this,permissionsString,100);
        }
        else
        {
            new LoadIconTask().execute();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Toast.makeText(SplashActivity.this, "Loading....", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 3000);
        }
    } // end of Oncreate


    public Boolean hasPermissions(Context context, String[] permissions)
    {
        boolean hasAllPermission = true;
        for(int i =0; i<7; i++)
        {
            if(ActivityCompat.checkSelfPermission(this,permissions[i])!= PackageManager.PERMISSION_GRANTED)
            {
                hasAllPermission = false ;
            }
        }

        return hasAllPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED
                    && grantResults[2]==PackageManager.PERMISSION_GRANTED
                    && grantResults[3]==PackageManager.PERMISSION_GRANTED
                    && grantResults[4]==PackageManager.PERMISSION_GRANTED
                    && grantResults[5]==PackageManager.PERMISSION_GRANTED
                    && grantResults[6]==PackageManager.PERMISSION_GRANTED){

                new LoadIconTask().execute();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Toast.makeText(SplashActivity.this, "Loading....", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 2500);

            }//end of inner if
            else
            {
                Toast.makeText(this, "PLEASE GRANT ALL THE PERMISSION TO CONTINUE...", Toast.LENGTH_SHORT).show();
                this.finish();
            }

        }//end of outer if
        else
        {
            Toast.makeText(this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
            this.finish();
        }

    }//end of onRequestPermissionresult()


    class LoadIconTask extends AsyncTask<Integer,Integer,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            for(int i=0;i<5;i++)
            {
                try {
                    Thread.sleep(500); publishProgress(i*25);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog.setVisibility(prog.VISIBLE);
            // BY.setVisibility(BY.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            prog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prog.setVisibility(prog.INVISIBLE);
            // BY.setVisibility(BY.VISIBLE);
        }
    }




}
