package main;

public class StatoCompletato extends StatoPrenotazione {
    public StatoCompletato(Prenotazione prenotazione) {
        super(prenotazione);
    }

    @Override
    public void completa(Prenotazione prenotazione) {

        System.out.println("La prenotazione è già completata.");
    }

    @Override
    public void annulla(Prenotazione prenotazione) {
        System.out.println("Impossibile annullare: la prenotazione è già completata.");
    }

}
