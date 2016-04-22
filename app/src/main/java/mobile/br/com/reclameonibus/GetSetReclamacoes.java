package mobile.br.com.reclameonibus;

/**
 * Created by Matheus on 18/11/2015.
 */
public class GetSetReclamacoes {
    private String linha;
    private String numero_ordem;
    private String hora;
    private String data;
    private String local;
    private String tipo_reclamacao;
    private String foto;

    public GetSetReclamacoes(String linha, String numero_ordem, String hora, String data, String local, String tipo_reclamacao, String foto) {
        this.linha = linha;
        this.numero_ordem = numero_ordem;
        this.hora = hora;
        this.data = data;
        this.local = local;
        this.tipo_reclamacao = tipo_reclamacao;
        this.foto = foto;
    }

    public GetSetReclamacoes() {
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public String getTipo_reclamacao() {
        return tipo_reclamacao;
    }

    public void setTipo_reclamacao(String tipo_reclamacao) {
        this.tipo_reclamacao = tipo_reclamacao;
    }

    public String getNumero_ordem() {
        return numero_ordem;
    }

    public void setNumero_ordem(String numero_ordem) {
        this.numero_ordem = numero_ordem;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String toString() {
        return this.getLinha() + " - " + this.getTipo_reclamacao();
    }
}