package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.UserModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Money extends AppCompatActivity {

    private TextView add_coin;

    private ImageView back;

    private EditText inCoin;

    private Button button;

    private int coin = 0;

    private PHPServiceAPI phpServiceAPI;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__money);

        session = new Session(getApplicationContext());
        System.out.println(session.getUserId());

        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);



        add_coin = (TextView) findViewById(R.id.Coin);

        back = (ImageView) findViewById(R.id.imageView6);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inCoin = (EditText) findViewById(R.id.addCoin);

        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(inCoin.getText().toString())){
                    coin = Integer.parseInt(inCoin.getText().toString());
                }
                add_coin(session.getUserId(),coin);
            }
        });
        getUser(session.getUserId());

    }
    private void getUser(String user_id) {
        Call<UserModel> call = phpServiceAPI.getUser(user_id);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userInfo = response.body();
                System.out.println(userInfo.getMoney_coin());
                add_coin.setText(userInfo.getMoney_coin());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void add_coin(String user_id,int coin){
        Call<UserModel> call = phpServiceAPI.add_coin(user_id, coin);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
        Toast.makeText(getApplicationContext(),"เติมเงินสำเร็จ",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
