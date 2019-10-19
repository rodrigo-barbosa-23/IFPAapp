package com.example.ifpaapp.DATABASE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseApp extends SQLiteOpenHelper {
    private static DatabaseApp databaseApp;
    private static final String NOME_DATABASE = "ifpa_belem.db";
    private static final int VERSAO_DATABASE = 1;

    public DatabaseApp(Context context) {
        super(context, NOME_DATABASE, null, VERSAO_DATABASE);
    }

    public static DatabaseApp getInstancia(Context context) {
        if (databaseApp == null) {
            databaseApp = new DatabaseApp(context);
        }
        return databaseApp;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String sqlLogins = "CREATE TABLE logins (" +
                "id_login INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario TEXT NOT NULL, " +
                "senha TEXT NOT NULL);";

        String sqlBens = "CREATE TABLE bens (" +
                "id_bem INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "setor TEXT NOT NULL, " +
                "codigo TEXT NOT NULL, " +
                "data_entrada TEXT NOT NULL, " +
                "descricao TEXT NOT NULL, " +
                "marca TEXT NOT NULL, " +
                "valor TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "categoria TEXT NOT NULL);";

        String sqlLevantamentos = "CREATE TABLE levantamentos (" +
                "id_levantamento INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "setor TEXT NOT NULL, " +
                "responsavel TEXT NOT NULL, " +
                "data_referente TEXT NOT NULL, " +
                "descricao TEXT NOT NULL);";

        String sqlRelatorios = "CREATE TABLE relatorios (" +
                "id_relatorio INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "categoria TEXT NOT NULL, " +
                "responsavel TEXT NOT NULL, " +
                "data_referente TEXT NOT NULL);";

        String sqlInsertLogin1 = "INSERT INTO logins (usuario, senha) " +
                "VALUES ('ifpa.belem', 'ifpa123');";

        database.execSQL(sqlLogins);
        database.execSQL(sqlBens);
        database.execSQL(sqlLevantamentos);
        database.execSQL(sqlRelatorios);
        database.execSQL(sqlInsertLogin1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS logins;");
        database.execSQL("DROP TABLE IF EXISTS bens;");
        database.execSQL("DROP TABLE IF EXISTS levantamentos;");
        database.execSQL("DROP TABLE IF EXISTS relatorios;");
        onCreate(database);
    }

    public SQLiteDatabase getEscritaDatabase() {
        return this.getWritableDatabase();
    }

    public Boolean validarLogin(String usuario, String senha) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM logins WHERE usuario=? " +
                "AND senha=?", new String[]{usuario, senha});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
