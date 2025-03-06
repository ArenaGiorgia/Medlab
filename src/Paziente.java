import java.time.LocalDate;

public class Paziente {

    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String cf;
    private String sesso;
    private Integer età;

    private Sede sede;

    public Paziente( String nome, String cognome, LocalDate dataNascita, String cf, String sesso, Integer età) {

        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.cf = cf;
        this.sesso = sesso;
        this.età = età;
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

    public Integer getEtà() {
        return età;
    }

    public Sede getSede() {
        return sede;
    }
}