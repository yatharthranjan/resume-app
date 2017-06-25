package com.kings.yatharth.yatharthresume.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.kings.yatharth.yatharthresume.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.Permission;
import java.security.Permissions;

/**
 * Created by yatharth on 24/06/17.
 */

public class OpenFile extends AppCompatActivity{

    private static final int REQUEST_CODE = 42;


    public void performFileSearch(){
        Intent in = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        in.addCategory(Intent.CATEGORY_OPENABLE);
        in.setType("image/*");
        String[] mimetypes = {"image/*", "application/pdf"};
        in.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(in,REQUEST_CODE);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        performFileSearch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri = null;
            if(data !=null){
                uri = data.getData();
                Log.i("info","URI : " + uri.toString());
                Toast.makeText(getApplicationContext(),uri.toString(),Toast.LENGTH_LONG).show();
                File f = null;
                File f2 = null;
                try {
                    f2 = new File(Environment.getExternalStorageDirectory().getPath().toString()+"/yatharth/PersonalInfo");
                    if(!f2.exists()){
                        boolean t =f2.mkdirs();
                        Log.i("Directory made",String.valueOf(t));
                    }

                    String type = getContentResolver().getType(uri);


                    if(type.equals("image/jpeg"))
                        f2 = new File(Environment.getExternalStorageDirectory().getPath().toString()+"/yatharth/PersonalInfo",uri.getLastPathSegment()+".jpeg");
                    else if(type.equals("image/png"))
                        f2 = new File(Environment.getExternalStorageDirectory().getPath().toString()+"/yatharth/PersonalInfo",uri.getLastPathSegment()+".png");
                    else if(type.equals("application/pdf"))
                        f2 = new File(Environment.getExternalStorageDirectory().getPath().toString()+"/yatharth/PersonalInfo",uri.getLastPathSegment()+".pdf");

                    if(!f2.exists()) {
                        f2.createNewFile();
                    }
                    OutputStream os = new FileOutputStream(f2);
                    IOUtils.copy(getContentResolver().openInputStream(uri), os);

                }catch (Exception e){
                    e.printStackTrace();
                }





               ResponseFuture<String> future =  Ion.with(this)
                        .load("https://morning-fortress-68069.herokuapp.com/upload/uploadfile/")
                        .setMultipartParameter("username","yatharth")
                        .setMultipartParameter("path","PersonalInfo/"+f2.getName())
                        .setMultipartFile("file", f2)
                        .asString();

                Log.i("info","request sent");


                future.setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        TextView tv = (TextView) findViewById(R.id.info);
                        tv.setText(result);
                        if(result != null)
                        Log.i("info",result);
                        else
                        Log.i("Exception",e.getMessage());
                    }
                });
            }
        }
    }
}
