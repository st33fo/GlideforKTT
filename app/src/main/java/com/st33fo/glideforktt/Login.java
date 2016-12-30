package com.st33fo.glideforktt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Login extends AppCompatActivity {
    private Button loginButton;
    private EditText username;
    private EditText password;
    private Connection.Response res = null;
    private String sendid;
    private String user;
    private String pass;
    static String sessionId;
    private static final String TAG = "MyActivity";
    private Document doc1;
    private int counter = 5;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());
    }

    private class loginKTT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user = username.getText().toString();
            pass = password.getText().toString();

        }



        @Override
        protected Void doInBackground(Void... params) {
            this.publishProgress();
            try {
                res = Jsoup.connect("http://www.kanyetothe.com/forum/index.php?action=login2")
                        .data("user", user, "passwrd", pass)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                        .method(Connection.Method.POST)
                        .execute();

                doc1 = res.parse();
                sessionId=res.cookie("PHPSESSID");


                Log.d(TAG, "Lawd");


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog = ProgressDialog.show(Login.this,"Loading","Loading",true);

            Login.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.show();
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



            Elements failLogin = doc1.select("p[class=error]");



            if (failLogin.toString().contains("Password incorrect") && counter != 0) {
                Toast.makeText(Login.this, "That password is incorrect", Toast.LENGTH_SHORT).show();
                new loginKTT().onPreExecute();
                counter--;
            } else if (failLogin.toString().contains("That username does not exist.") && counter != 0) {
                Toast.makeText(Login.this, "That username does not exist", Toast.LENGTH_SHORT).show();
                new loginKTT().onPreExecute();
                counter--;
            } else if (failLogin.toString().contains("You need to fill in a username")) {
                Toast.makeText(Login.this, "You need to fill in a username", Toast.LENGTH_SHORT).show();
                new loginKTT().onPreExecute();

            } else if (failLogin.toString().contains("You didn't enter your password.")) {
                Toast.makeText(Login.this, "You didn't enter your password.", Toast.LENGTH_SHORT).show();
                new loginKTT().onPreExecute();

            } else if (counter == 0) {
                loginButton.setEnabled(false);
                Toast.makeText(Login.this, "Login Disabled for 5 mins", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginButton.setEnabled(true);

                        counter = 5;

                    }
                }, 300000);


            } else {

               SecuredSharePreference.setCookies(Login.this,sessionId);
                Intent i = new Intent(Login.this, MainActivity.class);
                Login.this.finish();
                startActivity(i);

            }
            progressDialog.dismiss();


        }
    }
    public void loginToAccount(View v){
        new loginKTT().execute();
    }
    public static String getSessionID(){
        return sessionId;
    }
}
