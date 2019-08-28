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

    private String url = "https://glyphographic-runwa.000webhostapp.com/api/get_post_hpt.php";

    public List<String> arrList = new ArrayList<String>();
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spin = (Spinner) findViewById(R.id.spinner_search_HPT);
            arrList.add("กรุณาเลือกสถานที่รักษา");
            arrList.add("โรงพยาบาลปทุมเวช");
            arrList.add("โรงพยาบาลสัตว์ไอเวท");

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
