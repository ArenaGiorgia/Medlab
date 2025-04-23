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

/*l interfaccia che ci permette di gestire lo stato con il pattern state, dove sarà il personale di laboratorio ad
 switchare dallo stato della prenotazione in corso allo stato completato generando il referto vuoto (con descrizione
 in corso perche nel UC6 andremo a mettere le vere informazioni ) , nel void completa è il personale che decide lo
 stato */