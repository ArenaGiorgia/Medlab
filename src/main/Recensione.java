package main;

import java.time.LocalDate;
import java.util.UUID;

public class Recensione {
    private final String id;
    private final Paziente paziente;
    private final Sede sede;
    private final int valutazione; // 1-5 stelle
    private final String commento;
    private final LocalDate data;

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