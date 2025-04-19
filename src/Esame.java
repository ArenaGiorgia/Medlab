import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


public class Esame {
    private String codice;
    private LocalDate data;
    private LocalTime orario;
    private String nome;
    private boolean prenotato;
    public Esame(LocalDate data, LocalTime orario, String nome) {

        this.codice = UUID.randomUUID().toString().substring(0, 4);
        this.data = data;
        this.orario = orario;
        this.nome = nome;

    }

    public void setnome(String descrizione) {
        this.nome = descrizione;
    }

    public void setOrario(LocalTime orario) {
        this.orario = orario;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public boolean isPrenotato() {
        return prenotato;
    }

    public void prenotato() {
        this.prenotato = true;
    }

    public void annullaPrenotazione() {
        this.prenotato = false;
    }

    public String statoEsame() {
        return prenotato ? "Prenotato" : "Libero";
    }

    public String getNome() {
        return nome;
    }

    public String getCodice() {
        return this.codice;
    }

    public LocalDate getData() {
        return this.data;
    }

    public LocalTime getOrario() {
        return this.orario;
    }

    @Override
    public String toString() {
        return
                "Codice= " + this.codice +
                "  Data= " + this.data +
                "  Orario= " + this.orario +
                "  Stato= " + this.statoEsame();
    }
}
