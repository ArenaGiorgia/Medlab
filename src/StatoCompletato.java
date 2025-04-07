public class StatoCompletato implements StatoPrenotazione {

    @Override
    public void completa(Prenotazione prenotazione) {
        System.out.println("La prenotazione è già completata.");
    }

    @Override
    public void annulla(Prenotazione prenotazione) {
        System.out.println("Impossibile annullare: la prenotazione è già completata.");
    }

    @Override
    public String getNomeStato() {
        return "Completata";
    }
}
