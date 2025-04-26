package main;

public class Prenotazione {
    private String codice;
    private StatoPrenotazione stato;
    private Esame esame;
    private Paziente paziente;
    private Referto referto;

    public Prenotazione(Esame esame, Paziente paziente) {
        this.codice = esame.getCodice();
        this.esame= esame;
        this.paziente = paziente;
        this.referto = null;
        this.stato = new StatoInAttesa(this);
    }

    public StatoPrenotazione getStato() {
        return stato;
    }

    public Paziente getPaziente() {
        return paziente;
    }


    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
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



    public Esame getEsame() {
        return esame;
    }


    @Override
    public String toString() {
        return  "Codice: " + this.codice +
                "  Stato: " + this.stato +
                "  Esame: " + this.esame +
                "  Paziente: " + this.paziente +
                "  Referto: " + this.referto;
    }


}
