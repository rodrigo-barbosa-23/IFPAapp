package com.example.ifpaapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.ifpaapp.DADOS.DadosBens;
import com.example.ifpaapp.DADOS.DadosCategorias;
import com.example.ifpaapp.DADOS.DadosSetores;
import com.example.ifpaapp.DADOS.DadosStatus;
import com.example.ifpaapp.REPOSITORIOS.RepositorioBens;

import java.util.ArrayList;
import java.util.List;

public class EditarBensSetor extends AppCompatActivity {
    Spinner setor, status, categoria;
    EditText codigo, data_entrada, descricao, marca;
    CurrencyEditText valor;
    Button alterar, cancelar;

    ArrayAdapter<DadosSetores> arrayAdapterSetor;
    ArrayAdapter<DadosStatus> arrayAdapterStatus;
    ArrayAdapter<DadosCategorias> arrayAdapterCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_bens_setor);

        setor = findViewById(R.id.spinner_setor);
        status = findViewById(R.id.spinner_status);
        categoria = findViewById(R.id.spinner_categoria);
        codigo = findViewById(R.id.edit_codigo);
        data_entrada = findViewById(R.id.edit_data_entrada);
        descricao = findViewById(R.id.edit_descricao);
        marca = findViewById(R.id.edit_marca);
        valor = findViewById(R.id.edit_valor);
        alterar = findViewById(R.id.button_alterar);
        cancelar = findViewById(R.id.button_cancelar);

        this.carregarSetores();
        this.carregarStatus();
        this.carregarCategorias();
        this.carregarValoresCampos();

        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codigo.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditarBensSetor.this);
                    builder.setMessage("O campo 'CÓDIGO DO BEM' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    codigo.requestFocus();
                } else if (data_entrada.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditarBensSetor.this);
                    builder.setMessage("O campo 'DATA DE ENTRADA NO ALMOXARIFADO' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    data_entrada.requestFocus();
                } else if (descricao.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditarBensSetor.this);
                    builder.setMessage("O campo 'DESCRIÇÃO' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    descricao.requestFocus();
                } else if (marca.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditarBensSetor.this);
                    builder.setMessage("O campo 'MARCA' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    marca.requestFocus();
                } else {
                    alterarBens();
                    Toast.makeText(getApplicationContext(), "Registro alterado com sucesso.",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarBensSetor.this, ConsultarBensPorSetor.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarBensSetor.this, ConsultarBensPorSetor.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void carregarSetores() {

        List<DadosSetores> itens = new ArrayList<DadosSetores>();

        itens.add(new DadosSetores("DITIN", "Divisão de Tecnologia da Informação"));
        itens.add(new DadosSetores("PROAD", "Pró-Reitoria de Administração"));
        itens.add(new DadosSetores("RH", "Recursos Humanos"));

        arrayAdapterSetor = new ArrayAdapter<DadosSetores>(this, android.R.layout.simple_spinner_item, itens);
        arrayAdapterSetor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setor.setAdapter(arrayAdapterSetor);
    }

    protected void posicionarSetor(String chaveSetor) {

        for (int index = 0; index < arrayAdapterSetor.getCount(); index++) {

            if (((DadosSetores) arrayAdapterSetor.getItem(index)).getSigla().equals(chaveSetor)) {
                setor.setSelection(index);
                break;
            }
        }
    }

    protected void carregarStatus() {

        List<DadosStatus> itens = new ArrayList<DadosStatus>();

        itens.add(new DadosStatus("Antieconômico", "Antieconômico"));
        itens.add(new DadosStatus("Bom", "Bom"));
        itens.add(new DadosStatus("Irrecuperável", "Irrecuperável"));
        itens.add(new DadosStatus("Ocioso", "Ocioso"));
        itens.add(new DadosStatus("Recuperável", "Recuperável"));

        arrayAdapterStatus = new ArrayAdapter<DadosStatus>(this, android.R.layout.simple_spinner_item, itens);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(arrayAdapterStatus);
    }

    protected void posicionarStatus(String chaveStatus) {

        for (int index = 0; index < arrayAdapterStatus.getCount(); index++) {

            if (((DadosStatus) arrayAdapterStatus.getItem(index)).getSigla().equals(chaveStatus)) {
                status.setSelection(index);
                break;
            }
        }
    }

    protected void carregarCategorias() {

        List<DadosCategorias> itens = new ArrayList<DadosCategorias>();

        itens.add(new DadosCategorias("44.90.52.06", "Apar. e Equip. de Comunicação"));
        itens.add(new DadosCategorias("44.90.52.35", "Equipamentos de Proc. de Dados"));
        itens.add(new DadosCategorias("44.90.52.36", "Máq. Instal. e Utens. de Escritório"));

        arrayAdapterCategoria = new ArrayAdapter<DadosCategorias>(this, android.R.layout.simple_spinner_item, itens);
        arrayAdapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(arrayAdapterCategoria);
    }

    protected void posicionarCategoria(String chaveCategoria) {

        for (int index = 0; index < arrayAdapterCategoria.getCount(); index++) {

            if (((DadosCategorias) arrayAdapterCategoria.getItem(index)).getCodigo().equals(chaveCategoria)) {
                categoria.setSelection(index);
                break;
            }
        }
    }

    protected void carregarValoresCampos() {

        RepositorioBens repositorioBens = new RepositorioBens(this);

        Bundle extra = this.getIntent().getExtras();
        int id_bem = extra.getInt("id_bem");

        DadosBens dadosBens = repositorioBens.getBem(id_bem);

        this.posicionarSetor(dadosBens.getSetor());

        codigo.setText(dadosBens.getCodigo());
        data_entrada.setText(dadosBens.getData_entrada());
        descricao.setText(dadosBens.getDescricao());
        marca.setText(dadosBens.getMarca());
        valor.setText(dadosBens.getValor());

        this.posicionarStatus(dadosBens.getStatus());
        this.posicionarCategoria(dadosBens.getCategoria());
    }

    protected void alterarBens() {
        DadosBens dadosBens = new DadosBens();

        Bundle extra = this.getIntent().getExtras();
        int id_bem = extra.getInt("id_bem");

        DadosSetores dadosSetores = (DadosSetores) setor.getSelectedItem();
        dadosBens.setSetor(dadosSetores.getSigla());

        dadosBens.setCodigo(codigo.getText().toString().trim());
        dadosBens.setData_entrada(data_entrada.getText().toString().trim());
        dadosBens.setDescricao(descricao.getText().toString().trim());
        dadosBens.setMarca(marca.getText().toString().trim());

        String valorInicial = String.valueOf(valor.getRawValue());
        String valorFinal = "";

        if (valorInicial.length() > 2)
            valorFinal = valorInicial.substring(0, valorInicial.length() - 2) + "." + valorInicial.substring(valorInicial.length() - 2, valorInicial.length());
        else if (valorInicial.length() == 2)
            valorFinal = "0." + valorInicial;
        else if (valorInicial.length() == 1)
            valorFinal = "0.0" + valorInicial;
        else
            valorFinal = "0.00";

        dadosBens.setValor(valorFinal);

        DadosStatus dadosStatus = (DadosStatus) status.getSelectedItem();
        dadosBens.setStatus(dadosStatus.getSigla());

        DadosCategorias dadosCategorias = (DadosCategorias) categoria.getSelectedItem();
        dadosBens.setCategoria(dadosCategorias.getCodigo());

        RepositorioBens repositorioBens = new RepositorioBens(this);
        repositorioBens.atualizarBens(dadosBens, id_bem);
    }
}
