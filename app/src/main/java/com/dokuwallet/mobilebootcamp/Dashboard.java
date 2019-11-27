package com.dokuwallet.mobilebootcamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

public class Dashboard  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String userName = getIntent().getStringExtra("userName");
        TextView textviewDashboardWelcome = findViewById(R.id.textview_dashboard_welcome);
        if (userName != null) {
            textviewDashboardWelcome.setText(userName);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v ->finish());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Button buttonDashboardOpen = findViewById(R.id.button_dashboard_open);
        buttonDashboardOpen.setOnClickListener(v -> {
            Intent intentMain = new Intent(Dashboard.this, Profile.class);
            startActivity(intentMain);
        });
    }
}
