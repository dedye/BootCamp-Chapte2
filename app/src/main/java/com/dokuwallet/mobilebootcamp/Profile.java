package com.dokuwallet.mobilebootcamp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class Profile extends AppCompatActivity {
    private String TAG_FRAGMENT = "TAG_FRAGMENT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        FragmentTransaction fragdash = this.getSupportFragmentManager().beginTransaction();
        DataPribadi mFrag = new DataPribadi();

        fragdash.replace(R.id.main_frame, mFrag, TAG_FRAGMENT);
        fragdash.commit();
    }
}
