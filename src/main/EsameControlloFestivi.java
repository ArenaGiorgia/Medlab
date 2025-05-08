package main;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class  EsameControlloFestivi extends EsameDecorator {

    private boolean cronico;
    private Set<DayOfWeek> weekend;
    private Set<MonthDay> festiviNazionali;

    public EsameControlloFestivi(Esame esame, boolean cronico) {
        super(esame);
        this.cronico=cronico;
        inizializzaFestivi();
    }

    public void inizializzaFestivi() {
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
        if (cronico) return true;

        LocalDate dataEsame = esame.getData();
        DayOfWeek giorno = dataEsame.getDayOfWeek();
        MonthDay giornoFestivo = MonthDay.from(dataEsame);

        return !(weekend.contains(giorno) || festiviNazionali.contains(giornoFestivo));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
