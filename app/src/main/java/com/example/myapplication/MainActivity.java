package com.example.myapplication;
//ส่วนการเรียกอุปกรณ์ต่างๆ
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.HospitalModel;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//ส่วนคลาสหลัก
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    //ประกาศค่าต่างๆที่ต้องการใช้
    private ArrayList<String> hospitalList = new ArrayList<>();

    private PHPServiceAPI phpServiceAPI;

    private RadioGroup radioGroup;

    private RadioButton radioButton;

    private Button search;

    private String selectHospital = "";

    private Spinner spinnerHospital;

    private View button_add,button_user;

    private TextView name,coin;

    private Session session;

    // ส่วนหลัก
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(getApplicationContext());
        System.out.println(session.getUserId());

        if (session.getUserId().equals("") || session.getUserId() == null){
            Intent intent = new Intent(MainActivity.this,Login.class);
            startActivity(intent);
        }else{

        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View henderView = navigationView.getHeaderView(0);
        name = (TextView) henderView.findViewById(R.id.menu_username);
        coin = (TextView) henderView.findViewById(R.id.menu_coin);

        search = (Button) findViewById(R.id.search_button);

        button_add = (View) findViewById(R.id.imageIcon_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,User_Cat_list.class);
                startActivity(intent);
            }
        });

        button_user = findViewById(R.id.imageIcon_user);
        button_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.menu_update_user
                ,R.id.menu_update_pass
                ,R.id.menu_logout
        ).setDrawerLayout(drawerLayout).build();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_update_user:
                        Toast.makeText(getApplicationContext(),"update user",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Update_userData.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_update_pass:
                        Toast.makeText(getApplicationContext(),"update pass",Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(MainActivity.this, Re_pass.class);
                        startActivity(intent2);
                        break;
                    case R.id.menu_logout:
                        Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
                        session.clearUserId();
                        Intent intent3 = new Intent(MainActivity.this,Login.class);
                        startActivity(intent3);
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        getUser(session.getUserId());

        radioGroup = (RadioGroup) findViewById(R.id.bloodtype_group);

        hospitalList.add("กรุณาเลือกสถานที่รักษา");
        // Get HospitalModel list from api
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
                System.out.println(radioButton.getText().toString());
            }
        });
        //กดปุ่มค้นหา
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //เช็กค่าว่าง
                if (radioGroup.getCheckedRadioButtonId() == -1 || selectHospital == "กรุณาเลือกสถานที่รักษา"){
                    Toast.makeText(getApplicationContext(), "กรุณาเลือกชื่อโรงพยาบาลหรือกรุ๊ปเลือดให้ครบ", Toast.LENGTH_SHORT).show();
                }else {
                    //เรียกใช้งานฟังก์ชันโดยโยน พารามิเตอร์ 2 ค่า
                    System.out.println(selectHospital);
                    System.out.println(radioButton.getText().toString());
                    onClick_button_search();
                }
            }
        });
        }
    }
//เปลี่ยนหน้า
    public void onClick_button_search (){
        Intent intent = new Intent(this, PayDataCat.class);
        intent.putExtra("HPT_name",selectHospital);
        intent.putExtra("blood_type",radioButton.getText().toString());
        startActivity(intent);
    }
//ดึงข้อมูลจาก api
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
            }

            @Override
            public void onFailure(Call<List<HospitalModel>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
    private void getUser(String user_id) {
        Call<UserModel> call = phpServiceAPI.getUser(user_id);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userInfo = response.body();
                System.out.println(userInfo.getUser_name());
                System.out.println(userInfo.getMoney_coin());
                name.setText(userInfo.getUser_name());
                coin.setText(userInfo.getMoney_coin());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}
