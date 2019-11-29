package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.models.CatModel;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_Cat_list extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private ImageView back,plus,search,personal;
    private ListView listview;

    private TextView name,coin,add_coin,username;

    public static final String USER_CAT_ID = "com.example.myapplication.EXTRA_TEXT"; //รับไอดีมาจากหน้า login

    private PHPServiceAPI phpServiceAPI;
    private Session session; //เรียกค่า user id มาจาก session
   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.user_activity_cat_list);

       session = new Session(getApplicationContext());
       System.out.println(session.getUserId()); //ประกาศ session

        //ประกาศตัวเชื่อมไอดี
       final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout); //เชื่อมหน้าเมนู
       NavigationView navigationView = findViewById(R.id.nav_view); // ประเชื่อมค่าเมนู
       View henderView = navigationView.getHeaderView(0); // เชื่อค่าจากหน้าเมนู
       name = (TextView) henderView.findViewById(R.id.menu_username);
       coin = (TextView) henderView.findViewById(R.id.menu_coin);
       add_coin = (TextView) henderView.findViewById(R.id.add_coin);
       add_coin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(User_Cat_list.this,Add_Money.class);
               startActivity(intent);
           }
       });
       username = (TextView) findViewById(R.id.name);

       phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

       back = (ImageView) findViewById(R.id.black);
       plus = (ImageView) findViewById(R.id.plus);

       search = (ImageView) findViewById(R.id.search);
       personal = (ImageView) findViewById(R.id.personal);

       listview = (ListView) findViewById(R.id.listview);

       getUser(session.getUserId());
       updateData();

       appBarConfiguration = new AppBarConfiguration.Builder(
               R.id.menu_update_user
               ,R.id.menu_update_pass
               ,R.id.menu_logout
       ).setDrawerLayout(drawerLayout).build(); // ส่วนประกาศลิสของหน้าเมนู แก้ไขข้มมูล แก้ไขรหัส ล็อคเอาส์

       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               switch (menuItem.getItemId()){
                   case R.id.menu_update_user:
                       Toast.makeText(getApplicationContext(),"update user",Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(User_Cat_list.this, Update_userData.class);
                       startActivity(intent); //เมื่อกดปุ่มแก้ไขข้อมูลจะไปหน้าแก้ไขข้อมูล
                       break;
                   case R.id.menu_update_pass:
                       Toast.makeText(getApplicationContext(),"update pass",Toast.LENGTH_SHORT).show();
                       Intent intent2 = new Intent(User_Cat_list.this, Re_pass.class);
                       startActivity(intent2); // เมื่อกดปุ่มแก้ไขพาสเวิร์ดจะไปหน้าแก้ไขพาสเวิร์ด
                       break;
                   case R.id.menu_logout:
                       Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
                       session.clearUserId();
                       Intent intent3 = new Intent(User_Cat_list.this,Login.class);
                       startActivity(intent3); //เมื่อกดปุ่มล็อคเอาส์จะไปหน้าล็อกอิน
                       break;
               }
               drawerLayout.closeDrawers(); //ปิดหน้าเมนู
               return true;
           }
       });

       Call<List<CatModel>> call = phpServiceAPI.getCatRegister(session.getUserId());

       //รับค่าจาก catmodel
       call.enqueue(new Callback<List<CatModel>>() {
           @Override
           public void onResponse(Call<List<CatModel>> call, Response<List<CatModel>> response) {
                populateListView(response.body());
           }

           @Override
       public void onFailure(Call<List<CatModel>> call, Throwable t) {
//               Toast.makeText(User_Cat_list.this, t.getMessage(), Toast.LENGTH_LONG).show();
       }
   });

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(User_Cat_list.this,MainActivity.class);
               startActivity(intent); //กลับไปหน้าค้นหา
           }
       });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent plus = new Intent(User_Cat_list.this, Addcat_info.class);
                startActivity(plus); //ไปหน้าเพิ่มข้อมูลแมว
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Cat_list.this,MainActivity.class);
                startActivity(intent); //ไปหน้าค้นหา
            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END); //ไปหน้าเมนู
            }
        });
   }

    private void getUser(String user_id) {
        Call<UserModel> call = phpServiceAPI.getUser(user_id);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userInfo = response.body();
                name.setText(userInfo.getUser_name()); //รับชื่อมาแสดงในหน้าเมนู
                coin.setText(userInfo.getMoney_coin()); //รับจำนวนเงินมาแสดงหน้าเมนู
                username.setText(userInfo.getUser_name()); //รับค่าชื่อมาแสดงหน้า list
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println(t.getMessage()); //เมื่อไม่สำเร็จจะแสดงmessageจาก onFailure
            }
        });
    }

    //อัพเดตสเตตัสวันที่
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

        //ตัวสร้างลิสว่าหน้าตาเป็นยังไง
        class Adapter_cat extends ArrayAdapter<String> {
            Context context;
            private List<CatModel> catModels;

            Adapter_cat(Context c, List<CatModel> catModels){
                super(c,R.layout.row_list,R.id.menu_username);
                this.context = c;
                this.catModels = catModels;
            }
            @Override
            public int getCount(){
                return catModels.size();
            }
            @Override
            public long getItemId(int pos){
                return pos;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = layoutInflater.inflate(R.layout.row_list, parent, false); //เชื่อมหน้าโีรลลิส
                ImageView image = row.findViewById(R.id.imageView5); //เชื่อมแสดงรูปในหน้าลโรลิส
                TextView name_cat = row.findViewById(R.id.menu_username); //เชื่อมแสดงชื่อในหน้าโรลลิส
                TextView count = row.findViewById(R.id.countdown); //เชื่อมตัวนับถอยหลังวัน

                Button status = row.findViewById(R.id.heart1); //เชื่อมหัวใจจากหน้าโรลลิส
                Button fix = row.findViewById(R.id.edit1); //เชื่อมตัวปุ่มแก้ไขจากหน้าโรลลิส

                final View str_atc = row.findViewById(R.id.view3); //วิวเปลี่ยนสีรูปหัวใจ

                GradientDrawable gradientDrawable = (GradientDrawable)str_atc.getBackground().mutate();

                final CatModel thisCatmodel = catModels.get(position);
                name_cat.setText(thisCatmodel.getCat_name()); //ดึงข้อมูลแมวมากจาก catmodel

                if (thisCatmodel.getUrl_cat() !=null && thisCatmodel.getUrl_cat().length()>0){
                    Picasso.get().load(thisCatmodel.getUrl_cat()).placeholder(R.drawable.placeholder_img).into(image);
                    // ถ้าค่ารูปไม่เท่ากับ null หรือมีรูปภาพ ให้แสดงรูป
                }else {
                    Toast.makeText(context,"Empty image Url", Toast.LENGTH_LONG).show();
                    Picasso.get().load(R.drawable.placeholder_img).into(image); //ถ้าไม่มีรูปให้ดึงรูปป่าวมาแสดง
                }

                System.out.println(thisCatmodel.getStatus_cat());
                System.out.println(thisCatmodel.getCount());

                if (thisCatmodel.getCount() < 0){
                    count.setText("พร้อมบริจาค"); //เมื่อวันคูลดาวน์น้่อยกว่า 0 คือพร้อมบริจาค
                }else {
                    count.setText("เหลือ " + thisCatmodel.getCount() + " วัน"); //ถ้ามากกว่า 0 จะแสดง เหลือ.....วัน
                }

                if (thisCatmodel.getStatus_cat().equals("1")){
                    gradientDrawable.setColor(getColor(R.color.red));
                    System.out.println("red"); //ถ้าสเตตัสมีค่าเท่ากับ 1 หัวใจจะเป็นสีแดง
                }

                status.setOnClickListener(new View.OnClickListener() {
                    String num = thisCatmodel.getStatus_cat();
                    @Override
                    public void onClick(View v) {
                        if (num.equals("0")){
////                            GradientDrawable gradientDrawable = (GradientDrawable)str_atc.getBackground().mutate();
////                            gradientDrawable.setColor(getColor(R.color.red));
////                            num = "1";
////                            Update_Status(thisCatmodel.getCat_id(),num);
                            Toast.makeText(getApplicationContext(),"แมวของคุณยังไม่พร้อมบริจาคเลือด",Toast.LENGTH_LONG).show();
                            //ถ้าสเตตัสมีค่าเป็น 0 จะแสดงแมวของคุรยังไม่พร้อมบริจาค ปุ่มจะเป็นสีขาว
                        }else  if (num.equals("1")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(User_Cat_list.this);
                            builder.setTitle("Update ข้อมูลแมว?");
                            builder.setMessage("แมวของคุณได้บริจาคเลือดแล้ว");
                            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    GradientDrawable gradientDrawable = (GradientDrawable) str_atc.getBackground().mutate();
                                    gradientDrawable.setColor(getColor(R.color.white));
                                    num = "0";
                                    Update_Status(thisCatmodel.getCat_id(),num);
                                }
                            });
                            builder.setNegativeButton("ยกเลิก",null);
                            //ถ้าสเตตัสเป็น 1 หัวใจเป็นสีแดง หากกดที่ปุ่มหัวใจเพื่อต้องการอัพเดตวันบริจาคเลือด จะขึ้นแมวของคุณได้บริจาคเลือดแล้ว ถ้ากดตกลงสเตตัสจะอัพเดตเป็น 0 และเป็นวันปัจจุบันหัวใจเป็นสีขาว
                            //ถ้ากดยกเลิก จะไม่มีอะไรเปลี่ยนแปลง

                            AlertDialog dialog = builder.create();
                            dialog.show(); //สั่งเปิดdialog

                        }
                    }
                });
                fix.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMainActivity(thisCatmodel.getCat_id()); //เรียกฟังก์ชั่น openMainActivity
                    }
                });
                return row;
            }
        }
        //ตัวโชว์หน้าลิสวิว
        private void populateListView(List<CatModel> body){
       listview = findViewById(R.id.listview);
       Adapter_cat adapter = new Adapter_cat(this,body);
       listview.setAdapter(adapter);
        }
       //ไปหน้าแก้ไขข้อมมูลแมว
        private  void openMainActivity(String id){
       Intent intent = new Intent(this,Editcat_info.class);
       intent.putExtra(USER_CAT_ID,id);
       startActivity(intent);
    }
    //เรียกฟังก์ชั่นอัพเดตดาต้า
    private void updateData(){
        Call<Void> call = phpServiceAPI.update_data();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("update cat");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
}

