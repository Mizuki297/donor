package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.NumCatModel;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayDataCat extends AppCompatActivity {

    private PHPServiceAPI phpServiceAPI;

    public TextView numCat_select, user_coin;

    public ImageView back_icon;

    public Button pay_button;

    public String numCat = "0";

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_data_cat);

        session = new Session(getApplicationContext());
        System.out.println(session.getUserId());
        // Call get hospital
        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        final String getHPT_name = getIntent().getExtras().getString("HPT_name");
        final String getBlood_type = getIntent().getExtras().getString("blood_type");

        System.out.println(getHPT_name);
        System.out.println(getBlood_type);

        getCoin(); //แสดงเงินของ user

        user_coin = (TextView) findViewById(R.id.menu_coin);

        numCat_select = (TextView) findViewById(R.id.textView8);

        numCat(session.getUserId(),getHPT_name,getBlood_type);  // numCat  คือการแสดงผลการแมทย์ว่ามีเท่าไหร่

        back_icon = (ImageView) findViewById(R.id.imageView2);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pay_button = (Button) findViewById(R.id.button);
        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_coin.getText().toString().length() <= 1){
                    Toast.makeText(getApplicationContext(),"ยอดเงินไม่เพียงพอ",Toast.LENGTH_SHORT).show();
                }else{
                    if(numCat.equals("0")){
                        Toast.makeText(getApplicationContext(),"ไม่มีคนที่พล้อมบริจาคจะชำระเงินทำไม",Toast.LENGTH_SHORT).show();
                    }else {
                        updateCoin(session.getUserId());
                        onNextSelect(getBlood_type, getHPT_name);  //เปลี่ยนหน้าถัดไป
                    }
                }
            }
        });
    }
    public void onNextSelect(String blood_type,String HPT_name) {
        Intent intent = new Intent(this, CatGridAdapter.class);
        intent.putExtra("blood_type",blood_type);
        intent.putExtra("HPT_name",HPT_name);
        startActivity(intent);
    }
    private void updateCoin(String user_id){
        Call<Void> call = phpServiceAPI.updateCoin(user_id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    return;
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
        Toast.makeText(getApplicationContext(),"ชำระเงินสำเร็จ",Toast.LENGTH_SHORT).show();
    }
    private void numCat(String UserID,String getHPT_name,String getBlood_type) {
        Call<NumCatModel> call = phpServiceAPI.numCat(UserID,getHPT_name,getBlood_type);
        //รอการตอบกลับจาก API
        call.enqueue(new Callback<NumCatModel>() {
            @Override
            public void onResponse(Call<NumCatModel> call, Response<NumCatModel> response) {

                NumCatModel numCatInfo = response.body();
                System.out.println(numCatInfo.getNumCat());
                numCat = numCatInfo.getNumCat();
                numCat_select.setText(numCatInfo.getNumCat());
            }
            @Override
            public void onFailure(Call<NumCatModel> call, Throwable t) {

                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
    private void getCoin() {
        Call<UserModel> call = phpServiceAPI.getUser(session.getUserId());

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                UserModel coinInfo = response.body();
                user_coin.setText(coinInfo.getMoney_coin());

            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}
