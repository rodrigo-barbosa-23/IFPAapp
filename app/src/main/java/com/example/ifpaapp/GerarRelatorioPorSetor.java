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
import com.example.ifpaapp.DADOS.DadosLevantamentos;
import com.example.ifpaapp.DADOS.DadosSetores;
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

public class GerarRelatorioPorSetor extends AppCompatActivity {
    Spinner selecionar_setor;
    EditText responsavel, data_referente, descricao;
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
        setContentView(R.layout.gerar_relatorio_por_setor);

        selecionar_setor = findViewById(R.id.spinner_setor);
        responsavel = findViewById(R.id.edit_responsavel);
        data_referente = findViewById(R.id.edit_data_referente);
        descricao = findViewById(R.id.edit_descricao);
        baixar_pdf = findViewById(R.id.button_baixar_pdf);
        cancelar = findViewById(R.id.button_cancelar);

        this.carregarSetores();

        baixar_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responsavel.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GerarRelatorioPorSetor.this);
                    builder.setMessage("O campo 'RESPONSÁVEL PELO SETOR' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    responsavel.requestFocus();
                } else if (data_referente.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GerarRelatorioPorSetor.this);
                    builder.setMessage("O campo 'MÊS/ANO DO RELATÓRIO' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    data_referente.requestFocus();
                } else if (descricao.getText().toString().trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GerarRelatorioPorSetor.this);
                    builder.setMessage("O campo 'DESCRIÇÃO DO RELATÓRIO' é de preenchimento obrigatório.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    descricao.requestFocus();
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
                Intent intent = new Intent(GerarRelatorioPorSetor.this, PatrimonioMovel.class);
                startActivity(intent);
                finish();
            }
        });
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

            DadosLevantamentos dadosLevantamentos = new DadosLevantamentos();

            DadosSetores dadosSetores = (DadosSetores) selecionar_setor.getSelectedItem();
            dadosLevantamentos.setSetor(dadosSetores.getSigla());

            String responsavel1 = responsavel.getText().toString();
            String data_referente1 = data_referente.getText().toString();
            String descricao1 = descricao.getText().toString();

            Paragraph paragraphTitulo = new Paragraph();
            paragraphTitulo.add(new Phrase("LEVANTAMENTO PATRIMONIAL", fontTitle));
            paragraphTitulo.setAlignment(Element.ALIGN_CENTER);
            paragraphTitulo.setSpacingAfter(20);

            Paragraph paragraphSetor = new Paragraph();
            paragraphSetor.add(new Phrase("Setor: ", fontTextBold));
            paragraphSetor.add(String.valueOf(dadosSetores));
            paragraphSetor.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraphSetor.setSpacingAfter(5);

            Paragraph paragraphResponsavel = new Paragraph();
            paragraphResponsavel.add(new Phrase("Responsável pelo Setor: ", fontTextBold));
            paragraphResponsavel.add(responsavel1);
            paragraphResponsavel.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraphResponsavel.setSpacingAfter(5);

            Paragraph paragraphDataReferente = new Paragraph();
            paragraphDataReferente.add(new Phrase("Mês e Ano Referente ao Levantamento: ", fontTextBold));
            paragraphDataReferente.add(data_referente1);
            paragraphDataReferente.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraphDataReferente.setSpacingAfter(5);

            Paragraph paragraphDescricao = new Paragraph();
            paragraphDescricao.add(new Phrase("Descrição do Levantamento: ", fontTextBold));
            paragraphDescricao.add(descricao1);
            paragraphDescricao.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraphDescricao.setSpacingAfter(15);

            Paragraph paragraphTabelaBens = new Paragraph();
            paragraphTabelaBens.add(new Phrase("TABELA DE BENS", fontTextBold));
            paragraphTabelaBens.setAlignment(Element.ALIGN_CENTER);
            paragraphTabelaBens.setSpacingAfter(10);

            Paragraph paragraphAssinatura1 = new Paragraph();
            paragraphAssinatura1.add(new Phrase("____________________________________________\nAssinatura do Responsável"));
            paragraphAssinatura1.setAlignment(Element.ALIGN_CENTER);
            paragraphAssinatura1.setSpacingAfter(50);

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

            cell = new PdfPCell(new Phrase("CATEGORIA*", fontTextBoldSmall));
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

            Paragraph paragraphObs1 = new Paragraph();
            paragraphObs1.add(new Phrase("  * Os números apresentados nesta coluna representam o códgio das categorias. A definição desses código estão presentes no aplicativo IFPAapp.", fontTextTable));
            paragraphObs1.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraphObs1.setSpacingAfter(70);

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

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            Paragraph paragraphValorTotal = new Paragraph();
            paragraphValorTotal.add(new Phrase("Valor Total do Setor: ", fontTextBold));
            paragraphValorTotal.add(numberFormat.format(somatoria));
            paragraphValorTotal.setAlignment(Element.ALIGN_RIGHT);
            paragraphValorTotal.setSpacingAfter(5);

            document.add(image);
            document.add(paragraphTitulo);
            document.add(paragraphSetor);
            document.add(paragraphResponsavel);
            document.add(paragraphDataReferente);
            document.add(paragraphDescricao);
            document.add(paragraphTabelaBens);
            document.add(pdfPTable);
            document.add(paragraphValorTotal);
            document.add(paragraphObs1);
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
        DadosSetores dadosSetores = (DadosSetores) selecionar_setor.getSelectedItem();
        String setor = dadosSetores.getSigla();

        List<DadosBens> bens = repositorioBens.selecionarBensPorSetor(setor);

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        for (DadosBens bem : bens) {
            List<String> dataLine = new ArrayList<String>();
            dataLine.add(bem.getCodigo());
            dataLine.add(bem.getDescricao());
            dataLine.add(bem.getMarca());
            dataLine.add(bem.getData_entrada());
            dataLine.add(bem.getStatus());
            dataLine.add(bem.getCategoria());

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
        descricao.setText("");
    }
}
