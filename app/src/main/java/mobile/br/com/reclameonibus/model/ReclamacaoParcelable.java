package mobile.br.com.reclameonibus.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Matheus on 13/10/2016.
 */
public class ReclamacaoParcelable implements Parcelable {


    public static final Creator<ReclamacaoParcelable> CREATOR = new Creator<ReclamacaoParcelable>() {
        @Override
        public ReclamacaoParcelable createFromParcel(Parcel in) {
            return new ReclamacaoParcelable(in);
        }

        @Override
        public ReclamacaoParcelable[] newArray(int size) {
            return new ReclamacaoParcelable[size];
        }
    };
    String insertId;
    String linha_onibus;
    String num_ordem;
    String hora_ocorrido;
    String data_ocorrido;
    String local_ocorrido;
    String tipo_rec;
    String foto;

    ;

    public ReclamacaoParcelable(JSONObject jRec) throws JSONException {
        this.insertId = (jRec.getString("id"));
        this.linha_onibus = (jRec.getString("linha_onibus"));
        this.num_ordem = (jRec.getString("num_ordem"));
        this.hora_ocorrido = (jRec.getString("hora_ocorrido"));
        this.data_ocorrido = (jRec.getString("data_ocorrido"));
        this.local_ocorrido = (jRec.getString("local_ocorrido"));
        this.tipo_rec = (jRec.getString("tipo_rec"));
        this.foto = (jRec.getString("foto"));
    }

    protected ReclamacaoParcelable(Parcel in) {

        insertId = in.readString();
        linha_onibus = in.readString();
        num_ordem = in.readString();
        hora_ocorrido = in.readString();
        data_ocorrido = in.readString();
        local_ocorrido = in.readString();
        tipo_rec = in.readString();
        foto = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.insertId,
                this.linha_onibus,
                this.num_ordem,
                this.hora_ocorrido,
                this.data_ocorrido,
                this.local_ocorrido,
                this.tipo_rec,
                this.foto

//        dest.writeString(foto);
        });
    }

    public String getInsertId() {
        return insertId;
    }

    public void setInsertId(String insertId) {
        this.insertId = insertId;
    }

    public String getLinha_onibus() {
        return linha_onibus;
    }

    public void setLinha_onibus(String linha_onibus) {
        this.linha_onibus = linha_onibus;
    }

    public String getNum_ordem() {
        return num_ordem;
    }

    public void setNum_ordem(String num_ordem) {
        this.num_ordem = num_ordem;
    }

    public String getHora_ocorrido() {
        return hora_ocorrido;
    }

    public void setHora_ocorrido(String hora_ocorrido) {
        this.hora_ocorrido = hora_ocorrido;
    }

    public String getData_ocorrido() {
        return data_ocorrido;
    }

    public void setData_ocorrido(String data_ocorrido) {
        this.data_ocorrido = data_ocorrido;
    }

    public String getLocal_ocorrido() {
        return local_ocorrido;
    }

    public void setLocal_ocorrido(String local_ocorrido) {
        this.local_ocorrido = local_ocorrido;
    }

    public String getTipo_rec() {
        return tipo_rec;
    }

    public void setTipo_rec(String tipo_rec) {
        this.tipo_rec = tipo_rec;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
