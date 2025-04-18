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
    private Integer età;
    private Sede sede;
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
        this.età = calcolaEta(dataNascita);
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

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public String getCf() {

        return cf;
    }

    public String getSesso() {

        return sesso;
    }

    public Sede getSede() {

        return sede;
    }

    public void setSedi(List<Sede> sedi) {

        this.sedi = sedi;
    }

    public void setPrenotazioni(Map<String, Prenotazione> prenotazioni) {
        this.prenotazioniPaziente = prenotazioni;
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
        this.età = calcolaEta(dataNascita); // Aggiorna l'età
    }

    public void setCf(String cf) {
        this.cf = cf;
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
        if (this.password.equals(password)) {
            return true;
        }
        return false;
    }

    public void setMalatoCronico(boolean cronico) {
        this.cronico = cronico;
    }

    public boolean isCronico() {
        return cronico;
    }

    //metodo che mi servira per il caso duso 8 oltre che per l 1 per essere richiamato dall admin
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
                break;
            case 2:
                System.out.print("Inserisci il nuovo cognome del paziente: ");
                String cognome = scanner.nextLine();
                setCognome(cognome);
                break;
            case 3:
                LocalDate dataNascita = null;
                do {
                    try {
                        System.out.print("Inserisci la nuova data di nascita (yyyy-MM-dd): ");
                        String dataNascitaInput = scanner.nextLine();
                        dataNascita = LocalDate.parse(dataNascitaInput);
                        setDataNascita(dataNascita);
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
                } else if (risposta.equals("NO")) {
                    setMalatoCronico(false);
                } else {
                    System.out.println("Risposta non valida. Il paziente non è stato aggiornato.");
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
    public void modificaPassword(){ //ci servirà piu avanti per il caso d'uso 8 di modifica nostro
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


    @Override
    public String toString() {
        return "main.Paziente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", dataNascita=" + dataNascita +
                ", cf='" + cf + '\'' +
                ", sesso='" + sesso + '\'' +
                ", età=" + età +
                ", malatoCronico=" + cronico +'}';
    }


}