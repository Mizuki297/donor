package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uploadcare.android.library.api.UploadcareClient;
import com.uploadcare.android.library.api.UploadcareFile;
import com.uploadcare.android.library.callbacks.UploadcareFileCallback;
import com.uploadcare.android.library.exceptions.UploadcareApiException;
import com.uploadcare.android.library.upload.FileUploader;
import com.uploadcare.android.library.upload.Uploader;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Addcat_info extends AppCompatActivity {

    private PHPServiceAPI phpServiceAPI;

    private EditText name_cat,name_type, ago1, nam1, date1, blooddate;
    private Spinner sapin;
    private Button ok;
    private ImageView image;
    private Uri uri = null;
    private String cat_name,cat_type,blood_type,cat_bd,cat_weight,health_check_date,latest_donation = "";
    private ImageView imageblack;
    private int GALLERY_REQUEST_CODE = 1;

    private String response_image = "";

    private String file = "";

    private ProgressDialog progressDialog;

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcat_info);

        session = new Session(getApplicationContext());
        System.out.println(session.getUserId());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Progress");
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        phpServiceAPI = RetrofitInstance.getRetrofitInstance().create(PHPServiceAPI.class);


        name_cat = (EditText) findViewById(R.id.name_cat);
        name_type = (EditText) findViewById(R.id.name_type);
        ago1 = (EditText) findViewById(R.id.ago1);
        nam1 = (EditText) findViewById(R.id.nam1);
        date1 = (EditText) findViewById(R.id.date1);
        blooddate = (EditText) findViewById(R.id.blooddaet);
        sapin = (Spinner) findViewById(R.id.sapin);
        ok = (Button) findViewById(R.id.ok);
        image = (ImageView) findViewById(R.id.image);
        imageblack = (ImageView) findViewById(R.id.imageblack);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();

            }
        });

        cat_name = name_cat.getText().toString();
        System.out.println(cat_name);

        imageblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent black1 = new Intent(Addcat_info.this, User_Cat_list.class);
                startActivity(black1);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  cat_name = name_cat.getText().toString();
                  System.out.println(cat_name);
                  cat_type = name_type.getText().toString();
                  System.out.println(cat_type);
                  cat_bd = ago1.getText().toString();
                  System.out.println(cat_bd);
                  cat_weight = nam1.getText().toString();
                  System.out.println(cat_weight);
                  health_check_date = date1.getText().toString();
                  System.out.println(health_check_date);
                  latest_donation = blooddate.getText().toString();
                  System.out.println(latest_donation);

                    progressDialog.show();
                    progressDialog.setCancelable(false);

                  if (file == ""){
                      Toast.makeText(getApplicationContext(),"กรุณาเลือกรูปภาพ", Toast.LENGTH_LONG).show();
                  }else {
                      if (cat_name.matches("") || cat_type.matches("") || blood_type.matches("กรุ๊ปเลือด") || cat_bd.matches("1") || cat_weight.matches("2")
                              || cat_weight.matches("1") || health_check_date.matches("") || latest_donation.matches("")){
                          Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                      } else {

                          UploadcareClient client = new UploadcareClient(BuildConfig.UPLOADCARE_PUB_KEY,BuildConfig.UPLOADCARE_PRI_KEY);
                          Context context = getApplicationContext();
                          Uploader uploader = new FileUploader(client,uri,context).store(true);
                          uploader.uploadAsync(new UploadcareFileCallback() {
                              @Override
                              public void onFailure(@NotNull UploadcareApiException e) {
                                    System.out.println(e.getMessage());
                              }

                              @Override
                              public void onSuccess(UploadcareFile uploadcareFile) {
                                  System.out.println(uploadcareFile.getOriginalFileUrl());
                                  response_image = uploadcareFile.getOriginalFileUrl().toString();
                                  if (response_image != null || response_image != ""){
                                      AddCat(cat_name, cat_type, blood_type, cat_bd, cat_weight, health_check_date, latest_donation, response_image);
                                  }
                              }
                          });
                          progressDialog.dismiss();
                      }
                  }
            }
        });


        sapin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        blood_type = adapterView.getSelectedItem().toString();
                       System.out.println(blood_type);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );
    }
    private void AddCat(String cat_name, String cat_type, String blood_type, String cat_bd, String cat_weight
                        , String health_check_date, String latest_donation, String Url_image){
        Call<Void> call = phpServiceAPI.AddCat(cat_name,cat_type,blood_type,cat_bd,cat_weight,health_check_date,latest_donation,session.getUserId(),Url_image);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(),"บันทึกข้อมูลสำเร็จ", Toast.LENGTH_LONG).show();

                Intent ok = new Intent(Addcat_info.this, User_Cat_list.class);
                startActivity(ok);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
//        progressDialog.dismiss();
    }

    private void pickFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg","image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData()!= null){
            uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                image.setImageBitmap(bitmap);
                file = uri.getPath();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
