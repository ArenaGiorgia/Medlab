import com.sun.xml.internal.bind.v2.TODO;
//TODO: controllare in generale se mettere l if (corrente==null) con tutti dopo il login nei vari metodi
import java.time.DayOfWeek;
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
    private List<Sede> sedi;
    private Map<String, Prenotazione> prenotazioni;
    private Prenotazione prenotazioneCorrente;
    private PersonaleLaboratorio personaleLaboratorioCorrente;
    private Map<String,PersonaleLaboratorio> personaliLaboratori;
    private Referto refertoCorrente;

    private Medlab() {
        this.pazienti = new HashMap<String,Paziente>();
        this.prenotazioni = new HashMap<String, Prenotazione>();
        this.amministratore= new Amministratore();
        this.pazienteCorrente=null;
        this.prenotazioneCorrente=null;
        this.sedeCorrente=null;
        this.sedi = new ArrayList<>();
        this.personaleLaboratorioCorrente=null;
        this.personaliLaboratori=new HashMap<String,PersonaleLaboratorio>();
        this.refertoCorrente=null;
        CaricamentoDati();  //per caricare dati persistenti
    }

    public static Medlab getInstance() {
        if (medlab == null)
            medlab = new Medlab();
        else
            System.out.println("Istanza già creata");
        return medlab;
    }

    public Referto getRefertoCorrente() {
        return refertoCorrente;
    }

    public void setRefertoCorrente(Referto refertoCorrente) {
        this.refertoCorrente = refertoCorrente;
    }

    public void setPersonaleLaboratorioCorrente(PersonaleLaboratorio personaleLaboratorioCorrente) {
        this.personaleLaboratorioCorrente = personaleLaboratorioCorrente;
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

    public PersonaleLaboratorio getPersonaleLaboratorioCorrente() {
        return personaleLaboratorioCorrente;
    }
    public Paziente getPazienteCorrente() {
        return pazienteCorrente;
    }

    public Map<String, PersonaleLaboratorio> getPersonaliLaboratori() {
        return personaliLaboratori;
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
        if (pazienti.containsKey(cf) || personaliLaboratori.containsKey(cf)
                || amministratore.getCodiceFiscale().equals(cf)) {
            System.out.println("Errore: Esiste già una persona con questo codice fiscale!");
            return;
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
        nuovoPaziente(nome,cognome,dataNascita,cf,sesso,malatoCronico); //1.aggiungi tutti i dati del paziente
        if (this.pazienteCorrente == null) {
            System.out.println("Errore: Nessun paziente da confermare!");
            return;
        }

        System.out.println("Riepilogo informazioni inserite: ");
        System.out.println(this.pazienteCorrente.toString());
        confermaPaziente(); //2.conferma
        System.out.println("Paziente aggiunto con successo!");
        }

    public void nuovoPaziente(String nome, String cognome, LocalDate dataNascita, String cf, String sesso,boolean malatoCronico ) {
        Paziente paziente = new Paziente( nome, cognome, dataNascita, cf, sesso,malatoCronico);
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
        Sede sedeSelezionata=selezionaSedePaziente(codiceSede); //2. seleziona la sede (da vedere se fallo globale)
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
         pazienteCorrente.aggiungiSede(sedeSelezionata);
    }

    public boolean pazienteSedeAssociata(Sede sede) {
        return pazienteCorrente.getSedi().contains(sede);
    }

    public Sede selezionaSedePaziente(Integer codSede) {
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

        Sede sedeSelezionata = selezionaSedePaziente(codiceSede); // 2
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

        //iterazione 2 applicazione pattern decorator quindi in seleziona ESame sarà modificato da ora in poi il flusso
        Esame esameDecorato = new EsameControlloFestivi(esameSelezionato,pazienteCorrente);
//modifica del flussso del SelezionaEsame perche passa da questa classe
        if (!((EsameControlloFestivi) esameDecorato).prenotabile()) {
            DayOfWeek giorno = esameSelezionato.getData().getDayOfWeek();

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
        this.prenotazioneCorrente = new Prenotazione(esameSelezionato,pazienteCorrente);

    }


    public void ConfermaEsame() {
        if (this.prenotazioneCorrente == null) {
            System.out.println("Errore: Nessuna prenotazione da confermare!");
            return;
        }
        this.prenotazioni.put(this.prenotazioneCorrente.getCodice(), this.prenotazioneCorrente);
        this.pazienteCorrente.getPrenotazioni().put(this.prenotazioneCorrente.getCodice(), this.prenotazioneCorrente);
        this.prenotazioneCorrente.getEsame().prenotato();
        System.out.println("Prenotazione confermata");
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

        return prenotazioniGiornaliere < 3;
    }

    private boolean EsameDisponibile(String codiceEsame) {
        for (Map.Entry<String, Prenotazione> entry : prenotazioni.entrySet()) {
            if (entry.getValue().getEsame().getCodice().equals(codiceEsame)) {
                return false;
            }
        }
        return true;
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


    //Mi serve per vedere a quali sedi il paziente è associato
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
        this.personaleLaboratorioCorrente=this.personaliLaboratori.get(codice);
            return "personale";
        } else {
            return "credenziali errate";
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

    public boolean verificaPersonaleLaboratorio(String codice, String password) {
            PersonaleLaboratorio personaleLaboratorio = this.personaliLaboratori.get(codice);
                if (personaleLaboratorio != null && personaleLaboratorio.verificaPassword(password)) {
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
        Paziente paziente1 = new Paziente("Matteo","Milano",LocalDate.of(2000, 12, 11),"c","M",true);
        Paziente paziente2 = new Paziente("Maria","Salemi",LocalDate.of(1986, 9, 2),"SLMWG349P33G342LP","F",false);
        Paziente paziente3= new Paziente("Giuseppe","Paci",LocalDate.of(1958, 2, 9),"GPPPAI11R44Z573H","M",false);
        Sede sede1 = new Sede(0,"Catania");
        Sede sede2 = new Sede(1,"Messina");
        Sede sede3 = new Sede(2,"Palermo");
        PersonaleLaboratorio personaleLaboratorio1 = new PersonaleLaboratorio("b","Alessio","Tornabene",sede1);
        PersonaleLaboratorio personaleLaboratorio2 = new PersonaleLaboratorio("ARNAGG00R14D362F","Giorgia","Arena",sede2);
        PersonaleLaboratorio personaleLaboratorio3 = new PersonaleLaboratorio("MSMCI99DSDV563G","Maria","Musumeci",sede3);
        this.personaliLaboratori.put(personaleLaboratorio1.getCf(),personaleLaboratorio1);
        this.personaliLaboratori.put(personaleLaboratorio2.getCf(),personaleLaboratorio2);
        this.personaliLaboratori.put(personaleLaboratorio3.getCf(),personaleLaboratorio3);
        this.sedi.add(sede1);
        this.sedi.add(sede2);
        this.sedi.add(sede3);
        this.pazienti.put(paziente1.getCf(), paziente1);
        this.pazienti.put(paziente2.getCf(), paziente2);
        this.pazienti.put(paziente3.getCf(), paziente3);
      //   this.amministratore=new Amministratore();

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
        System.out.println(this.sedeCorrente.toString());
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
        Sede sede = selezionaSedePaziente(codice);
        if (sede != null) {
            sede.modificaSede();
            this.sedeCorrente = null;
        } else {
            System.out.println("Errore: Sede non trovata.");
        }
    }


//UC5 Gestione referti (inserimento)
public void aggiungiReferto() {
    if (personaleLaboratorioCorrente == null) {
        System.out.println("Errore: Nessun personale di laboratorio attualmente autenticato!");
        return;
    }

    Scanner scanner = new Scanner(System.in);

    while (true) {
        visualizzaListaEsamiPrenotati(); // Flusso 1

        System.out.print("Inserisci il codice dell'esame da selezionare oppure 'STOP' per terminare: ");
        String codiceEsame = scanner.nextLine().trim();

        if (codiceEsame.equalsIgnoreCase("STOP")) {
            break;
        }

        Prenotazione prenotazioneSelezionata = selezionaPrenotazione(codiceEsame); // Flusso 2

        if (prenotazioneSelezionata == null) {
            System.out.println("Errore: Nessuna prenotazione trovata per questo esame.");
            continue;
        }

        System.out.print("Vuoi completare o annullare questa prenotazione? (SI/NO): ");
        String scelta = scanner.nextLine().trim().toUpperCase();

        if (scelta.equals("SI")) {
            inserisciStato(prenotazioneSelezionata); // Flusso 3

            // Se l'esame è stato completato, rimuovilo dalla lista delle prenotazioni
            if (prenotazioneSelezionata.getStato() instanceof StatoCompletato) {
                personaleLaboratorioCorrente.getSede().getEsami().remove(codiceEsame);
                System.out.println("Prenotazione completata con successo.");
            }
        } else if (scelta.equals("NO")) {
            prenotazioneSelezionata.setStato(new StatoAnnullato(prenotazioneSelezionata));
            personaleLaboratorioCorrente.getSede().getEsami().remove(codiceEsame);
            System.out.println("Prenotazione annullata.");
        } else {
            System.out.println("Azione non valida. Scegli 'SI' o 'NO'.");
            continue;
        }

        System.out.print("Vuoi selezionare un altro esame? (SI/NO): ");
        String risposta = scanner.nextLine().trim().toUpperCase();
        if (risposta.equals("NO")) break;
    }

    System.out.println("Gestione referti completata.");
}

    public void visualizzaListaEsamiPrenotati() {
        if (personaleLaboratorioCorrente == null || personaleLaboratorioCorrente.getSede() == null) {
            System.out.println("Errore: Nessun personale di laboratorio attualmente autenticato o sede non assegnata.");
            return;
        }

        //utilizzo il proxy pattern per vedere solo quelli del personale di laboratorio
        PazienteProvider pazienteProvider = new SedePazienteProxy(medlab);
        List<Paziente> pazientiSede = pazienteProvider.getAllPazienti();

        boolean esameTrovato = false;
        System.out.println("Esami in attesa di referto per oggi:");
        for (Paziente paziente : pazientiSede) {
            for (Prenotazione prenotazione : prenotazioni.values()) {
                if (prenotazione.getPaziente().equals(paziente) && prenotazione.getStato() instanceof StatoInAttesa) {
                    Esame esame = prenotazione.getEsame();
                    if (esame.isPrenotato() && esame.getData().equals(LocalDate.now())) {
                        System.out.println("Codice: " + prenotazione.getCodice() +
                                " Esame: " + esame.getNome() +
                                " Data: " + esame.getData() +
                                " Ora: " + esame.getOrario() +
                                " Paziente: " + paziente.getNome() + " " + paziente.getCognome());
                        esameTrovato = true;
                    }
                }
            }
        }

        if (!esameTrovato) {
            System.out.println("Non ci sono esami in attesa di referto per oggi.");
        }
    }

    private Prenotazione selezionaPrenotazione(String codice) {

        // Utilizza il Proxy per ottenere i pazienti della sede corrente
        PazienteProvider pazienteProvider = new SedePazienteProxy(medlab);
        List<Paziente> pazientiSede = pazienteProvider.getAllPazienti();

        for (Paziente paziente : pazientiSede) {
            for (Prenotazione prenotazione : paziente.getPrenotazioniPaziente().values()) {
                if (prenotazione.getCodice().equals(codice)) {
                    return prenotazione;
                }
            }
        }
        return null;
    }


    public void inserisciStato(Prenotazione prenotazione) {
        if (prenotazione == null) {
            System.out.println("Errore: La prenotazione non esiste.");
            return;
        }

        StatoPrenotazione statoCorrente = prenotazione.getStato();

        if (statoCorrente instanceof StatoInAttesa) {
            statoCorrente.completa(prenotazione);
        } else if (statoCorrente instanceof StatoCompletato) {
            System.out.println("La prenotazione è già completata.");
        } else if (statoCorrente instanceof StatoAnnullato) {
            System.out.println("La prenotazione è già annullata.");
        }
    }

//UC6
//Aggiorna referto, dove il personale accede vede i pazienti della sua sede e poi ne seleziona uno e gli mette la descrizione del referto
    public void aggiornaReferto(){
        if (personaleLaboratorioCorrente == null) {
            System.out.println("Errore: Nessun personale di laboratorio attualmente autenticato!");
            return;
        }
        Scanner scanner = new Scanner(System.in);

        visualizzaPazientiAssociatiAllaSede(); //flusso 1.

        PazienteProvider pazienteProvider = getPazienteAccessProxy();
        System.out.print("Inserisci il codice fiscale del paziente a cui aggiornare il referto: ");
        String cf = scanner.nextLine().trim();

        Paziente pazienteSelezionato = pazienteProvider.getPazienteByCF(cf); //flusso 2.

        visualizzaPrenotazioniConfermate(pazienteSelezionato);  //flusso 3.

            System.out.print("Inserisci il codice della prenotazione da aggiornare: ");
            String codicePren = scanner.nextLine().trim();

            Prenotazione prenotazioneSelezionata = selezionaPrenotazione(codicePren); // flusso 4.

            if (prenotazioneSelezionata == null) {
                System.out.println("Errore: codice prenotazione non valido.");
                return;
            }

            prenotazioneCorrente = prenotazioneSelezionata;
            refertoCorrente = prenotazioneCorrente.getReferto();
            inserisciReferto(); //flusso 5.
            confermaReferto(); //flusso 6.
        }


    public void visualizzaPazientiAssociatiAllaSede() {
        if (personaleLaboratorioCorrente == null) {
            System.out.println("Errore: Nessun personale di laboratorio attualmente autenticato!");
            return;
        }

        PazienteProvider pazienteProvider = getPazienteAccessProxy();
        List<Paziente> pazientiSede = pazienteProvider.getAllPazienti();

        if (pazientiSede.isEmpty()) {
            System.out.println("Nessun paziente associato alla tua sede.");
            return;
        }

        System.out.println("Pazienti associati alla tua sede:");
        for (Paziente paziente : pazientiSede) {
            System.out.println("Nome: " + paziente.getNome() +
                    " Cognome: " + paziente.getCognome() +
                    " Codice Fiscale: " + paziente.getCf());
        }
    }
    public void visualizzaPrenotazioniConfermate(Paziente paziente) {
        if (paziente == null) {
            System.out.println("Errore: Paziente non valido.");
            return;
        }

        boolean trovato = false;
        Map<String, Prenotazione> prenotazioni = paziente.getPrenotazioniPaziente();

        System.out.println("Prenotazioni confermate per il paziente " + paziente.getNome() + " " + paziente.getCognome() + ":");

        for (Map.Entry<String, Prenotazione> entry : prenotazioni.entrySet()) {
            Prenotazione prenotazione = entry.getValue();
            Referto referto = prenotazione.getReferto();

            if (prenotazione.getStato() instanceof StatoCompletato) {
                if (referto != null && (referto.getRisultato() == null || referto.getRisultato().isEmpty())) {
                    System.out.println("Codice: " + prenotazione.getCodice() +
                            " Esame: " + prenotazione.getEsame().getNome() +
                            " Data: " + prenotazione.getEsame().getData() +
                            " Ora: " + prenotazione.getEsame().getOrario());
                    trovato = true;
                }
            }
        }

        if (!trovato) {
            System.out.println("Nessuna prenotazione confermata trovata per questo paziente.");
        }
    }



    //mi serve per il proxy
    public PazienteProvider getPazienteAccessProxy() {
        return new SedePazienteProxy(this);
    }

    //mi serve per il proxy
    public Paziente getPazienteByCF(String cf) {
        return pazienti.get(cf);
    }


    public void inserisciReferto() {
        if (refertoCorrente == null) {
            System.out.println("Errore: nessun referto selezionato.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci la descrizione del referto: ");
        String descrizione = scanner.nextLine().trim();

        refertoCorrente.setReferto(descrizione);
        System.out.println("Descrizione inserita con successo.");
    }

    public void confermaReferto() {
        if (refertoCorrente == null || prenotazioneCorrente == null) {
            System.out.println("Errore: referto o prenotazione non impostati.");
            return;
        }

        Paziente paziente = prenotazioneCorrente.getPaziente();

        if (paziente == null) {
            System.out.println("Errore: paziente non trovato.");
            return;
        }

        paziente.getRefertiCorrenti().put(refertoCorrente.getId(), refertoCorrente);
        System.out.println("Referto confermato e salvato nel profilo del paziente.");

        refertoCorrente = null;
        prenotazioneCorrente = null;
    }



//UC11 - aggiunge un personale di laboratorio al sistema
    public void aggiungiPersonale(){
        Scanner scanner = new Scanner(System.in);
        Sede sedesel=null;
        System.out.print("Inserisci il nome del personale di laboratorio: ");
        String nome = scanner.nextLine();
        System.out.print("Inserisci il cognome del personale di laboratoio: ");
        String cognome = scanner.nextLine();
        System.out.print("Inserisci il cf del personale di laboratoio: ");
        String cf = scanner.nextLine();
        System.out.print("Inserisci il codice della sede in cui lavorerà: ");
        int codSede = Integer.parseInt(scanner.nextLine());
        for (Sede sede : sedi){
            if (sede.getCodice()==codSede) sedesel=sede;
        }

        inserisciPersonaleLab(cf,nome, cognome,sedesel);
        confermaPersonaleLab();
    }

    public void inserisciPersonaleLab(String cf, String nome, String cognome, Sede sede){
        PersonaleLaboratorio personale = new PersonaleLaboratorio( cf, nome, cognome,sede);
        this.personaleLaboratorioCorrente = personale;

    }
    public void confermaPersonaleLab() {

        if (this.personaleLaboratorioCorrente == null) {
            System.out.println("Errore: Nessun personale ha eseguito l'accesso!");
            return;
        }
        System.out.println("Riepilogo informazioni inserite: ");
        System.out.print(this.personaleLaboratorioCorrente.toString());
        this.personaleLaboratorioCorrente = null;
    }


    @Override
    public String toString() {
        return "Medlab{" +
                "pazienti=" + pazienti +
                ", pazienteCorrente=" + pazienteCorrente +
                ", sedeCorrente=" + sedeCorrente +
                ", amministratore=" + amministratore +
                ", sedi=" + sedi +
                ", prenotazioni=" + prenotazioni +
                ", prenotazioneCorrente=" + prenotazioneCorrente +
                ", personaleLaboratorioCorrente=" + personaleLaboratorioCorrente +
                ", personaliLaboratori=" + personaliLaboratori +
                ", refertoCorrente=" + refertoCorrente +
                '}';
    }
}



