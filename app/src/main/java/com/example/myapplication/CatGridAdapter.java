package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.CatModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatGridAdapter extends AppCompatActivity {

    public static  final  String EXTRA_CAT_ID = "com.example.myapplication.EXTRA_TEXT";

    public ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);


        PHPServiceAPI phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);

        final String getHPT_name = getIntent().getExtras().getString("HPT_name");
        final String getBlood_type = getIntent().getExtras().getString("blood_type");

        Call<List<CatModel>> call = phpServiceAPI.getcat_list(getHPT_name, getBlood_type);

        call.enqueue(new Callback<List<CatModel>>() {
            @Override
            public void onResponse(Call<List<CatModel>> call, Response<List<CatModel>> response) {
                populateGridView(response.body());
            }

            @Override
            public void onFailure(Call<List<CatModel>> call, Throwable t) {
                Toast.makeText(CatGridAdapter.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        back = (ImageView) findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    class GridViewAdapter extends BaseAdapter {
        private List<CatModel> catModels;
        private Context context;

        public GridViewAdapter(Context context, List<CatModel> catModels) {
            this.context = context;
            this.catModels = catModels;
        }

        @Override
        public int getCount() {
            return catModels.size();
        }

        @Override
        public Object getItem(int pos) {
            return catModels.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.row_item, viewGroup, false);
            }

            TextView catName = view.findViewById(R.id.grid_cat_name);
            ImageView catImg = view.findViewById(R.id.grid_cat_img);

            /*
            * array catModels
            * [{name: "", id: ""}, {name: "", id: ""}, {name: "", id: ""}, {name: "", id: ""}]
            * position = index 0, 1
            * thisCatModel = catModels[0]
            * */

            final CatModel thisCatModel = catModels.get(position);

            catName.setText(thisCatModel.getCat_name());

            if (thisCatModel.getUrl_cat() != null && thisCatModel.getUrl_cat().length() > 0) {
                Picasso.get().load(thisCatModel.getUrl_cat()).placeholder(R.drawable.placeholder_img).into(catImg);
            } else {
                Toast.makeText(context, "Empty Image Url", Toast.LENGTH_LONG).show();
                Picasso.get().load(R.drawable.placeholder_img).into(catImg);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, thisCatModel.getCat_name(), Toast.LENGTH_SHORT).show();
                    openMainActivity(thisCatModel.getCat_id());
                }
            });
            return view;
        }
    }

    private GridViewAdapter adapter;
    private GridView mGridView;

    private void populateGridView(List<CatModel> catModelList) {
        mGridView = findViewById(R.id.Grid_one);
        adapter = new GridViewAdapter(this, catModelList);
        mGridView.setNumColumns(2);
        mGridView.setAdapter(adapter);
    }

    // รับ parameter ชื่อ id แบบ สตริง
    // เปิดหน้าอื่นและส่ง parameter
    private void openMainActivity(String id) {
        Intent intent = new Intent(this, CatDetial.class);
        // เปิดหน้า MainActivity
        intent.putExtra(EXTRA_CAT_ID, id);
        startActivity(intent);
    }

}