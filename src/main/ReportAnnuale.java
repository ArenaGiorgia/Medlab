package main;

import java.time.LocalDate;
import java.util.Map;

public class ReportAnnuale extends Report {
    public ReportAnnuale(Map<String, Prenotazione> tutte) {
        super("Annuale", tutte);
    }

    @Override
    public boolean filtroData(Prenotazione p) {
        return p.getEsame().getData().getYear() == LocalDate.now().getYear();
    }
}