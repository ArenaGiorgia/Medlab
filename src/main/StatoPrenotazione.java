package main;

public abstract class StatoPrenotazione {
private   Prenotazione prenotazione ;

    public StatoPrenotazione(Prenotazione prenotazione) {

        this.prenotazione = prenotazione;
    }

    public void completa(Prenotazione prenotazione) {
            System.out.println("Operazione non consentita nello stato attuale.");
        }

        public void annulla(Prenotazione prenotazione) {
            System.out.println("Operazione non consentita nello stato attuale.");
        }

        public abstract String getNomeStato();
}
