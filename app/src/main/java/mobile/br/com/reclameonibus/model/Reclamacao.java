package mobile.br.com.reclameonibus.model;

import android.os.AsyncTask;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mobile.br.com.reclameonibus.async.Connection;
import mobile.br.com.reclameonibus.async.JsonParser;

/**
 * Created by Matheus on 14/09/2016.
 */
@Table(name = "reclamacao")
public class Reclamacao extends Model {
    public static final String COLUMN_ID_INTERNA = "ID_INTERNA";
    public static final String COLUMN_INSERT_ID = "INSERT_ID";
    public static final String COLUMN_LINHA_ONIBUS = "LINHA_ONIBUS";
    public static final String COLUMN_NUM_ORDEM = "NUM_ORDEM";
    public static final String COLUMN_HORA = "HORA";
    public static final String COLUMN_DATA = "DATA";
    public static final String COLUMN_LOCAL = "LOCAL";
    public static final String COLUMN_TIPO_REC = "TIPO_REC";
    public static final String COLUMN_FOTO = "FOTO";

    public static final String COLUMN_USER = "USER_ID";


    @Column(name = COLUMN_ID_INTERNA)
    public int idInterna;
    @Column(name = COLUMN_INSERT_ID)
    public String insertId;
    @Column(name = COLUMN_LINHA_ONIBUS)
    public String linha_onibus;
    @Column(name = COLUMN_NUM_ORDEM)
    public String num_ordem;
    @Column(name = COLUMN_HORA)
    public String hora_ocorrido;
    @Column(name = COLUMN_DATA)
    public String data_ocorrido;
    @Column(name = COLUMN_LOCAL)
    public String local_ocorrido;
    @Column(name = COLUMN_TIPO_REC)
    public String tipo_rec;
    @Column(name = COLUMN_FOTO)
    public String foto;

//    @Column(name = COLUMN_USER)
//    public User user_id = User.getLoggedUser();


    public Reclamacao(String linha_onibus, String hora_ocorrido, String data_ocorrido, String local_ocorrido, String tipo_rec) {
        this.linha_onibus = linha_onibus;
        this.hora_ocorrido = hora_ocorrido;
        this.data_ocorrido = data_ocorrido;
        this.local_ocorrido = local_ocorrido;
        this.tipo_rec = tipo_rec;
        this.idInterna = 1;
    }

    public static Reclamacao getReclamacaoAtiva() {
        return new Select().from(Reclamacao.class).executeSingle();
    }

    public static boolean cascadeLogout() {
        getReclamacaoAtiva().delete();
        return getReclamacaoAtiva() == null;
    }

    public ArrayList<NameValuePair> getParams() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("action", "insert"));
        params.add(new BasicNameValuePair("linha_onibus", linha_onibus));
        params.add(new BasicNameValuePair("num_ordem", num_ordem));
        params.add(new BasicNameValuePair("hora_ocorrido", hora_ocorrido));
        params.add(new BasicNameValuePair("data_ocorrido", data_ocorrido));
        params.add(new BasicNameValuePair("local_ocorrido", local_ocorrido));
        params.add(new BasicNameValuePair("tipo_rec", tipo_rec));
        params.add(new BasicNameValuePair("foto", foto));
//        params.add(new BasicNameValuePair("nome", user.insertId));

        return params;
    }

    public int getIdInterna() {
        return idInterna;
    }

    public void setIdInterna(int idInterna) {
        this.idInterna = idInterna;
    }

    public String getInsertId() {
        return insertId;
    }

    public void setInsertId(String insertId) {
        this.insertId = insertId;
    }

    public String getLinha_onibus() {
        return linha_onibus;
    }

    public void setLinha_onibus(String linha_onibus) {
        this.linha_onibus = linha_onibus;
    }

    public String getNum_ordem() {
        return num_ordem;
    }

//    public User getUser() {
//        return user_id;
//    }

    public void setNum_ordem(String num_ordem) {
        this.num_ordem = num_ordem;
    }

    public String getHora() {
        return hora_ocorrido;
    }

    public void setHora(String hora_ocorrido) {
        this.hora_ocorrido = hora_ocorrido;
    }

    public String getData() {
        return data_ocorrido;
    }

    public void setData(String data_ocorrido) {
        this.data_ocorrido = data_ocorrido;
    }

    public String getLocal() {
        return local_ocorrido;
    }

    public void setLocal(String local_ocorrido) {
        this.local_ocorrido = local_ocorrido;
    }

    public String getTipo_rec() {
        return tipo_rec;
    }

    public void setTipo_rec(String tipo_rec) {
        this.tipo_rec = tipo_rec;
    }

//    public void setUser(User user) {
//        this.user_id = user;
//    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void signupInBackground(final ReclamacaoInsertCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {

            String message;

            @Override
            protected Boolean doInBackground(Void... params) {
                JSONObject jObjectResponse = JsonParser.postDataObject(Connection.REC_URL, JsonParser.POST, getParams());
                try {
                    message = jObjectResponse.getString("message");
                    if (jObjectResponse.getBoolean("error")) {
                        return true;
                    } else {
                        setInsertId(jObjectResponse.getString("response"));
                        cascadeSave();
                        return false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Boolean error) {
                //se existe alguma mensagem de erro.
                if (error) {
                    callback.OnInsertError(message);
                } else {
                    callback.OnInsertDone(message);
                }
            }
        }.execute();
    }

    public void cascadeSave() {
        this.save();
    }


    public interface ReclamacaoEditCallback {
        public void onEditError(String message);

        public void onEditDone(String message);
    }

    public interface ReclamacaoInsertCallback {
        public void OnInsertError(String message);

        public void OnInsertDone(String message);
    }

}
