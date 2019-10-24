package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        SharedPreferences getUser = getPreferences(Context.MODE_PRIVATE);
//        String currentUser = getUser.getString("user_id","");
//        System.out.println("currenUser->");
//        System.out.println(currentUser);

        session = new Session(getApplicationContext());
//        session.getUserId(user_id);
        System.out.println("is register "+session.getUserId());

        phpServiceAPI=RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);
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

        hospitalList.add("กรุณาเลือกโรงพยาบาล");

        getHospitalList();

        ArrayAdapter <String> adapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, hospitalList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospital.setAdapter(adapter);

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



        bRegister.setOnClickListener(this);
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



                if (user_name.matches("") || user_s_name.matches("")
                || username.matches("") || password.matches("")
                || confirm_password.matches("") || user_email.matches("")
                || user_tel.matches("") || user_line_id.matches("") || HPT_name == "กรุณาเลือกโรงพยาบาล"){
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();

                }else if (!password.equals(confirm_password)){
                    Toast.makeText(getApplicationContext(),"passwordไมตรงกัน",Toast.LENGTH_SHORT).show();
                }

                else{
                    System.out.println(user_name + user_s_name +  username + password + confirm_password
                            + user_email + user_tel + user_line_id);
                    Register();
                }

                break;

        }

    }
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
    private void Register(){
        Call<List<User>> call=phpServiceAPI.numUser(user_name, user_s_name, username, password, user_email, user_tel, user_line_id,HPT_name);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> getlist = response.body();

                for (User post:getlist){
                    String user_re = post.getUser();
                    System.out.println(user_re);
                }


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

}


