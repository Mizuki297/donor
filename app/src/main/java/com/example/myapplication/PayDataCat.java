package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayDataCat extends AppCompatActivity {

    private PHPServiceAPI phpServiceAPI;

    public TextView numCat_select, user_coin;

    public ImageView back_icon;

    public Button pay_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_data_cat);

        // Call get hospital
        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        final String getUserID = getIntent().getExtras().getString("user_id");
        String getHPT_name = getIntent().getExtras().getString("HPT_name");
        final String getBlood_type = getIntent().getExtras().getString("blood_type");

        System.out.println(getUserID);
        System.out.println(getHPT_name);
        System.out.println(getBlood_type);

        getCoin();

        user_coin = (TextView) findViewById(R.id.textView4);
//        if (getCoin != 0 ){
//        coin.setText(getCoin);
//        }else{coin.setText(0);}

        numCat_select = (TextView) findViewById(R.id.textView8);

        numCat(getUserID,getHPT_name,getBlood_type);

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
//                updateCoin(getUserID);
                onNextSelect(getBlood_type);
            }
        });
    }
    public void onNextSelect(String blood_type) {
        Intent intent = new Intent(this,cat_list.class);
        intent.putExtra("blood_type",blood_type);
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
    private void numCat(String getUserID,String getHPT_name,String getBlood_type) {
        Call<List<NumCat>> call = phpServiceAPI.numCat(getUserID,getHPT_name,getBlood_type);
        //รอการตอบกลับจาก API
        call.enqueue(new Callback<List<NumCat>>() {
            @Override
            public void onResponse(Call<List<NumCat>> call, Response<List<NumCat>> response) {
                if (!response.isSuccessful()) {
                    // textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<NumCat> getList = response.body();

                for (NumCat post : getList) {
                    String content = "";
                    content += "numCat: " + post.getNumCat() + "\n";

                    System.out.println(content);
                    numCat_select.setText(post.getNumCat());
                }
            }
            @Override
            public void onFailure(Call<List<NumCat>> call, Throwable t) {

                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
    private void getCoin() {
        Call<List<User>> call = phpServiceAPI.getUser("1");

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    // textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<User> getList = response.body();

                for (User post: getList) {
                    String content = "";
                    content += "money_coin: " + post.getMoney_coin() + "\n";

                    System.out.println(content);
                    user_coin.setText(post.getMoney_coin());
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}
