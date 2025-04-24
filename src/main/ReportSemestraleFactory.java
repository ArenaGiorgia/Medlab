package main;

import java.util.Map;

public class ReportSemestraleFactory implements ReportFactory {
@Override
    public ReportSemestrale createReport(Map<String,Prenotazione> prenotazione) {
    return new ReportSemestrale(prenotazione);
}
}
