package main;

import java.time.LocalDate;
import java.util.UUID;

public class Recensione {
    private  String id;
    private  Paziente paziente;
    private  Sede sede;
    private  int valutazione; // 1-5 stelle
    private  String commento;
    private  LocalDate data;
    private boolean letta;

    public Recensione(Paziente paziente, Sede sede, int valutazione, String commento) {
        this.id = UUID.randomUUID().toString();
        this.paziente = paziente;
        this.sede = sede;
        this.valutazione = Math.max(1, Math.min(5, valutazione));
        this.commento = commento;
        this.data = LocalDate.now();
        this.letta= false;
    }

    public Sede getSede() {
        return sede;
    }

    public int getValutazione() {
        return valutazione;
    }

    public String getCommento() {
        return commento;
    }

    public Paziente getPaziente() { return paziente; }
    public LocalDate getData() { return data; }

    public void marcaComeLetta() {
        this.letta = true;
    }


    @Override
    public String toString() {
        return (letta ? "[VISTA] " : "[NUOVA] ") +
                this.valutazione + "/5 | Sede: " + this.sede.getNome() +
                " | Autore: " + this.paziente.getNome() + " " + this.paziente.getCognome() +
                " | Commento: " + this.commento;
    }
}