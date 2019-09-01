package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Request;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public String[]arr ={"กรุณาเลือกสถานที่รักษา","โรงพยาบาลสัตว์ธัญญมิตร","โรงพยาบาลสัตว์บ้านฟ้า"};

    private PHPServiceAPI phpServiceAPI;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private Button search;

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://glyphographic-runwa.000webhostapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        phpServiceAPI = retrofit.create(PHPServiceAPI.class);

        search = (Button) findViewById(R.id.search_button);

        radioGroup = (RadioGroup) findViewById(R.id.bloodtype_group);

        final Spinner spin = (Spinner) findViewById(R.id.spinner_search_HPT);

        ArrayAdapter<String> arrAD = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, arr);

        arrAD.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spin.setAdapter(arrAD);

        spin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        text = adapterView.getItemAtPosition(i).toString();
//                        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = (RadioButton) findViewById(i);
//                Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                createPost();
            }
        });
    }
    private void createPost() {
        final PostData post = new PostData(1,radioButton.getText().toString(),text,"new Text");

//        Map<String, String> fields = new HashMap<>();
//        fields.put("user_id","1");
//        fields.put("Blood_type","");
//        fields.put("HPT_name","");


        Call<PostData> call = phpServiceAPI.createPost(post);

        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                PostData postData = response.body();

                String content ="";
                content += "user_id "+ postData.getUser_id()+"\n";
                content += "Blood_type "+ postData.getBlood_type()+"\n";
                content += "HPT_name "+ postData.getHPT_name()+"\n";
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onClick_button_add (View view){
        Button btn_next = (Button)findViewById(R.id.button_add);
        Intent intent = new Intent();
        startActivity(intent);
        finish();
    }

}
