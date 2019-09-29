package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatDetial extends AppCompatActivity {

    private ImageButton backwordPageBtn;

    private TextView cat_name, cat_type, cat_blood, cat_age, cat_weight, cat_Health_check_date, cat_Latest_donation, user_line_id;

    private View line;


    private ImageView Tell, photo;


    private String tel;

    private String lineUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_detial);


        Intent intent = getIntent();
        String cat_id = intent.getStringExtra(cat_list.EXTRA_CAT_ID);

        cat_name = (TextView) findViewById(R.id.cat_name);
        cat_type = (TextView) findViewById(R.id.cat_type);
        cat_blood = (TextView) findViewById(R.id.cat_blood);
        cat_age = (TextView) findViewById(R.id.cat_age);
        cat_weight = (TextView) findViewById(R.id.cat_weight);
        cat_Health_check_date = (TextView) findViewById(R.id.cat_Health_check);
        cat_Latest_donation = (TextView) findViewById(R.id.Cat_Latest_donation);
        user_line_id = (TextView) findViewById(R.id.Line);
        line = (View) findViewById(R.id.view9);
        Tell = (ImageView) findViewById(R.id.Tell);
        photo = (ImageView) findViewById(R.id.Photocat);

        PHPServiceAPI phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        Call<CatModel> call = phpServiceAPI.getCatDetail(cat_id);

        call.enqueue(new Callback<CatModel>() {
            @Override
            public void onResponse(Call<CatModel> call, Response<CatModel> response) {
                // data response = {}
                CatModel catDetail = response.body();

                // แสดงข้อมูลที่ดึงมาได้
                cat_name.setText(catDetail.getCat_name());
                cat_type.setText(catDetail.getCat_type());
                cat_blood.setText(catDetail.getBlood_type());
                cat_age.setText(catDetail.getCat_bd());
                cat_weight.setText(catDetail.getCat_weight());
                cat_Health_check_date.setText(catDetail.getHealth_check_date());
                cat_Latest_donation.setText(catDetail.getLatest_donation());
                user_line_id.setText(catDetail.getUser_line_id());
                tel = catDetail.getUser_tel();

                lineUserId = catDetail.getUser_line_id();

                // แสดงรูปที่ดึงมาได้
                if (catDetail.getUrl_cat() != null && catDetail.getUrl_cat().length() > 0) {
                    Picasso.get().load(catDetail.getUrl_cat()).placeholder(R.drawable.placeholder_img).into(photo);
                } else {
                    Toast.makeText(CatDetial.this, "Empty Image Url", Toast.LENGTH_LONG).show();
                    Picasso.get().load(R.drawable.placeholder_img).into(photo);
                }

            }

            @Override
            public void onFailure(Call<CatModel> call, Throwable t) {
                Toast.makeText(CatDetial.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Tell.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tel:" + tel));
                startActivity(intent);
            }
        });

        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLineApplication(lineUserId);
            }
        });

        backwordPageBtn = (ImageButton) findViewById(R.id.img_btn_back);

        backwordPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackwordPage();
            }
        });

    }

    // กลับไปยังหน้า cat_list
    private void setBackwordPage() {
        onBackPressed();
    }

    // รับ user line id เข้ามาเพื่อสั่งเปิด line
    private void openLineApplication(String user_line_id) {
        System.out.println(user_line_id);
        Uri uri = Uri.parse("http://line.me/ti/p/~" + user_line_id);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
