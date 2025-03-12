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
        this.pazienteCorrente=null;
        this.sedi = new ArrayList<>();
        CaricamentoDati();  //per caricare dati persistenti
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

//UC1 Gestione pazienti Medlab (inserimento paziente)
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

    //Eliminare il paziente
    public void eliminaPaziente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il codice fiscale del paziente da eliminare: ");
        String codiceFiscale = scanner.nextLine();

        if (pazienti.containsKey(codiceFiscale)) {
            pazienti.remove(codiceFiscale);
            System.out.println("Paziente con codice fiscale " + codiceFiscale + " eliminato con successo.");
        } else {
            System.out.println("Errore: Nessun paziente trovato con il codice fiscale specificato.");
        }
    }


    //modificare il paziente da amministratore
    public void ModificaPazienteAmministratore(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Clienti disponibili: ");
        for (Paziente paziente : pazienti.values()) {
            System.out.println(paziente.toString());
        }
        System.out.print("Inserisci il codice fiscale del paziente da modificare: ");
        String codiceFiscale = scanner.nextLine();
        modificaPaziente(codiceFiscale);
    }
//il modificaPaziente che richiama quello del paziente per modificare ditettamente dalla classe
    public void modificaPaziente( String cf){
        Paziente paziente = selezionaPaziente(cf);
        paziente.modificaPaziente();
        this.pazienteCorrente = null;
    }

    public Paziente selezionaPaziente( String cf){
Paziente paziente = this.pazienti.get(cf);
if(paziente == null){
    System.out.println("Errore: Paziente non trovato.");
}
this.pazienteCorrente = paziente;
return paziente;
    }



    //UC2 Registrazione sede laboratorio
    public void RegistrazioneSede() {
        if (pazienteCorrente == null) {
            System.out.println("Errore: Nessun paziente attualmente autenticato!");
            return;
        }
        visualizzaSedi();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il codice della sede scelta: ");
        int codiceSede=-1;
        while (codiceSede<0) {
            try {
                codiceSede = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un codice(intero) valido.");
            }
        }
        Sede sedeSelezionata=selezionaSedePaziente(codiceSede);
        if (sedeSelezionata != null) {
            confermaSede(sedeSelezionata);
        } else {
            System.out.println("Sede non trovata. Riprova.");
        }

    }
    public void confermaSede(Sede sedeSelezionata) {
        pazienteCorrente.setSede(sedeSelezionata);
        System.out.println("Sede " + sedeSelezionata.getNome() + " assegnata a: " + pazienteCorrente.getNome() + " " + pazienteCorrente.getCognome());
    }

    public Sede selezionaSedePaziente(Integer codSede) {
        for (Sede sede : sedi){
            if (sede.getCodice()==codSede) {
                return sede;
            }
        }
                return null;
    }

    public void visualizzaSedi() {
        System.out.println("Sedi disponibili:");
        for (Sede sede : sedi) {
            System.out.println(sede.toString());
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
            this.pazienteCorrente = this.pazienti.get(codice);
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
    public void logout() {
        this.pazienteCorrente = null;
        System.out.println("Logout eseguito con successo.");
    }

    //caricamento dei dati inseriti da default come gli utenti e le prenotazioni e le sedi
    public void CaricamentoDati(){
        Paziente paziente1 = new Paziente("Matteo","Milano",LocalDate.of(2000, 12, 11),"MMLNOS00P22V462F","M");
        Paziente paziente2 = new Paziente("Maria","Salemi",LocalDate.of(1986, 9, 2),"SLMWG349P33G342LP","F");
        Sede Sede1 = new Sede("Catania",0);
        Sede Sede2 = new Sede("Messina",1);
        this.sedi.add(Sede1);
        this.sedi.add(Sede2);  // Aggiunta dell'oggetto Sede alla lista
        this.pazienti.put(paziente1.getCf(), paziente1);
        this.pazienti.put(paziente2.getCf(), paziente2);

    }

    // UC4 Inserimento nuova sede di laboratorio
    public void aggiungiSede() {
        Scanner scanner = new Scanner(System.in);
        Integer codice;
        while (true) {
            try {
                System.out.print("Inserisci il codice della sede nuova (numero intero): ");
                codice = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un numero intero per il codice sede! ");
            }
        }
        // Verifica se una sede con lo stesso codice esiste già
        for (Sede sede : sedi) {
            if (sede.getCodice() == codice) {
                System.out.println("Errore: Una sede con il codice " + codice + " esiste già.");
                return; // Esci senza aggiungere la sede
            }
        }

        System.out.println("Inserisci nome sede: ");
        String nome = scanner.nextLine();

        Sede nuovaSede = new Sede(nome,codice);  // Creazione di un oggetto Sede
        sedi.add(nuovaSede);  // Aggiunta dell'oggetto Sede alla lista
        System.out.println("Sede " + nome + " aggiunta con successo.");
    }

    @Override
    public String toString() {
        return "Medlab{" +
                "pazienteCorrente=" + pazienteCorrente +
                '}';
    }
}


