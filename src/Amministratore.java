public class Amministratore {
    private String codiceFiscale;
    private String password;

public Amministratore(){
    this.codiceFiscale ="a"; //settato al codice fiscale di alessio
    this.password = "a"; //password setta di default da me
}

    public String getCodiceFiscale() {
        return codiceFiscale;
    }
    public boolean verificaPassword(String password) {
        return this.password.equals(password);
    }

}