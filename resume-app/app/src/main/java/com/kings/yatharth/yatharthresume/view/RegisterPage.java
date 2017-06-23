package com.kings.yatharth.yatharthresume.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.kings.yatharth.yatharthresume.R;
import com.kings.yatharth.yatharthresume.controller.AsyncTaskCompleteListener;
import com.kings.yatharth.yatharthresume.controller.HttpRequestAsynTask;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterPage extends AppCompatActivity implements AsyncTaskCompleteListener {

    CircularProgressButton cpb;
    MaterialEditText uname;
    MaterialEditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        cpb = (CircularProgressButton) findViewById(R.id.btnSignUp);

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
                    String username = uname.getText().toString().trim();
                    String password = pass.getText().toString().trim();
                    cpb.setIndeterminateProgressMode(true); // turn on indeterminate progress
                    cpb.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress
                    HttpRequestAsynTask async = new HttpRequestAsynTask(getApplicationContext(),RegisterPage.this);
                    async.execute(new String[]{"https://warm-dawn-60641.herokuapp.com/user/createuser?username="+ username +"&password="+password});
                }
            }
        });
    }

    @Override
    public void onTaskComplete(Object result) {
        String res = result.toString();
        cpb.setProgress(100);
        if(res.contains("Already Exists")){
            uname.setError("Already Exists");
            cpb.setProgress(0);
        }else if(res.contains("created")){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        Intent i =new Intent(RegisterPage.this, Home.class);
                        i.putExtra("username",uname.getText().toString().trim());
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
