package com.sayeedul.easy_find_3;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.sayeedul.easy_find_3.Adapters.UserAdapter;
import com.sayeedul.easy_find_3.Pojo.UserProfile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , SearchView.OnQueryTextListener {


    //SearchView searchView;
    Cursor cursor;

    RecyclerView recyclerView;
    static UserAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<UserProfile>UserClickcpy,UserClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);



        recyclerView = findViewById(R.id.recyclerview1);

        UserClick = new ArrayList<UserProfile>();

        adapter = new UserAdapter();
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            UserClick.add(new UserProfile(name,phonenumber));
            adapter.addItem(new UserProfile(name,phonenumber));
        }
        adapter.notifyDataSetChanged();
        cursor.close();

        adapter.setOnClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Need to see here again...
                UserProfile userClick = UserClick.get(position);
                String s1 = userClick.getName();
                String s2 = userClick.getNumber();
                showAlert(s1,s2);
            }
        });


        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText, String name, String number) {
                Log.d("Text",messageText);
                Toast.makeText(MainActivity.this,"Message: "+messageText +" From : "+name,Toast.LENGTH_LONG).show();

                String msg="";
                if(SearchName(name.toLowerCase())!=null)
                    msg = name + " Contact Number is : "+ SearchName(name);
                else
                    msg = "Ooops! Contact with name : "+ name +", Not Found ! ";

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, msg, null, null);
                Toast.makeText(MainActivity.this, msg+" .SENT SUCCESSFULLY... ", Toast.LENGTH_SHORT).show();

                // Adding in DataBase.....................................................................

                SmsHistoryActivity.saveToDB(number, SearchName(name), name);

                //-----------------------------------------------------------------------------------------

            }
        });



    }// end of Oncreate...

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addContact)
        {
            // Handle the camera action
            startActivity(new Intent(MainActivity.this,AddContactActivity.class));
            adapter.notifyDataSetChanged();

        }
        else if (id == R.id.nav_seeHistory)
        {
            Toast.makeText(this, "Sqlite Database for SMS sent History", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,SmsHistoryActivity.class));
        }
        else if (id == R.id.nav_about)
        {
            // call AlertDialogBox
            alertAbout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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



    private void showAlert(final String name, final String num)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(name.toUpperCase());
        builder.setIcon(R.drawable.contact5);
        builder.setMessage("Mobile Number : "+num);
        builder.setCancelable(false);

        builder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Calling "+name+"! please wait...", Toast.LENGTH_SHORT).show();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+num));
                startActivity(callIntent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "You pressed Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String SearchName(String name1)
    {
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if(name.toLowerCase().contains(name1.toLowerCase()))
                return phonenumber;
        }
        cursor.close();
        //progressDoalog.dismiss();
        return "";
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void alertAbout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ABOUT US");
        builder.setIcon(R.drawable.about);
        builder.setCancelable(false);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Thank you!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(R.layout.alert_about_main);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}//endo fo class.
