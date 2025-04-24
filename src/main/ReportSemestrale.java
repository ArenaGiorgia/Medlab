package main;

import java.time.LocalDate;
import java.util.Map;

public class ReportSemestrale extends Report {
    public ReportSemestrale(Map<String, Prenotazione> tutte) {
        super("Semestrale", tutte);
    }

    @Override
    protected boolean filtroData(Prenotazione p) {
        LocalDate data = p.getEsame().getData();
        LocalDate oggi = LocalDate.now();
        int semestreOggi = (oggi.getMonthValue() - 1) / 6;
        int semestreData = (data.getMonthValue() - 1) / 6;
        return data.getYear() == oggi.getYear() && semestreOggi == semestreData;
    }
}
