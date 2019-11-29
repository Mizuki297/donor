package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.models.HospitalModel;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button bRegister;
    EditText etName, etLastname, etUsername, etPassword, etConfirm_Password, etEmail, etTel, etLineID;
    private PHPServiceAPI phpServiceAPI;
    private Spinner spinnerHospital;
    private ArrayList<String>hospitalList = new ArrayList<>();
    private String user_name = "";
    private String user_s_name;
    private String username;
    private String password;
    private String confirm_password;
    private String user_email;
    private String user_tel;
    private String user_line_id;
    private String HPT_name;

    private ImageView back;

    private ProgressDialog progressDialog;
    //เป็นการประกาศตัวแปร มีการใช้เซอร์วิสเอพีไอ มีการเเรียกใช้ลิสอาเรย์

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Progress");
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        phpServiceAPI= RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);
        etName = (EditText) findViewById(R.id.etName);
        etLastname = (EditText) findViewById(R.id.etLastname);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirm_Password = (EditText) findViewById(R.id.etConfirmPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etTel = (EditText) findViewById(R.id.etTel);
        etLineID = (EditText) findViewById(R.id.etLineID);
        bRegister = (Button) findViewById(R.id.bRegister);
        spinnerHospital = (Spinner) findViewById(R.id.etHospital);
        back = (ImageView) findViewById(R.id.back_icon);
        //เป็นการรับค่าของตัวแปรชนิดนั้นๆ

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //การเซตปุ่มย้อนกลับ

        hospitalList.add("กรุณาเลือกโรงพยาบาล");

        getHospitalList();
        //รับค่าลิสของโรงพยาบาล

        ArrayAdapter <String> adapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, hospitalList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospital.setAdapter(adapter);
        //เป็นส่วนของตัวอแดปเตอร์ที่ใช้สร้างอาเรย์ลิสหรือดร็อปดาวน์ของโรงพยาบาล

        spinnerHospital.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        HPT_name = adapterView.getSelectedItem().toString();
                        System.out.println(HPT_name);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
        //เป็นการแสดงส่วนของส่วนของประกอบต่างๆของรีจิสเตอร์เช่นโรงพยาบาล

        bRegister.setOnClickListener(this);
        //รีจิสเตอร์ออนคลิ๊ก
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bRegister:
                user_name = etName.getText().toString();
                user_s_name = etLastname.getText().toString();
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                confirm_password = etConfirm_Password.getText().toString();
                user_email = etEmail.getText().toString();
                user_tel = etTel.getText().toString();
                user_line_id = etLineID.getText().toString();
                //เป็นการใช้เก็ตเก็บค่าตัวแปร

                if (user_name.matches("") || user_s_name.matches("")
                || username.matches("") || password.matches("")
                || confirm_password.matches("") || user_email.matches("")
                || user_tel.matches("") || user_line_id.matches("") || HPT_name.equals("กรุณาเลือกโรงพยาบาล")){
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                    //กำหนดเงื่อนไขให้ข้อมูลทุกช่องมีการดักข้อมูลไว้ ถ้ากรอกข้อมูลไม่ครบจะมีโทสขึ้นแสดงว่าให้กรอกให้ครบ

                }else if (!password.equals(confirm_password)){
                    Toast.makeText(getApplicationContext(),"password ไมตรงกัน",Toast.LENGTH_SHORT).show();
                }
                //เป็นเงื่อนไขเอลอิฟในกรณีที่กรอกพาสเวิร์ดไม่ตรงกัน

                else{
                    System.out.println(user_name + user_s_name +  username + password + confirm_password
                            + user_email + user_tel + user_line_id);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    Register();
                }
                //ถ้ารีจิสเตอร์เสร็จจะแสดงไดอาล็อกว่ารีจิสเตอร์เสร็จแล้วหรือไม่ผ่าน

                break;
        }

    }
    private void getHospitalList() {
        Call<List<HospitalModel>> call = phpServiceAPI.getHospital();

        call.enqueue(new Callback<List<HospitalModel>>() {
            @Override
            public void onResponse(Call<List<HospitalModel>> call, Response<List<HospitalModel>> response) {
                List<HospitalModel> getList = response.body();

                for (HospitalModel post: getList) {
                    String content = "";
                    content += "HPT_id: " + post.getHPT_id() + "\n";
                    content += "HPT_name: " + post.getHPT_name() + "\n";
                    content += "HPT_location: " + post.getHPT_location() + "\n";

                    System.out.println(content);

                    hospitalList.add(post.getHPT_name());
                }
                //เรียกใช้ค่าของลิสโรงพยาบาลเมื่อมีการกำหนดค่าลิสเสร็จหนึ่งสองสามในส่วนอาร์เรย์อแดปเตอร์เสร็จแล้ว จะเป็นการทำงานหรือการเรสปอนด์ของส่วนเก็ตฮอสพิทอลลิสนี้
            }

            @Override
            public void onFailure(Call<List<HospitalModel>> call, Throwable t) {
                System.out.println(t.getMessage());
                //แสดงไดอาล็อกเมื่อรีจิสเตอร์
            }
        });
    }
    private void Register(){
        Call<UserModel> call=phpServiceAPI.numUser(user_name, user_s_name, username, password, user_email, user_tel, user_line_id,HPT_name);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel registerInfo = response.body();

                if (registerInfo.getStatus() == 0){
                    System.out.println(registerInfo.getDescription());
                    Toast.makeText(getApplicationContext(),registerInfo.getDescription(),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else{
                    System.out.println(registerInfo.getDescription());
                    Toast.makeText(getApplicationContext(),registerInfo.getDescription(),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(Register.this,Login.class);
                    startActivity(intent);
                }
                //เป็นการทำงานของรีจิสเตอร์ถ้าค่าเท่ากับๆศูนย์คือไม่สำเร็จ จะมีไดอาล็อกขึ้นแสดงว่าไม่สามารถใช้ได้เช่นเพราะมีการใช้ซ้ำ
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
            //เป็นเมื่อผิดพลาดข้อมูลจะแสดงว่าออนเฟลเลอร์
        });

    }

}


