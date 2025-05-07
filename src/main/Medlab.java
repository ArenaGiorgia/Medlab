package main;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Medlab extends Observable{
    private static Medlab medlab;
    private Map < String, Paziente> pazienti;
    private Paziente pazienteCorrente;
    private Sede sedeCorrente;
    private Amministratore amministratore;
    private Prenotazione prenotazioneCorrente;
    private PersonaleLaboratorio personaleLaboratorioCorrente;
    private List<Sede> sedi;
    private Map<String, Prenotazione> prenotazioni;
    private Map<String, PersonaleLaboratorio> personaleLaboratori;
    private Report reportCorrente;

    private Medlab() {
        this.pazienti = new HashMap< String, Paziente>();
        this.prenotazioni = new HashMap<String, Prenotazione>();
        this.amministratore = new Amministratore();
        this.pazienteCorrente = null;
        this.prenotazioneCorrente = null;
        this.sedeCorrente = null;
        this.sedi = new ArrayList<>();
        this.personaleLaboratorioCorrente = null;
        this.personaleLaboratori = new HashMap<String, PersonaleLaboratorio>();
        this.reportCorrente=null;
        addObserver(this.amministratore);
        caricamentoDati();
    }

    public static Medlab getInstance() {
        if (medlab == null)
            medlab = new Medlab();
        else
            System.out.println("Istanza già creata");
        return medlab;
    }


    public Map<String, PersonaleLaboratorio> getPersonaleLaboratori() {
        return personaleLaboratori;
    }

    public void setReportCorrente(Report reportCorrente) {
        this.reportCorrente = reportCorrente;
    }

    public void setSedeCorrente(Sede sedeCorrente) {
        this.sedeCorrente = sedeCorrente;
    }

    public Sede getSedeCorrente() {
        return sedeCorrente;
    }

    public void setPazienteCorrente(Paziente pazienteCorrente) {
        this.pazienteCorrente = pazienteCorrente;
    }
    public void setPrenotazioni(Map<String, Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public void setPersonaleLaboratori(Map<String, PersonaleLaboratorio> personaleLaboratori) {
        this.personaleLaboratori = personaleLaboratori;
    }
    public void setPazienti(Map<String, Paziente> pazienti) {
        this.pazienti = pazienti;
    }

    public void setSedi(List<Sede> sedi) {
        this.sedi = sedi;
    }


    public PersonaleLaboratorio getPersonaleLaboratorioCorrente() {
        return personaleLaboratorioCorrente;
    }

    public Paziente getPazienteCorrente() {
        return pazienteCorrente;
    }


    public Map<String, Paziente> getPazienti() {
        return pazienti;
    }

    public List<Sede> getSedi() {
        return sedi;
    }

    public Map<String, Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }


    public Amministratore getAmministratore() {
        return this.amministratore;
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

        String cf = null;
        boolean codiceFiscaleValido = false;

        while (!codiceFiscaleValido) {
            System.out.print("Inserisci il codice fiscale del paziente: ");
            cf = scanner.nextLine().trim();

            if (cf.isEmpty()) {
                System.out.println("Errore: Il codice fiscale non può essere vuoto!");
            } else if (pazienti.containsKey(cf) || personaleLaboratori.containsKey(cf)
                    || amministratore.getCodiceFiscale().equals(cf)) {
                System.out.println("Errore: Esiste già una persona con questo codice fiscale!");
            } else {
                codiceFiscaleValido = true;
            }
        }

        String sesso;
        do {
            try {
                System.out.print("Inserisci il sesso del paziente (M/F): ");
                sesso = scanner.nextLine().trim().toUpperCase();

                if (!sesso.equals("M") && !sesso.equals("F")) {
                    throw new IllegalArgumentException("Errore: Devi inserire solo 'M' per maschio o 'F' per femmina.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (true);

        boolean malatoCronico = false;
        do {
            try {
                System.out.print("Il paziente è malato cronico? (SI/NO): ");
                String risposta = scanner.nextLine().trim().toUpperCase();
                if (risposta.equals("SI")) {
                    malatoCronico = true;
                    break;
                } else if (risposta.equals("NO")) {
                    malatoCronico = false;
                    break;
                } else {
                    throw new IllegalArgumentException("Errore: Devi rispondere con 'SI' o 'NO'.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (true);

        nuovoPaziente(nome, cognome, dataNascita, cf, sesso, malatoCronico); //flusso 1.

        if (this.pazienteCorrente == null) {
            System.out.println("Errore: Nessun paziente da confermare!");
            return;
        }

        System.out.println("Riepilogo informazioni inserite: ");
        System.out.println(this.pazienteCorrente.toString());
        confermaPaziente();  // flusso 2.
        System.out.println("Paziente aggiunto con successo!");
    }


    public void nuovoPaziente(String nome, String cognome, LocalDate dataNascita, String cf, String sesso, boolean malatoCronico) {
        if (pazienti.containsKey(cf)) {
            throw new IllegalArgumentException("Codice fiscale già registrato");
        }
        Paziente paziente = new Paziente(nome, cognome, dataNascita, cf, sesso, malatoCronico);
        this.pazienteCorrente = paziente;
    }
    public void confermaPaziente() {
        this.pazienti.put(this.pazienteCorrente.getCf(), this. pazienteCorrente);
        this.pazienteCorrente = null;

    }

    // UC1 Eliminare il paziente
    public void eliminaPaziente() { //rispettando la regola di buisness *b
        Scanner scanner = new Scanner(System.in);

        System.out.println("Pazienti disponibili:");
        for (Map.Entry<String, Paziente> entry : pazienti.entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.print("Inserisci il codice fiscale del paziente da eliminare: ");
        String codiceFiscale = scanner.nextLine();
        Paziente pazienteDaEliminare = null;

        for (Map.Entry<String, Paziente> entry : pazienti.entrySet()) {
            if (entry.getKey().equals(codiceFiscale)) {
                pazienteDaEliminare = entry.getValue();
                break;
            }
        }
        if (pazienteDaEliminare == null) {
            System.out.println("Errore: Nessun paziente trovato.");
            return;
        }
        if (!pazienteDaEliminare.getPrenotazioniPaziente().isEmpty()) {
            System.out.println("Errore: Il paziente ha prenotazioni in corso e non può essere eliminato.");
            return;
        }
        for (Iterator<Map.Entry<String, Paziente>> iterator = pazienti.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Paziente> entry = iterator.next();
            if (entry.getKey().equals(codiceFiscale)) {
                iterator.remove();
                System.out.println("Paziente eliminato con successo.");
                return;
            }
        }
    }

    // UC1 modificare il paziente da amministratore
    public void modificaPazienteAmministratore() {
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

    public void modificaPaziente(String cf) {  //ci puo servire per l'UC8
        Paziente paziente = selezionaPaziente(cf);
        if (paziente != null) {
            paziente.modificaPaziente();
            this.pazienteCorrente = null;
        } else {
            System.out.println("Errore: Paziente non trovato.");
        }
    }

    public Paziente selezionaPaziente(String cf) {
        Paziente paziente = this.pazienti.get(cf);
        if (paziente != null)
            this.pazienteCorrente = paziente;
        return paziente;
    }

    //UC2 Registrazione sede laboratorio
    public void RegistrazioneSede() {
        visualizzaSedi(); //1. visualizza tutte le sedi attuali
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il codice della sede scelta: ");
        int codiceSede = -1;
        while (codiceSede < 0) {
            try {
                codiceSede = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un codice(intero) valido.");
            }
        }
        Sede sedeSelezionata=selezionaSede(codiceSede); //2. seleziona la sede (da vedere se fallo globale)
        if (sedeSelezionata !=null) {
        if (!pazienteSedeAssociata(sedeSelezionata)) {
            confermaSede(sedeSelezionata);
            System.out.println("Sede " + sedeSelezionata.getNome() +
                    " assegnata a: " + pazienteCorrente.getNome() +
                    " " + pazienteCorrente.getCognome());
        }
       else  {
           System.out.println("Il paziente ha già questa sede.");
    }
        } else{
        System.out.println("Sede non trovata. Riprova.");
        }

    }

    public void confermaSede(Sede sedeSelezionata) {
         pazienteCorrente.getSedi().add(sedeSelezionata);
    }

    public boolean pazienteSedeAssociata(Sede sede) {
        return pazienteCorrente.getSedi().contains(sede);
    }

    public Sede selezionaSede(Integer codSede) {
        for (Sede sede : this.sedi){
            if (sede.getCodice().equals(codSede)) {
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
        if (pazienteCorrente.getSedi().isEmpty()) {
            System.out.println("Il paziente non ha sedi associate. Impossibile prenotare un esame.");
            return;
        }

        visualizzaSedePaziente(pazienteCorrente);//1. visaulizzazione

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

        Sede sedeSelezionata = selezionaSede(codiceSede); // 2
        if (sedeSelezionata == null) {
            System.out.println("Errore: Sede non trovata.");
            return;
        }

        if (!pazienteCorrente.getSedi().contains(sedeSelezionata)) {
            System.out.println("Errore: La sede selezionata non è associata a questo paziente.");
            return;
        }
        VisualizzaEsamiDisponibili(sedeSelezionata); //3

        System.out.print("Inserisci il codice dell'esame che desideri prenotare: ");
        String codiceEsame = scanner.nextLine().trim();

        Esame esameSelezionato = sedeSelezionata.getEsami().get(codiceEsame);
        if (esameSelezionato == null) {
            System.out.println("Errore: Esame non trovato.");
            return;
        }

        //applicazione pattern decorator 
        EsameControlloFestivi esameDecorato = new EsameControlloFestivi(esameSelezionato,pazienteCorrente);
//modifica del flussso del SelezionaEsame perche passa da questa classe
        if (!esameDecorato.prenotabile()) {
            DayOfWeek giorno = esameDecorato.getData().getDayOfWeek();

            if (giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY) {
                System.out.println("Errore: L'esame è prenotabile solo da pazienti cronici perché cade di " + giorno + ".");
            } else {
                System.out.println("Errore: Esame riservato ai malati cronici.");
            }
            return;
        }

        LocalDate dataEsame = esameSelezionato.getData();
        if (!PrenotazioniMaxPerGiorno(pazienteCorrente, dataEsame)) { //regola di buisness
            System.out.println("Errore: Hai già raggiunto il limite massimo di 3 prenotazioni per il giorno " + dataEsame);
            return;
        }
        if (!EsameDisponibile(esameSelezionato.getCodice())) {
            System.out.println("Errore: Questo esame è già stato prenotato.");
            return;
        }

        SelezionaEsame(esameSelezionato); //4
        ConfermaEsame(); // 5
        System.out.println("Prenotazione confermata");

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
        if (pazienteCorrente == null || esameSelezionato.isPrenotato()) {
            throw new IllegalStateException("Errore: Nessun paziente attualmente autenticato o l'esame è gia prenotato.");
        }
        if (!EsameDisponibile(esameSelezionato.getCodice())) {
            throw new IllegalStateException("Errore: Questo esame è già stato prenotato.");
        }
        this.prenotazioneCorrente = new Prenotazione(esameSelezionato, pazienteCorrente);
    }

    public void ConfermaEsame() {
        if (this.prenotazioneCorrente == null) {
            System.out.println("Errore: Nessuna prenotazione da confermare!");
            return;
        }
        this.prenotazioni.put(this.prenotazioneCorrente.getCodice(), this.prenotazioneCorrente);
        this.pazienteCorrente.getPrenotazioni().put(this.prenotazioneCorrente.getCodice(), this.prenotazioneCorrente);
        this.prenotazioneCorrente.getEsame().prenotato();
        this.prenotazioneCorrente = null;
    }

    //Metodo per la regola di buisness R8 di massimo 3 prenotazioni al giorno
    public boolean PrenotazioniMaxPerGiorno(Paziente paziente, LocalDate data) {
        int prenotazioniGiornaliere = 0;
        for (Map.Entry<String, Prenotazione> entry : paziente.getPrenotazioni().entrySet()) {
            if (entry.getValue().getEsame().getData().equals(data)) {
                prenotazioniGiornaliere++;
            }
        }
        System.out.println("Prenotazioni trovate per " +paziente.getNome() +
                " " +paziente.getCognome()+ " in data " +data+ ": " + prenotazioniGiornaliere);

        return prenotazioniGiornaliere <= 3;
    }

    public boolean EsameDisponibile(String codiceEsame) {
        for (Map.Entry<String, Prenotazione> entry : prenotazioni.entrySet()) {
            if (entry.getValue().getEsame().getCodice().equals(codiceEsame)) {
                return false;
            }
        }
        return true;
    }

    public void visualizzaSedePaziente(Paziente paziente) {
        if (paziente.getSedi().isEmpty()) {
            System.out.println("Il paziente non ha sedi associate.");
        } else {
            System.out.println("Sedi associate al paziente:");
            for (Sede sede : paziente.getSedi()) {
                System.out.println(sede.toString());
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
        } else if (verificaPersonaleLaboratorio(codice, password)) {
        this.personaleLaboratorioCorrente=this.personaleLaboratori.get(codice);
            return "personale";
        } else {
            return "credenziali errate";
        }
    }
    public boolean verificaAmministratore( String codice, String password) {
        return this.amministratore != null &&
                this.amministratore.getCodiceFiscale().equals(codice) && this.amministratore.verificaPassword(password);
    }

    public boolean verificaPaziente( String codice, String password) {
        Paziente paziente = this.pazienti.get(codice);
        return paziente != null && paziente.verificaPassword(password);
    }

    public boolean verificaPersonaleLaboratorio(String codice, String password) {
            PersonaleLaboratorio personaleLaboratorio = this.personaleLaboratori.get(codice);
        return personaleLaboratorio != null && personaleLaboratorio.verificaPassword(password);
    }

    public void logout() {
        this.pazienteCorrente = null;
        System.out.println("Logout eseguito con successo.");
    }

    //caricamento dei dati inseriti da default come gli utenti e le prenotazioni e le sedi
    public void caricamentoDati(){
        Paziente paziente1 = new Paziente("Matteo","Milano",LocalDate.of(2000, 12, 11),"c","M",true);
        Paziente paziente2 = new Paziente("Maria","Salemi",LocalDate.of(1986, 9, 2),"SLMWG349P33G342LP","F",false);
        Paziente paziente3= new Paziente("Giuseppe","Paci",LocalDate.of(1958, 2, 9),"GPPPAI11R44Z573H","M",false);
        Sede sede1 = new Sede(0,"Catania");
        Sede sede2 = new Sede(1,"Messina");
        Sede sede3 = new Sede(2,"Palermo");
        PersonaleLaboratorio personaleLaboratorio1 = new PersonaleLaboratorio("b","Alessio","Tornabene",sede1);
        PersonaleLaboratorio personaleLaboratorio2 = new PersonaleLaboratorio("ARNAGG00R14D362F","Giorgia","Arena",sede2);
        PersonaleLaboratorio personaleLaboratorio3 = new PersonaleLaboratorio("MSMCI99DSDV563G","Maria","Musumeci",sede3);
        this.personaleLaboratori.put(personaleLaboratorio1.getCf(),personaleLaboratorio1);
        this.personaleLaboratori.put(personaleLaboratorio2.getCf(),personaleLaboratorio2);
        this.personaleLaboratori.put(personaleLaboratorio3.getCf(),personaleLaboratorio3);
        this.sedi.add(sede1);
        this.sedi.add(sede2);
        this.sedi.add(sede3);
        this.pazienti.put(paziente1.getCf(), paziente1);
        this.pazienti.put(paziente2.getCf(), paziente2);
        this.pazienti.put(paziente3.getCf(), paziente3);
        this.amministratore=new Amministratore();

        SedePazienteProxy proxy = new SedePazienteProxy(this);
        personaleLaboratorio1.setPazienteProvider(proxy);
        personaleLaboratorio2.setPazienteProvider(proxy);
        personaleLaboratorio3.setPazienteProvider(proxy);
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
        if (this.sedeCorrente == null) {
            System.out.println("Errore: Nessuna sede da confermare!");
            return;
        }
        System.out.println("Riepilogo informazioni inserite: ");
        System.out.println(this.sedeCorrente);
        confermaSede(); //2. conferma
        System.out.println("Sede aggiunta con successo!");
    }

    public void nuovaSede(String nome,Integer codice) {
        Sede sede = new Sede(codice,nome);
        this.sedeCorrente=sede;
    }


    public void confermaSede() {
        this.sedi.add(this.sedeCorrente);
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
        Sede sede = selezionaSede(codice);
        if (sede != null) {
            sede.modificaSede();
            this.sedeCorrente = null;
        } else {
            System.out.println("Errore: Sede non trovata.");
        }
    }
    // UC5
    public void aggiungiReferto(){
        if(personaleLaboratorioCorrente!=null){
            personaleLaboratorioCorrente.aggiungiReferto();
        }
        else{
            System.out.println("Errore: Personale non autenticato.");
        }
    }


//UC6 Aggiorna referto, il personale accede vede i pazienti della sua sede e poi ne seleziona uno e gli mette la descrizione del referto
public void aggiornaReferto(){
        if(personaleLaboratorioCorrente!=null){
            personaleLaboratorioCorrente.aggiornaReferto();
        }
        else{
            System.out.println("Errore: Personale non autenticato.");
        }
}
   //UC6 elimina referto
   public void eliminaReferto(){
       if(personaleLaboratorioCorrente!=null){
           personaleLaboratorioCorrente.eliminaReferto();
       }
       else{
           System.out.println("Errore: Personale non autenticato.");
       }
   }


    //UC7 visualizza referti
    public void visualizzaRefertiPaziente() {
        if (pazienteCorrente != null) {
            pazienteCorrente.visualizzaRefertiAssociatiEsami();
        } else {
            System.out.println("Errore: Paziente non autenticato.");
        }
    }

//UC8 Modifica dati personali paziente
public void modificaPaziente() {
    if (pazienteCorrente != null) {
        pazienteCorrente.modificaPaziente();
    } else {
        System.out.println("Errore: Paziente non autenticato.");
    }
}

    //UC9- PATTERN OBSERVER
    public List<Sede> visualizzaSediRecensibili(Paziente paziente) {

        List<Prenotazione> prenotazioniCompletate = paziente.getPrenotazioni().values().stream()
                .filter(p -> p.getStato() instanceof StatoCompletato)
                .collect(Collectors.toList());

        List<Sede> sediRecensibili = new ArrayList<>();

        List<Sede> sediAssociate = paziente.getSedi();

        for (Prenotazione prenotazione : prenotazioniCompletate) {
            // Cerca la sede tra quelle associate al paziente
            for (Sede sede : sediAssociate) {
                boolean giaPresente = sediRecensibili.stream()
                        .anyMatch(s -> s.getCodice().equals(sede.getCodice()));
                if (!giaPresente) {
                    // Aggiungi la sede se il paziente ha almeno una prenotazione completata lì
                    sediRecensibili.add(sede);
                    break;
                }
            }
        }
        return sediRecensibili;
    }

    public Sede selezionaSedeRecensibile(Paziente paziente,Scanner scanner) {
        if (paziente == null) {
            System.out.println("Paziente non valido!");
            return null;
        }

        List<Sede> sediRecensibili = visualizzaSediRecensibili(paziente);
        if (sediRecensibili.isEmpty()) {
            System.out.println("Non hai ancora completato esami in nessuna sede.");
            return null;
        }

        System.out.println("Scegli una sede da recensire:");
        IntStream.range(0, sediRecensibili.size())
                .forEach(i -> System.out.printf("%d. %s\n", i + 1, sediRecensibili.get(i).getNome()));

        try {
            int scelta = scanner.nextInt() - 1;
            scanner.nextLine();

            if (scelta >= 0 && scelta < sediRecensibili.size()) {
                return sediRecensibili.get(scelta);
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
        }

        System.out.println("Scelta non valida!");
        return null;
    }

    public Recensione creaRecensione(Paziente paziente, Sede sede,Scanner scanner) {
        if (paziente == null || sede == null) {
            System.out.println("Dati insufficienti per creare la recensione");
            return null;
        }

        int stelle = 0;
        while (stelle < 1 || stelle > 5) {
            System.out.print("Valutazione (1-5 stelle): ");
            try {
                stelle = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Inserisci un numero valido!");
            }
        }

        System.out.print("Commento (opzionale): ");
        String commento = scanner.nextLine();

        return new Recensione(paziente, sede, stelle, commento);
    }

    public void lasciaRecensione(Scanner scanner) {
        if (pazienteCorrente == null) {
            System.out.println("Devi essere loggato come paziente!");
            return;
        }

        Sede sedeScelta = selezionaSedeRecensibile(pazienteCorrente,scanner);
        if (sedeScelta == null) {
            return;
        }

        Recensione recensioneCorrente = creaRecensione(pazienteCorrente, sedeScelta,scanner);
       confermaRecensione(recensioneCorrente);
    }
    public void confermaRecensione(Recensione recensione) {
        if (recensione == null) {
            System.out.println("Errore: Recensione nulla non può essere aggiunta.");
            return;
        }
        this.amministratore.aggiungiRecensioneNonLetta(recensione);

        setChanged();
        notifyObservers(recensione);

        System.out.println("Recensione inviata con successo!");
    }


    //UC10 Aggiunta di un nuovo esame
    public void aggiungiNuovoEsame() {
        Scanner scanner = new Scanner(System.in);

        visualizzaSedi(); // Flusso 1

        System.out.print("Inserisci il codice della sede dove aggiungere l'esame: ");
        int codiceSede = -1;
        while (codiceSede < 0) {
            try {
                codiceSede = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un codice (intero) valido.");
            }
        }

        this.sedeCorrente = selezionaSede(codiceSede); // Flusso 2

        if (sedeCorrente == null) {
            System.out.println("Errore: Codice sede non trovato.");
            return;
        }

        VisualizzaEsamiDisponibili(sedeCorrente); // Flusso 3

        System.out.print("Inserisci il nome dell'esame da aggiungere: ");
        String nomeEsame = scanner.nextLine().trim();

        LocalDate dataEsame = null;
        LocalTime orarioEsame = null;

        while (true) {
            try {
                System.out.print("Inserisci la data dell'esame (YYYY-MM-DD): ");
                dataEsame = LocalDate.parse(scanner.nextLine());

                System.out.print("Inserisci l'orario dell'esame (HH:mm): ");
                orarioEsame = LocalTime.parse(scanner.nextLine());

                if (!sedeCorrente.isOrarioDisponibile(dataEsame, orarioEsame)) {
                    System.out.println("Errore: Orario non disponibile, deve esserci almeno 1 ora e 30 minuti di distanza.");
                    continue;
                }

                break;

            } catch (Exception e) {
                System.out.println("Errore: Data o orario non validi.");
            }
        }
        nuovoEsame(dataEsame,orarioEsame,nomeEsame); //flusso 4.
        System.out.println("Esame aggiunto con successo.");

    }

    public void nuovoEsame(LocalDate data, LocalTime orario, String nome) {
        if (this.sedeCorrente != null) {
            this.sedeCorrente.aggiungiEsame(data, orario, nome);
        }
        this.sedeCorrente = null;
    }

    //UC11 - aggiunge un personale di laboratorio al sistema
    public void aggiungiPersonale() {
        if (this.amministratore == null) {
            System.out.println("Accesso negato: Amministratore non autenticato.");
            return;
        }
        Scanner scanner = new Scanner(System.in);

        if (!visualizzaSediDisponibili()) { //flusso 1.
            return;
        }

        System.out.print("Inserisci il codice della sede in cui lavorerà: ");
        int codiceSede = -1;
        while (codiceSede < 0) {
            try {
                codiceSede = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Errore: Devi inserire un codice (intero) valido.");
            }
        }

        Sede sedeSelezionata = selezionaSedePersonaleLab(codiceSede); // flusso 2.

        if (sedeSelezionata == null) {
            System.out.println("Errore: Codice sede non valido o sede già occupata.");
            return;
        }


        System.out.print("Inserisci il nome del personale di laboratorio: ");
        String nome = scanner.nextLine();
        System.out.print("Inserisci il cognome del personale di laboratorio: ");
        String cognome = scanner.nextLine();
        String cf = null;
        boolean codiceFiscaleValido = false;

        while (!codiceFiscaleValido) {

            System.out.print("Inserisci il codice fiscale del personale di laboratorio: ");
            cf = scanner.nextLine().trim();

            if (cf.isEmpty()) {
                System.out.println("Errore: Il codice fiscale non può essere vuoto!");
            } else if (pazienti.containsKey(cf) || personaleLaboratori.containsKey(cf)
                    || amministratore.getCodiceFiscale().equals(cf)) {
                System.out.println("Errore: Esiste già una persona con questo codice fiscale!");
            } else {
                codiceFiscaleValido = true;
            }
        }

        inserisciPersonaleLab(cf, nome, cognome, sedeSelezionata);  // flusso 3.

        if (this.personaleLaboratorioCorrente == null) {
            System.out.println("Errore: Nessun personale da confermare!");
            return;
        }

        System.out.println("Riepilogo informazioni inserite: ");
        System.out.println(this.personaleLaboratorioCorrente);
        confermaPersonaleLab(); // flusso 4.
        System.out.println("Personale di laboratorio aggiunto con successo!");
    }


    public boolean visualizzaSediDisponibili() {
        boolean almenoUnaDisponibile = false;
        System.out.println("Sedi disponibili:");
        for (Sede sede : sedi) {
            boolean occupata = false;
            for (PersonaleLaboratorio p : personaleLaboratori.values()) {
                if (p.getSede().equals(sede)) {
                    occupata = true;
                    break;
                }
            }
            if (!occupata) {
                System.out.println(sede.toString());
                almenoUnaDisponibile = true;
            }
        }

        if (!almenoUnaDisponibile) {
            System.out.println("Nessuna sede disponibile per aggiungere personale.");

        }
        return almenoUnaDisponibile;
    }


    public Sede selezionaSedePersonaleLab(Integer codSede) {
        for (Sede sede : this.sedi) {
            if (sede.getCodice().equals(codSede)) {
                for (PersonaleLaboratorio personale : personaleLaboratori.values()) {
                    if (personale.getSede().equals(sede)) {
                        return null;
                    }
                }
                return sede;
            }
        }
        return null;
    }

    public void inserisciPersonaleLab(String cf, String nome, String cognome, Sede sede){
        PersonaleLaboratorio personale = new PersonaleLaboratorio( cf, nome, cognome,sede);
        this.personaleLaboratorioCorrente = personale;
    }

    public void confermaPersonaleLab()
    {
        this.personaleLaboratori.put(this.personaleLaboratorioCorrente.getCf(), this.personaleLaboratorioCorrente);
        this.personaleLaboratorioCorrente = null;
    }

    //UC12 Generazione del report
    public void generaReportDemografico() {
        if (this.amministratore == null) {
            System.out.println("Accesso negato: Amministratore non autenticato.");
            return;
        }

        String tipologia = InserisciTipoReport(); //flusso 1

        if (tipologia == null) {
            System.out.println("Tipo di report non valido.");
            return;
        }

        creaReport(tipologia); //flusso 2
        visualizzaReport(); //flusso 3
    }

    public String InserisciTipoReport() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il tipo di report (mensile, semestrale, annuale): ");
        String tipo = scanner.nextLine().toLowerCase();

        if (tipo.equals("mensile") || tipo.equals("semestrale") || tipo.equals("annuale")) {
            return tipo;
        } else {
            return null;
        }
    }


    public Report getReportCorrente() {
        return reportCorrente;
    }

    public void creaReport(String tipo) {
        switch (tipo) {

            case "mensile":
                ReportMensileFactory factoryMensile = new ReportMensileFactory();
                ReportMensile reportMensile = factoryMensile.createReport(this.prenotazioni);
                this.setReportCorrente(reportMensile);
                break;
            case "semestrale":
                ReportSemestraleFactory factorySemestrale = new ReportSemestraleFactory();
                ReportSemestrale reportSemestrale = factorySemestrale.createReport(this.prenotazioni);
                this.setReportCorrente(reportSemestrale);
                break;
            case "annuale":
                ReportAnnualeFactory factoryAnnuale = new ReportAnnualeFactory();
                ReportAnnuale reportAnnuale = factoryAnnuale.createReport(this.prenotazioni);
                this.setReportCorrente(reportAnnuale);
                break;
        }
    }

    public void visualizzaReport() {
        if (reportCorrente != null) {
            System.out.println(reportCorrente.toString());
        } else {
            System.out.println("Nessun report da visualizzare.");
        }
    }

    //UC13 visualizza le prenotazioni attive
    public void visualizzaPrenotazioniAttive() {
        if (pazienteCorrente != null) {
            pazienteCorrente.stampaPrenotazioniAttive();
        } else {
            System.out.println("Errore: Paziente non autenticato.");
        }
    }


    @Override
    public String toString() {
        return "Medlab{" +
                "pazienti=" + pazienti +
                ", pazienteCorrente=" + pazienteCorrente +
                ", sedeCorrente=" + sedeCorrente +
                ", amministratore=" + amministratore +
                ", prenotazioneCorrente=" + prenotazioneCorrente +
                ", personaleLaboratorioCorrente=" + personaleLaboratorioCorrente +
                ", sedi=" + sedi +
                ", prenotazioni=" + prenotazioni +
                ", personaliLaboratori=" + personaleLaboratori +
                ", reportCorrente=" + reportCorrente +
                '}';
    }


}



