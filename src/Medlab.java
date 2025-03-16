import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Medlab {
    private static Medlab medlab;
    private Map < String, Paziente> pazienti;
    private Paziente pazienteCorrente;
    private Sede sedeCorrente;
    private Amministratore amministratore;
    private List<Sede> sedi;
    private Map<String, Prenotazione> prenotazioni;
    private Prenotazione prenotazioneCorrente;


    private Medlab() {
        this.pazienti = new HashMap<String,Paziente>();
        this.prenotazioni = new HashMap<String, Prenotazione>();
        this.amministratore= new Amministratore();
        this.pazienteCorrente=null;
        this.prenotazioneCorrente=null;
        this.sedeCorrente=null;
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

    public Map<String, Paziente> getPazienti() {
        return pazienti;
    }

    public void setPazienteCorrente(Paziente pazienteCorrente) {
        this.pazienteCorrente = pazienteCorrente;
    }
    public void setPazienti(Map<String, Paziente> pazienti) {
        this.pazienti = pazienti;
    }
    public void setSedi(List<Sede> sedi) {
        this.sedi = sedi;
    }
    public void setPrenotazioneCorrente(Prenotazione prenotazioneCorrente) {
        this.prenotazioneCorrente = prenotazioneCorrente;
    }

    public Paziente getPazienteCorrente() {
        return pazienteCorrente;
    }

    public List<Sede> getSedi() {
        return sedi;
    }

    public Map<String, Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public Prenotazione getPrenotazioneCorrente() {
        return prenotazioneCorrente;
    }
    public String getAmministratore() {
        return amministratore.getCodiceFiscale();
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
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Data non valida. Inserisci una data valida.");
            }
        } while (true);
        System.out.print("Inserisci il codice fiscale del paziente: ");
        String cf = scanner.nextLine();
        if (cf.isEmpty()) {
            System.out.println("Errore: Il codice fiscale non può essere vuoto!");
            return;
        }
        if (pazienti.containsKey(cf)) {
            System.out.println("Errore: Esiste già un paziente con questo codice fiscale!");
            return;
        }
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
        nuovoPaziente(nome,cognome,dataNascita,cf,sesso); //1.aggiungi tutti i dati del paziente
        confermaPaziente(); //2.conferma
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

    // UC1 Eliminare il paziente
    public void eliminaPaziente() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pazienti disponibili: ");
        for (Paziente paziente : pazienti.values()) {
            System.out.println(paziente.toString());
        }
        System.out.print("Inserisci il codice fiscale del paziente da eliminare: ");
        String codiceFiscale = scanner.nextLine();

        if (pazienti.containsKey(codiceFiscale)) {
            pazienti.remove(codiceFiscale);
            System.out.println("Paziente con codice fiscale " + codiceFiscale + " eliminato con successo.");

        } else {
            System.out.println("Errore: Nessun paziente trovato con il codice fiscale specificato.");
        }
    }

    // UC1 modificare il paziente da amministratore
    public void modificaPazienteAmministratore(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pazienti disponibili: ");
        for (Paziente paziente : pazienti.values()) {
            System.out.println(paziente.toString());
        }
        System.out.print("Inserisci il codice fiscale del paziente da modificare: ");
        String codiceFiscale = scanner.nextLine();
        modificaPaziente(codiceFiscale);
    }
//il modificaPaziente che richiama quello del paziente per modificare ditettamente dalla classe
    //ci puo servire per l'UC8
    public void modificaPaziente( String cf){
        Paziente paziente = selezionaPaziente(cf);
        if (paziente != null) {
            paziente.modificaPaziente();
            this.pazienteCorrente = null;
        } else {
            System.out.println("Errore: Paziente non trovato.");
        }
    }
    public Paziente selezionaPaziente( String cf){
    Paziente paziente = this.pazienti.get(cf);
    if(paziente != null)
        this.pazienteCorrente = paziente;
         return paziente;
    }

    //UC2 Registrazione sede laboratorio
    public void RegistrazioneSede() {
        if (pazienteCorrente == null) {
            System.out.println("Errore: Nessun paziente attualmente autenticato!");
            return;
        }
        visualizzaSedi(); //1. visualizza le sedi disponibili
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
        Sede sedeSelezionata=selezionaSedePaziente(codiceSede); //2. seleziona la sede
        if (sedeSelezionata != null) {
            confermaSede(sedeSelezionata); //3. conferma sede
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

    //UC 3 prenotazione all'esame di una relativa sede
    public void PrenotazioneEsame(String cf) {
        if(pazienteCorrente==null){
            System.out.println("Errore: Nessun paziente attualmente autenticato!");
            return;
        }
        if (!visualizzaSedePaziente(pazienteCorrente)) {  // 1.Controlla se il paziente ha sedi associate
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleziona il codice della sede per la prenotazione: ");
        int codiceSede = -1;
        while (codiceSede < 0) {
            try {
                codiceSede = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un numero intero per il codice sede!");
            }
        }

        Sede sedeSelezionata = selezionaSedePaziente(codiceSede); //2. seleziona quella dove fare la prenotazione

        if (sedeSelezionata != null) {
            if (!pazienteCorrente.getSedi().contains(sedeSelezionata)) {
                System.out.println("Errore: La sede selezionata non è associata a questo paziente.");
                return;
            }
            visualizzaEsamiDisponibili(sedeSelezionata); //3.visualizza gli esami disponibili di quella sede

            System.out.print("Inserisci il nome dell'esame che desideri prenotare: ");
            String nomeEsame = scanner.nextLine();

            Esame esameSelezionato = SelezionaEsame(sedeSelezionata,nomeEsame); //4. seleziona l esame della lista

            if (esameSelezionato == null) {
                System.out.println("Errore: Esame non trovato.");
                return;
            }
                try {
                    System.out.print("Inserisci la data dell'esame (yyyy-MM-dd) che desideri prenotare: ");
                    LocalDate dataEsame = LocalDate.parse(scanner.nextLine());
                    if (!PrenotazioniMaxPerGiorno(pazienteCorrente, dataEsame)) { //per la regola di buisness R8
                        System.out.println("Errore: Hai già 3 prenotazioni per questo giorno.");
                        return;
                    }
                    System.out.print("Inserisci l'orario dell'esame (HH:mm) che desideri prenotare: ");
                    LocalTime orarioEsame = LocalTime.parse(scanner.nextLine());
                    confermaEsame(esameSelezionato,dataEsame,orarioEsame); //5. esame confermato
                } catch (DateTimeParseException e) {
                    System.out.println("Errore: La data o l'orario inserito non sono nel formato corretto. Prenotazione non effettuata.");
                }
            }
        else {
            System.out.println("Sede non trovata. Riprova.");
        }
    }
    private void confermaEsame(Esame esameSelezionato, LocalDate dataEsame, LocalTime orarioEsame) {
        if (pazienteCorrente == null) {
            System.out.println("Errore: Nessun paziente attualmente autenticato.");
            return;
        }
        if (!esameSelezionato.getData().equals(dataEsame) || !esameSelezionato.getOrario().equals(orarioEsame)) {
            System.out.println("Errore: Esame o orario non trovato.");
            return;
        }
        if (!EsameDisponibile(dataEsame, orarioEsame)) {
            System.out.println("Errore: L'orario selezionato non è disponibile.");
            return;
        }
        Prenotazione prenotazione = new Prenotazione(esameSelezionato, pazienteCorrente);
        prenotazione.confermaPrenotazione();
        this.prenotazioni.put(prenotazione.getCodice(), prenotazione);
        this.prenotazioneCorrente = prenotazione;
        esameSelezionato.prenotato();
        System.out.println("Prenotazione confermata per l'esame: " + esameSelezionato.getNome() +
                " il " + dataEsame + " alle " + orarioEsame);
    }

    //metodo per la regola di buisness R8 di massimo 3 prenotazioni al giorno
    public boolean PrenotazioniMaxPerGiorno(Paziente paziente, LocalDate data) {
        int prenotazioniGiornaliere = 0;
        for (Prenotazione prenotazione : prenotazioni.values()) {
            LocalDate dataEsame = prenotazione.getEsame().getData();
            if (dataEsame.equals(data)) {
                prenotazioniGiornaliere++;
            }
        }
        return prenotazioniGiornaliere < 3;
    }



    private Esame SelezionaEsame(Sede sedeSelezionata, String nomeEsame) {
        return sedeSelezionata.getEsami().get(nomeEsame);
    }

    // Metodo per visualizzare gli esami disponibili per una sede con date e orari
    public void visualizzaEsamiDisponibili(Sede sede) {
        System.out.println("Esami disponibili presso la sede " + sede.getNome() + ":");
        for (Esame esame : sede.getEsami().values()) {
            System.out.println("Codice: " + esame.getCodice() + " - nome: " + esame.getNome() +
                    " - Data: " + esame.getData() + " - Orario: " + esame.getOrario() + " - Stato: " + esame.statoEsame());
        }
    }

    public boolean EsameDisponibile(LocalDate data, LocalTime orario) {
        for (Prenotazione prenotazione : this.prenotazioni.values()) {
            LocalTime orarioInizio = prenotazione.getEsame().getOrario();
            LocalTime orarioFine = orarioInizio.plusMinutes(90);
            if (prenotazione.getEsame().getData().equals(data) &&
                    ((orario.isAfter(orarioInizio) && orario.isBefore(orarioFine)) || orario.equals(orarioInizio))) {
                return false;
            }
        }
        return true;
    }

public boolean visualizzaSedePaziente(Paziente p){ //mi serve per vedere a quali sedi il paziente è associato
        if (p.getSedi().isEmpty()) {
            System.out.println("Non hai associato nessuna sede.");
            return false;
        }
        System.out.println("Le sedi associate al paziente sono: ");
        for (Sede sede : p.getSedi()) {
            System.out.println(sede.toString());
        }
        return true;
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
        Sede sede1 = new Sede(0,"Catania");
        Sede sede2 = new Sede(1,"Messina");
        sede1.caricaEsami();
        sede2.caricaEsami();
        this.sedi.add(sede1);
        this.sedi.add(sede2);
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
        System.out.print("Inserisci nome sede:");
        String nome = scanner.nextLine();
        if (nome.isEmpty()) {
            System.out.println("Errore: Il nome della sede non può essere vuoto!");
            return;
        }
        for (Sede s : sedi) {
            if (s.getCodice().equals(codice)) {
                System.out.println("Errore: Esiste già una sede con questo codice!");
                return;
            }
        }
        nuovaSede(nome,codice); //1. nuova sede
        confermaSede(); //2. conferma
    }
    public void nuovaSede(String nome,Integer codice) {
        Sede sede = new Sede(codice,nome);
        this.sedeCorrente=sede;
    }
    public void confermaSede() {
        if (this.sedeCorrente == null) {
            System.out.println("Errore: Nessuna sede da confermare!");
            return;
        }
        this.sedi.add(this.sedeCorrente);
        System.out.println("Riepilogo informazioni inserite: ");
        System.out.print(this.sedeCorrente.toString());
        this.sedeCorrente= null;
    }
//UC4 Elimina sede
public void eliminaSede() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Sedi disponibili: ");
    for (Sede sede : sedi) {
        System.out.println(sede.toString());
    }
    System.out.print("Inserisci il codice della sede da eliminare: ");
    Integer codice=null;
    while (true) {
        try {
            codice = Integer.parseInt(scanner.nextLine());
            break;
        } catch (NumberFormatException e) {
            System.out.println("Errore: Il codice deve essere un numero intero.");
        }
    }
    for (int i = 0; i < sedi.size(); i++) {
        if (sedi.get(i).getCodice().equals(codice)) {
            sedi.remove(i);
            System.out.println("La sede con codice " + codice + " è stata eliminata con successo.");
            return;
        }
    }
    System.out.println("Errore: Nessuna sede trovata con il codice specificato.");
}
//UC4 modifica Sede
    public void modificaSedeAmministratore(){
    Scanner scanner = new Scanner(System.in);
    System.out.println("Sedi disponibili: ");
    for (Sede sede : sedi) {
        System.out.println(sede.toString());
    }
    System.out.print("Inserisci il codice della sede da modificare: ");
    Integer codice = Integer.parseInt(scanner.nextLine());
    modificaSede(codice);
}
 public void modificaSede( Integer codice){
        Sede sede = selezionaSedePaziente(codice);
        if (sede != null) {
            sede.modificaSede();
            this.sedeCorrente = null;
        } else {
            System.out.println("Errore: Sede non trovata.");
        }
    }

    @Override
    public String toString() {
        return "Medlab{" +
                "pazienteCorrente=" + pazienteCorrente +
                '}';
    }
}



