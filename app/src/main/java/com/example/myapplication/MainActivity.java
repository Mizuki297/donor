package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<String> arrList = new ArrayList<>();
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spin = (Spinner) findViewById(R.id.spinner_search_HPT);
        arrList.add("สถานที่รักษา");
        //เพื่มข้อมูลลงอาเรย์
        ArrayAdapter<String> arrAD = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,arrList);
        spin.setAdapter(arrAD);
    }
    public void onClick_button_add (View view){
        Button btn_next = (Button)findViewById(R.id.button_add);
        Intent intent = new Intent();
        startActivity(intent);
        finish();
    }

}
