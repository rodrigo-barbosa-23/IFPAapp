package com.example.ifpaapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ifpaapp.DADOS.DadosBens;
import com.example.ifpaapp.DADOS.DadosCategorias;
import com.example.ifpaapp.DADOS.DadosRelatorios;
import com.example.ifpaapp.REPOSITORIOS.RepositorioBens;
import com.example.ifpaapp.TEMPLATES.TemplateHeaderFooter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GerarRelatorioPorCategoria extends AppCompatActivity {
    Spinner selecionar_categoria;
    EditText responsavel, data_referente;
    Button baixar_pdf, cancelar;

    private BigDecimal somatoria = new BigDecimal("0.00");

    private static final int STORAGE_CODE = 1000;

    private Font fontTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private Font fontTextBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private Font fontTextBoldSmall = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
    private Font fontTextTable = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerar_relatorio_por_categoria);

        selecionar_categoria = findViewById(R.id.spinner_categoria);
        responsavel = findViewById(R.id.edit_responsavel);
        data_referente = findViewById(R.id.edit_data_referente);
        baixar_pdf = findViewById(R.id.button_baixar_pdf);
        cancelar = findViewById(R.id.button_cancelar);

        this.carregarCategorias();

        baixar_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responsavel.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GerarRelatorioPorCategoria.this);
                    builder.setMessage("O campo 'RESPONSÁVEL PELO RELATÓRIO' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    responsavel.requestFocus();
                } else if (data_referente.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GerarRelatorioPorCategoria.this);
                    builder.setMessage("O campo 'MÊS/ANO DO RELATÓRIO' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    data_referente.requestFocus();
                } else {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, STORAGE_CODE);
                        } else {
                            gerarPDF();
                        }
                    } else {
                        gerarPDF();
                    }
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GerarRelatorioPorCategoria.this, PatrimonioMovel.class);
                startActivity(intent);
                finish();
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    gerarPDF();
                else
                    Toast.makeText(getApplicationContext(), "Permissão negada.",
                            Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void gerarPDF() {
        Document document = new Document(PageSize.A4.rotate(), 30, 20, 30, 20);
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String filePath = Environment.getExternalStorageDirectory() + "/" + fileName + ".pdf";

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));

            TemplateHeaderFooter templateHeaderFooter = new TemplateHeaderFooter();
            pdfWriter.setPageEvent(templateHeaderFooter);

            document.open();

            document.addAuthor("IFPAapp");

            InputStream inputStream = getAssets().open("logo.PNG");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Element.ALIGN_CENTER);
            image.setSpacingAfter(10);

            DadosRelatorios dadosRelatorios = new DadosRelatorios();

            DadosCategorias dadosCategorias = (DadosCategorias) selecionar_categoria.getSelectedItem();
            dadosRelatorios.setCategoria(dadosCategorias.getCodigo());

            String responsavel1 = responsavel.getText().toString();
            String data_referente1 = data_referente.getText().toString();

            Paragraph paragraphTitulo = new Paragraph();
            paragraphTitulo.add(new Phrase("RELATÓRIO MENSAL DE BENS MÓVEIS - RMB", fontTitle));
            paragraphTitulo.setAlignment(Element.ALIGN_CENTER);
            paragraphTitulo.setSpacingAfter(20);

            Paragraph paragraphResponsavel = new Paragraph();
            paragraphResponsavel.add(new Phrase("Responsável pelo Relatório: ", fontTextBold));
            paragraphResponsavel.add(responsavel1);
            paragraphResponsavel.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraphResponsavel.setSpacingAfter(5);

            Paragraph paragraphDataReferente = new Paragraph();
            paragraphDataReferente.add(new Phrase("Mês e Ano Referente ao Relatório: ", fontTextBold));
            paragraphDataReferente.add(data_referente1);
            paragraphDataReferente.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraphDataReferente.setSpacingAfter(15);

            Paragraph paragraphTabelaBens = new Paragraph();
            paragraphTabelaBens.add(new Phrase("TABELA DE BENS\n", fontTextBold));
            paragraphTabelaBens.add("Categoria: " + String.valueOf(dadosCategorias));
            paragraphTabelaBens.setAlignment(Element.ALIGN_CENTER);
            paragraphTabelaBens.setSpacingAfter(10);

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            Paragraph paragraphAssinatura1 = new Paragraph();
            paragraphAssinatura1.add(new Phrase("____________________________________________\nAssinatura do Responsável"));
            paragraphAssinatura1.setAlignment(Element.ALIGN_CENTER);
            paragraphAssinatura1.setSpacingAfter(10);
            paragraphAssinatura1.setSpacingBefore(100);


            PdfPTable pdfPTable = new PdfPTable(7);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingAfter(3);

            PdfPCell cell;
            cell = new PdfPCell(new Phrase("CÓDIGO", fontTextBoldSmall));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPaddingBottom(10);
            cell.setPaddingLeft(5);
            cell.setPaddingRight(5);
            cell.setPaddingTop(10);
            pdfPTable.addCell(cell);

            cell = new PdfPCell(new Phrase("DESCRIÇÃO", fontTextBoldSmall));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPaddingBottom(10);
            cell.setPaddingLeft(5);
            cell.setPaddingRight(5);
            cell.setPaddingTop(10);
            pdfPTable.addCell(cell);

            cell = new PdfPCell(new Phrase("MARCA", fontTextBoldSmall));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPaddingBottom(10);
            cell.setPaddingLeft(5);
            cell.setPaddingRight(5);
            cell.setPaddingTop(10);
            pdfPTable.addCell(cell);

            cell = new PdfPCell(new Phrase("DATA DE ENTRADA NO ALMOXARIFADO", fontTextBoldSmall));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPaddingBottom(10);
            cell.setPaddingLeft(5);
            cell.setPaddingRight(5);
            cell.setPaddingTop(10);
            pdfPTable.addCell(cell);

            cell = new PdfPCell(new Phrase("STATUS", fontTextBoldSmall));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPaddingBottom(10);
            cell.setPaddingLeft(5);
            cell.setPaddingRight(5);
            cell.setPaddingTop(10);
            pdfPTable.addCell(cell);

            cell = new PdfPCell(new Phrase("SETOR", fontTextBoldSmall));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPaddingBottom(10);
            cell.setPaddingLeft(5);
            cell.setPaddingRight(5);
            cell.setPaddingTop(10);
            pdfPTable.addCell(cell);

            cell = new PdfPCell(new Phrase("VALOR", fontTextBoldSmall));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPaddingBottom(10);
            cell.setPaddingLeft(5);
            cell.setPaddingRight(5);
            cell.setPaddingTop(10);
            pdfPTable.addCell(cell);

            List<List<String>> dataset = getBens();
            for (List<String> record : dataset) {
                for (String field : record) {
                    cell = new PdfPCell(new Phrase(field));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.WHITE);
                    cell.setPadding(5);
                    pdfPTable.addCell(cell);
                }
            }

            Paragraph paragraphValorTotal = new Paragraph();
            paragraphValorTotal.add(new Phrase("Valor Total da Categoria: ", fontTextBold));
            paragraphValorTotal.add(numberFormat.format(somatoria));
            paragraphValorTotal.setAlignment(Element.ALIGN_RIGHT);
            paragraphValorTotal.setSpacingAfter(10);

            document.add(image);
            document.add(paragraphTitulo);
            document.add(paragraphResponsavel);
            document.add(paragraphDataReferente);
            document.add(paragraphTabelaBens);
            document.add(pdfPTable);
            document.add(paragraphValorTotal);
            document.add(paragraphAssinatura1);

            document.close();

            Toast.makeText(this, fileName + ".pdf\nis saved to\n" + filePath, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        limparCampos();
        finish();
    }

    public List<List<String>> getBens() {
        List<List<String>> data = new ArrayList<List<String>>();

        RepositorioBens repositorioBens = new RepositorioBens(this);
        DadosCategorias dadosCategorias = (DadosCategorias) selecionar_categoria.getSelectedItem();
        String categoria = dadosCategorias.getCodigo();

        List<DadosBens> bens = repositorioBens.selecionarBensPorCategoria(categoria);

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        for (DadosBens bem : bens) {
            List<String> dataLine = new ArrayList<String>();
            dataLine.add(bem.getCodigo());
            dataLine.add(bem.getDescricao());
            dataLine.add(bem.getMarca());
            dataLine.add(bem.getData_entrada());
            dataLine.add(bem.getStatus());
            dataLine.add(bem.getSetor());

            BigDecimal bigDecimal = new BigDecimal(bem.getValor());
            dataLine.add(numberFormat.format(bigDecimal));

            somatoria = somatoria.add(bigDecimal);

            data.add(dataLine);
        }

        return data;
    }

    protected void limparCampos() {
        responsavel.setText("");
        data_referente.setText("");
    }
}
