package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.CatModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Editcat_info extends AppCompatActivity {

    private PHPServiceAPI phpServiceAPI;

    private EditText name_cat,name_type, ago1, nam1, date1, blooddate,groupblood;
    private Button ok;
    private ImageView image,menu;
//    private Uri uri;
    private String cat_name,cat_type,blood_type,cat_bd,cat_weight,health_check_date,latest_donation,blood_type2 = "";
    private ImageView imageblack;
//    private int GALLERY_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcat_info);

        Intent intent = getIntent();
        final String cat_id = intent.getStringExtra(User_Cat_list.USER_CAT_ID);

        name_cat = (EditText) findViewById(R.id.name_cat);
        name_type = (EditText) findViewById(R.id.name_type);
        ago1 = (EditText) findViewById(R.id.ago1);
        nam1 = (EditText) findViewById(R.id.nam1);
        date1 = (EditText) findViewById(R.id.date1);
        blooddate = (EditText) findViewById(R.id.blooddaet);
        groupblood= (EditText) findViewById(R.id.groupblood);
        ok = (Button) findViewById(R.id.ok);
        image = (ImageView) findViewById(R.id.image);
        imageblack = (ImageView) findViewById(R.id.imageblack);
        menu = (ImageView) findViewById(R.id.menu);


        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        Call<CatModel> call = phpServiceAPI.getCatDetail(cat_id);

        call.enqueue(new Callback<CatModel>() {
            @Override
            public void onResponse(Call<CatModel> call, Response<CatModel> response) {
                CatModel catDetail = response.body();

                name_cat.setText(catDetail.getCat_name());
                name_type.setText(catDetail.getCat_type());
                ago1.setText(catDetail.getCat_bd());
                nam1.setText(catDetail.getCat_weight());
                date1.setText(catDetail.getHealth_check_date());
                blooddate.setText(catDetail.getLatest_donation());
                groupblood.setText(catDetail.getBlood_type());

                 if (catDetail.getUrl_cat() !=null && catDetail.getUrl_cat().length()>0){
                    Picasso.get().load(catDetail.getUrl_cat()).placeholder(R.drawable.placeholder_img).into(image);
                }else {
                    Toast.makeText(Editcat_info.this,"Empty Image Url", Toast.LENGTH_LONG).show();
                    Picasso.get().load(R.drawable.placeholder_img).into(image);
                }
            }

            @Override
            public void onFailure(Call<CatModel> call, Throwable t) {
                Toast.makeText(Editcat_info.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Editcat_info.this,menu);
                popup.getMenuInflater().inflate(R.menu.statusmenu,popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(Editcat_info.this,"" + item.getTitle(), Toast.LENGTH_LONG).show();
                        Update_Status(cat_id,"2");
                        return true;
                    }
                });
                popup.show();
            }
        });


        cat_name = name_cat.getText().toString();
        System.out.println(cat_name);

        imageblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent black1 = new Intent(Editcat_info.this, User_Cat_list.class);

                startActivity(black1);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat_name = name_cat.getText().toString();
                System.out.println(cat_name);
                cat_type = name_type.getText().toString();
                System.out.println(cat_type);
                cat_bd = ago1.getText().toString();
                System.out.println(cat_bd);
                cat_weight = nam1.getText().toString();
                System.out.println(cat_weight);
                health_check_date = date1.getText().toString();
                System.out.println(health_check_date);
                latest_donation = blooddate.getText().toString();
                System.out.println(latest_donation);
                blood_type = groupblood.getText().toString();
                System.out.println(blood_type);

                if (cat_name.matches("") || cat_type.matches("") || blood_type.matches("") || cat_bd.matches("1") || cat_weight.matches("2")
                        || cat_weight.matches("1") || health_check_date.matches("") || latest_donation.matches("")){
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else {
                    UpdateCat(cat_id,cat_name,cat_type,blood_type,cat_bd,cat_weight,health_check_date,latest_donation);
                    Intent ok = new Intent(Editcat_info.this, User_Cat_list.class);
                    startActivity(ok);
                }
                if (blood_type.equals("A") || blood_type.equals("B") || blood_type.equals("AB")){

                }else {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมมูลเป็น A,B,AB เท่านั้น", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private void  Update_Status (String cat_id, String status_cat){
        Call<Void> call = phpServiceAPI.Update_Status(cat_id,status_cat);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    private void UpdateCat(String cat_id, String cat_name, String cat_type, String blood_type, String cat_bd, String cat_weight
            , String health_check_date, String latest_donation){
        Call<Void> call = phpServiceAPI.UpdateCat(cat_id,cat_name,cat_type,blood_type,cat_bd,cat_weight,health_check_date,latest_donation);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        Toast.makeText(getApplicationContext(),"บันทึกข้อมูลสำเร็จ", Toast.LENGTH_LONG).show();
            }


    }

