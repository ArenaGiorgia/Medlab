package main;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class Report {
    private String codiceReport;
    private LocalDate dataGenerazione;
    protected Map<String, Prenotazione> prenotazioni;
    protected String tipoReport;

    public Report() {
        this.codiceReport = UUID.randomUUID().toString().substring(0, 6);
        this.dataGenerazione = LocalDate.now();
        this.prenotazioni = new HashMap<>();
        this.tipoReport = "Medlab";
    }

    public String getCodiceReport() {
        return codiceReport;
    }

    public LocalDate getDataGenerazione() {
        return dataGenerazione;
    }

    public abstract void genera(Map<String, Prenotazione> tuttePrenotazioni);

    @Override
    public String toString() {
        Set<Paziente> pazientiUnici = prenotazioni.values().stream()
                .map(Prenotazione::getPaziente)
                .collect(Collectors.toSet());

        int total = pazientiUnici.size();
        if (total == 0) return "Nessuna prenotazione " + tipoReport.toLowerCase() + " nel periodo specificato.";

        long maschi = pazientiUnici.stream().filter(p -> p.getSesso().equalsIgnoreCase("M")).count();
        long femmine = pazientiUnici.stream().filter(p -> p.getSesso().equalsIgnoreCase("F")).count();
        long under18 = pazientiUnici.stream().filter(p -> p.getEta() < 18).count();
        long over18 = pazientiUnici.stream().filter(p -> p.getEta() >= 18).count();
        long cronici = pazientiUnici.stream().filter(Paziente::isCronico).count();
        long nonCronici = total - cronici;

        return "Report " + tipoReport + " codice: " + codiceReport + ", data: " + dataGenerazione + "\n" +
                "Prenotazioni analizzate: " + prenotazioni.size() + "\n" +
                "- Maschi: " + String.format("%.2f", maschi * 100.0 / total) + "%\n" +
                "- Femmine: " + String.format("%.2f", femmine * 100.0 / total) + "%\n" +
                "- Under 18: " + String.format("%.2f", under18 * 100.0 / total) + "%\n" +
                "- Over 18: " + String.format("%.2f", over18 * 100.0 / total) + "%\n" +
                "- Cronici: " + String.format("%.2f", cronici * 100.0 / total) + "%\n" +
                "- Non cronici: " + String.format("%.2f", nonCronici * 100.0 / total) + "%\n";
    }
}
