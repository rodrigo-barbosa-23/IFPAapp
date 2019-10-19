package com.example.ifpaapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuApp extends AppCompatActivity {
    Button patrimonio_imovel, patrimonio_movel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_app);

        patrimonio_imovel = findViewById(R.id.button_patrimonio_imovel);
        patrimonio_movel = findViewById(R.id.button_patrimonio_movel);

        patrimonio_imovel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuApp.this);
                builder.setMessage("Ainda não há funcionalidades disponíveis para este módulo.");
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        });

        patrimonio_movel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuApp.this, PatrimonioMovel.class);
                startActivity(intent);
            }
        });
    }
}
