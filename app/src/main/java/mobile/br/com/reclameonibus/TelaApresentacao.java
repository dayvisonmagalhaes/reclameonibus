package mobile.br.com.reclameonibus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class TelaApresentacao extends Activity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_apresentacao);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                finish();

                Intent intent = new Intent();
                intent.setClass(TelaApresentacao.this, TelaFiltro.class);
                startActivity(intent);
            }
        }, 3000);
    }

    @Override
    public void run() {

    }
}
