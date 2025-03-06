import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Medlab {
    private static Medlab medlab;
    private Map < String, Paziente> pazienti;
    private Paziente pazienteCorrente;

    private Medlab() {
        this.pazienti = new HashMap<>();;
    }

    public Map<String, Paziente> getPazienti() {
        return pazienti;
    }

    public static Medlab getInstance() {
        if (medlab == null)
            medlab = new Medlab();
        else
            System.out.println("Istanza già creata");
        return medlab;
    }

    public void aggiungiPaziente() {
        Scanner scanner = new Scanner(System.in);
        LocalDate dataNascita = null;
        System.out.print("Inserisci il nome del paziente: ");
        String nome = scanner.nextLine();
        System.out.print("Inserisci il cognome del paziente: ");
        String cognome = scanner.nextLine();
        do {
            try {
                System.out.print("Inserisci la data di nascita del paziente (yyyy-MM-dd): ");
                String dataNascitaInput = scanner.nextLine();
                dataNascita = LocalDate.parse(dataNascitaInput);
            } catch (DateTimeParseException e) {
                System.out.println("Data non valida. Inserisci una data valida.");
            }
        } while (dataNascita == null);
        System.out.print("Inserisci il codice fiscale del paziente: ");
        String cf = scanner.nextLine();
        System.out.print("Inserisci il sesso del paziente (m/f): ");
        String sesso = scanner.nextLine();
        System.out.print("Inserisci l'età del paziente: ");
        Integer età = Integer.parseInt(scanner.nextLine());

        nuovoPaziente( nome,  cognome, dataNascita, cf,  sesso, età);

    }

    public void nuovoPaziente(String nome, String cognome, LocalDate dataNascita, String cf, String sesso, int età) {
        Paziente paziente = new Paziente( nome, cognome, dataNascita, cf, sesso, età);
        this.pazienteCorrente = paziente;
    }
    public void confermaPaziente() {

        this.pazienti.put(this.pazienteCorrente.getCf(), this. pazienteCorrente);
        this.pazienteCorrente = null;

    }
    public void visualizzaPazienti() {
        if (pazienti.isEmpty()) {
            System.out.println("Nessun paziente registrato.");
        } else {
            for (Map.Entry<String, Paziente> entry : pazienti.entrySet()) {
                System.out.println("CF: " + entry.getKey() + " - " + "nome: " + entry.getValue().getNome() + " cognome: " + entry.getValue().getCognome());
            }
        }
    }


    public void prenotazioneEsame() {
    }

    ;

}
