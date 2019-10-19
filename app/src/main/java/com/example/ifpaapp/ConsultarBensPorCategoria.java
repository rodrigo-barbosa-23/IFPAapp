package com.example.ifpaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ifpaapp.ADAPTERS.ListaBensCategoria;
import com.example.ifpaapp.DADOS.DadosBens;
import com.example.ifpaapp.DADOS.DadosCategorias;
import com.example.ifpaapp.REPOSITORIOS.RepositorioBens;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConsultarBensPorCategoria extends AppCompatActivity {
    public Spinner selecionar_categoria;
    public TextView valor_total;
    ListView lista_bens_categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_bens_por_categoria);

        selecionar_categoria = findViewById(R.id.spinner_selecionar_categoria);
        valor_total = findViewById(R.id.text_valor_total);

        selecionar_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DadosCategorias dadosCategorias = (DadosCategorias) adapterView.getItemAtPosition(i);
                carregarBensPorCategoria(dadosCategorias.getCodigo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lista_bens_categoria = this.findViewById(R.id.list_bens2);
        this.carregarCategorias();
        this.carregarBensCadastrados();
    }

    private void carregarBensCadastrados() {
        RepositorioBens repositorioBens = new RepositorioBens(this);

        List<DadosBens> bens = repositorioBens.selecionarTodosBens();

        lista_bens_categoria.setAdapter(new ListaBensCategoria(this, bens));
    }

    private void carregarBensPorCategoria(String categoria) {
        RepositorioBens repositorioBens = new RepositorioBens(this);

        List<DadosBens> bens = repositorioBens.selecionarBensPorCategoria(categoria);

        BigDecimal somatoria = new BigDecimal("0.00");
        for (DadosBens dadosBens : bens) {
            BigDecimal valorBem = new BigDecimal(dadosBens.getValor());
            somatoria = somatoria.add(valorBem);
        }

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        valor_total.setText("Valor Total da Categoria: " + numberFormat.format(somatoria));

        lista_bens_categoria.setAdapter(new ListaBensCategoria(this, bens));
    }

    protected void carregarCategorias() {
        ArrayAdapter<DadosCategorias> arrayAdapter;

        List<DadosCategorias> itens = new ArrayList<DadosCategorias>();

        itens.add(new DadosCategorias("44.90.52.06", "Apar. e Equip. de Comunicação"));
        itens.add(new DadosCategorias("44.90.52.35", "Equipamentos de Proc. de Dados"));
        itens.add(new DadosCategorias("44.90.52.36", "Máq. Instal. e Utens. de Escritório"));

        arrayAdapter = new ArrayAdapter<DadosCategorias>(this, android.R.layout.simple_spinner_item, itens);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selecionar_categoria.setAdapter(arrayAdapter);
    }
}
