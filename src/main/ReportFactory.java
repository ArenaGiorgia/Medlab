package main;

import java.util.Map;

public interface ReportFactory {

    public Report createReport(Map<String, Prenotazione> prenotazioni);

}
