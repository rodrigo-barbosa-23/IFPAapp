package com.example.ifpaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PatrimonioMovel extends AppCompatActivity {
    Button cadastrar_bens, consultar_bens_setor, consultar_bens_categoria;
    Button gerar_relatorio_setor, gerar_relatorio_categoria;
    Button categorias_bens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrimonio_movel);

        cadastrar_bens = findViewById(R.id.button_bens_cadastro);
        consultar_bens_setor = findViewById(R.id.button_bens_consulta_setor);
        consultar_bens_categoria = findViewById(R.id.button_bens_consulta_categoria);
        gerar_relatorio_setor = findViewById(R.id.button_relatorio_setor);
        gerar_relatorio_categoria = findViewById(R.id.button_relatorio_categoria);
        categorias_bens = findViewById(R.id.button_categorias_bens);

        cadastrar_bens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatrimonioMovel.this, CadastrarBens.class);
                startActivity(intent);
            }
        });

        consultar_bens_setor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatrimonioMovel.this, ConsultarBensPorSetor.class);
                startActivity(intent);
            }
        });

        consultar_bens_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatrimonioMovel.this, ConsultarBensPorCategoria.class);
                startActivity(intent);
            }
        });

        gerar_relatorio_setor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatrimonioMovel.this, GerarRelatorioPorSetor.class);
                startActivity(intent);
            }
        });

        gerar_relatorio_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatrimonioMovel.this, GerarRelatorioPorCategoria.class);
                startActivity(intent);
            }
        });

        categorias_bens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatrimonioMovel.this, CategoriasBens.class);
                startActivity(intent);
            }
        });
    }
}
