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


@Table(name = "usuario")
public class User extends Model {
    public static final String COLUMN_ID_INTERNA = "ID_INTERNA";
    public static final String COLUMN_INSERT_ID = "INSERT_ID";
    public static final String COLUMN_NOME = "NOME";
    public static final String COLUMN_TEL = "TELEFONE";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_EMAIL_CONF = "EMAIL_CONF";
    public static final String COLUMN_SENHA = "SENHA";
    public static final String COLUMN_SENHA_CONF = "SENHA_CONF";
    public static final String COLUMN_BAIRRO = "BAIRRO";

    @Column(name = COLUMN_ID_INTERNA)
    public int idInterna;
    @Column(name = COLUMN_INSERT_ID)
    public String insertId;
    @Column(name = COLUMN_NOME)
    public String nome;
    @Column(name = COLUMN_TEL)
    public String telefone;
    @Column(name = COLUMN_EMAIL)
    public String email;
    @Column(name = COLUMN_SENHA)
    public String senha;
    @Column(name = COLUMN_BAIRRO)
    public String bairro;

    public User() {
    }

    public User(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.idInterna = 1;
    }

    public User(JSONObject jUser) throws JSONException {
        this.setIdInterna(1);
        this.setInsertId(jUser.getString("id"));
        this.setNome(jUser.getString("nome"));
        this.setTelefone(jUser.getString("telefone"));
        this.setEmail(jUser.getString("email"));
//        senha;
        this.setSenha(jUser.getString("senha"));
        this.setBairro(jUser.getString("bairro"));


    }

    public static User getLoggedUser() {
        return new Select().from(User.class).executeSingle();
    }

    public static boolean cascadeLogout() {
        getLoggedUser().delete();
        return getLoggedUser() == null;
    }

    public static void loginInBackground(final String email, final String senha, final UserLoginCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            String message = "";
            User user = null;

            @Override
            protected Boolean doInBackground(Void... params) {
                ArrayList<NameValuePair> paramsData = new ArrayList<NameValuePair>();

                paramsData.add(new BasicNameValuePair("action", "login"));
                paramsData.add(new BasicNameValuePair("email", email));
                paramsData.add(new BasicNameValuePair("senha", senha));

                JSONObject jObjectResponse = JsonParser.postDataObject(Connection.AUTH_URL, JsonParser.POST, paramsData);

                try {
                    message = jObjectResponse.getString("message");
                    if (!jObjectResponse.getBoolean("error")) {
                        user = new User(jObjectResponse.getJSONObject("response"));
                        user.cascadeSave();
                        return true;
                    } else {
                        return false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    message = e.getMessage();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (!success) {
                    message = "Não foi possível efetuar o login: " + message;
                }
                callback.OnLoginRequestDone(message, user);
            }
        }.execute();

    }

    public ArrayList<NameValuePair> getParams() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("action", "signup"));
        params.add(new BasicNameValuePair("nome", nome));
        params.add(new BasicNameValuePair("telefone", telefone));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("senha", senha));
        params.add(new BasicNameValuePair("bairro", bairro));

        return params;
    }

    public ArrayList<NameValuePair> getParamsEdit() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("action", "edit"));
        params.add(new BasicNameValuePair("nome", nome));
        params.add(new BasicNameValuePair("telefone", telefone));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("senha", senha));
        params.add(new BasicNameValuePair("bairro", bairro));


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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void signupInBackground(final UserSignUpCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {

            String message;

            @Override
            protected Boolean doInBackground(Void... params) {
                JSONObject jObjectResponse = JsonParser.postDataObject(Connection.AUTH_URL,
                        JsonParser.POST, getParams());
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
                if (error) { //se existe alguma mensagem de erro.
                    callback.OnSignUpError(message);
                } else {
                    callback.OnSignUpDone(message);
                }
            }
        }.execute();}

    public void editInBackground(final UserEditCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {

            String message;

            @Override
            protected Boolean doInBackground(Void... params) {
                JSONObject jObjectResponse = JsonParser.postDataObject(Connection.AUTH_URL, JsonParser.GET, getParamsEdit());
                try {
                    message = jObjectResponse.getString("message");
                    return jObjectResponse.getBoolean("error");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Boolean error) {
                //se existe alguma mensagem de erro.
                if (error) {
                    callback.onEditError(message);
                } else {
                    callback.onEditDone(message);
                }
            }
        }.execute();
    }

    public void cascadeSave() {
        this.save();
    }

    public interface UserEditCallback {
        void onEditDone(String message);

        void onEditError(String message);
    }

    public interface UserSignUpCallback {
        void OnSignUpDone(String message);

        void OnSignUpError(String message);
    }

    public interface UserLoginCallback {
        void OnLoginRequestDone(String message, User user);
    }
}