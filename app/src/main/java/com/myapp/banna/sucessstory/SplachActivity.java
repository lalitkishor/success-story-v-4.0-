package com.myapp.banna.sucessstory;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ViewFlipper;


public class SplachActivity extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper viewFlipper;
    Button next, previous;
    // private final int SPLASH_DISPLAY_LENGTH = 1000;
    public   static  Intent getIntent(Context context , boolean path){
        Intent i = new Intent(context,LoginActivity.class);
        i.putExtra("splach",path);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splach);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        if(v==next){
            if(viewFlipper.getRootView()==next.getRootView()){
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
            }else{
                viewFlipper.showNext();
            }

        }

    }

   }

