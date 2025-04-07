public interface StatoPrenotazione {
    void completa(Prenotazione prenotazione);
    void annulla(Prenotazione prenotazione);
    String getNomeStato();
}

/*l interfaccia che ci permette di gestire lo stato con il pattern state, dove sarà il personale di laboratorio ad
 switchare dallo stato della prenotazione in corso allo stato completato generando il referto vuoto (con descrizione
 in corso perche nel UC6 andremo a mettere le vere informazioni ) , nel void completa è il personale che decide lo
 stato */