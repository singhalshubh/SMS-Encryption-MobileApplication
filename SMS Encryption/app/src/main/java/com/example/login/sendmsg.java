package com.example.login;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class sendmsg extends AppCompatActivity {
    byte[] key = new byte[16];
    byte[] iv = new byte[12];
    Button button,access,info;
    EditText editText, editText2;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    int clickCounter=0;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmsg);
        final Intent intent = getIntent();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        ListView lv = (ListView) findViewById(R.id.messageList);
        lv.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(sendmsg.this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(sendmsg.this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(sendmsg.this, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                ActivityCompat.requestPermissions(sendmsg.this, new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }
        access = (Button) findViewById(R.id.access);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        final String[] sms1 = {null};
        editText2 = (EditText) findViewById(R.id.editText2);
        info = (Button) findViewById(R.id.info);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString();
                String[] number1 = new String[1000];
                int k = 0;
                int counter = 0;
                int init = 0;
                for(int i=0;i<number.length()-1;i++) {
                    if(number.charAt(i) == ',') {
                        number1[k++] =  number.substring(init,i);
                        init = i+1;
                    }
                }
                Log.d("Phone",number1[0]);
                Log.d("Phone",number1[k-1]);
                Log.d("Sucess" , "Done with pay");
                String sms = editText2.getText().toString();
                sms1[0] = Encr(sms);
                if (number.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Enter Phone no please",
                            Toast.LENGTH_LONG).show();
                } else {

                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        int u=0;
                        for(int i=0;i<k;i++) {
                            smsManager.sendTextMessage(number1[i], null, sms1[0], null, null);
                        }
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                        listItems.add(number + ":" + sms + "--Enc--"+ sms1[0]);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "SMS failed, please try again later!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

        });


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String h = editText2.getText().toString();
                if(h.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter SMS",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent5 = new Intent(sendmsg.this, enc.class);
                    intent5.putExtra("sms",h);
                    startActivity(intent5);
                }
            }
        });
        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(sendmsg.this,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(sendmsg.this, Manifest.permission.READ_CONTACTS)) {
                        ActivityCompat.requestPermissions(sendmsg.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                    } else {
                        ActivityCompat.requestPermissions(sendmsg.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                    }
                }
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });
    }

        public void onActivityResult(int reqCode, int resultCode, Intent data) {
            super.onActivityResult(reqCode, resultCode, data);
            String pnumber = null;
            try {
                switch (reqCode) {
                    case (100):
                        if (resultCode == Activity.RESULT_OK) {
                            Uri contactData = data.getData();
                            Cursor cur = getContentResolver().query(contactData, null, null, null, null);
                            ContentResolver contentResolver = getContentResolver();
                            if(cur.moveToFirst()) {
                                String id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                                Cursor phoneCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?", new String[]{id}, null);
                                    Log.d("ejfvewhfvwevhfweghfwe", id);
                                while (phoneCur.moveToNext()) {
                                    pnumber = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                }
                                editText.setText(editText.getText()+ pnumber + ",");
                                phoneCur.close();
                            }
                                cur.close();
                            }
                        break;
                        }
                }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.e("Nope", e.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error :: ", e.toString());
            }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    if(ContextCompat.checkSelfPermission(sendmsg.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public String Encr(String plain1) {
        byte[] plainText = plain1.getBytes();
        String cipher1 = null;
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        //NEVER REUSE THIS IV WITH SAME KEY
        secureRandom.nextBytes(iv);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv); //128 bit auth tag length
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            byte[] cipherText = cipher.doFinal(plainText);
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
            byteBuffer.putInt(iv.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);
            byte[] cipherMessage = byteBuffer.array();
            cipher1 = cipherMessage.toString();
            for(int i=0;i<plain1.length();i++) {
                int random = new Random().nextInt(25) + 0;
                cipher1+=(char)random;
            }
            String var="",var1="";
            int counter = -1;

            for(int i=1;counter<plain1.length()-1;i+=2) {
                counter++;
                for(int j=0;j<i;j++) {
                    var = var + cipher1.charAt(j);
                }

                for(int j=i;j<cipher1.length();j++) {
                    var1 = var1 + cipher1.charAt(j);
                }

                cipher1 = "";
                int c = plain1.charAt(counter);
                c = c+2;
                cipher1 = var + (char)c + var1;
                System.out.println(cipher1);
                var = "";
                var1 = "";

            }
            int c = plain1.length() + 48;
            cipher1+=(char) c;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return cipher1;
    }


}
