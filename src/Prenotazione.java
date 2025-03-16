import java.util.UUID;

public class Prenotazione {
    private String codice;
    private boolean conferma;  //se è effettuato è true e quindi generiamo il referto per le statistiche
    private Esame esame;
    private Paziente paziente;

    public Prenotazione(Esame esame, Paziente paziente) {
        this.codice = UUID.randomUUID().toString().substring(0, 8);
        this.esame= esame;
        this.conferma=false;
        this.paziente = paziente;
    }

    public String getCodice() {
        return codice;
    }

    public boolean confermaPrenotazione() {
        return conferma;
    }

    public Esame getEsame() {
        return esame;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public void setConferma(boolean conferma) {
        this.conferma = conferma;
    }

    public void setEsame(Esame esame) {
        this.esame = esame;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "codice='" + codice + '\'' +
                ", conferma=" + conferma +
                ", esame=" + esame +
                ", paziente=" + paziente +
                '}';
    }
}
