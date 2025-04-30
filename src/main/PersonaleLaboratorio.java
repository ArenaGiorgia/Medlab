package main;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PersonaleLaboratorio
{
private String cf;
private String nome;
private String cognome;
private String password;
private Sede sede;
private PazienteProvider pazienteProvider;
private Map<String,Referto> referti;
private Referto refertoCorrente;


    public PersonaleLaboratorio(String cf, String nome, String cognome, Sede sede) {
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.password=cf; //password settata automaticamente al codice fiscale
        this.sede = sede;
        this.referti = new HashMap<String,Referto>();
        this.refertoCorrente = null;


}


    public void setRefertoCorrente(Referto refertoCorrente) {
        this.refertoCorrente = refertoCorrente;
    }

    public Referto getRefertoCorrente() {
        return refertoCorrente;
    }

    public Map<String, Referto> getReferti() {
        return referti;
    }

    public void setReferti(Map<String, Referto> referti) {
        this.referti = referti;
    }

    //per il proxy
    public void setPazienteProvider(PazienteProvider provider) {
        this.pazienteProvider = provider;
    }

    //per il proxy
    public List<Paziente> listaPazientiDellaSede() {
        return pazienteProvider.getAllPazienti();
    }

    //metodo che ci serve per fare l UC5
    public void aggiungiReferto() {
        if (this.sede == null) {
            System.out.println("Errore: Nessuna sede associata.");
            return;
        }

        if (!visualizzaListaEsamiPrenotati()) { //flusso 1.
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Inserisci il codice dell'esame da selezionare oppure 'STOP' per terminare: ");
            String codiceEsame = scanner.nextLine().trim();

            if (codiceEsame.equalsIgnoreCase("STOP")) break;

            Prenotazione prenotazioneSelezionata = selezionaPrenotazione(codiceEsame); //flusso 2.

            if (prenotazioneSelezionata == null) {
                System.out.println("Errore: Nessuna prenotazione trovata per questo esame.");
                continue;
            }

            System.out.print("Vuoi completare o annullare questa prenotazione? (SI/NO): ");
            String scelta = scanner.nextLine().trim().toUpperCase();

            if (scelta.equals("SI")) {
                inserisciStato(prenotazioneSelezionata); // flusso 3.

                if (prenotazioneSelezionata.getStato() instanceof StatoCompletato) {
                    this.sede.getEsami().remove(codiceEsame);
                    System.out.println("Prenotazione completata con successo.");
                }

            } else if (scelta.equals("NO")) {
                prenotazioneSelezionata.setStato(new StatoAnnullato(prenotazioneSelezionata));
                this.sede.getEsami().remove(codiceEsame);
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

    public boolean visualizzaListaEsamiPrenotati() {
        List<Paziente> pazientiSede = pazienteProvider.getAllPazienti();
        boolean esameTrovato = false;

        System.out.println("Esami in attesa di referto per oggi:");
        for (Paziente paziente : pazientiSede) {
            for (Prenotazione prenotazione : paziente.getPrenotazioniPaziente().values()) {
                if (prenotazione.getStato() instanceof StatoInAttesa &&
                        prenotazione.getEsame().isPrenotato() &&
                        prenotazione.getEsame().getData().equals(LocalDate.now())) {

                    Esame esame = prenotazione.getEsame();
                    System.out.println("Codice: " + prenotazione.getCodice() +
                            " Esame: " + esame.getNome() +
                            " Data: " + esame.getData() +
                            " Ora: " + esame.getOrario() +
                            " Paziente: " + paziente.getNome() + " " + paziente.getCognome());

                    esameTrovato = true;
                }
            }
        }

        if (!esameTrovato) {
            System.out.println("Non ci sono esami in attesa di referto per oggi.");
        }

        return esameTrovato;
    }

    public Prenotazione selezionaPrenotazione(String codice) {
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
        StatoPrenotazione statoCorrente = prenotazione.getStato();

        if (statoCorrente instanceof StatoInAttesa) {
            statoCorrente.completa(prenotazione);
        } else if (statoCorrente instanceof StatoCompletato) {
            System.out.println("La prenotazione è già completata.");
        } else if (statoCorrente instanceof StatoAnnullato) {
            System.out.println("La prenotazione è già annullata.");
        }
    }


//metodo per l UC6
public void aggiornaReferto() {
    Scanner scanner = new Scanner(System.in);

    if (!visualizzaPazientiAssociatiAllaSede()) { //flusso 1.
        return;
    }

    System.out.print("Inserisci il codice fiscale del paziente a cui aggiornare il referto: ");
    String cf = scanner.nextLine().trim();

    Paziente pazienteSelezionato = selezionaPazienteProxy(cf); //flusso 2.

    if (!visualizzaPrenotazioniConfermate(pazienteSelezionato)) { //flusso 3.
        return;
    }

    System.out.print("Inserisci il codice della prenotazione da aggiornare: ");
    String codicePren = scanner.nextLine().trim();

    Prenotazione prenotazioneSelezionata = selezionaPrenotazione(codicePren); //flusso 4.

    if (prenotazioneSelezionata == null) {
        System.out.println("Errore: codice prenotazione non valido.");
        return;
    }

    this.refertoCorrente = prenotazioneSelezionata.getReferto();

    inserisciReferto();
    confermaReferto(pazienteSelezionato);
}

    public boolean visualizzaPazientiAssociatiAllaSede() {
        List<Paziente> pazientiSede = pazienteProvider.getAllPazienti();

        if (pazientiSede.isEmpty()) {
            System.out.println("Nessun paziente associato alla tua sede.");
            return false;
        }

        System.out.println("Pazienti associati alla tua sede:");
        for (Paziente paziente : pazientiSede) {
            System.out.println("Nome: " + paziente.getNome() +
                    " Cognome: " + paziente.getCognome() +
                    " Codice Fiscale: " + paziente.getCf());
        }

        return true;
    }

    public Paziente selezionaPazienteProxy(String cf) {
        Paziente pazienteSelezionato = pazienteProvider.getPazienteByCF(cf);
        if (pazienteSelezionato == null) {
            System.out.println("Errore: Paziente non trovato con il codice fiscale " + cf);
        }
        return pazienteSelezionato;
    }

    public boolean visualizzaPrenotazioniConfermate(Paziente paziente) {
        boolean trovato = false;
        Map<String, Prenotazione> prenotazioni = paziente.getPrenotazioniPaziente();

        System.out.println("Prenotazioni confermate per " + paziente.getNome() + " " + paziente.getCognome() + ":");

        for (Prenotazione pren : prenotazioni.values()) {
            Referto ref = pren.getReferto();
            if (pren.getStato() instanceof StatoCompletato && (ref == null || ref.getRisultato() == null || ref.getRisultato().isEmpty())) {
                System.out.println("Codice: " + pren.getCodice() +
                        " Esame: " + pren.getEsame().getNome() +
                        " Data: " + pren.getEsame().getData() +
                        " Ora: " + pren.getEsame().getOrario());
                trovato = true;
            }
        }

        if (!trovato) {
            System.out.println("Nessuna prenotazione confermata trovata.");
        }

        return trovato;
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
    }

    public void confermaReferto(Paziente paziente) {
        if (refertoCorrente == null) {
            System.out.println("Errore: referto non impostato.");
            return;
        }

        String idReferto = refertoCorrente.getId();
         referti = paziente.getRefertiCorrenti();

        if (referti.containsKey(idReferto)) {
            System.out.println("Errore: esiste già un referto con questo ID.");
        } else {
            referti.put(idReferto, refertoCorrente);
            System.out.println("Referto confermato e salvato nel profilo del paziente.");
        }

        refertoCorrente = null;
    }

    //UC6 elimina referto
    public void eliminaReferto() {
        Scanner scanner = new Scanner(System.in);

        if (!visualizzaPazientiAssociatiAllaSede()) {
            return;
        }

        System.out.print("Inserisci il codice fiscale del paziente: ");
        String cf = scanner.nextLine().trim();
        Paziente paziente = selezionaPazienteProxy(cf);
        if (paziente == null) {
            return;
        }

        if (!visualizzaPrenotazioniConfermate(paziente)) {
            return;
        }

        System.out.print("Inserisci il codice della prenotazione da cui eliminare il referto: ");
        String codicePren = scanner.nextLine().trim();
        Prenotazione prenotazione = selezionaPrenotazione(codicePren);
        if (prenotazione == null) {
            System.out.println("Errore: Prenotazione non valida.");
            return;
        }

        Referto referto = prenotazione.getReferto();
        if (referto == null) {
            System.out.println("Errore: Nessun referto associato a questa prenotazione.");
            return;
        }

        System.out.print("Sei sicuro di voler eliminare il referto? (SI/NO): ");
        String conferma = scanner.nextLine().trim().toUpperCase();

        if (conferma.equals("SI")) {
            prenotazione.setReferto(null);
            paziente.getRefertiCorrenti().remove(referto.getId());
            System.out.println("Referto eliminato con successo.");
        } else {
            System.out.println("Operazione annullata.");
        }
    }



    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Sede getSede() {
        return sede;
    }

    public String getCf() {
        return cf;
    }


    public void setCf(String cf) {
        this.cf = cf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Boolean verificaPassword(String password) {

        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return  "Cf= " + this.cf +
                " Nome= " + this.nome +
                " Cognome= " + this.cognome +
                " Sede= " + this.sede ;
    }
}
