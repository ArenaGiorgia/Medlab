package main;

public class PersonaleLaboratorio
{
private String cf;
private String nome;
private String cognome;
private String password;
private Sede sede;

    public PersonaleLaboratorio(String cf, String nome, String cognome, Sede sede) {
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.password=cf; //password settata automaticamente al codice fiscale
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

    public Boolean verificaPassword(String password) {
        if (this.password.equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "main.PersonaleLaboratorio{" +
                "cf='" + cf + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", sede=" + sede +
                '}';
    }
}
