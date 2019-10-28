package com.example.myapplication;

import android.content.Context;
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

    private TextView name,coin;

    public static final String USER_CAT_ID = "com.example.myapplication.EXTRA_TEXT";

    private PHPServiceAPI phpServiceAPI;
    private Session session;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.user_activity_cat_list);

       session = new Session(getApplicationContext());
       System.out.println(session.getUserId());

       final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
       NavigationView navigationView = findViewById(R.id.nav_view);
       View henderView = navigationView.getHeaderView(0);
       name = (TextView) henderView.findViewById(R.id.menu_username);
       coin = (TextView) henderView.findViewById(R.id.menu_coin);

       phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

       back = (ImageView) findViewById(R.id.black);
       plus = (ImageView) findViewById(R.id.plus);

       search = (ImageView) findViewById(R.id.search);
       personal = (ImageView) findViewById(R.id.personal);

       listview = (ListView) findViewById(R.id.listview);

       getUser(session.getUserId());

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
                       Intent intent = new Intent(User_Cat_list.this, Update_userData.class);
                       startActivity(intent);
                       break;
                   case R.id.menu_update_pass:
                       Toast.makeText(getApplicationContext(),"update pass",Toast.LENGTH_SHORT).show();
                       Intent intent2 = new Intent(User_Cat_list.this, Re_pass.class);
                       startActivity(intent2);
                       break;
                   case R.id.menu_logout:
                       Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_SHORT).show();
                       Intent intent3 = new Intent(User_Cat_list.this,Login.class);
                       startActivity(intent3);
                       break;
               }
               drawerLayout.closeDrawers();
               return true;
           }
       });

       Call<List<CatModel>> call = phpServiceAPI.getCatRegister(session.getUserId());

       call.enqueue(new Callback<List<CatModel>>() {
           @Override
           public void onResponse(Call<List<CatModel>> call, Response<List<CatModel>> response) {
                populateListView(response.body());
           }

           @Override
           public void onFailure(Call<List<CatModel>> call, Throwable t) {
               Toast.makeText(User_Cat_list.this, t.getMessage(), Toast.LENGTH_LONG).show();
           }
       });

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(User_Cat_list.this,MainActivity.class);
               startActivity(intent);
           }
       });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent plus = new Intent(User_Cat_list.this, Addcat_info.class);
                startActivity(plus);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Cat_list.this,MainActivity.class);
                startActivity(intent);
            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
   }

    private void getUser(String user_id) {
        Call<UserModel> call = phpServiceAPI.getUser(user_id);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userInfo = response.body();
                name.setText(userInfo.getUser_name());
                coin.setText(userInfo.getMoney_coin());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                System.out.println(t.getMessage());
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
                View row = layoutInflater.inflate(R.layout.row_list, parent, false);
                ImageView image = row.findViewById(R.id.imageView5);
                TextView name_cat = row.findViewById(R.id.menu_username);

                Button status = row.findViewById(R.id.heart1);
                Button fix = row.findViewById(R.id.edit1);

                final View str_atc = row.findViewById(R.id.view3);

                GradientDrawable gradientDrawable = (GradientDrawable)str_atc.getBackground().mutate();

                final CatModel thisCatmodel = catModels.get(position);
                name_cat.setText(thisCatmodel.getCat_name());

                if (thisCatmodel.getUrl_cat() !=null && thisCatmodel.getUrl_cat().length()>0){
                    Picasso.get().load(thisCatmodel.getUrl_cat()).placeholder(R.drawable.placeholder_img).into(image);
                }else {
                    Toast.makeText(context,"Empty image Url", Toast.LENGTH_LONG).show();
                    Picasso.get().load(R.drawable.placeholder_img).into(image);
                }

                System.out.println(thisCatmodel.getStatus_cat());

                if (thisCatmodel.getStatus_cat().equals("1")){
                    gradientDrawable.setColor(getColor(R.color.red));
                    System.out.println("red");
                }

                status.setOnClickListener(new View.OnClickListener() {
                    String num = thisCatmodel.getStatus_cat();
                    @Override
                    public void onClick(View v) {
                        if (num.equals("0")){
                            GradientDrawable gradientDrawable = (GradientDrawable)str_atc.getBackground().mutate();
                            gradientDrawable.setColor(getColor(R.color.red));
                            num = "1";
                            Update_Status(thisCatmodel.getCat_id(),num);
                        }else  if (num.equals("1")){
                            GradientDrawable gradientDrawable = (GradientDrawable)str_atc.getBackground().mutate();
                            gradientDrawable.setColor(getColor(R.color.white));
                            num = "0";
                            Update_Status(thisCatmodel.getCat_id(),num);
                        }
                    }
                });
                fix.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMainActivity(thisCatmodel.getCat_id());
                    }
                });
                return row;
            }
        }
        private void populateListView(List<CatModel> body){
       listview = findViewById(R.id.listview);
       Adapter_cat adapter = new Adapter_cat(this,body);
       listview.setAdapter(adapter);
        }
        private  void openMainActivity(String id){
       Intent intent = new Intent(this,Editcat_info.class);
       intent.putExtra(USER_CAT_ID,id);
       startActivity(intent);
   }
}

