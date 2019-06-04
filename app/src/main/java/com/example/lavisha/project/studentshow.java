package com.example.lavisha.project;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

public class studentshow extends AppCompatActivity {
    private Spinner mySpinner;
    // Custom Spinner adapter (ArrayAdapter<User>)
    // You can define as a private to use it in the all class
    // This is the object that is going to do the "magic"
    private ArrayAdapter adapter;
    private DBManager mDBManager;
    private TextView  tvNursery, tvJr, tvSr,tvPlay;
    private List<String> arrBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentshow);

        mDBManager = new DBManager(studentshow.this);

        tvPlay = (TextView) findViewById(R.id.tvPlay);
        tvNursery = (TextView) findViewById(R.id.tvNursery);
        tvJr = (TextView) findViewById(R.id.tvJr);
        tvSr = (TextView) findViewById(R.id.tvSr);


        arrBooks.add("Play Book");
        arrBooks.add("Nursery Book");
        arrBooks.add("Jr. Book");
        arrBooks.add("Sr. Book");


        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, arrBooks);
        mySpinner = (Spinner) findViewById(R.id.spinner);
        mySpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id)
            {
                // Here you get the current item (a User object) that is selected by its position
                String book = (String) adapter.getItem(position);

                mDBManager.insertBookCount(book);
                displayViewCOunt();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }


    public void displayViewCOunt() {
        tvPlay.setText("" + mDBManager.getBookCount("Play Book"));
        tvNursery.setText("" + mDBManager.getBookCount("Nursery Book"));
        tvJr.setText("" + mDBManager.getBookCount("Jr. Book"));
        tvSr.setText("" + mDBManager.getBookCount("Sr. Book"));
    }


}
