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
        Esame esame6 = new Esame(LocalDate.now(), LocalTime.of(8, 0), "Visita cardiologica");
        Esame esame7 = new Esame(LocalDate.now(), LocalTime.of(11, 0), "Test allergie");
        Esame esame8 = new Esame(LocalDate.now(), LocalTime.of(12, 30), "Elettrocardiogramma");
        Esame esame9 = new Esame(LocalDate.now(), LocalTime.of(15, 0), "Visita oculistica");

        esami.put(esame1.getCodice(), esame1);
        esami.put(esame2.getCodice(), esame2);
        esami.put(esame3.getCodice(), esame3);
        esami.put(esame4.getCodice(), esame4);
        esami.put(esame5.getCodice(), esame5);
        esami.put(esame6.getCodice(), esame6);
        esami.put(esame7.getCodice(), esame7);
        esami.put(esame8.getCodice(), esame8);
        esami.put(esame9.getCodice(), esame9);
    }

    //metodi per aggiungere un esame mi servirà per l'UC10 dell amministratore
    public void aggiungiEsame(LocalDate data, LocalTime orario, String nome) {
        Esame nuovoEsame = new Esame(data, orario, nome);
        Esame esameDecorato = new EsameControlloFestivi(nuovoEsame, null);
        this.esami.put(esameDecorato.getCodice(), nuovoEsame);
    }

    public boolean isOrarioDisponibile(LocalDate dataEsame, LocalTime orarioEsame) {
        for (Esame esame : this.esami.values()) {
            if (esame.getData().equals(dataEsame)) {
                long differenzaSecondi = Math.abs(esame.getOrario().toSecondOfDay() - orarioEsame.toSecondOfDay());
                if (differenzaSecondi < 5400) { // meno di 1h e 30m
                    return false;
                }
            }
        }
        return true;
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



    @Override
    public String toString() {
        return
                "Codice: " + this.codice +
                "  Nome: " + this.nome;
    }
}
