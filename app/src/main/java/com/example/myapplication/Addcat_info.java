package com.example.myapplication;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.services.PHPServiceAPI;
import com.example.myapplication.services.RetrofitInstance;
import com.uploadcare.android.library.api.UploadcareClient;
import com.uploadcare.android.library.api.UploadcareFile;
import com.uploadcare.android.library.callbacks.UploadcareFileCallback;
import com.uploadcare.android.library.exceptions.UploadcareApiException;
import com.uploadcare.android.library.upload.FileUploader;
import com.uploadcare.android.library.upload.Uploader;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Addcat_info extends AppCompatActivity {

    private PHPServiceAPI phpServiceAPI;

    private TextView hcd,ld;

    private EditText name_cat,name_type, ago1, nam1;
    private Spinner sapin;
    private Button ok;
    private ImageView image;
    private Uri uri = null;
    private String cat_name,cat_type,blood_type,cat_bd,cat_weight,health_check_date,latest_donation = "";
    private ImageView imageblack,cal1,cal2;
    private int GALLERY_REQUEST_CODE = 1;

    private int intDay,intMonth,intYear;

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

        getCurrentDateTime();

        name_cat = (EditText) findViewById(R.id.name_cat);
        name_type = (EditText) findViewById(R.id.name_type);
        ago1 = (EditText) findViewById(R.id.ago1);
        nam1 = (EditText) findViewById(R.id.nam1);

        hcd = (TextView) findViewById(R.id.date);
        ld = (TextView) findViewById(R.id.blooddate);

        cal1 = (ImageView) findViewById(R.id.cal1);
        cal2 = (ImageView) findViewById(R.id.cal2);
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

        cal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        cal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog1();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 progressDialog.show();
                 progressDialog.setCancelable(false);

                  cat_name = name_cat.getText().toString();
                  System.out.println(cat_name);
                  cat_type = name_type.getText().toString();
                  System.out.println(cat_type);
                  cat_bd = ago1.getText().toString();
                  System.out.println(cat_bd);
                  cat_weight = nam1.getText().toString();
                  System.out.println(cat_weight);

                  if (file == ""){
                      Toast.makeText(getApplicationContext(),"กรุณาเลือกรูปภาพ", Toast.LENGTH_LONG).show();
                      progressDialog.dismiss();
                  }else {
                      if (cat_name.matches("") || cat_type.matches("") || blood_type.matches("กรุ๊ปเลือด") || cat_bd.matches("1") || cat_weight.matches("2")
                              || cat_weight.matches("1") || health_check_date.matches("") || latest_donation.matches("")) {
                          Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                          progressDialog.dismiss();
                      } else {

                          if (Integer.parseInt(cat_bd) < 3 || Integer.parseInt(cat_weight) < 3){
                              progressDialog.dismiss();
                              Toast.makeText(getApplicationContext(),"แมวของคุณต้องมีน้ำหนัก 3 kg และอายุ 3 ปีขึ้นไป",Toast.LENGTH_LONG).show();
                          }else {
                              UploadcareClient client = new UploadcareClient(BuildConfig.UPLOADCARE_PUB_KEY, BuildConfig.UPLOADCARE_PRI_KEY);
                              Context context = getApplicationContext();
                              Uploader uploader = new FileUploader(client, uri, context).store(true);
                              uploader.uploadAsync(new UploadcareFileCallback() {
                                  @Override
                                  public void onFailure(@NotNull UploadcareApiException e) {
                                      System.out.println(e.getMessage());
                                      Toast.makeText(getApplicationContext(),"อัพโหลดไม่สำเร็จ กรุณาลองอีกครั้ง",Toast.LENGTH_LONG).show();
                                      progressDialog.dismiss();
                                  }

                                  @Override
                                  public void onSuccess(UploadcareFile uploadcareFile) {
                                      System.out.println(uploadcareFile.getOriginalFileUrl());
                                      response_image = uploadcareFile.getOriginalFileUrl().toString();
                                      if (response_image != null || response_image != "") {
                                          AddCat(cat_name, cat_type, blood_type, cat_bd, cat_weight, health_check_date, latest_donation, response_image);
                                          progressDialog.dismiss();
                                      }
                                  }
                              });
                          }
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
        progressDialog.dismiss();
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
    private void getCurrentDateTime(){
        Calendar calendar = Calendar.getInstance();
        intDay = calendar.get(Calendar.DAY_OF_MONTH);
        intMonth = calendar.get(Calendar.MONTH);
        intYear = calendar.get(Calendar.YEAR);

//        date.setText(intYear+"/"+intMonth+"/"+intDay);
    }
    private void showDateDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(Addcat_info.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month = month +1;
                        hcd.setText(year+"/"+month+"/"+day);
                        System.out.println(hcd.getText());
                        health_check_date = hcd.getText().toString();
                        System.out.println(health_check_date);
                    }
                },intYear,intMonth,intDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private void showDateDialog1(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(Addcat_info.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month +1;
                        ld.setText(year+"/"+month+"/"+day);
                        System.out.println(ld.getText());
                        latest_donation = ld.getText().toString();
                        System.out.println(latest_donation);
                    }
                },intYear,intMonth,intDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}
