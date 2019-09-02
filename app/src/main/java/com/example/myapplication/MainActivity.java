package com.example.myapplication;
//ส่วนการเรียกอุปกรณ์ต่างๆ
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Request;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//ส่วนคลาสหลัก
public class MainActivity extends AppCompatActivity {

    //ประกาศค่าต่างๆที่ต้องการใช้

    private ArrayList<String> hospitalList = new ArrayList<>();

    private PHPServiceAPI phpServiceAPI;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private Button search;

    private String selectHospital = "";

    private Spinner spinnerHospital;

    // ส่วนหลัก
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //แก้ไขค่าต่างๆ
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

        phpServiceAPI = retrofit.create(PHPServiceAPI.class);

        // Call get hospital
        phpServiceAPI = retrofit.create(PHPServiceAPI.class);

        search = (Button) findViewById(R.id.search_button);

        radioGroup = (RadioGroup) findViewById(R.id.bloodtype_group);

        hospitalList.add("กรุณาเลือกสถานที่รักษา");
        // Get Hospital list from api
        getHospitalList();

        //dropdown
        spinnerHospital = (Spinner) findViewById(R.id.spinner_search_HPT);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, hospitalList);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinnerHospital.setAdapter(adapter);

        //เเมื่อกดเลือก
        spinnerHospital.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectHospital = adapterView.getSelectedItem().toString();
                        System.out.println(selectHospital);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );

        //เมื่อกดเลือก
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = (RadioButton) findViewById(i);
//                Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        //กดปุ่มค้นหา
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                createPost();
            }
        });
    }//สร้างต่วส่งข้อมูลไป api
    private void createPost() {
        final PostData post = new PostData(""+selectHospital,""+radioButton.getText().toString(),1);

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

                System.out.println(content);
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

//เปลี่ยนหน้า
    public void onClick_button_add (View view){
        Button btn_next = (Button)findViewById(R.id.button_add);
        Intent intent = new Intent();
        startActivity(intent);
        finish();
    }
//ดึงข้อมูลจาก api
    private void getHospitalList() {
        Call<List<Hospital>> call = phpServiceAPI.getHospital();

        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                if (!response.isSuccessful()) {
                    // textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Hospital> getList = response.body();

                for (Hospital post: getList) {
                    String content = "";
                    content += "HPT_id: " + post.getHPT_id() + "\n";
                    content += "HPT_name: " + post.getHPT_name() + "\n";
                    content += "HPT_location: " + post.getHPT_location() + "\n";

                    System.out.println(content);

                    hospitalList.add(post.getHPT_name());
                }

            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}
