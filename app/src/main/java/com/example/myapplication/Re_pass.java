package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.LoginModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Re_pass extends AppCompatActivity {

    private PHPServiceAPI phpServiceAPI; //
    private Session session; //

    private EditText old_pass,new_pass,con_pass; //
    private Button button; //
    private ImageView back; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pass_layout);

        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        session = new Session(getApplicationContext());
        System.out.println(session.getUserId());

        old_pass = (EditText) findViewById(R.id.editText6); // oldpass เชื่อม edittext6
        new_pass = (EditText) findViewById(R.id.editText5); // newpass  เชื่อม edittext5
        con_pass = (EditText) findViewById(R.id.editText7); // conpass เชื่อม edittext7

        button = (Button) findViewById(R.id.button2); // button เชื่อม button2
        back = (ImageView) findViewById(R.id.imageView3); // back เชื่อม imageView3

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // กำหนดให้ เมื่อคลิกปุ่ม back จะย้อนกลับไปหน้าก่อนหน้านี้
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //กำหนดให้เมื่อคลิกเปุ่ม button เป็นยืนยัน

                if (old_pass.getText().toString().matches("") || new_pass.getText().toString().matches("") || con_pass.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show(); //ถ้ากรอกรหัสผ่านเก่า รหัสผ่านใหม่ และยืนยันรหัสผ่านไม่ครบ จะขึ้นแสดงว่า กรุณากรอกข้อมูลให้ครบ
                }else {
                    if (new_pass.getText().toString().matches(con_pass.getText().toString())){
                        Update_password(old_pass.getText().toString(),new_pass.getText().toString()); //เมื่อรหัสผ่านเก่ากับรหัสผ่านใหม่แมทกัน ก็จะอัพเดปรหัสผ่าน
                    }else{
                        Toast.makeText(getApplicationContext(),"password ไม่ตรงกัน",Toast.LENGTH_SHORT).show(); //แต่ถ้ารหัสผ่านไม่ตรงกัน จะขึ้นแสดงว่า password ไม่ตรงกัน
                    }
                }
            }
        });

    }
    private void Update_password(String old_password,String new_password){ //ฟังชันUpdatepassword รับ รหัสผ่านเก่า และรหัสผ่านใหม่
        Call<LoginModel> call = phpServiceAPI.update_pass(session.getUserId(),old_password,new_password); // เรียก php อัปเดพพาส รับ ข้อมูล รหัสผ่านเก่า และรหัสผ่านใหม่
        call.enqueue(new Callback<LoginModel>() { //
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel updateInfo = response.body();
                if (updateInfo.status == 0){
                    Toast.makeText(getApplicationContext(),updateInfo.description,Toast.LENGTH_SHORT).show();
                    onBackPressed(); // ถ้าสถานะส่งกลับมาเป็น 0 คืออัพเดปสำเร็จ และ description จะโชว์ว่าสำเร็จ แล้วจะกลับไปหน้าเก่า
                }else{
                    Toast.makeText(getApplicationContext(),updateInfo.description,Toast.LENGTH_SHORT).show(); // ถ้าอัปเดพไม่สำเร็จdescription จะโชว์ว่าไม่สำเร็จ และยังอยู่หน้าเดิม
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

            }
        });
    }

}
