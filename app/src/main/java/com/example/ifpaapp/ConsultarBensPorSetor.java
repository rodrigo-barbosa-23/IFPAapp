package com.example.ifpaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ifpaapp.ADAPTERS.ListaBensSetor;
import com.example.ifpaapp.DADOS.DadosBens;
import com.example.ifpaapp.DADOS.DadosSetores;
import com.example.ifpaapp.REPOSITORIOS.RepositorioBens;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConsultarBensPorSetor extends AppCompatActivity {
    public Spinner selecionar_setor;
    public TextView valor_total;
    ListView lista_bens_setor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_bens_por_setor);

        selecionar_setor = findViewById(R.id.spinner_selecionar_setor);
        valor_total = findViewById(R.id.text_valor_total);

        selecionar_setor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DadosSetores dadosSetores = (DadosSetores) adapterView.getItemAtPosition(i);
                carregarBensPorSetor(dadosSetores.getSigla());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lista_bens_setor = this.findViewById(R.id.list_bens);
        this.carregarSetores();
        this.carregarBensCadastrados();
    }

    private void carregarBensCadastrados() {
        RepositorioBens repositorioBens = new RepositorioBens(this);

        List<DadosBens> bens = repositorioBens.selecionarTodosBens();

        lista_bens_setor.setAdapter(new ListaBensSetor(this, bens));
    }

    private void carregarBensPorSetor(String setor) {
        RepositorioBens repositorioBens = new RepositorioBens(this);

        List<DadosBens> bens = repositorioBens.selecionarBensPorSetor(setor);

        BigDecimal somatoria = new BigDecimal("0.00");
        for (DadosBens dadosBens : bens) {
            BigDecimal valorBem = new BigDecimal(dadosBens.getValor());
            somatoria = somatoria.add(valorBem);
        }

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        valor_total.setText("Valor Total do Setor: " + numberFormat.format(somatoria));

        lista_bens_setor.setAdapter(new ListaBensSetor(this, bens));
    }

    protected void carregarSetores() {
        ArrayAdapter<DadosSetores> arrayAdapter;

        List<DadosSetores> itens = new ArrayList<DadosSetores>();

        itens.add(new DadosSetores("DITIN", "Divisão de Tecnologia da Informação"));
        itens.add(new DadosSetores("PROAD", "Pró-Reitoria de Administração"));
        itens.add(new DadosSetores("RH", "Recursos Humanos"));

        arrayAdapter = new ArrayAdapter<DadosSetores>(this, android.R.layout.simple_spinner_item, itens);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selecionar_setor.setAdapter(arrayAdapter);
    }
}
