package mobile.br.com.reclameonibus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Matheus on 01/11/2015.
 */
public class DBUsuario extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "ReclameOnibusDB.sqlite";
    private static final String TAG = "sql";
    private static final int VERSAO_BANCO = 1;

    public DBUsuario(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando tabela usuarios..");
        db.execSQL("create table if not exists table_usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nome text, telefone numeric, email text, senha numeric, bairro text);");
        db.execSQL("create table if not exists table_reclamacoes (id INTEGER PRIMARY KEY AUTOINCREMENT, linha text, numero_ordem numeric, hora text, data datetext, local text, tipo_reclamacao text, foto image);");
        Log.d(TAG, "Tabelas criada com sucesso");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public String insereUsuarios(GetSetUsuarios getSetUsuarios) {

        SQLiteDatabase db = getWritableDatabase();
        long resultado;
        ContentValues values = new ContentValues();

        values.put("nome", getSetUsuarios.getNome());
        values.put("telefone", getSetUsuarios.getTelefone());
        values.put("email", getSetUsuarios.getEmail());
        values.put("senha", getSetUsuarios.getSenha());
        values.put("bairro", getSetUsuarios.getBairro());

        resultado = getWritableDatabase().insert("table_usuarios", null, values);

        if (resultado == -1) {
            db.close();
            return "Erro ao inserir registro";
        } else {
            db.close();
            return "Registro inserido com sucesso";
        }
    }

    public boolean validarUsuarios(String email, String senha) {

        SQLiteDatabase db = getWritableDatabase();
        String[] AND = new String[]{email, senha};
        Cursor c = db.query("table_usuarios", null, "email =? AND senha =?", AND, null, null, null);

        if (c.getCount() > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;

    }

    public boolean existsEmail(String email){
        SQLiteDatabase db = getWritableDatabase();
        String[] AND = new String[]{email};
        Cursor c = db.query("table_usuarios", null, "email =?", AND, null, null, null);

        if (c.getCount() > 0){
            db.close();
            return true;
        }
        db.close();
        return false;
    }


}

