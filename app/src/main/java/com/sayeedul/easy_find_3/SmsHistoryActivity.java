package com.sayeedul.easy_find_3;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.sayeedul.easy_find_3.Adapters.SmsHistoryAdapter;
import com.sayeedul.easy_find_3.Adapters.UserAdapter;
import com.sayeedul.easy_find_3.DbHelper.SmsHistoryOpenHelper;
import com.sayeedul.easy_find_3.Pojo.SmsHistoryPojo;
import com.sayeedul.easy_find_3.Pojo.UserProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SmsHistoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    static SmsHistoryAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SmsHistoryPojo> UserClickcpy,UserClick;
    static SmsHistoryOpenHelper smsHistoryOpenHelper;

    TextView noItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noItem = findViewById(R.id.NoItemTV);

        recyclerView = findViewById(R.id.recyclerview_history);

        UserClick = new ArrayList<SmsHistoryPojo>();

        smsHistoryOpenHelper = new SmsHistoryOpenHelper(this);

        layoutManager = new LinearLayoutManager(this);
        adapter = new SmsHistoryAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Cursor cursor = smsHistoryOpenHelper.getData();
        while(cursor.moveToNext()) {

            String from_num = cursor.getString(cursor.getColumnIndexOrThrow(smsHistoryOpenHelper.FROM_NUMBER));
            String sent_num = cursor.getString(cursor.getColumnIndexOrThrow(smsHistoryOpenHelper.SENT_NUMBER));
            String sent_name = cursor.getString(cursor.getColumnIndexOrThrow(smsHistoryOpenHelper.SENT_NAME));
            String sent_date = cursor.getString(cursor.getColumnIndexOrThrow(smsHistoryOpenHelper.SENT_DATE));

            adapter.addItem(new SmsHistoryPojo(from_num,sent_num,sent_name,sent_date));

            UserClick.add(new SmsHistoryPojo(from_num,sent_name,sent_name,sent_date));
        }
        adapter.notifyDataSetChanged();
        cursor.close();

        adapter.setOnClickListener(new SmsHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Need to see here again...
                SmsHistoryPojo userclick = UserClick.get(position);
                String str_from_num = userclick.getNumber_in();
                String str_sent_name = userclick.getName_out();
                String str_sent_num = userclick.getNumber_out();
                String str_sent_date = userclick.getDate();
                showAlert(str_from_num, str_sent_num, str_sent_name, str_sent_date);
            }
        });

        if(adapter.getItemCount()==0)
        {
            noItem.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    // YAHAN PE DATA KAISE DALEGA.....SEE HERE>><><><>><><><><><><><><><><><><><><><><><><><>>><>><><><><><><><><

    public static void saveToDB(String from_num ,String sent_num,String sent_name) {

        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sent_date = df.format(c.getTime());
        // formattedDate have current date/time
        smsHistoryOpenHelper.insertProduct(from_num, sent_num, sent_name, sent_date);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchBar);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Contacts....");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        UserClick.clear();
        UserClick = adapter.filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        UserClick.clear();
        UserClick = adapter.filter(s);
        return true;
    }


    private void showAlert(final String str_from_num ,final String str_sent_num, final String str_sent_name
            ,final String str_sent_date)
    {
        String msg = "Sent Name : "+str_sent_name+"\nSent Contact : "+str_sent_num +
                    "\nSent to : "+str_from_num + "\nOn Date : "+str_sent_date;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SMS SENT DETAIL");
        builder.setIcon(R.drawable.contact_history);
        builder.setMessage(msg);
        builder.setCancelable(false);

        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SmsHistoryActivity.this, "Okay Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SmsHistoryActivity.this, "Cancel Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    protected void onDestroy() {
        smsHistoryOpenHelper.close();
        super.onDestroy();
    }

}
