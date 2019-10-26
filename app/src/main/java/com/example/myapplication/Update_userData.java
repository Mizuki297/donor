package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_userData extends AppCompatActivity {

    private  PHPServiceAPI phpServiceAPI;

    private EditText name,lastname,tel;
    private TextView lineid;
    private Button ok;
    private ImageView back;

    private String user_name,user_s_name,user_tal,user_line_id,user_password;

    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_layout);

        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        session = new Session(getApplicationContext());
        System.out.println(session.getUserId());

        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.lastname);
        tel = (EditText) findViewById(R.id.tel);
        lineid = (TextView) findViewById(R.id.lineid);
        ok = (Button) findViewById(R.id.ok);
        back = (ImageView)findViewById(R.id.back);


        getUser(session.getUserId());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = name.getText().toString();
                String user_s = lastname.getText().toString();
                String user_t = tel.getText().toString();
                String user_l = lineid.getText().toString();

                update_user(session.getUserId(),user_name,user_s,user_t,user_l);

            }
        });


    }
    private void  getUser(String user_id){
        Call<List<User>> cal1 = phpServiceAPI.getUser(user_id);

        cal1.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> getlist = response.body();

                for (User post:getlist){
                    user_name = post.getUser_name();
                    user_s_name = post.getUser_s_name();
                    user_tal = post.getUser_tel();
                    user_line_id = post.getUser_line_id();
                    user_password = post.getUser_password();

                }
                System.out.println(user_name);
                System.out.println(user_s_name);
                System.out.println(user_tal);
                System.out.println(user_line_id);
                System.out.println(user_password);

                name.setText(user_name);
                lastname.setText(user_s_name);
                tel.setText(user_tal);
                lineid.setText(user_line_id);


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }

        });
    }
    private void update_user(String user_id, String user_name, String user_s_name, String user_tal, String user_line_id){
        Call <Void> call = phpServiceAPI.update_user(user_id, user_name, user_s_name, user_tal, user_line_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
