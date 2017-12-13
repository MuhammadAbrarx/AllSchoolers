package com.abdullahsoft.allschoolers.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.abdullahsoft.allschoolers.R;


public class DynamicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        GenerateLayout();
    }

    public void GenerateLayout() {

    }
}
