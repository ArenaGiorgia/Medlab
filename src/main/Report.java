package main;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class Report {
    private String codiceReport;
    private LocalDate dataGenerazione;
    private String tipoReport;
    private Map<String, Prenotazione> prenotazioni;


    public Report(String tipoReport, Map<String, Prenotazione> prenotazioni) {
        this.codiceReport = UUID.randomUUID().toString().substring(0, 6);
        this.dataGenerazione = LocalDate.now();
        this.tipoReport = tipoReport;
        this.prenotazioni = filtraPrenotazioni(prenotazioni);
    }


    protected abstract boolean filtroData(Prenotazione p);


    private Map<String, Prenotazione> filtraPrenotazioni(Map<String, Prenotazione> tutte) {
        return tutte.entrySet().stream()
                .filter(e -> filtroData(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private double calcolaPercentuale(int parte, int totale) {
        return (totale == 0) ? 0.0 : (parte * 100.0 / totale);
    }

    @Override
    public String toString() {
        int maschi = 0, femmine = 0;
        int under18 = 0, over18 = 0;
        int cronici = 0, nonCronici = 0;
        int totale = prenotazioni.size();


        for (Prenotazione p : prenotazioni.values()) {
            Paziente paziente = p.getPaziente();

            if (paziente.getSesso().equalsIgnoreCase("M")) maschi++;
            else femmine++;

            if (paziente.getEta() < 18) under18++;
            else over18++;

            if (paziente.isCronico()) cronici++;
            else nonCronici++;
        }

        StringBuilder report = new StringBuilder();
        report.append("=== Report ").append(tipoReport).append(" (").append(codiceReport).append(") ===\n")
                .append("Generato il: ").append(dataGenerazione).append("\n")
                .append("Totale prenotazioni: ").append(totale).append("\n\n")
                .append("Maschi: ").append(maschi).append(" (").append(String.format("%.2f", calcolaPercentuale(maschi, totale))).append("%)\n")
                .append("Femmine: ").append(femmine).append(" (").append(String.format("%.2f", calcolaPercentuale(femmine, totale))).append("%)\n")
                .append("Under 18: ").append(under18).append(" (").append(String.format("%.2f", calcolaPercentuale(under18, totale))).append("%)\n")
                .append("Over 18: ").append(over18).append(" (").append(String.format("%.2f", calcolaPercentuale(over18, totale))).append("%)\n")
                .append("Cronici: ").append(cronici).append(" (").append(String.format("%.2f", calcolaPercentuale(cronici, totale))).append("%)\n")
                .append("Non cronici: ").append(nonCronici).append(" (").append(String.format("%.2f", calcolaPercentuale(nonCronici, totale))).append("%)\n");

        return report.toString();
    }
}
