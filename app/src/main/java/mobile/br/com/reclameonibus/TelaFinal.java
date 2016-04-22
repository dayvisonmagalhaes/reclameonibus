package mobile.br.com.reclameonibus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class TelaFinal extends Activity implements OnClickListener {

    private TextView txtMensagemEnviada;
    Intent intentEmail = new Intent(Intent.ACTION_SEND);

    public void startNovaReclamacao() {

        startActivity(new Intent(TelaFinal.this, TelaReclamacoes.class));
    }

    public void startFechar() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mensagem");
        builder.setMessage("Deseja sair do aplicativo?");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent sair = new Intent(Intent.ACTION_MAIN);
                sair.addCategory(Intent.CATEGORY_HOME);
                sair.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(sair);
            }

        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_final);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button btNovaReclamacao = (Button) findViewById(R.id.btNovaReclamacao);
        btNovaReclamacao.setOnClickListener(this);

        Button btFechar = (Button) findViewById(R.id.btFechar);
        btFechar.setOnClickListener(this);

        RadioGroup group = (RadioGroup) findViewById(R.id.radioEmail);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean marcou = R.id.rbEnviaEmail == checkedId;
                if (marcou) {
                    startActivity(intentEmail);
                }
            }
        });

        txtMensagemEnviada = (TextView) findViewById(R.id.txtMensagemEnviada);

        mensagemGerada();
    }

    protected void onResume() {
        super.onResume();

    }

    public void mensagemGerada() {

        String linha = this.getIntent().getStringExtra("linha");
        String numero_ordem = this.getIntent().getStringExtra("numero_ordem");
        String hora = this.getIntent().getStringExtra("hora");
        String data = this.getIntent().getStringExtra("data");
        String local = this.getIntent().getStringExtra("local");
        String tipo_reclamacao = this.getIntent().getStringExtra("tipo_reclamacao");

        String mensagemGerada = "O ônibus " + linha +
                " de numero " + numero_ordem + " " +
                tipo_reclamacao +
                " as " + hora + " do dia " + data +
                " em " + local;

        intentEmail.setType(mensagemGerada);
        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"m.navega6@gmail.com"});
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Reclame Ônibus");
        intentEmail.putExtra(Intent.EXTRA_TEXT, mensagemGerada);

        this.txtMensagemEnviada.setText(mensagemGerada);
        this.txtMensagemEnviada.setSingleLine(false);
        this.txtMensagemEnviada.setMaxLines(4);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNovaReclamacao:
                startNovaReclamacao();
                break;

            case R.id.btFechar:
                startFechar();
                break;
        }
    }

}
