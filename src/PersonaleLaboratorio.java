import java.util.HashMap;
import java.util.Map;

public class PersonaleLaboratorio
{
private String cf;
private String nome;
private String cognome;
private String password;
private Map<String, Referto> referti;
private Sede sede;
    public PersonaleLaboratorio(String cf, String nome, String cognome, Sede sede) {
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.password=cf; //password settata automaticamente al codice fiscale
        this.referti = new HashMap<String, Referto>();
        this.sede = sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Sede getSede() {
        return sede;
    }

    public String getCf() {
        return cf;
    }

    public Map<String, Referto> getReferti() {
        return referti;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }



    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void aggiungiReferto(String codicePrenotazione, Referto referto) { //per aggiunger e il referto
        this.referti.put(codicePrenotazione, referto);
    }

    public Boolean verificaPassword(String password) {
        if (this.password.equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "PersonaleLaboratorio{" +
                "cf='" + cf + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", sede=" + sede +
                '}';
    }
}
