package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private PHPServiceAPI phpServiceAPI;
    private Button bLogin;
    private EditText etUsername, etPassword;
    private String username, password, user_id;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tvRegisterLink;


        // Call get hospital
        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);


        etUsername = (EditText) findViewById(R.id.Telephone);
        etPassword = (EditText) findViewById(R.id.Hospital);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);


        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                System.out.println(username);
                System.out.println(password);

//                if (username.matches("") || password.matches("")){
//                    Toast.makeText(getApplicationContext(),"pls enter data",Toast.LENGTH_SHORT).show();
//                }else{
                    login(username, password);
//                }
//
//                Intent intent = new Intent(this,MainActivity.class);
//                intent.putExtra("user_id",user_id);
//                startActivity(intent);
//                if (user_id.length() >= 1){
//                    Toast.makeText(getApplicationContext(),"login ok",Toast.LENGTH_LONG).show();
//
//                }else{
//                    Toast.makeText(getApplicationContext(),"login Nor ok",Toast.LENGTH_LONG).show();
//                }
                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }
        private void login(String username, String password){
            Call <Check_login> call = phpServiceAPI.login(username, password);
            call.enqueue(new Callback<Check_login>() {
                @Override
                public void onResponse(Call<Check_login> call, Response<Check_login> response) {
                    Check_login getList = response.body();
                        user_id = getList.getUser_id();
//                        String token = getList.getToken();
//                        System.out.println(user_id);

//                    SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("user_id",user_id);
//                    editor.commit();
//
//
//                    SharedPreferences getUser = getPreferences(Context.MODE_PRIVATE);
//                    String currentUser = getUser.getString("user_id","");
//
//                    System.out.println("currenUser->");
//                    System.out.println(currentUser);
                    session = new Session(getApplicationContext());
                    session.setUserId(user_id);


                    System.out.println(session.getUserId());

                }

                @Override
                public void onFailure(Call<Check_login> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    System.out.println(t.getMessage());

                }
            });
        }
}
