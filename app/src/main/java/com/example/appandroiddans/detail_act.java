package com.example.appandroiddans;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class detail_act extends AppCompatActivity {

    ImageView back_btn, img_profile_company;
    TextView Company_tv, Location_tv, Company_url, Title_tv, type_tv, Desc_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        back_btn = findViewById(R.id.back_btn);
        img_profile_company = findViewById(R.id.img_profile_company);
        Company_tv = findViewById(R.id.Company_tv);
        Location_tv = findViewById(R.id.Location_tv);
        Company_url = findViewById(R.id.Company_url);
        Title_tv = findViewById(R.id.Title_tv);
        type_tv = findViewById(R.id.type_tv);
        Desc_tv = findViewById(R.id.Desc_tv);

        Bundle bundle = getIntent().getExtras();

        String mtitle = bundle.getString("title");
        String mloc   = bundle.getString("location");
        String murl   = bundle.getString("url");
        String mtype    = bundle.getString("type");
        String mcompany = bundle.getString("company");
        String mlogo    = bundle.getString("company_logo");
        String mdes     = bundle.getString("description");

        Glide.with(this).load(mlogo).into(img_profile_company);
        Company_tv.setText(mcompany);
        Location_tv.setText(mloc);
        Company_url.setText(murl);
        Title_tv.setText(mtitle);
        type_tv.setText(mtype);
        Desc_tv.setText(mdes);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void back()
    {
        Intent intent = new Intent(detail_act.this, dashboard_act.class);
        startActivity(intent);
        finish();
    }
}