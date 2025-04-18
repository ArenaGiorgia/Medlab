package main;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EsameControlloFestivi extends EsameDecorator {

    protected Paziente paziente;
    protected Set<DayOfWeek> weekend;
    protected Set<MonthDay> festiviNazionali;

    public EsameControlloFestivi(Esame esame, Paziente paziente) {
        super(esame);
        this.paziente = paziente;
        inizializzaFestivi();
    }

    private void inizializzaFestivi() {
        weekend = new HashSet<>(Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

        festiviNazionali = new HashSet<>(Arrays.asList(
                MonthDay.of(1, 1),    // Capodanno
                MonthDay.of(4, 25),   // Liberazione
                MonthDay.of(5, 1),    // Lavoratori
                MonthDay.of(8, 15),   // Ferragosto
                MonthDay.of(12, 25),  // Natale
                MonthDay.of(12, 26)   // Santo Stefano
        ));
    }

    @Override
    public boolean prenotabile() {
        if (paziente.isCronico()) {
            return true; // Pazienti cronici hanno sempre accesso
        }

        LocalDate dataEsame = esame.getData();
        DayOfWeek giorno = dataEsame.getDayOfWeek();
        MonthDay giornoFestivo = MonthDay.from(dataEsame);

        return !(weekend.contains(giorno) || festiviNazionali.contains(giornoFestivo));
    }

    @Override
    public String toString() {
        return esame.toString(); // Mantieni il comportamento originale se non vuoi aggiunte
    }
}
