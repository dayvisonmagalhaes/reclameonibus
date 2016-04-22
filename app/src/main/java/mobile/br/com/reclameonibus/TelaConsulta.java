package mobile.br.com.reclameonibus;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class TelaConsulta extends Activity implements OnClickListener {

    private ListView listaReclamacoes;
    private List<GetSetReclamacoes> reclamacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_consulta);
        listaReclamacoes = (ListView) findViewById(R.id.lista_reclamacoes);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    protected void onResume() {
        super.onResume();
        this.carregaLista();
    }

    private void carregaLista() {
        DBReclamacoes dao = new DBReclamacoes(this);
        reclamacoes = dao.getLista();
        dao.close();

        ArrayAdapter<GetSetReclamacoes> adapter = new ArrayAdapter<GetSetReclamacoes>(this, android.R.layout.simple_list_item_1, reclamacoes);
        this.listaReclamacoes.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_consulta, menu);
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

    }
}
