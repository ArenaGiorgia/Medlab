package main;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Paziente {

    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String cf;
    private String password;
    private String sesso;
    private boolean cronico;
    private Integer eta;
    private List<Sede> sedi;
    private Map<String, Prenotazione> prenotazioniPaziente;
    private Map<String, Referto> refertiCorrenti;
    public Paziente(String nome, String cognome, LocalDate dataNascita, String cf, String sesso, boolean cronico) {

        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.cf = cf;
        this.password=cf; //password settata automaticamente al codice fiscale
        this.sesso = sesso;
        this.eta = calcolaEta(dataNascita);
        this.prenotazioniPaziente = new HashMap<>();
        this.sedi = new ArrayList<>();
        this.refertiCorrenti = new HashMap<>();
        this.cronico = cronico;
    }


    public List<Sede> getSedi() {

        return sedi;
    }

    public Map<String, Prenotazione> getPrenotazioni() {

        return prenotazioniPaziente;
    }

    public Map<String, Referto> getRefertiCorrenti() {
        return refertiCorrenti;

    }

    private int calcolaEta(LocalDate dataNascita) {

        return Period.between(dataNascita, LocalDate.now()).getYears();
    }
    public String getNome() {

        return nome;
    }

    public String getCognome() {
        return cognome;
    }


    public String getCf() {

        return cf;
    }

    public String getSesso() {

        return sesso;
    }

    public Integer getEta() {
        return eta;
    }


    public Map<String, Prenotazione> getPrenotazioniPaziente() {
        return prenotazioniPaziente;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
        this.eta = calcolaEta(dataNascita);
    }


    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Boolean verificaPassword(String password) {
        return this.password.equals(password);
    }

    public void setMalatoCronico(boolean cronico) {
        this.cronico = cronico;
    }

    public boolean isCronico() {
        return cronico;
    }

    //metodo sia UC1 che UC8
    public void modificaPaziente() {
    Scanner scanner = new Scanner(System.in);
    int scelta;
    do {
        System.out.println("Seleziona il campo da modificare: ");
        System.out.println("1. Nome");
        System.out.println("2. Cognome");
        System.out.println("3. Data di nascita");
        System.out.println("4. Sesso");
        System.out.println("5. Password");
        System.out.println("6. Malato cronico");
        System.out.println("0. Esci");
        System.out.print("Inserisci il numero corrispondente: ");
        scelta = scanner.nextInt();
        scanner.nextLine();

        switch (scelta) {
            case 1:
                System.out.print("Inserisci il nuovo nome del paziente: ");
                String nome = scanner.nextLine();
                setNome(nome);
                System.out.println("Nome modificato con successo. ");
                break;
            case 2:
                System.out.print("Inserisci il nuovo cognome del paziente: ");
                String cognome = scanner.nextLine();
                setCognome(cognome);
                System.out.println("Cognome modificato con successo. ");
                break;
            case 3:
                LocalDate dataNascita;
                do {
                    try {
                        System.out.print("Inserisci la nuova data di nascita (yyyy-MM-dd): ");
                        String dataNascitaInput = scanner.nextLine();
                        dataNascita = LocalDate.parse(dataNascitaInput);
                        setDataNascita(dataNascita);
                        System.out.println("Data modificata con successo. ");
                        break;
                    } catch (DateTimeParseException e) {
                        System.out.println("Data non valida. Inserisci una data valida.");
                    }
                } while (true);
                break;
            case 4:
                String sesso;
                do {
                    System.out.print("Inserisci il nuovo sesso (M/F): ");
                    sesso = scanner.nextLine();
                    if (sesso.equals("M") || sesso.equals("F")) {
                        setSesso(sesso);
                        System.out.println("Sesso modificato con successo. ");
                        break;
                    } else {
                        System.out.println("Errore: Devi inserire solo 'M' per maschio o 'F' per femmina.");
                    }
                } while (true);
                break;
            case 5:
                modificaPassword();
                break;
            case  6:
                System.out.print("Il paziente è malato cronico? (SI/NO): ");
                String risposta = scanner.nextLine().trim().toUpperCase();
                if (risposta.equals("SI")) {
                    setMalatoCronico(true);
                    System.out.println("campo modificato con successo. ");
                } else if (risposta.equals("NO")) {
                    setMalatoCronico(false);
                    System.out.println("campo modificato con successo. ");
                } else {
                    System.out.println("Risposta non valida. ");
                }
                break;
            case 0:
                System.out.println("Modifica paziente terminata.");
                break;
            default:
                System.out.println("Scelta non valida. Riprova.");
                break;
        }
    } while (scelta != 0);

    System.out.println("Dati del paziente aggiornati con successo.");
}

//metodo sia per Uc1 che Uc8
    public void modificaPassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserire la vecchia password: ");
        String oldPassword = scanner.nextLine();
        if(getPassword().equals(oldPassword)){
            System.out.print("Inserire la nuova password: ");
            String newPassword = scanner.nextLine();
            setPassword(newPassword);
            System.out.println("Password modificata con successo!");
        }
        else{
            System.out.println("Password errata.");
        }
    }

    //metodo per l UC7
    public void visualizzaRefertiAssociatiEsami() {
        if (this.refertiCorrenti.isEmpty()) {
            System.out.println("Nessun referto disponibile per questo paziente.");
            return;
        }

        boolean refertiTrovati = false;
        System.out.println("Referti associati agli esami per il paziente " + this.nome + " " + this.cognome + ":");

        for (Map.Entry<String, Prenotazione> entry : this.prenotazioniPaziente.entrySet()) {
            Prenotazione prenotazione = entry.getValue();
            Referto referto = prenotazione.getReferto();

            if (referto != null && referto.getRisultato() != null && !referto.getRisultato().isEmpty()) {
                System.out.println("---------------------------------------------");
                System.out.println("➤ Esame: " + prenotazione.getEsame().getNome());
                System.out.println("   Data: " + prenotazione.getEsame().getData());
                System.out.println("   Ora: " + prenotazione.getEsame().getOrario());
                System.out.println("   Referto Id: " + referto.getId());
                System.out.println("   Data referto: " + referto.getData());
                System.out.println("   Risultato: " + referto.getRisultato());
                refertiTrovati = true;
            }
        }

        if (!refertiTrovati) {
            System.out.println("Nessun referto associato a esami trovati per questo paziente.");
        }
    }

//metodo per l UC13
    public void stampaPrenotazioniAttive() {
        System.out.println("Prenotazioni attive per il paziente: " + nome + " " + cognome);

        boolean trovato = false;
        for (Map.Entry<String, Prenotazione> entry : prenotazioniPaziente.entrySet()) {
            Prenotazione prenotazione = entry.getValue();
            if (prenotazione.getStato() instanceof StatoInAttesa) {

                Esame esame = prenotazione.getEsame();

                if (esame.isPrenotato()) {
                    System.out.println("Codice prenotazione: " + entry.getKey() + " Esame: " + esame.getNome()
                            + " Data: " + esame.getData() + " Orario: " + esame.getOrario());
                    trovato = true;
                }
            }
        }
        if (!trovato) {
            System.out.println("Nessuna prenotazione attiva.");
        }
    }

    @Override
    public String toString() {
        return
                "Nome= " + this.nome +
                "  Cognome= " + this.cognome +
                "  DataNascita= " + this.dataNascita +
                "  Cf= " + this.cf +
                "  Sesso= " + this.sesso +
                "  Età= " + this.eta +
                "  MalatoCronico= " + this.cronico ;
    }


}