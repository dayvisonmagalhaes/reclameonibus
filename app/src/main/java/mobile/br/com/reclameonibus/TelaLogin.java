package mobile.br.com.reclameonibus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TelaLogin extends Activity implements OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        Button btLogin = (Button) findViewById(R.id.btLoginCadastro);
        btLogin.setOnClickListener(this);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_login, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Opcoes no action bar
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_confirmar) {
            return true;
        }

        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onClick(View v) {

        EditText tEmail = (EditText) findViewById(R.id.tEmailLogin);
        EditText tSenha = (EditText) findViewById(R.id.tSenhaLogin);

        String email = tEmail.getText().toString();
        String senha = tSenha.getText().toString();

        DBUsuario db = new DBUsuario(TelaLogin.this);

        if (db.validarUsuarios(email, senha)) {
            Toast.makeText(getBaseContext(), "Logado com sucesso", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(this, TelaFiltroConsultaOuReclamacao.class);
            startActivity(it);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this); //mensagem de erro na tela
            builder.setTitle("Erro");
            builder.setMessage("Login e/ou senha inválido");
            builder.setNeutralButton("Fechar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}
