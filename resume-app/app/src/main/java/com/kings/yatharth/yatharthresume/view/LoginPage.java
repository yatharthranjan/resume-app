package com.kings.yatharth.yatharthresume.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.InflateException;
import android.view.View;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.kings.yatharth.yatharthresume.R;
import com.kings.yatharth.yatharthresume.controller.AsyncTaskCompleteListener;
import com.kings.yatharth.yatharthresume.controller.HttpRequestAsynTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity implements AsyncTaskCompleteListener{

    CircularProgressButton cpb;
    MaterialEditText uname;
    MaterialEditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        cpb = (CircularProgressButton) findViewById(R.id.btnSignIn);

        Typeface fontRoboMed = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Medium.ttf");
        cpb.setTypeface(fontRoboMed);

        uname = (MaterialEditText) findViewById(R.id.username);

        pass = (MaterialEditText) findViewById(R.id.password);

        cpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pass.getText().toString().contains(",") ){
                    pass.setError("Cannot contain ,");
                }
                else if(uname.getText().toString().contains(",")){
                    uname.setError("Cannot contain ,");
                }
                else{
                    cpb.setIndeterminateProgressMode(true); // turn on indeterminate progress
                    cpb.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress
                    HttpRequestAsynTask async = new HttpRequestAsynTask(getApplicationContext(),LoginPage.this);
                    async.execute(new String[]{"https://warm-dawn-60641.herokuapp.com/user/getusers"});

                }
            }
        });
    }

    @Override
    public void onTaskComplete(Object result) {
        cpb.setProgress(100); // set progress to 100 or -1 to indicate complete or error state

        String result1 = result.toString();
        String username = uname.getText().toString().trim();
        String password = pass.getText().toString().trim();

        try {
            JSONArray users = new JSONArray(result1);

            boolean flag1 = false;
            boolean flag2 = false;
            for(int i=0; i < users.length(); i++){
                JSONObject user = users.getJSONObject(i);
                final String usrname = user.getString("username");
                String passw = user.getString("password");

                if(username.equals(usrname)) {
                    flag1 = true;
                    if (password.equals(passw)) {
                        flag2 = true;

                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                        Intent i =new Intent(LoginPage.this, Home.class);
                                        i.putExtra("username",usrname);
                                        startActivity(i);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        t.start();

                    }
                }
            }
            if(flag1 == false){
                uname.setError("User not Found!");
                cpb.setProgress(0);
            }else if(flag2 == false){
                pass.setError("Incorrect password");
                cpb.setProgress(0);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
