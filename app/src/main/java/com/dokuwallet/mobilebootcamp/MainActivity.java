package com.dokuwallet.mobilebootcamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonElement;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Call<JsonElement> callData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView edittextMainUsername = findViewById(R.id.edittext_main_username);
        TextView edittexMainPassword = findViewById(R.id.edittext_main_password);

        progressBar = findViewById(R.id.progressBar_main);

        Button buttonMainSubmit = findViewById(R.id.Button_main_submit);
        buttonMainSubmit.setOnClickListener(v ->
        {
            if (!"".equals(edittextMainUsername.getText().toString()) && !"".equals(edittexMainPassword.getText().toString())) {
                progressBar.setVisibility(View.VISIBLE);
                doLogin("Login", edittextMainUsername.getText().toString(), edittexMainPassword.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Silahkan lengkapi Username dan Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doLogin(String reguestType, String username, String password) {
        ListApiInterface transactionApiInterface = ServiceGenerator.ApiService(BuildConfig.domainCop);
        callData = transactionApiInterface.Login(reguestType, username, password);
        callData.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response.body().toString());
                            if ("0000".equals(obj.getString("responseCode"))) {
                                Toast.makeText(getApplicationContext(), "Selamat Datang  " + username, Toast.LENGTH_SHORT).show();

                                Intent intentMain = new Intent(getApplicationContext(), DashboardBottomNavigation.class);
                                intentMain.putExtra("userName", username);
                                startActivity(intentMain);
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("responseMsg"), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            Log.e(getClass().getSimpleName(), "Exception message [" + e.getMessage() + "]");
                        }
                    } else {
                        call.cancel();
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Exception message [" + e.getMessage() + "]");
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                if (!call.isCanceled()) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
