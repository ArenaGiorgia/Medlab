package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Sede {
    private Integer codice;
    private String nome;
    private Map<String, Esame> esami; //di default deve avere una serie di esami che poi associero alle prenotazioni

    public Sede(Integer codice,String nome) {
        this.nome = nome;
        this.codice = codice;
        this.esami = new HashMap<>();
        caricaEsami();

    }


    public Integer getCodice() {

        return codice;
    }

    public String getNome() {

        return nome;
    }


    public void setNome(String nome) {

        this.nome = nome;
    }

    public Map<String, Esame> getEsami() {

        return esami;
    }

    public void caricaEsami() {

            Esame esame1 = new Esame(LocalDate.now(), LocalTime.of(9, 0), "Analisi del sangue");
            Esame esame2 = new Esame(LocalDate.now(), LocalTime.of(10, 30), "Ecografia addome");
            Esame esame3 = new Esame(LocalDate.now(), LocalTime.of(13, 30), "Radiografia torace");
            Esame esame4 = new Esame(LocalDate.of(2026, 3, 19), LocalTime.of(7, 30), "Radiografia torace");
            Esame esame5 = new Esame(LocalDate.of(2026, 12, 25), LocalTime.of(10, 0), "Ecografia di Babbo Natale");
            esami.put(esame1.getCodice(), esame1);
            esami.put(esame2.getCodice(), esame2);
            esami.put(esame3.getCodice(),esame3);
            esami.put(esame4.getCodice(),esame4);
            esami.put(esame5.getCodice(),esame5); //per verificare il decoratore


    }

    //metodi per aggiungere un esame mi servirà per l'UC10 dell amministratore anche
    public void aggiungiEsame(Esame esame) {
        if (!this.esami.containsKey(esame.getCodice())) {
            this.esami.put(esame.getCodice(), esame);
        } else {
            System.out.println("main.Esame con il codice: " + esame.getCodice() + " già presente.");
        }
    }
    public void modificaSede() {
        Scanner scanner = new Scanner(System.in);
        int scelta;
        do {
            System.out.println("Seleziona il campo da modificare per la sede: ");
            System.out.println("1. Nome della sede");
            System.out.println("0. Esci");
            System.out.print("Inserisci il numero corrispondente: ");
            scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    System.out.print("Inserisci il nuovo nome della sede: ");
                    String nome = scanner.nextLine();
                    setNome(nome);  // Metodo per impostare il nuovo nome
                    break;
                case 0:
                    System.out.println("Modifica sede terminata con successo.");
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
                    break;
            }
        } while (scelta != 0);
    }

    public void mostraEsamiDisponibili() {
        for (Map.Entry<String, Esame> entry : esami.entrySet()) {
            String codice = entry.getKey();
            Esame esame = entry.getValue();
            System.out.println("Codice: " + codice + " | main.Esame: " + esame);
        }
    }


    @Override
    public String toString() {
        return
                "Codice: " + this.codice +
                "  Nome: " + this.nome;
    }
}
