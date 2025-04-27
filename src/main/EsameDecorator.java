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
