package test;

import main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class MedlabTest {
    private Medlab medlab;

    @BeforeEach
    public void setUp() {
        medlab = Medlab.getInstance();
        medlab.setPazienti(new HashMap<>());
        medlab.setSedi(new ArrayList<>());
        medlab.setPrenotazioni(new HashMap<>());
        medlab.setPersonaliLaboratori(new HashMap<>());
    }

    @Test
    public void testSingleton() {
        Medlab instance1 = Medlab.getInstance();
        Medlab instance2 = Medlab.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testAggiungiPaziente() {
        LocalDate dataNascita = LocalDate.of(1990, 1, 1);
        medlab.nuovoPaziente("Mario", "Rossi", dataNascita, "MRORSS90A01H501R", "M", false);
        medlab.confermaPaziente();
        assertEquals(1, medlab.getPazienti().size());
        assertNotNull(medlab.getPazienti().get("MRORSS90A01H501R"));
    }

    @Test
    public void testAggiungiSede() {
        medlab.nuovaSede("Sede Roma", 1);
        medlab.confermaSede();
        assertEquals(1, medlab.getSedi().size());
        assertEquals("Sede Roma", medlab.getSedi().get(0).getNome());
    }

    @Test
    public void testPrenotazioneEsame() {
        // Aggiungi paziente
        LocalDate dataNascita = LocalDate.of(1990, 1, 1);
        medlab.nuovoPaziente("Mario", "Rossi", dataNascita, "MRORSS90A01H501R", "M", false);
        medlab.confermaPaziente();
        // Aggiungi sede
        medlab.nuovaSede("Sede Roma", 1);
        medlab.confermaSede();
        // Aggiungi esame
        Esame esame = new Esame(LocalDate.now().plusDays(1), LocalTime.of(10, 0), "Esame sangue");
        medlab.getSedi().get(0).aggiungiEsame(esame);
        // Seleziona paziente
        medlab.setPazienteCorrente(medlab.getPazienti().get("MRORSS90A01H501R"));
        // Prova a prenotare
        medlab.SelezionaEsame(esame);
        medlab.ConfermaEsame();
        assertEquals(1, medlab.getPrenotazioni().size());
    }

    @Test
    public void testVerificaAccesso() {
        // Configura un paziente di test
        Paziente p = new Paziente("Test", "User", LocalDate.now(), "TESTCF", "M", false);
        p.setPassword("password");
        medlab.getPazienti().put("TESTCF", p);

        assertEquals("paziente", medlab.VerificaAccesso("TESTCF", "password"));
        assertEquals("credenziali errate", medlab.VerificaAccesso("TESTCF", "wrongpass"));
    }

    @Test
    public void testStatePattern() {
        LocalDate dataNascita = LocalDate.of(1990, 1, 1);
        Prenotazione p = new Prenotazione(new Esame(LocalDate.now().plusDays(1), LocalTime.of(10, 0), "Esame sangue"), new Paziente("Mario", "Rossi", dataNascita, "MRORSS90A01H501R", "M", false));
        assertTrue(p.getStato() instanceof StatoInAttesa);
        p.getStato().completa(p);
        assertTrue(p.getStato() instanceof StatoCompletato);
    }
    @Test
    public void testDecoratorPattern() {
        // 1. Setup pazienti
        Paziente pazienteCronico = new Paziente("Mario", "Rossi", LocalDate.of(1980, 1, 1), "MRORSS80A01H501R", "M", true ); // Malato cronico
        Paziente pazienteNonCronico = new Paziente("Luigi", "Verdi", LocalDate.of(1990, 5, 15), "LGIVRD90E15H501R", "M", false ); // Non malato cronico);
        // 2. Creazione esame di sabato (festivo)
        Esame esameSabato = new Esame(LocalDate.of(2023, Month.DECEMBER, 25), LocalTime.of(10, 30), "Esame del sangue");
        // 3. Test con paziente cronico (DEVE poter prenotare)
        Esame decoratoCronico = new EsameControlloFestivi(esameSabato, pazienteCronico);
        assertTrue(((EsameControlloFestivi) decoratoCronico).prenotabile(),
                "Un paziente cronico dovrebbe poter prenotare nei festivi");
        // 4. Test con paziente non cronico (NON deve poter prenotare)
        Esame decoratoNonCronico = new EsameControlloFestivi(esameSabato, pazienteNonCronico);
        assertFalse(((EsameControlloFestivi) decoratoNonCronico).prenotabile(),
                "Un paziente non cronico NON dovrebbe poter prenotare nei festivi");

        // 5. Test aggiuntivo: verifica che in giorno feriale entrambi possano prenotare
        Esame esameLunedì = new Esame(
                LocalDate.of(2023, Month.DECEMBER, 18), // Lunedì (non festivo)
                LocalTime.of(14, 0),
                "Radiografia"
        );

        Esame decoratoFerialeCronico = new EsameControlloFestivi(esameLunedì, pazienteCronico);
        Esame decoratoFerialeNonCronico = new EsameControlloFestivi(esameLunedì, pazienteNonCronico);

        assertAll(
                () -> assertTrue(((EsameControlloFestivi) decoratoFerialeCronico).prenotabile(),
                        "Dovrebbe essere prenotabile in giorno feriale (cronico)"),
                () -> assertTrue(((EsameControlloFestivi) decoratoFerialeNonCronico).prenotabile(),
                        "Dovrebbe essere prenotabile in giorno feriale (non cronico)")
        );
    }

}