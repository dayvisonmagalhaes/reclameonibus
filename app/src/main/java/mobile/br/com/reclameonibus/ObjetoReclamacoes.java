

//CLASSE PARA ARMAZENAR O QUE FOI DIGITADO PELO USUARIO EM UM OBJETO
// PARA EM SEGUIDA INSERIR NO BANCO

package mobile.br.com.reclameonibus;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Matheus on 19/11/2015.
 */
public class ObjetoReclamacoes {

    private AutoCompleteTextView txtLinha;
    private EditText txtOrdem;
    private TextView displayTime;
    private TextView displayDate;
    private EditText tLocal;
    private Spinner spReclamacao;
    private Button btFoto;

    private GetSetReclamacoes getSetReclamacoes;

    public ObjetoReclamacoes(TelaReclamacoes activity) {

        this.getSetReclamacoes = new GetSetReclamacoes();

        this.txtLinha = (AutoCompleteTextView) activity.findViewById(R.id.txtLinha);
        this.txtOrdem = (EditText) activity.findViewById(R.id.txtOrdem);
        this.displayTime = (TextView) activity.findViewById(R.id.displayTime);
        this.displayDate = (TextView) activity.findViewById(R.id.displayDate);
        this.tLocal = (EditText) activity.findViewById(R.id.tLocal);
        this.spReclamacao = (Spinner) activity.findViewById(R.id.spReclamacao);
        this.btFoto = (Button) activity.findViewById(R.id.btFoto);

        getSetReclamacoes = new GetSetReclamacoes();
    }


    public GetSetReclamacoes buscaParaInserir() {
        getSetReclamacoes.setLinha(txtLinha.getText().toString());
        getSetReclamacoes.setNumero_ordem(txtOrdem.getText().toString());
        getSetReclamacoes.setHora(displayTime.getText().toString());
        getSetReclamacoes.setData(displayDate.getText().toString());
        getSetReclamacoes.setLocal(tLocal.getText().toString());
        getSetReclamacoes.setTipo_reclamacao(spReclamacao.getSelectedItem().toString());
//      getSetReclamacoes.setFoto(btFoto.getText().toString());

        return getSetReclamacoes;
    }

}
