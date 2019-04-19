package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class viewsms extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsms);
        Intent intent = getIntent();
        Button button = (Button) findViewById(R.id.craft);
        Button inbox = (Button) findViewById(R.id.inbox);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(viewsms.this,sendmsg.class);
                startActivity(intent1);
            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(viewsms.this,inbox.class);
                startActivity(intent2);
            }
        });
    }
}
