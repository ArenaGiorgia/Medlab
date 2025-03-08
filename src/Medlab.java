import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Medlab {
    private static Medlab medlab;
    private Map < String, Paziente> pazienti;
    private Paziente pazienteCorrente;
    private Amministratore amministratore;
    private List<Sede> sedi;

    private Medlab() {
        this.pazienti = new HashMap<String,Paziente>();
        this.amministratore= new Amministratore();
        // this.pazienteCorrente=null;
        this.sedi = new ArrayList<>();
    }



    public static Medlab getInstance() {
        if (medlab == null)
            medlab = new Medlab();
        else
            System.out.println("Istanza già creata");
        return medlab;
    }

    public String getAmministratore() {
        return amministratore.getCodiceFiscale();
    }

    public Map<String, Paziente> getPazienti() {
        return pazienti;
    }
//UC1
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
        String sesso;
        do {
            try {
                System.out.print("Inserisci il sesso del paziente (M/F): ");
                sesso = scanner.nextLine();

                if (!sesso.equals("M") && !sesso.equals("F")) {
                    throw new IllegalArgumentException("Errore: Devi inserire solo 'M' per maschio o 'F' per femmina.");
                }
                break;

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
        nuovoPaziente(nome,cognome,dataNascita,cf,sesso);
        confermaPaziente();
    }

    public void nuovoPaziente(String nome, String cognome, LocalDate dataNascita, String cf, String sesso ) {
        Paziente paziente = new Paziente( nome, cognome, dataNascita, cf, sesso);
        this.pazienteCorrente = paziente;
    }
    public void confermaPaziente() {
        if (this.pazienteCorrente == null) {
            System.out.println("Errore: Nessun paziente da confermare!");
            return;
        }
        this.pazienti.put(this.pazienteCorrente.getCf(), this. pazienteCorrente);

        System.out.println("Riepilogo informazioni inserite: ");
        System.out.print(this.pazienteCorrente.toString());
        this.pazienteCorrente = null;

    }
    public void aggiungiSede() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci codice sede: ");
        int codice = Integer.parseInt(scanner.nextLine());
        System.out.println("Inserisci nome sede: ");
        String nome = scanner.nextLine();

        Sede nuovaSede = new Sede(nome,codice);  // Creazione di un oggetto Sede
        sedi.add(nuovaSede);  // Aggiunta dell'oggetto Sede alla lista
        System.out.println("Sede " + nome + " aggiunta con successo.");
    }
    public void selezionaSedePerPaziente() {

       /* if (pazienteCorrente.getSede()!= null)
            System.out.println("Hai già una sede preferita: " + pazienteCorrente.getSede().getNome()); */

        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il codice della sede scelta: ");
        int codiceSede = Integer.parseInt(scanner.nextLine());
        for (Sede sede : sedi) {
            if (sede.getCodice()==codiceSede) {
                pazienteCorrente.setSede(sede);
                System.out.println("Sede " + sede.getNome() + " assegnata a: " +  pazienteCorrente.getNome() + pazienteCorrente.getCognome());
                return;
            }
        }
        System.out.println("Sede non trovata. Riprova.");
    }




    public void visualizzaSedi() {
        System.out.println("Sedi disponibili:");
        for (Sede sede : sedi) {
            System.out.println(sede.getCodice() + ": " + sede.getNome());
        }
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

    //metodo per verificare nel menu se sta accedendo un amministratore,paziente o personale di laboratorio
    public String VerificaAccesso( String codice, String password) {
        if (verificaAmministratore(codice, password)) {
            return "amministratore";
        } else if (verificaPaziente(codice, password)) {
            return "paziente";
       /* } else if (verificaPersonaleLaboratorio(codice, password)) {
            return "personale";  */
        } else {
            return "invalid_error";
        }
    }
    public boolean verificaAmministratore( String codice, String password) {
        if (this.amministratore != null &&
                this.amministratore.getCodiceFiscale().equals(codice) && this.amministratore.verificaPassword(password)) {
            return true;
        }
        return false;
    }


    public boolean verificaPaziente( String codice, String password) {
        Paziente paziente = this.pazienti.get(codice);
        if (paziente != null && paziente.verificaPassword(password)) {
            return true;
        }
        return false;
    }


    public void prenotazioneEsame() {
    }
    @Override
    public String toString() {
        return "Medlab{" +
                "pazienteCorrente=" + pazienteCorrente +
                '}';
    }
}


