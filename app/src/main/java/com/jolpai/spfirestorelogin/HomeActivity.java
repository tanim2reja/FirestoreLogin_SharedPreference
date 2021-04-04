package com.jolpai.spfirestorelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.getSupportActionBar().setTitle("Home");

        TextView txtWelcome = findViewById(R.id.txtWelcome);

        txtWelcome.setText("Welcome to Home.");

        Button btnSignOut = findViewById(R.id.btnSignout);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.getSharedPreferences("SavedUser", 0).edit().clear().commit();
                navigateTo(LoginActivity.class);
                finish();
            }
        });
    }

    private void navigateTo(Class targetClass){
        Intent intent = new Intent(this, targetClass);
        startActivity(intent);
    }
}