package mobile.br.com.reclameonibus;

/**
 * Created by Matheus on 20/11/2015.
 */
public class GetSetUsuarios {

    private String nome;
    private String telefone;
    private String email;
    private String senha;
    private String bairro;

    public GetSetUsuarios(String nome, String telefone, String email, String senha, String bairro) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.bairro = bairro;
    }

    public GetSetUsuarios() {
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

    @Override
    public String toString() {
        return "GetSetUsuarios{" +
                "nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", bairro='" + bairro + '\'' +
                '}';
    }
}


