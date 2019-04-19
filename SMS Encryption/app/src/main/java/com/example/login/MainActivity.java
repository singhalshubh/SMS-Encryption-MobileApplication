package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.login.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static android.support.v4.content.ContextCompat.startActivity;
/*
public class MainActivity extends AppCompatActivity {
    EditText user1, pass1;
    Button login;
    String pass, user;
    String link = "http://10.0.2.2/Login/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user1 = (EditText) findViewById(R.id.username);
        Log.d("Hey", "Checking");

        pass1 = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, viewsms.class);
                startActivity(intent);
            }

        });

    }
}
*/
public class MainActivity extends AppCompatActivity {
    EditText user1, pass1;
    Button login,reset;
    String pass, user;
    String link = "http://192.168.43.226/Login/login.php";
    String link1 = "http://192.168.43.226/Login/login1.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user1 = (EditText) findViewById(R.id.username);
        Log.d("Hey", "Checking");

        pass1 = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = user1.getText().toString();
                if(user.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill the phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    new RequestTask1(MainActivity.this).execute(link1, user, null);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = user1.getText().toString();
                pass = pass1.getText().toString();
                if (user.isEmpty() || pass.isEmpty() || user.length() <10 || pass.length() < 8) {
                    Toast.makeText(MainActivity.this, "Please fill the credentials. Password should be minimum 8 characters", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("PASS", "ENTERING THREAD");
                    new RequestTask(MainActivity.this).execute(link, user, pass);
                }
            }
        });

    }
}


     class RequestTask extends AsyncTask<String, String, String> {
         private Context context;

         public RequestTask(Context context) {
             this.context = context;
         }

         protected void onPreExecute() {
             super.onPreExecute();
             //Do anything with response..
         }

         URLConnection conn;
         OutputStreamWriter wr;

         @Override
         protected String doInBackground(String... params) {
             String link = params[0];
             String user = params[1];
             String pass = params[2];
             JSONObject postDataParams = new JSONObject();
             //to decalre the url
             URL url = null;
             try {
                 url = new URL(link);
             } catch (MalformedURLException e1) {
                 e1.printStackTrace();
                 Log.d("Error", "URL WRONG");

             }
             //to decalre the JSON
             try {
                 postDataParams.put("phoneno", user);
                 postDataParams.put("password", pass);
             } catch (JSONException e1) {
                 e1.printStackTrace();
                 Log.d("FAIL", "FAIL TO OBTAIN DATAGRAMS");
             }
             Log.d("PASS", postDataParams.toString());
             //Connection and sending
             HttpURLConnection conn = null;
             try {
                 conn = (HttpURLConnection) url.openConnection();
                 Log.d("PASS", "connection ok");
             } catch (IOException e1) {
                 e1.printStackTrace();
             }
             try {
                 conn.setRequestMethod("POST");
             } catch (ProtocolException e1) {
                 e1.printStackTrace();
             }
             conn.setDoInput(true);
             conn.setDoOutput(true);
             Log.d("PASS", "PASS");
             OutputStream os = null;
             try {
                 os = conn.getOutputStream();
                 Log.d("PASS", "PASSos");
             } catch (IOException e1) {
                 e1.printStackTrace();
             }
             BufferedWriter writer = null;
             Log.d("PASS", "PASS");
             /////BECOMING MAD.................................
             try {
                 writer = new BufferedWriter(
                         new OutputStreamWriter(os, "UTF-8"));
                 Log.d("PASS", "PASS AFTER INITILASING WRITE");
             } catch (UnsupportedEncodingException e1) {
                 Log.d("FAIL", "FAIL BEFORE INITILASING WRITE");
                 e1.printStackTrace();
             }
             try {
                 writer.write(getPostDataString(postDataParams));
                 Log.d("PASS", "DATA POST PASS");
             } catch (Exception e1) {
                 e1.printStackTrace();
                 Log.d("FAIL", "FAIL POST PASS");
             }
             try {
                 writer.flush();
                 writer.close();
                 os.close();
             } catch (IOException e1) {
                 e1.printStackTrace();
             }
             //TESTING
             int responseCode = 0;
             InputStream is1 = null;
             try {
                 is1 = conn.getInputStream();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             try {
                 responseCode = conn.getResponseCode();
             } catch (IOException e1) {
                 e1.printStackTrace();
             }
             //reading the data
             BufferedReader in = null;
             try {
                 in = new BufferedReader(new
                         InputStreamReader(is1, "UTF-8"));
             } catch (Exception e) {
                 e.printStackTrace();
             }

             StringBuffer sb = new StringBuffer("");
             String line = "";
             try {
                 line = String.valueOf(in.read());
             } catch (IOException e) {
                 e.printStackTrace();
             }
             sb.append(line);

             Log.d("LINE", line);
             try {
                 in.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }

             //remove if remove the commnets from above
             return line;
         }

         protected void onPostExecute(String result) {
             if(Integer.parseInt(result) != 48) {
                 Intent intent = new Intent(context, viewsms.class);
                 Log.d("Toast is", result);
                 Log.d("Toast is", "Already SEEN");
                 context.startActivity(intent);
             }
             else {
                 Toast.makeText(context, "Please fill the correct credentials", Toast.LENGTH_SHORT).show();
             }
         }

         public String getPostDataString(JSONObject params) throws Exception {

             StringBuilder result = new StringBuilder();
             boolean first = true;

             Iterator<String> itr = params.keys();

             while(itr.hasNext()){

                 String key= itr.next();
                 Object value = params.get(key);

                 if (first)
                     first = false;
                 else
                     result.append("&");

                 result.append(URLEncoder.encode(key, "UTF-8"));
                 result.append("=");
                 result.append(URLEncoder.encode(value.toString(), "UTF-8"));

             }
             return result.toString();
         }

         //Do anything with response..

     }

/////////////////FOR THE FOROT PASSWORD PURPOSES
         class RequestTask1 extends AsyncTask<String, String, String> {
    private Context context;

    public RequestTask1(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        //Do anything with response..
    }

    URLConnection conn;
    OutputStreamWriter wr;

    @Override
    protected String doInBackground(String... params) {
        String link = params[0];
        String user = params[1];
        JSONObject postDataParams = new JSONObject();
        //to decalre the url
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            Log.d("Error", "URL WRONG");

        }
        //to decalre the JSON
        try {
            postDataParams.put("phoneno", user);
        } catch (JSONException e1) {
            e1.printStackTrace();
            Log.d("FAIL", "FAIL TO OBTAIN DATAGRAMS");
        }
        Log.d("PASS", postDataParams.toString());
        //Connection and sending
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            Log.d("PASS", "connection ok");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        Log.d("PASS", "PASS");
        OutputStream os = null;
        try {
            os = conn.getOutputStream();
            Log.d("PASS", "PASSos");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        BufferedWriter writer = null;
        Log.d("PASS", "PASS");
        /////BECOMING MAD.................................
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            Log.d("PASS", "PASS AFTER INITILASING WRITE");
        } catch (UnsupportedEncodingException e1) {
            Log.d("FAIL", "FAIL BEFORE INITILASING WRITE");
            e1.printStackTrace();
        }
        try {
            writer.write(getPostDataString(postDataParams));
            Toast.makeText(context, "You can set your password now", Toast.LENGTH_SHORT).show();
            Log.d("PASS", "DATA POST PASS");
        } catch (Exception e1) {
            e1.printStackTrace();
            Log.d("FAIL", "FAIL POST PASS");
        }
        try {
            writer.flush();
            writer.close();
            os.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //TESTING
        int responseCode = 0;
        InputStream is1 = null;
        try {
            is1 = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            responseCode = conn.getResponseCode();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //reading the data
        BufferedReader in = null;
        try {
            in = new BufferedReader(new
                    InputStreamReader(is1, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer("");
        String line = "";
        try {
            line = String.valueOf(in.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sb.append(line);

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //remove if remove the commnets from above
        return line;
    }
    protected void onPostExecute(String result) {
        Log.d("Toast is", result);
        Log.d("Toast is", "Already SEEN");
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    //Do anything with response..

}



