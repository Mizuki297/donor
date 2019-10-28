package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.LoginModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Re_pass extends AppCompatActivity {

    private PHPServiceAPI phpServiceAPI;
    private Session session;

    private EditText old_pass,new_pass,con_pass;
    private Button button;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pass_layout);

        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        session = new Session(getApplicationContext());
        System.out.println(session.getUserId());

        old_pass = (EditText) findViewById(R.id.editText6);
        new_pass = (EditText) findViewById(R.id.editText5);
        con_pass = (EditText) findViewById(R.id.editText7);

        button = (Button) findViewById(R.id.button2);
        back = (ImageView) findViewById(R.id.imageView3);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (old_pass.getText().toString().matches("") || new_pass.getText().toString().matches("") || con_pass.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                }else {
                    if (new_pass.getText().toString().matches(con_pass.getText().toString())){
                        Update_password(old_pass.getText().toString(),new_pass.getText().toString());
                    }else{
                        Toast.makeText(getApplicationContext(),"password ไม่ตรงกัน",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    private void Update_password(String old_password,String new_password){
        Call<LoginModel> call = phpServiceAPI.update_pass(session.getUserId(),old_password,new_password);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel updateInfo = response.body();
                if (updateInfo.status == 0){
                    Toast.makeText(getApplicationContext(),updateInfo.description,Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    Toast.makeText(getApplicationContext(),updateInfo.description,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

            }
        });
    }

}
