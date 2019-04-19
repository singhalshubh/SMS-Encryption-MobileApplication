package com.example.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
   import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

    public class inbox extends Activity {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_inbox);
                ListView lv = (ListView) findViewById(R.id.messageList);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,fetchInbox());
                lv.setAdapter(adapter);
                if (ContextCompat.checkSelfPermission(inbox.this,
                        Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(inbox.this, Manifest.permission.READ_SMS)) {
                        ActivityCompat.requestPermissions(inbox.this, new String[]{Manifest.permission.READ_SMS}, 1);
                    } else {
                        ActivityCompat.requestPermissions(inbox.this, new String[]{Manifest.permission.READ_SMS}, 1);
                    }
                }

            }
    public  ArrayList<String> fetchInbox(){
                ArrayList<String> sms = new ArrayList<String>();

                Uri uri = Uri.parse("content://sms/inbox");
                Cursor cursor = getContentResolver().query(uri,new String[]{"address","body"},null,null,null);
                if(cursor.moveToFirst()) {
                    while (cursor.moveToNext()) {
                        String address = cursor.getString(0);
                        String body = cursor.getString(1);
                        sms.add(address + ":" + dec(body));
                    }
                }
            return sms;
    }

    public String dec(String cipher1) {
        int counter = -1;
        int lenp = cipher1.charAt(cipher1.length()-1) -48;

        String plain1 = "";
        for(int i=1;counter<lenp-1;i+=2) {
                char y = cipher1.charAt(i);
                int g = y;
                g = g-2;
                plain1+=(char)g;
                counter++;
            }
        return plain1;
        }
    }