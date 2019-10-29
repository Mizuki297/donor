package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.UserModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_userData extends AppCompatActivity {

    private PHPServiceAPI phpServiceAPI;

    private EditText name,lastname,tel;
    private TextView lineid;
    private Button ok;
    private ImageView back;

    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_layout);

        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        session = new Session(getApplicationContext());
        System.out.println(session.getUserId());

        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.lastname);
        tel = (EditText) findViewById(R.id.tel);
        lineid = (TextView) findViewById(R.id.lineid);
        ok = (Button) findViewById(R.id.ok);
        back = (ImageView)findViewById(R.id.back_icon);


        getUser(session.getUserId());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = name.getText().toString();
                String user_s = lastname.getText().toString();
                String user_t = tel.getText().toString();
                String user_l = lineid.getText().toString();

                update_user(session.getUserId(),user_name,user_s,user_t,user_l);

            }
        });
    }
    private void  getUser(String user_id){
        Call<UserModel> cal1 = phpServiceAPI.getUser(user_id);

        cal1.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userInfo = response.body();
                name.setText(userInfo.getUser_name());
                lastname.setText(userInfo.getUser_s_name());
                tel.setText(userInfo.getUser_tel());
                lineid.setText(userInfo.getUser_line_id());
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }
    private void update_user(String user_id, String user_name, String user_s_name, String user_tal, String user_line_id){
        Call <Void> call = phpServiceAPI.update_user(user_id, user_name, user_s_name, user_tal, user_line_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
