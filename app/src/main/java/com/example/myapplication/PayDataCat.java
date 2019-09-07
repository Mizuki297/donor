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

        Gson gson = new GsonBuilder().serializeNulls().create();

        // log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header", "xyz")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        //ประกาศค่า url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://glyphographic-runwa.000webhostapp.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Call get hospital
        phpServiceAPI = retrofit.create(PHPServiceAPI.class);

        String getCoin = getIntent().getExtras().getString("coin");
        int getUserID = getIntent().getExtras().getInt("user_id");
        String getHPT_name = getIntent().getExtras().getString("HPT_name");
        String getBlood_type = getIntent().getExtras().getString("Blood_type");

        System.out.println(getUserID);
        System.out.println(getHPT_name);
        System.out.println(getBlood_type);

        user_coin = (TextView) findViewById(R.id.textView4);
        user_coin.setText(getCoin);
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
//                onClickPay();

            }
        });
    }
//    public void onClickPay() {
//        Intent intent = new Intent();
//        startActivity(intent);
//    }

    private void numCat(int getUserID,String getHPT_name,String getBlood_type) {
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
        //แสดงกรณีบันทึกสำเร็จ
//        Toast.makeText(getApplicationContext(),"สำเร็จ",Toast.LENGTH_SHORT).show();
    }

}
