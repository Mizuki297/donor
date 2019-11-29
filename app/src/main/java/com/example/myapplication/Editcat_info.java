package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.CatModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Editcat_info extends AppCompatActivity {

    private PHPServiceAPI phpServiceAPI;

    private EditText name_cat,name_type, ago1, nam1,groupblood;
    private Button ok;
    private ImageView image,menu;

    private TextView hcd,ld;

    private String cat_name,cat_type,blood_type,cat_bd,cat_weight,health_check_date,latest_donation,blood_type2 = "";
    private ImageView imageblack,cal1,cal2;

    private int intDay,intMonth,intYear; // ตัวแปรวันเดือนปี


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcat_info);

        final Intent intent = getIntent();
        final String cat_id = intent.getStringExtra(User_Cat_list.USER_CAT_ID);

        name_cat = (EditText) findViewById(R.id.name_cat);
        name_type = (EditText) findViewById(R.id.name_type);
        ago1 = (EditText) findViewById(R.id.ago1);
        nam1 = (EditText) findViewById(R.id.nam1);

        hcd = (TextView) findViewById(R.id.date);
        ld = (TextView) findViewById(R.id.blooddate);

        cal1 = (ImageView) findViewById(R.id.cal1);
        cal2 = (ImageView) findViewById(R.id.cal2);

        groupblood= (EditText) findViewById(R.id.groupblood);
        ok = (Button) findViewById(R.id.ok);
        image = (ImageView) findViewById(R.id.image);
        imageblack = (ImageView) findViewById(R.id.imageblack);
        menu = (ImageView) findViewById(R.id.menu);

        getCurrentDateTime();

        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        Call<CatModel> call = phpServiceAPI.getCatDetail(cat_id);

        call.enqueue(new Callback<CatModel>() {
            @Override
            public void onResponse(Call<CatModel> call, Response<CatModel> response) {
                CatModel catDetail = response.body();
                //ส่งข้อมูลไปแต่ไม่ส่งกลับ


                //นำข้อมูลมาแสดง
                name_cat.setText(catDetail.getCat_name());
                name_type.setText(catDetail.getCat_type());
                ago1.setText(catDetail.getCat_bd());
                nam1.setText(catDetail.getCat_weight());
                hcd.setText(catDetail.getHealth_check_date());
                ld.setText(catDetail.getLatest_donation());
                groupblood.setText(catDetail.getBlood_type());

                 if (catDetail.getUrl_cat() !=null && catDetail.getUrl_cat().length()>0){
                    Picasso.get().load(catDetail.getUrl_cat()).placeholder(R.drawable.placeholder_img).into(image);
                    //ถ้าหากมีรูปภาพจะถูกแทนที่ด้วยรูป
                }else {
                    Toast.makeText(Editcat_info.this,"Empty Image Url", Toast.LENGTH_LONG).show();
                    Picasso.get().load(R.drawable.placeholder_img).into(image); //ถ้าไม่มีรูปจะถูกแทนที่ด้วยรูปว่าง
                }
            }

            @Override
            public void onFailure(Call<CatModel> call, Throwable t) {
                Toast.makeText(Editcat_info.this, t.getMessage(), Toast.LENGTH_LONG).show();
                //ถ้าส่งไปแล้วผิดพลาดจะแสดงข้อความว่าผิดพลาดยังไง
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
                        Intent intent = new Intent(Editcat_info.this,User_Cat_list.class);
                        startActivity(intent);
                        return true;
                    }
                });
                popup.show();
                //หน้าป๊อปอัพลบข้อมูล ถ้ากดกดขึ้นมาสเตตัสจะเป็น สอง แล้วตัวลบข้อมูลจะแสดงขึ้นมา หลังจากลบแล้วจะกลับไปยังหน้าลิส
            }
        });


        cat_name = name_cat.getText().toString();
        System.out.println(cat_name); //เก็บชื่อแมวเป็นสริงแล้วเอาส์ปริ้นออกมา

        imageblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent black1 = new Intent(Editcat_info.this, User_Cat_list.class);

                startActivity(black1); //กดปุ่มย้อนกลับ
            }
        });
        cal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(); //เลือกปกิทิน
            }
        });
        cal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog1(); //เลือกปกิทิน
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //เก็บข้อมูลเป็นสตริง
                cat_name = name_cat.getText().toString();
                System.out.println(cat_name);
                cat_type = name_type.getText().toString();
                System.out.println(cat_type);
                cat_bd = ago1.getText().toString();
                System.out.println(cat_bd);
                cat_weight = nam1.getText().toString();
                System.out.println(cat_weight);
                health_check_date = hcd.getText().toString();
                System.out.println(health_check_date);
                latest_donation = ld.getText().toString();
                System.out.println(latest_donation);
                blood_type = groupblood.getText().toString();
                System.out.println(blood_type);

                if (cat_name.matches("") || cat_type.matches("") || blood_type.matches("") || cat_bd.matches("1") || cat_weight.matches("2")
                        || cat_weight.matches("1") || health_check_date.matches("") || latest_donation.matches("")){
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                    //กดหนดเงื่อนไขเช่นไม่ให้แคทเนมเป็นค่าว่าง น้ำหนังเป็นหนึ่ง ให้แสดงกรุณากรอกข้อมูลให้ครบ
                } else {
                    if (blood_type.equals("A") || blood_type.equals("B") || blood_type.equals("AB")){
                        if (Integer.parseInt(cat_bd) < 3 || Integer.parseInt(cat_weight) < 3){
                            Toast.makeText(getApplicationContext(),"แมวของคุณต้องมีน้ำหนัก 3 kg และอายุ 3 ปีขึ้นไป",Toast.LENGTH_LONG).show();
                            //ถ้าน้ำหนักและอายุน้อยกว่าสามให้แสดง แมวของคุณต้องมีน้ำหนักสามกิโลกรัมและอายุสามปีขึ้นไป
                        }else {
                            UpdateCat(cat_id, cat_name, cat_type, blood_type, cat_bd, cat_weight, health_check_date, latest_donation);
                            Intent ok = new Intent(Editcat_info.this, User_Cat_list.class);
                            startActivity(ok);
                            //ถ้าข้อมูลครบให้อัพเดตขแก้ไข้อมูล และไปยังหน้าลิส
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมมูลเป็น A,B,AB เท่านั้น", Toast.LENGTH_LONG).show();
                        //ถ้าเลือกกรุ๊ปเลือดที่ไม่ใช้เอ บี เอบี จะขึ้นว่า กรุณากรอกข้อมูลเป็น เอ บี เอบี เท่านั้น
                    }
                }
            }
        });

    }
    private void  Update_Status (String cat_id, String status_cat){
        Call<Void> call = phpServiceAPI.Update_Status(cat_id,status_cat);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //ส่งข้อมูลไปแต่ไม่ส่งกลับ
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                //ถ้าส่งไปแล้วผิดพลาดจะแสดงข้อความว่าผิดพลาดยังไง
            }
        });
    }
    private void UpdateCat(String cat_id, String cat_name, String cat_type, String blood_type, String cat_bd, String cat_weight
            , String health_check_date, String latest_donation){
        Call<Void> call = phpServiceAPI.UpdateCat(cat_id,cat_name,cat_type,blood_type,cat_bd,cat_weight,health_check_date,latest_donation);
        //เเรียกอัพเดตข้อมูล
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //ส่งข้อมูลไปแต่ไม่ส่งกลับ

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                //ถ้าส่งไปแล้วผิดพลาดจะแสดงข้อความว่าผิดพลาดยังไง

            }
        });Toast.makeText(getApplicationContext(),"บันทึกข้อมูลสำเร็จ", Toast.LENGTH_LONG).show();
        //ถ้ากดปุ่มยืนยันแล้วข้อมูลถูกต้องหมดจะขึ้นว่างบันทึกข้อมูลสำเร็จ และไปยังหน้าลิส
    }
    private void getCurrentDateTime(){
        Calendar calendar = Calendar.getInstance();
        intDay = calendar.get(Calendar.DAY_OF_MONTH);
        intMonth = calendar.get(Calendar.MONTH);
        intYear = calendar.get(Calendar.YEAR);
        //ประกาศรับค่าวัน เดือน ปี
//        date.setText(intYear+"/"+intMonth+"/"+intDay);
    }
    private void showDateDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(Editcat_info.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month +1; //เดือนบวกหนึ่งเพราะปกติเดือนแรกจะนับที่ศูนย์ บวกหนึ่งจะได้สิบสองเดือนพอดี
                        hcd.setText(year+"/"+month+"/"+day);
                        System.out.println(hcd.getText()); //วันเดือนปีจะไปแสดงที่ year month day
                        health_check_date = hcd.getText().toString();
                        System.out.println(health_check_date); //วัน เดือน ปี จะถุกเก็บอยู่ที่ health_check_date
                    }
                },intYear,intMonth,intDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private void showDateDialog1(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(Editcat_info.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month +1; //เดือนบวกหนึ่งเพราะปกติเดือนแรกจะนับที่ศูนย์ บวกหนึ่งจะได้สิบสองเดือนพอดี
                        ld.setText(year+"/"+month+"/"+day);
                        System.out.println(ld.getText()); //วันเดือนปีจะไปแสดงที่ year month day
                        latest_donation = ld.getText().toString();
                        System.out.println(latest_donation); //วัน เดือน ปี จะถุกเก็บอยู่ที่ latest_donation
                    }
                },intYear,intMonth,intDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}

