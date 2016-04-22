package mobile.br.com.reclameonibus;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TelaFiltroConsultaOuReclamacao extends Activity implements OnClickListener {



    public void startTelaConsulta() {

        startActivity(new Intent(TelaFiltroConsultaOuReclamacao.this, TelaConsulta.class));


    }

    public void startTelaReclamacao() {

        startActivity(new Intent(TelaFiltroConsultaOuReclamacao.this, TelaReclamacoes.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_filtro_consulta_ou_reclamacao);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView bemVindo = (TextView) findViewById(R.id.txtBemVindo);
        String nome = getIntent().getStringExtra("nome");
        bemVindo.setText("Bem vindo: " + nome);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_filtro_consulta_ou_reclamacao, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
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
        switch (v.getId()) {
            case R.id.btConsultar:
                startTelaConsulta();
                break;

            case R.id.btReclamacao:
                startTelaReclamacao();
                break;

        }
    }
}
