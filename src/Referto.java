import java.time.LocalDate;
import java.time.LocalTime;

public class Referto
{
    private String Id ;
    private LocalDate data;
    private String risultato;

    public Referto(String id, LocalDate data) {
        this.Id = id;
        this.data = data;
        this.risultato = null;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setDate(LocalDate data) {
        this.data = data;
    }

    public void setRisultato(String risultato) {
        this.risultato = risultato;
    }

    public String getId() {
        return Id;
    }

    public LocalDate getData() {
        return data;
    }

    public String getRisultato() {
        return risultato;
    }

}

