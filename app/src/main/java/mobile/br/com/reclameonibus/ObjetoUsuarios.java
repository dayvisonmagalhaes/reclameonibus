//CLASSE PARA ARMAZENAR O QUE FOI DIGITADO PELO USUARIO EM UM OBJETO
// PARA EM SEGUIDA INSERIR NO BANCO

package mobile.br.com.reclameonibus;

import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Matheus on 20/11/2015.
 */
public class ObjetoUsuarios {

    private EditText tnome;
    private EditText tTelefone;
    private EditText tEmail;
    private EditText tSenha;
    private Spinner spBairro;
    private GetSetUsuarios getSetUsuarios;

    public ObjetoUsuarios(TelaCadastro activity) {

        this.getSetUsuarios = new GetSetUsuarios();

        this.tnome = (EditText) activity.findViewById(R.id.tNome);
        this.tTelefone = (EditText) activity.findViewById(R.id.tTelefone);
        this.tEmail = (EditText) activity.findViewById(R.id.tEmail);
        this.tSenha = (EditText) activity.findViewById(R.id.tSenha);
        this.spBairro = (Spinner) activity.findViewById(R.id.spBairro);

        getSetUsuarios = new GetSetUsuarios();
    }

    public GetSetUsuarios buscaParaInserir() {
        getSetUsuarios.setNome(tnome.getText().toString());
        getSetUsuarios.setTelefone(tTelefone.getText().toString());
        getSetUsuarios.setEmail(tEmail.getText().toString());
        getSetUsuarios.setSenha(tSenha.getText().toString());
        getSetUsuarios.setBairro(spBairro.getSelectedItem().toString());

        return getSetUsuarios;
    }
}
