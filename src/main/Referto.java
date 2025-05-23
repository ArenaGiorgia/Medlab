package main;

import java.time.LocalDate;


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

    public void setReferto(String risultato) {
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

    @Override
    public String toString() {
        return  "Id=" + Id +
                "  Data=" + data +
                "  Risultato=" + risultato;
    }


}

