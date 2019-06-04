package com.example.lavisha.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TellActivity extends AppCompatActivity {
private EditText ed;
private Button bt;
private Button bt2;//why you defined this?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell);

        ed=(EditText)findViewById(R.id.editText);
        bt=(Button)findViewById(R.id.b);
        bt2=(Button)findViewById(R.id.button2);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(ed.getText().toString());
            }
        });
    }
    public void check(String ed){
        if((ed.equals("1122"))){
        Intent intent=new Intent(TellActivity.this, TeacherShow.class);
        startActivity(intent);


    }else{
        Toast.makeText(getApplicationContext(),"Wrong key", Toast.LENGTH_SHORT).show();
    }
}

    public void cl(View view) {
        Intent intent=new Intent(TellActivity.this,studentshow.class);
        startActivity(intent);
    }
}
