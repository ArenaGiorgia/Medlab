public class StatoAnnullato extends StatoPrenotazione {
    public StatoAnnullato(Prenotazione prenotazione) {
        super(prenotazione);
    }

    @Override
    public void completa( Prenotazione prenotazione) {
        System.out.println("Impossibile completare: la prenotazione è stata annullata.");
    }

    @Override
    public void annulla(Prenotazione prenotazione) {

        System.out.println("La prenotazione è già annullata.");
    }
    @Override
    public String getNomeStato() {
        return "Annullato";
    }

}
