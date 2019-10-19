package com.example.ifpaapp.REPOSITORIOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ifpaapp.DATABASE.DatabaseApp;
import com.example.ifpaapp.DADOS.DadosBens;

import java.util.ArrayList;
import java.util.List;

public class RepositorioBens {
    DatabaseApp databaseApp;

    public RepositorioBens(Context context) {
        databaseApp = DatabaseApp.getInstancia(context);
    }

    public void cadastrarBens(DadosBens dadosBens) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("setor", dadosBens.getSetor());
        contentValues.put("codigo", dadosBens.getCodigo());
        contentValues.put("data_entrada", dadosBens.getData_entrada());
        contentValues.put("descricao", dadosBens.getDescricao());
        contentValues.put("marca", dadosBens.getMarca());
        contentValues.put("valor", dadosBens.getValor());
        contentValues.put("status", dadosBens.getStatus());
        contentValues.put("categoria", dadosBens.getCategoria());

        databaseApp.getEscritaDatabase().insert("bens", null, contentValues);
    }

    public List<DadosBens> selecionarTodosBens() {
        List<DadosBens> bens = new ArrayList<DadosBens>();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT id_bem, descricao, marca, codigo, data_entrada, valor, status, categoria " +
                "FROM bens ORDER BY descricao");

        SQLiteDatabase database = databaseApp.getReadableDatabase();

        Cursor cursor = database.rawQuery(stringBuilder.toString(), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            DadosBens bem = new DadosBens();

            bem.setId_bem(cursor.getInt(cursor.getColumnIndex("id_bem")));
            bem.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            bem.setMarca(cursor.getString(cursor.getColumnIndex("marca")));
            bem.setCodigo(cursor.getString(cursor.getColumnIndex("codigo")));
            bem.setData_entrada(cursor.getString(cursor.getColumnIndex("data_entrada")));
            bem.setValor(cursor.getString(cursor.getColumnIndex("valor")));
            bem.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            bem.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));

            bens.add(bem);

            cursor.moveToNext();
        }
        cursor.close();

        return bens;
    }

    public List<DadosBens> selecionarBensPorSetor(String setor) {
        List<DadosBens> bens = new ArrayList<DadosBens>();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT id_bem, descricao, marca, codigo, data_entrada, valor, status, categoria " +
                "FROM bens WHERE setor = ? ORDER BY descricao");

        SQLiteDatabase database = databaseApp.getReadableDatabase();

        Cursor cursor = database.rawQuery(stringBuilder.toString(), new String[]{setor});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            DadosBens bem = new DadosBens();

            bem.setId_bem(cursor.getInt(cursor.getColumnIndex("id_bem")));
            bem.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            bem.setMarca(cursor.getString(cursor.getColumnIndex("marca")));
            bem.setCodigo(cursor.getString(cursor.getColumnIndex("codigo")));
            bem.setData_entrada(cursor.getString(cursor.getColumnIndex("data_entrada")));
            bem.setValor(cursor.getString(cursor.getColumnIndex("valor")));
            bem.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            bem.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));

            bens.add(bem);

            cursor.moveToNext();
        }
        cursor.close();

        return bens;
    }

    public List<DadosBens> selecionarBensPorCategoria(String categoria) {
        List<DadosBens> bens = new ArrayList<DadosBens>();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT id_bem, setor, descricao, marca, codigo, data_entrada, valor, status " +
                "FROM bens WHERE categoria = ? ORDER BY descricao");

        SQLiteDatabase database = databaseApp.getReadableDatabase();

        Cursor cursor = database.rawQuery(stringBuilder.toString(), new String[]{categoria});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            DadosBens bem = new DadosBens();

            bem.setId_bem(cursor.getInt(cursor.getColumnIndex("id_bem")));
            bem.setSetor(cursor.getString(cursor.getColumnIndex("setor")));
            bem.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            bem.setMarca(cursor.getString(cursor.getColumnIndex("marca")));
            bem.setCodigo(cursor.getString(cursor.getColumnIndex("codigo")));
            bem.setData_entrada(cursor.getString(cursor.getColumnIndex("data_entrada")));
            bem.setValor(cursor.getString(cursor.getColumnIndex("valor")));
            bem.setStatus(cursor.getString(cursor.getColumnIndex("status")));

            bens.add(bem);

            cursor.moveToNext();
        }
        cursor.close();

        return bens;
    }

    public Integer excluirBens(int id_bem) {
        return databaseApp.getEscritaDatabase().delete("bens", "id_bem = ?",
                new String[]{Integer.toString(id_bem)});
    }

    public void atualizarBens(DadosBens dadosBens, int id_bem) {

        ContentValues contentValues = new ContentValues();

        contentValues.put("setor", dadosBens.getSetor());
        contentValues.put("codigo", dadosBens.getCodigo());
        contentValues.put("data_entrada", dadosBens.getData_entrada());
        contentValues.put("descricao", dadosBens.getDescricao());
        contentValues.put("marca", dadosBens.getMarca());
        contentValues.put("valor", dadosBens.getValor());
        contentValues.put("status", dadosBens.getStatus());
        contentValues.put("categoria", dadosBens.getCategoria());

        databaseApp.getEscritaDatabase().update("bens", contentValues, "id_bem = ?", new String[]{Integer.toString(id_bem)});
    }

    public DadosBens getBem(int id_bem) {
        Cursor cursor = databaseApp.getEscritaDatabase().rawQuery("SELECT * FROM bens WHERE id_bem= " + id_bem, null);
        cursor.moveToFirst();

        DadosBens dadosBens = new DadosBens();

        dadosBens.setId_bem(cursor.getInt(cursor.getColumnIndex("id_bem")));
        dadosBens.setSetor(cursor.getString(cursor.getColumnIndex("setor")));
        dadosBens.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        dadosBens.setMarca(cursor.getString(cursor.getColumnIndex("marca")));
        dadosBens.setCodigo(cursor.getString(cursor.getColumnIndex("codigo")));
        dadosBens.setData_entrada(cursor.getString(cursor.getColumnIndex("data_entrada")));
        dadosBens.setValor(cursor.getString(cursor.getColumnIndex("valor")));
        dadosBens.setStatus(cursor.getString(cursor.getColumnIndex("status")));
        dadosBens.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));

        return dadosBens;

    }
}
