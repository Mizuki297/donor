package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.LoginModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private PHPServiceAPI phpServiceAPI;
    private Button bLogin;
    private EditText etUsername, etPassword;
    private String username, password;
    private Session session;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tvRegisterLink;

        session = new Session(getApplicationContext());

        // Call get hospital
        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Progress");
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

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

                if (username.matches("") || password.matches("")){
                    Toast.makeText(getApplicationContext(),"pls enter data",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    login(username, password);
                }
                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }
        private void login(String username, String password){
            Call <LoginModel> call = phpServiceAPI.login(username, password);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    LoginModel userInfo = response.body();
                    if (userInfo.status == 0){
                        session.setUserId(userInfo.user_id);
                        System.out.println(userInfo.description);
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        progressDialog.dismiss();
                        startActivity(intent);
                    }else{
                        session.clearUserId();
                        System.out.println(userInfo.description);
                        Toast.makeText(getApplicationContext(),userInfo.description,Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    System.out.println(t.getMessage());

                }
            });
        }
}
