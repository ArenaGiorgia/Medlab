import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Paziente {

    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String cf;
    private String password;
    private String sesso;
    private Integer età;
    private Sede sede;


    public Paziente(String nome, String cognome, LocalDate dataNascita, String cf, String sesso) {

        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.cf = cf;
        this.password=cf; //password settata automaticamente al codice fiscale
        this.sesso = sesso;
        this.età = calcolaEta(dataNascita);
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

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
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
//metodo che mi servira per il caso duso 8 oltre che per l 1 per essere richiamato dall admin
public void modificaPaziente() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("I clienti nel sistema sono: ");
    System.out.println(toString());
    System.out.print("Inserisci il codice fiscale del paziente da modificare: ");
    String codiceFiscale = scanner.nextLine();

    int scelta;
    do {
        System.out.println("\nSeleziona il campo da modificare: ");
        System.out.println("1. Nome");
        System.out.println("2. Cognome");
        System.out.println("3. Data di nascita");
        System.out.println("4. Sesso");
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
        return "Paziente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", dataNascita=" + dataNascita +
                ", cf='" + cf + '\'' +
                ", sesso='" + sesso + '\'' +
                ", età=" + età + '}';
    }
}