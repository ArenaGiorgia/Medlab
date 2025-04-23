package main;

import java.time.LocalDate;
import java.util.Map;

public class ReportAnnuale extends Report {
    public ReportAnnuale() {
        this.tipoReport = "ANNUALE";
    }

    @Override
    public void genera(Map<String, Prenotazione> tuttePrenotazioni) {
        LocalDate unAnnoFa = LocalDate.now().minusYears(1);

        for (Prenotazione p : tuttePrenotazioni.values()) {
            if (p.getEsame().getData().isAfter(unAnnoFa)) {
                this.prenotazioni.put(p.getCodice(), p);
            }
        }
    }
}
