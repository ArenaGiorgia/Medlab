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

    public Recensione(Paziente paziente, Sede sede, int valutazione, String commento) {
        this.id = UUID.randomUUID().toString();
        this.paziente = paziente;
        this.sede = sede;
        this.valutazione = Math.max(1, Math.min(5, valutazione)); // Forza tra 1 e 5
        this.commento = commento;
        this.data = LocalDate.now();
    }

    // Getters
    public String getId() { return id; }
    public Paziente getPaziente() { return paziente; }
    public Sede getSede() { return sede; }
    public int getValutazione() { return valutazione; }
    public String getCommento() { return commento; }
    public LocalDate getData() { return data; }

    @Override
    public String toString() {
        return String.format(
                "★ %s/5 | main.Sede: %s | Autore: %s %s\nCommento: %s",
                valutazione, sede.getNome(), paziente.getNome(), paziente.getCognome(), commento
        );
    }
}