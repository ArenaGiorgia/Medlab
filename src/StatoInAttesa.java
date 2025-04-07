import java.time.LocalDate;

public class StatoInAttesa implements StatoPrenotazione {

    @Override
    public void completa(Prenotazione prenotazione) {
       //il referto lo creiamo direttamente con il metodo creaReferto();
        Referto referto = new Referto(prenotazione.getCodice(), LocalDate.now()); //coidce uguale alla prenotazione
        prenotazione.setReferto(referto);
        prenotazione.setStato(new StatoCompletato());
        System.out.println("Prenotazione completata e referto generato.");
    }

    @Override
    public void annulla(Prenotazione prenotazione) {
        prenotazione.setStato(new StatoAnnullato());
        System.out.println("Prenotazione annullata.");
    }

    @Override
    public String getNomeStato() {
        return "In Attesa";
    }
}
