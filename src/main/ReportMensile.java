package main;

import java.time.LocalDate;
import java.util.Map;

public class ReportMensile extends Report {
    public ReportMensile() {
        this.tipoReport = "MENSILE";
    }

    @Override
    public void genera(Map<String, Prenotazione> tuttePrenotazioni) {
        LocalDate unMeseFa = LocalDate.now().minusMonths(1);

        for (Prenotazione p : tuttePrenotazioni.values()) {
            if (p.getEsame().getData().isAfter(unMeseFa)) {
                this.prenotazioni.put(p.getCodice(), p);
            }
        }
    }
}
