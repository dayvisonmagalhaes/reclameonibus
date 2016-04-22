package mobile.br.com.reclameonibus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;


public class TelaReclamacoes extends Activity implements OnClickListener {

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    //***************Atributos para Data*************************
    int pYear;
    int pMonth;
    int pDay;
    //*************************************************************
    int hora;
    int minuto;

    private ObjetoReclamacoes helper;
    private AutoCompleteTextView txtLinha;
    private EditText txtOrdem;
    private TextView displayTime;
    private TextView displayDate;
    private EditText tLocal;
    private Spinner spReclamacao;
    private Button btFoto;
    private TextView pDisplayDate;
    private Button pPickDate;

    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplayData();
                    displayToastData();
                }
            };
    private TextView pDisplayTime;
    private Button pTimerPick;

    private TimePickerDialog.OnTimeSetListener pTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hora = hourOfDay;
                    minuto = minute;
                    updateDisplayHora();
                    displayToastHora();
                }
            };

    public void startCancelar() {

        Intent cancelar = new Intent(this, TelaReclamacoes.class);
        cancelar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(cancelar);
    }

    //Autocomplete para as linhas de onibus
    public void initLinhas() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.linhas, android.R.layout.simple_dropdown_item_1line);
        AutoCompleteTextView txtLinha = (AutoCompleteTextView) findViewById(R.id.txtLinha);
        txtLinha.setAdapter(adapter);
        //***************************************
    }

    /**
     * Atualizar TextView da data
     */
    private void updateDisplayData() {
        pDisplayDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(String.format("%02d", pDay)).append("/")
                        .append(pMonth + 1).append("/")
                        .append(pYear).append(" "));
    }

    /**
     * Atualizar TextView da hora
     */
    private void updateDisplayHora() {
        pDisplayTime.setText(
                new StringBuilder()

                        .append(hora).append(":")
                        .append(String.format("%02d", minuto)).append(" "));

    }


    //*******Mensagem de alteração de data********
    private void displayToastData() {
        Toast.makeText(this, new StringBuilder().append("A data escolhida foi: ").append(pDisplayDate.getText()), Toast.LENGTH_SHORT).show();
    }

    //*******Mensagem de alteração de hora********
    private void displayToastHora() {
        Toast.makeText(this, new StringBuilder().append("A hora escolhida foi: ").append(pDisplayTime.getText()), Toast.LENGTH_SHORT).show();
    }
    //************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_reclamacao);
        initLinhas();
        listReclamacoes();

        helper = new ObjetoReclamacoes(this);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button btReclamacao = (Button) findViewById(R.id.btReclamacao);
        btReclamacao.setOnClickListener(this);

        Button btCancelar = (Button) findViewById(R.id.btCancelar);
        btCancelar.setOnClickListener(this);

        pDisplayDate = (TextView) findViewById(R.id.displayDate);
        pPickDate = (Button) findViewById(R.id.pickDate);

        pDisplayTime = (TextView) findViewById(R.id.displayTime);
        pTimerPick = (Button) findViewById(R.id.timerPick);

        /** Listener for click event of the button */
        pPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        pTimerPick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        /** Pega a data atual */
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-02:00"));
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        hora = cal.get(Calendar.HOUR_OF_DAY);
        minuto = cal.get(Calendar.MINUTE);

        /** Coloca no display a hora alterada */
        updateDisplayData();
        updateDisplayHora();
    }

    /**
     * Novo dialog para DatePicker
     */
    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        pDateSetListener,
                        pYear, pMonth, pDay);

            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, pTimeSetListener, hora, minuto, false);
        }
        return null;
    }

    // **********************************

    // Lista pre definida de reclamaçoes

    public void listReclamacoes() {
        Spinner spReclamacao = (Spinner) findViewById(R.id.spReclamacao);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.reclamacoes,
                android.R.layout.simple_spinner_dropdown_item);
        spReclamacao.setAdapter(adapter2);
        // **********************************
    }

    private boolean ehVazio(String linha, String local) {
        boolean ret = false;
        if (TextUtils.isEmpty(linha)) {
            txtLinha.requestFocus(); //seta o foco para o campo user
            txtLinha.setError("Digite a linha do ônibus");
            ret = true;
        }
        if (TextUtils.isEmpty(local)) {
            tLocal.requestFocus(); //seta o foco para o campo password
            tLocal.setError("Ative o GPS ou escreva o local");
            ret = true;
        }

        return ret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_reclamacao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

        txtLinha = (AutoCompleteTextView) findViewById(R.id.txtLinha);
        txtOrdem = (EditText) findViewById(R.id.txtOrdem);
        displayTime = (TextView) findViewById(R.id.displayTime);
        displayDate = (TextView) findViewById(R.id.displayDate);
        tLocal = (EditText) findViewById(R.id.tLocal);
        spReclamacao = (Spinner) findViewById(R.id.spReclamacao);
        btFoto = (Button) findViewById(R.id.btFoto);

        final String txtLinhaString = txtLinha.getText().toString();
        final String txtOrdemString = txtOrdem.getText().toString();
        final String displayTimeString = displayTime.getText().toString();
        final String displayDateString = displayDate.getText().toString();
        final String tLocalString = tLocal.getText().toString();
        final String spReclamacaoString = spReclamacao.getSelectedItem().toString();
        String btFotoString = "";

        if (v.getId() == R.id.btReclamacao) {
            if (!ehVazio(txtLinhaString, tLocalString)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Mensagem");
                builder.setMessage("O ônibus " + txtLinhaString + " da linha " + txtOrdemString + " " +
                        spReclamacaoString.toUpperCase() + " as " + displayTimeString + " do dia " + displayDateString + " em " + tLocalString);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String resultado;
                        GetSetReclamacoes getSetReclamacoes = helper.buscaParaInserir();
                        DBReclamacoes rec = new DBReclamacoes(TelaReclamacoes.this);
                        resultado = rec.insereReclamacoes(getSetReclamacoes);
                        Toast.makeText(getBaseContext(), resultado, Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(TelaReclamacoes.this, TelaFinal.class);
                        it.putExtra("linha", txtLinhaString);
                        it.putExtra("numero_ordem", txtOrdemString);
                        it.putExtra("hora", displayTimeString);
                        it.putExtra("data", displayDateString);
                        it.putExtra("local", tLocalString);
                        it.putExtra("tipo_reclamacao", spReclamacaoString);

                        startActivity(it);
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Reclamação cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }

        }
        if (v.getId() == R.id.btCancelar) {
            startCancelar();
        }

    }
}
