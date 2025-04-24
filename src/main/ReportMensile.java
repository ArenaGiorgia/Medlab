package main;

import java.time.LocalDate;
import java.util.Map;

public class ReportMensile extends Report {
    public ReportMensile(Map<String, Prenotazione> tutte) {
        super("Mensile", tutte);
    }

    @Override
    protected boolean filtroData(Prenotazione p) {
        LocalDate data = p.getEsame().getData();
        LocalDate oggi = LocalDate.now();
        return data.getMonth() == oggi.getMonth() && data.getYear() == oggi.getYear();
    }
}
