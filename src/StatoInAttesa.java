import java.time.LocalDate;

public class StatoInAttesa extends StatoPrenotazione {
    public StatoInAttesa(Prenotazione prenotazione) {
        super(prenotazione);
    }

    @Override
    public void completa(Prenotazione prenotazione) {
        Referto referto = new Referto(prenotazione.getCodice(), LocalDate.now()); //coidce uguale alla prenotazione
        prenotazione.setReferto(referto); //da vedere
        prenotazione.setStato(new StatoCompletato(prenotazione));
        System.out.println("Prenotazione completata e referto generato.");
    }

    @Override
    public void annulla(Prenotazione prenotazione) {
        prenotazione.setStato(new StatoAnnullato(prenotazione));
        System.out.println("Prenotazione annullata.");

    }

    @Override
    public String getNomeStato() {
        return "In Attesa";
    }

}
