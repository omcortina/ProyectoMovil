package com.example.turistmap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActivityPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent inicio = new Intent(ActivityPrincipal.this, IndexCliente.class);
                startActivity(inicio);
            }
        }, 4000);

    }
}
