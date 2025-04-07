import java.util.UUID;

public class Prenotazione {
    private String codice;
    private StatoPrenotazione stato;
    private Esame esame;
    private Paziente paziente;
    private Referto referto;

    public Prenotazione(Esame esame, Paziente paziente) {
        this.codice = UUID.randomUUID().toString();
        this.esame= esame;
        this.paziente = paziente;
        this.referto = null;
        this.stato = new StatoInAttesa();
    }

    public StatoPrenotazione getStato() {
        return stato;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void completa() {
        stato.completa(this);
    }

    public void annulla() {
        stato.annulla(this);
    }

    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public String getStatoCorrente() {
        return stato.getNomeStato();

    }

    public void setReferto(Referto referto) {
        this.referto = referto;
    }

    public Referto getReferto() {
        return this.referto;
    }

    public String getCodice() {
        return codice;
    }

    public void setEsame(Esame esame) {
        this.esame = esame;
    }

    public Esame getEsame() {
        return esame;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }


    @Override
    public String toString() {
        return "Prenotazione{" +
                "codice='" + codice + '\'' +
                ", esame=" + esame +
                ", paziente=" + paziente +
                '}';
    }


}
