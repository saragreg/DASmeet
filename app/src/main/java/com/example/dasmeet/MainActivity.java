package com.example.dasmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    String usuLog = "sara";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragInfo=  getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
        Bundle bundle=new Bundle();
        bundle.putString("usu",usuLog);
        fragInfo.setArguments(bundle);


    }
}