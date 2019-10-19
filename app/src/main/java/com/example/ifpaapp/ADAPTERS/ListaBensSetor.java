package com.example.ifpaapp.ADAPTERS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifpaapp.ConsultarBensPorSetor;
import com.example.ifpaapp.EditarBensSetor;
import com.example.ifpaapp.DADOS.DadosBens;
import com.example.ifpaapp.DADOS.DadosSetores;
import com.example.ifpaapp.R;
import com.example.ifpaapp.REPOSITORIOS.RepositorioBens;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaBensSetor extends BaseAdapter {
    private static LayoutInflater layoutInflater = null;
    List<DadosBens> bens = new ArrayList<DadosBens>();
    RepositorioBens repositorioBens;
    private ConsultarBensPorSetor consultarBensPorSetor;

    public ListaBensSetor(ConsultarBensPorSetor consultarBensPorSetor, List<DadosBens> bens) {
        this.bens = bens;
        this.consultarBensPorSetor = consultarBensPorSetor;
        this.layoutInflater = (LayoutInflater) this.consultarBensPorSetor.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.repositorioBens = new RepositorioBens(consultarBensPorSetor);
    }

    @Override
    public int getCount() {
        return bens.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = layoutInflater.inflate(R.layout.lista_bens_setor, null);

        TextView id_bem1 = (TextView) view.findViewById(R.id.text_id_bem2);
        TextView descricao_marca1 = (TextView) view.findViewById(R.id.text_descricao_marca2);
        TextView codigo1 = (TextView) view.findViewById(R.id.text_codigo2);
        TextView data_entrada1 = (TextView) view.findViewById(R.id.text_data_entrada2);
        TextView valor1 = (TextView) view.findViewById(R.id.text_valor2);
        TextView status1 = (TextView) view.findViewById(R.id.text_status2);
        TextView categoria1 = (TextView) view.findViewById(R.id.text_categoria2);
        Button editar1 = (Button) view.findViewById(R.id.button_editar);
        Button excluir1 = (Button) view.findViewById(R.id.button_excluir);

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(bens.get(position).getValor()));

        id_bem1.setText("ID do Bem: " + bens.get(position).getId_bem());
        descricao_marca1.setText(bens.get(position).getDescricao() + " - " + bens.get(position).getMarca());
        codigo1.setText("Código do Bem: " + bens.get(position).getCodigo());
        data_entrada1.setText("Data de Entrada: " + bens.get(position).getData_entrada());
        valor1.setText("Valor: " + numberFormat.format(bigDecimal));
        status1.setText("Status do Bem: " + this.getStatus(bens.get(position).getStatus()));
        categoria1.setText("Categoria: " + this.getCategoria(bens.get(position).getCategoria()));

        editar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRedirecionar = new Intent(consultarBensPorSetor, EditarBensSetor.class);
                intentRedirecionar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentRedirecionar.putExtra("id_bem", bens.get(position).getId_bem());
                consultarBensPorSetor.startActivity(intentRedirecionar);
                consultarBensPorSetor.finish();
            }
        });

        excluir1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(consultarBensPorSetor);
                alert.setMessage("Deseja realmente excluir este item?");
                alert.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        repositorioBens.excluirBens(bens.get(position).getId_bem());
                        Toast.makeText(consultarBensPorSetor, "Registro excluído com sucesso.",
                                Toast.LENGTH_SHORT).show();
                        atualizarLista();
                    }
                });
                alert.setNegativeButton("NÃO", null);
                alert.show();
            }
        });

        return view;
    }

    public String getStatus(String siglaStatus) {
        if (siglaStatus.equals("Antieconômico"))
            return "Antieconômico";
        else if (siglaStatus.equals("Bom"))
            return "Bom";
        else if (siglaStatus.equals("Irrecuperável"))
            return "Irrecuperável";
        else if (siglaStatus.equals("Ocioso"))
            return "Ocioso";
        else
            return "Recuperável";
    }

    public String getCategoria(String codigoCategoria) {
        if (codigoCategoria.equals("44.90.52.06"))
            return "Apar. e Equip. de Comunicação";
        else if (codigoCategoria.equals("44.90.52.35"))
            return "Equipamentos de Proc. de Dados";
        else
            return "Máq. Instal. e Utens. de Escritório";
    }

    public void atualizarLista() {
        this.bens.clear();
        Spinner selecionar_setor1 = (Spinner) consultarBensPorSetor.findViewById(R.id.spinner_selecionar_setor);
        DadosSetores dadosSetores = (DadosSetores) selecionar_setor1.getSelectedItem();
        this.bens = repositorioBens.selecionarBensPorSetor(dadosSetores.getSigla());

        BigDecimal somatoria = new BigDecimal("0.00");
        for (DadosBens dadosBens : bens) {
            BigDecimal valorBem = new BigDecimal(dadosBens.getValor());
            somatoria = somatoria.add(valorBem);
        }

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        consultarBensPorSetor.valor_total.setText("Valor Total do Setor: " + numberFormat.format(somatoria));

        this.notifyDataSetChanged();
    }
}
