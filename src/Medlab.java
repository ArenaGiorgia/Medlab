import com.sun.xml.internal.bind.v2.TODO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Medlab {
    private static Medlab medlab;
    private Map < String, Paziente> pazienti;
    private Paziente pazienteCorrente;
    private Sede sedeCorrente;
    private Amministratore amministratore;
    private List<Sede> sedi; //abbiamo utilizzato la lista perche abbiamo le sedi ordinate e ci possono essere duplicazioni ma le gestiamo col codice
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
    public void eliminaPaziente() { //da fare estensione *b dove verifica se ci sono prenotazioni prenotate
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pazienti disponibili: ");
        for (Map.Entry<String, Paziente> entry : pazienti.entrySet()) {
            System.out.println(entry.getValue().toString());
        }
        System.out.print("Inserisci il codice fiscale del paziente da eliminare: ");
        String codiceFiscale = scanner.nextLine();
        boolean pazienteTrovato = false;
        for (Map.Entry<String, Paziente> entry : pazienti.entrySet()) {
            if (entry.getKey().equals(codiceFiscale)) {
                Paziente p = entry.getValue();
                boolean prenotazioneAttiva = false; // Verifica se ci sono prenotazioni attive

                for (Map.Entry<String, Prenotazione> prenotazioneEntry : prenotazioni.entrySet()) {
                    Prenotazione prenotazione = prenotazioneEntry.getValue();
                    if (prenotazione.getPaziente().equals(p)) {
                        prenotazioneAttiva = true;
                        break;
                    }
                }
                if (prenotazioneAttiva) {
                    System.out.println("Errore: Il paziente ha prenotazioni in corso e non può essere eliminato.");
                } else {
                    pazienti.remove(entry.getKey());
                    System.out.println("Paziente con codice fiscale " + codiceFiscale + " eliminato con successo.");
                }
                pazienteTrovato = true;
                break;
            }
        }
        if (!pazienteTrovato) {
            System.out.println("Errore: Nessun paziente trovato con il codice fiscale specificato.");
        }


    }
    // UC1 modificare il paziente da amministratore
    public void modificaPazienteAmministratore(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pazienti disponibili: ");
        for (Map.Entry<String, Paziente> entry : pazienti.entrySet()) {
            Paziente paziente = entry.getValue();
            System.out.println(paziente.toString());
        }
        System.out.print("Inserisci il codice fiscale del paziente da modificare: ");
        String codiceFiscale = scanner.nextLine();
        modificaPaziente(codiceFiscale);
    }

    public void modificaPaziente( String cf){  //ci puo servire per l'UC8
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
        visualizzaSedi(); //1. visualizza tutte le sedi attuali
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
        if (pazienteCorrente.getSedi().contains(sedeSelezionata)) {
            System.out.println("Il paziente ha già questa sede.");
        } else {
            pazienteCorrente.getSedi().add(sedeSelezionata);
            System.out.println("Sede " + sedeSelezionata.getNome() + " Codice: " + sedeSelezionata.getCodice() + " assegnata a: " + pazienteCorrente.getNome() + " " + pazienteCorrente.getCognome());
        }
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
        if (sedi.isEmpty()) {
            System.out.println("Non ci sono sedi disponibili.");
        } else {
            System.out.println("Sedi disponibili:");
            for (Sede sede : sedi) {
                System.out.println(sede.toString());
            }
        }
    }

    //UC 3 prenotazione all'esame di una relativa sede
    public void PrenotazioneEsame() {
            if (pazienteCorrente == null) {
            System.out.println("Errore: Nessun paziente attualmente autenticato!");
            return;
        }

        if (!visualizzaSedePaziente(pazienteCorrente)) { // 1
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleziona il codice della sede per la prenotazione: "); //Regola di buisness perche posso avere piu sedi associate
        int codiceSede = -1;

        while (codiceSede < 0) {
            try {
                codiceSede = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un numero intero per il codice sede!");
            }
        }

        Sede sedeSelezionata = selezionaSedePaziente(codiceSede); // 2
        if (sedeSelezionata == null) {
            System.out.println("Errore: Sede non trovata.");
            return;
        }

        if (!pazienteCorrente.getSedi().contains(sedeSelezionata)) {
            System.out.println("Errore: La sede selezionata non è associata a questo paziente.");
            return;
        }
        VisualizzaEsamiDisponibili(sedeSelezionata); //4

        System.out.print("Inserisci il codice dell'esame che desideri prenotare: ");
        String codiceEsame = scanner.nextLine().trim();

        Esame esameSelezionato = sedeSelezionata.getEsami().get(codiceEsame);
        if (esameSelezionato == null) {
            System.out.println("Errore: Esame non trovato.");
            return;
        }
        LocalDate dataEsame = esameSelezionato.getData();
        if (!PrenotazioniMaxPerGiorno(pazienteCorrente, dataEsame)) {
            System.out.println("Errore: Hai già raggiunto il limite massimo di 3 prenotazioni per il giorno " + dataEsame);
            return;
        }
        if (!EsameDisponibile(esameSelezionato.getCodice())) {
            System.out.println("Errore: Questo esame è già stato prenotato.");
            return;
        }

        SelezionaEsame(esameSelezionato); //5
        ConfermaEsame(); // 6
    }

  public void  VisualizzaEsamiDisponibili(Sede sedeSelezionata){
      sedeSelezionata.getEsami().entrySet().removeIf(entry -> entry.getValue().getData().isBefore(LocalDate.now()));

      // Ordinamento degli esami disponibili per orario
      Map<String, Esame> esamiOrdinati = sedeSelezionata.getEsami().entrySet().stream()
              .sorted(Map.Entry.comparingByValue(Comparator.comparing(Esame::getOrario)))
              .sorted(Map.Entry.comparingByValue(Comparator.comparing(Esame::getData)))
              .collect(Collectors.toMap(
                      Map.Entry::getKey,
                      Map.Entry::getValue,
                      (e1, e2) -> e1,
                      LinkedHashMap::new
              ));

      System.out.println("Esami disponibili in ordine di orario:");
      for (Map.Entry<String,Esame> entry : esamiOrdinati.entrySet()) {
          System.out.println(entry.getValue().toString());
      }


  }

    public void SelezionaEsame(Esame esameSelezionato) {
        if (pazienteCorrente == null) {
            System.out.println("Errore: Nessun paziente attualmente autenticato.");
            return;
        }
        if (!EsameDisponibile(esameSelezionato.getCodice())) {
            System.out.println("Errore: Questo esame è già stato prenotato.");
            return;
        }
        Prenotazione prenotazione = new Prenotazione(esameSelezionato, pazienteCorrente);
        this.prenotazioneCorrente = prenotazione;
    }

    public void ConfermaEsame() {
        if (this.prenotazioneCorrente == null) {
            System.out.println("Errore: Nessuna prenotazione da confermare!");
            return;
        }
        this.prenotazioni.put(this.prenotazioneCorrente.getCodice(), this.prenotazioneCorrente);
        this.pazienteCorrente.getPrenotazioni().put(this.prenotazioneCorrente.getCodice(), this.prenotazioneCorrente);
        this.prenotazioneCorrente.getEsame().prenotato();
        System.out.println("Prenotazione confermata per l'esame: " + this.prenotazioneCorrente.getEsame().getNome() +
                " il " + this.prenotazioneCorrente.getEsame().getData() + " alle " + this.prenotazioneCorrente.getEsame().getOrario() + " a nome di: "
                + pazienteCorrente.getNome() + " " + pazienteCorrente.getCognome());
        this.prenotazioneCorrente = null;
    }

    // Metodo per trovare la sede in base a un esame
    private Sede trovaSedePerEsame(Esame esame) {
        for (Sede sede : sedi) {
            if (sede.getEsami().containsValue(esame)) {
                return sede;
            }
        }
        return null;
    }
    // Metodo per visualizzare le prnotazioni associate al paziente, quindi visualizzare la mappa all'interno del Paziente
 /* public void visualizzaPrenotazioniPaziente() {
        if (pazienteCorrente == null) {
            System.out.println("Errore: Nessun paziente attualmente autenticato!");
            return;
        }
        Map<String, Prenotazione> prenotazioniPaziente = pazienteCorrente.getPrenotazioni();
        if (prenotazioniPaziente.isEmpty()) {
            System.out.println("Nessuna prenotazione trovata per " + pazienteCorrente.getNome() + " " + pazienteCorrente.getCognome());
            return;
        }
        TreeMap<String, Prenotazione> prenotazioniOrdinate = new TreeMap<>(new EsameComparator(
                prenotazioniPaziente.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getEsame()))
        ));

        prenotazioniOrdinate.putAll(prenotazioniPaziente);
        System.out.println("Prenotazioni di " + pazienteCorrente.getNome() + " " + pazienteCorrente.getCognome() + ":");
        for (Prenotazione prenotazione : prenotazioniOrdinate.values()) {
            Esame esame = prenotazione.getEsame();
            Sede sede = trovaSedePerEsame(esame);
            System.out.println("Esame: " + esame.getNome() +
                    " - Data: " + esame.getData() +
                    " - Orario: " + esame.getOrario()+
                    " - Sede: " + (sede != null ? sede.getNome() : "Non trovata"));
        }
    }
*/

    //Metodo per la regola di buisness R8 di massimo 3 prenotazioni al giorno
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


//Visualizza gli Esami disponibili in maniera ordinata
  /*  public void visualizzaEsamiDisponibili(Sede sede) {
        if (sede.getEsami().isEmpty()) {
            System.out.println("Nessun esame disponibile presso la sede " + sede.getNome());
            return;
        }
        TreeMap<String, Esame> esamiOrdinati = new TreeMap<>(new EsameComparator(sede.getEsami()));
        esamiOrdinati.putAll(sede.getEsami());

        System.out.println("\n Esami disponibili presso la sede " + sede.getNome() + ":");
        for (Esame esame : esamiOrdinati.values()) {
            System.out.println("Codice: " + esame.getCodice() +
                    " - Nome: " + esame.getNome() +
                    " - Data: " + esame.getData() +
                    " - Orario: " + esame.getOrario()+
                    " - Stato: " + esame.statoEsame());
        }
    }*/


 // Check esame disponibile
    private boolean EsameDisponibile(String codiceEsame) {
        for (Prenotazione prenotazione : prenotazioni.values()) {
            if (prenotazione.getEsame().getCodice().equals(codiceEsame)) {
                return false;
            }
        }
        return true;
    }
    //Mi serve per vedere a quali sedi il paziente è associato
public boolean visualizzaSedePaziente(Paziente p){
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

    //Metodo per verificare nel menu se sta accedendo un amministratore,paziente o personale di laboratorio
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
    Iterator<Sede> iterator = sedi.iterator(); //meglio l iterator per evitare le eccezioni
    while (iterator.hasNext()) {
        Sede sede = iterator.next();
        if (sede.getCodice().equals(codice)) {
            iterator.remove();
            System.out.println("La sede con codice " + codice + " è stata eliminata con successo.");
            return;
        }
    }
    System.out.println("Errore: Nessuna sede trovata con il codice specificato.");
}
//UC4 modifica Sede
    public void modificaSedeAmministratore(){
    Scanner scanner = new Scanner(System.in);
    Integer codice=null;
    System.out.println("Sedi disponibili: ");
    for (Sede sede : sedi) {
        System.out.println(sede.toString());
    }
    System.out.print("Inserisci il codice della sede da modificare: ");
    codice = Integer.parseInt(scanner.nextLine());
    modificaSede(codice);
}
 public void modificaSede(Integer codice){
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



