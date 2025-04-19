package main;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class EsameDecorator extends Esame {
    protected Esame esame;

    public EsameDecorator(Esame esame) {
        super(esame.getData(), esame.getOrario(), esame.getNome());
        this.esame = esame;
    }
    public boolean prenotabile() {
        return true;
    }
    @Override
    public String getCodice() {
        return esame.getCodice();
    }

    @Override
    public LocalDate getData() {
        return esame.getData();
    }

    @Override
    public LocalTime getOrario() {
        return esame.getOrario();
    }

    @Override
    public String getNome() {
        return esame.getNome();
    }

    @Override
    public boolean isPrenotato() {
        return esame.isPrenotato();
    }

    @Override
    public void prenotato() {
        esame.prenotato();
    }

    @Override
    public void annullaPrenotazione() {
        esame.annullaPrenotazione();
    }

    @Override
    public String statoEsame() {
        return esame.statoEsame();
    }

    @Override
    public String toString() {
        return esame.toString();
    }
}

//mantiene il comportamento originale di esame , ma da esso posso poi aggiungere tutte le mie "decorazioni"
/*UC3 e UC10 dove possiamo andare a fare le prenotazioni possiamo dire che abbiamo voluto aggiungere delle funzionalita
al nostro progetto, dove stavolta è stata anche aggiunta una funzione per non mettere giorni festivi e feriali e
riservarli solo ai pazienti che sono malati cronici.
 */