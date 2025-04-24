package main;

import java.util.Map;

public class ReportMensileFactory implements ReportFactory {
@Override
    public ReportMensile createReport(Map<String,Prenotazione> prenotazione) {
    return new ReportMensile(prenotazione);
}

}