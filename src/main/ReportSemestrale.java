package main;

import java.time.LocalDate;
import java.util.Map;

public class ReportSemestrale extends Report {
    public ReportSemestrale() {
        this.tipoReport = "SEMESTRALE";
    }

    @Override
    public void genera(Map<String, Prenotazione> tuttePrenotazioni) {
        LocalDate seiMesiFa = LocalDate.now().minusMonths(6);

        for (Prenotazione p : tuttePrenotazioni.values()) {
            if (p.getEsame().getData().isAfter(seiMesiFa)) {
                this.prenotazioni.put(p.getCodice(), p);
            }
        }
    }
}
