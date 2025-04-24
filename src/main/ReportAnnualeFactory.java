package main;

import java.util.Map;

public class ReportAnnualeFactory implements ReportFactory {

   @Override
    public ReportAnnuale createReport(Map<String,Prenotazione> prenotazione) {
        return new ReportAnnuale(prenotazione);
    }

}
