import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Map<String, Esame> getEsami() {
        return esami;
    }

    public void caricaEsami() {

            Esame esame1 = new Esame(LocalDate.of(2025, 3, 20), LocalTime.of(9, 0), "Analisi del sangue");
            Esame esame2 = new Esame(LocalDate.of(2025, 3, 20), LocalTime.of(10, 30), "Ecografia addome");
            Esame esame3 = new Esame(LocalDate.of(2025, 3, 20), LocalTime.of(12, 0), "Radiografia torace");
            esami.put(esame1.getCodice(), esame1);
            esami.put(esame2.getCodice(), esame2);

    }


    //metodi per aggiungere un esame mi servirà per l'UC10 dell amministratore anche
    public void aggiungiEsame(Esame esame) {
        if (!this.esami.containsKey(esame.getNome())) {
            this.esami.put(esame.getNome(), esame);
        } else {
            System.out.println("Esame: " + esame.getNome() + " già presente.");
        }
    }
    public void modificaSede(List<Sede> sedi) { //converebbe forse una mappa per evitare duplicati ma cosi funziona
        Scanner scanner = new Scanner(System.in);
        int scelta;
        do {
            System.out.println("Seleziona il campo da modificare per la sede: ");
            System.out.println("1. Codice della sede"); // da modificare e facciamo da default e inseriamo solo il nome nuovo della sede
            System.out.println("2. Nome della sede");
            System.out.println("0. Esci");
            System.out.print("Inserisci il numero corrispondente: ");
            scelta = scanner.nextInt();
            scanner.nextLine();  // Consuma la nuova linea lasciata da nextInt()

            switch (scelta) {
                case 1:
                    Integer codice = null;
                    do {
                        try {
                            System.out.print("Inserisci il nuovo codice della sede (numero intero): ");
                            codice = Integer.parseInt(scanner.nextLine());
                            boolean codiceEsistente = false;
                            for (Sede s : sedi) {
                                if (!s.equals(this) && s.getCodice().equals(codice)) {
                                    codiceEsistente = true;
                                    break;
                                }
                            }
                            if (codiceEsistente) {
                                System.out.println("Errore: Esiste già una sede con questo codice! Riprova.");
                            } else {
                                setCodice(codice);
                                System.out.println("Codice sede aggiornato con successo!");
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Errore: Devi inserire un numero intero per il codice della sede.");
                        }
                    } while (true);
                    break;
                case 2:
                    System.out.print("Inserisci il nuovo nome della sede: ");
                    String nome = scanner.nextLine();
                    setNome(nome);  // Metodo per impostare il nuovo nome
                    break;
                case 0:
                    System.out.println("Modifica sede terminata.");
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
                    break;
            }
        } while (scelta != 0);
    }

    public void mostraEsamiDisponibili() {
        for (Esame e : esami.values()) {
            System.out.println(e);
        }
    }


    @Override
    public String toString() {
        return "Sede{" +
                "codice=" + codice +
                ", nome='" + nome + '\'' +
                '}';
    }
}
