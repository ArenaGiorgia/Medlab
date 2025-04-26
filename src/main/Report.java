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

        String report = "=== Report " + tipoReport + " (" + codiceReport + ") ===\n" +
                "Generato il: " + dataGenerazione + "\n" +
                "Totale prenotazioni: " + totale + "\n\n" +
                "Maschi: " + maschi + " (" + String.format("%.2f", calcolaPercentuale(maschi, totale)) + "%)\n" +
                "Femmine: " + femmine + " (" + String.format("%.2f", calcolaPercentuale(femmine, totale)) + "%)\n" +
                "Under 18: " + under18 + " (" + String.format("%.2f", calcolaPercentuale(under18, totale)) + "%)\n" +
                "Over 18: " + over18 + " (" + String.format("%.2f", calcolaPercentuale(over18, totale)) + "%)\n" +
                "Cronici: " + cronici + " (" + String.format("%.2f", calcolaPercentuale(cronici, totale)) + "%)\n" +
                "Non cronici: " + nonCronici + " (" + String.format("%.2f", calcolaPercentuale(nonCronici, totale)) + "%)\n";

        return report;
    }
}
