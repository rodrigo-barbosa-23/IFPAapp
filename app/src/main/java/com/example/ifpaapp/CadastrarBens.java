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

public class CadastrarBens extends AppCompatActivity {
    Spinner setor, status, categoria;
    EditText codigo, data_entrada, descricao, marca;
    CurrencyEditText valor;
    Button cadastrar, cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_bens);

        setor = findViewById(R.id.spinner_setor);
        status = findViewById(R.id.spinner_status);
        categoria = findViewById(R.id.spinner_categoria);
        codigo = findViewById(R.id.edit_codigo);
        data_entrada = findViewById(R.id.edit_data_entrada);
        descricao = findViewById(R.id.edit_descricao);
        marca = findViewById(R.id.edit_marca);
        valor = findViewById(R.id.edit_valor);
        cadastrar = findViewById(R.id.button_cadastrar);
        cancelar = findViewById(R.id.button_cancelar);

        this.carregarValoresSpinners();

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codigo.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarBens.this);
                    builder.setMessage("O campo 'CÓDIGO DO BEM' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    codigo.requestFocus();
                } else if (data_entrada.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarBens.this);
                    builder.setMessage("O campo 'DATA DE ENTRADA NO ALMOXARIFADO' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    data_entrada.requestFocus();
                } else if (descricao.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarBens.this);
                    builder.setMessage("O campo 'DESCRIÇÃO' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    descricao.requestFocus();
                } else if (marca.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarBens.this);
                    builder.setMessage("O campo 'MARCA' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    marca.requestFocus();
                } else {
                    cadastrarBens();
                    Toast.makeText(getApplicationContext(), "Registro salvo com sucesso.",
                            Toast.LENGTH_SHORT).show();
                    limparCampos();
                    finish();
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastrarBens.this, PatrimonioMovel.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void carregarValoresSpinners() {
        ArrayAdapter<DadosSetores> arrayAdapterSetor;

        List<DadosSetores> itensSetor = new ArrayList<DadosSetores>();

        itensSetor.add(new DadosSetores("DITIN", "Divisão de Tecnologia da Informação"));
        itensSetor.add(new DadosSetores("PROAD", "Pró-Reitoria de Administração"));
        itensSetor.add(new DadosSetores("RH", "Recursos Humanos"));

        arrayAdapterSetor = new ArrayAdapter<DadosSetores>(this, android.R.layout.simple_spinner_item, itensSetor);
        arrayAdapterSetor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setor.setAdapter(arrayAdapterSetor);

        /*----------------------------------------------------------------------------------------*/

        ArrayAdapter<DadosStatus> arrayAdapterStatus;

        List<DadosStatus> itensStatus = new ArrayList<DadosStatus>();

        itensStatus.add(new DadosStatus("Antieconômico", "Antieconômico"));
        itensStatus.add(new DadosStatus("Bom", "Bom"));
        itensStatus.add(new DadosStatus("Irrecuperável", "Irrecuperável"));
        itensStatus.add(new DadosStatus("Ocioso", "Ocioso"));
        itensStatus.add(new DadosStatus("Recuperável", "Recuperável"));

        arrayAdapterStatus = new ArrayAdapter<DadosStatus>(this, android.R.layout.simple_spinner_item, itensStatus);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(arrayAdapterStatus);

        /*----------------------------------------------------------------------------------------*/

        ArrayAdapter<DadosCategorias> arrayAdapterCategoria;

        List<DadosCategorias> itensCategoria = new ArrayList<DadosCategorias>();

        itensCategoria.add(new DadosCategorias("44.90.52.06", "Apar. e Equip. de Comunicação"));
        itensCategoria.add(new DadosCategorias("44.90.52.35", "Equipamentos de Proc. de Dados"));
        itensCategoria.add(new DadosCategorias("44.90.52.36", "Máq. Instal. e Utens. de Escritório"));

        arrayAdapterCategoria = new ArrayAdapter<DadosCategorias>(this, android.R.layout.simple_spinner_item, itensCategoria);
        arrayAdapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(arrayAdapterCategoria);
    }

    protected void cadastrarBens() {
        DadosBens dadosBens = new DadosBens();

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
        repositorioBens.cadastrarBens(dadosBens);
    }

    protected void limparCampos() {
        codigo.setText("");
        data_entrada.setText("");
        descricao.setText("");
        marca.setText("");
        valor.setText("");
    }
}
