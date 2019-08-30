package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String url = "https://glyphographic-runwa.000webhostapp.com/api/get_post_request.php";

    String[]arr ={"กรุณาเลือกสถานที่รักษา","โรงพยาบาลปทุมเวช","โรงพยาบาลสัตว์ไอเวท"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spin = (Spinner) findViewById(R.id.spinner_search_HPT);

        ArrayAdapter<String> arrAD = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,arr);

        arrAD.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spin.setAdapter(arrAD);

        spin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView , View view, int i, long l) {
                        String text = adapterView.getItemAtPosition(i).toString();
                        Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
    }

    public void onClick_button_add (View view){
        Button btn_next = (Button)findViewById(R.id.button_add);
        Intent intent = new Intent();
        startActivity(intent);
        finish();
    }

}
