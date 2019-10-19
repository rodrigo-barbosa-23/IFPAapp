package com.example.ifpaapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ifpaapp.DATABASE.DatabaseApp;

public class LogIn extends AppCompatActivity {
    DatabaseApp databaseApp;
    EditText usuario, senha;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        databaseApp = new DatabaseApp(this);

        usuario = findViewById(R.id.edit_usuario);
        senha = findViewById(R.id.edit_senha);
        login = findViewById(R.id.button_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario1 = usuario.getText().toString();
                String senha1 = senha.getText().toString();

                Boolean validacaoLogin = databaseApp.validarLogin(usuario1, senha1);
                if (validacaoLogin == true) {
                    Intent intent = new Intent(Login.this, MenuApp.class);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setMessage("Usuário e/ou senha inválidos.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
                limparCampos();
            }
        });
    }

    protected void limparCampos() {
        usuario.setText("");
        senha.setText("");
    }
}
