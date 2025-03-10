import java.time.LocalDate;
import java.time.Period;
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