package mobile.br.com.reclameonibus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matheus on 03/11/2015.
 */
public class DBReclamacoes extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "ReclameOnibusDB.sqlite";
    private static final String TAG = "sql";
    private static final int VERSAO_BANCO = 1;


    public DBReclamacoes(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

//        Log.d(TAG, "Criando tabela usuarios..");
//        db.execSQL("create table if not exists table_reclamacoes (id INTEGER PRIMARY KEY AUTOINCREMENT, linha text, numero_ordem numeric, hora text, data datetext, local text, tipo_reclamacao text, foto image);");
//        Log.d(TAG, "Tabela criada com sucesso");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String insereReclamacoes(GetSetReclamacoes getSetReclamacoes) {

        SQLiteDatabase db = getWritableDatabase();
        long resultado;
        ContentValues values = new ContentValues();

        values.put("linha", getSetReclamacoes.getLinha());
        values.put("numero_ordem", getSetReclamacoes.getNumero_ordem());
        values.put("hora", getSetReclamacoes.getHora());
        values.put("data", getSetReclamacoes.getData());
        values.put("local", getSetReclamacoes.getLocal());
        values.put("tipo_reclamacao", getSetReclamacoes.getTipo_reclamacao());
        values.put("foto", getSetReclamacoes.getFoto());

        resultado = getWritableDatabase().insert("table_reclamacoes", null, values);

        if (resultado == -1) {
            db.close();
            return "Erro ao inserir registro";
        } else {
            db.close();
            return "Registro inserido com sucesso";
        }

    }

    public List<GetSetReclamacoes> getLista() {
        List<GetSetReclamacoes> reclamacoes = new ArrayList<GetSetReclamacoes>();
        Cursor c = getReadableDatabase()
                .rawQuery("SELECT * FROM " + "table_reclamacoes" + ";", null);
        while (c.moveToNext()) {
            GetSetReclamacoes getSetReclamacoes = new GetSetReclamacoes();
            getSetReclamacoes.setLinha(c.getString(c.getColumnIndex("linha")));
            getSetReclamacoes.setTipo_reclamacao(c.getString(c.getColumnIndex("tipo_reclamacao")));
            reclamacoes.add(getSetReclamacoes);
        }
        c.close();
        return reclamacoes;
    }

}


