package main;

import java.util.Map;

public interface ReportFactory {

     Report createReport(Map<String, Prenotazione> prenotazioni);

}
