package com.kings.yatharth.yatharthresume.view;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.kings.yatharth.yatharthresume.R;
import com.kings.yatharth.yatharthresume.controller.AsyncTaskCompleteListener;
import com.kings.yatharth.yatharthresume.controller.HttpRequestAsynTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

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
                    async.execute(new String[]{});

                }
            }
        });

    }

    @Override
    public void onTaskComplete(Object result) {
        cpb.setProgress(100); // set progress to 100 or -1 to indicate complete or error state

        String username = uname.getText().toString().trim();
        String password = pass.getText().toString().trim();

    }
}
