package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import main.Sede;
import main.Esame;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@DisplayName("Test per la classe Sede")
class SedeTest {

    private Sede sede;
    private final Integer codiceTest = 1;
    private final String nomeTest = "Sede Centrale";

    @BeforeEach
    void setUp() {
        sede = new Sede(codiceTest, nomeTest);
    }

    @Test
    @DisplayName("Test costruttore e getter di base")
    void testCostruttoreEGetter() {
        assertAll(
                () -> assertEquals(codiceTest, sede.getCodice(), "Il codice della sede non corrisponde"),
                () -> assertEquals(nomeTest, sede.getNome(), "Il nome della sede non corrisponde"),
                () -> assertNotNull(sede.getEsami(), "La mappa degli esami non dovrebbe essere null")
        );
    }

    @Test
    @DisplayName("Test setter nome")
    void testSetNome() {
        String nuovoNome = "Nuova Sede";
        sede.setNome(nuovoNome);
        assertEquals(nuovoNome, sede.getNome(), "Il nome della sede non è stato aggiornato correttamente");
    }

    @Test
    @DisplayName("Test caricaEsami")
    void testCaricaEsami() {
        Map<String, Esame> esami = sede.getEsami();
        assertAll(
                () -> assertFalse(esami.isEmpty(), "La mappa degli esami non dovrebbe essere vuota"),
                () -> assertEquals(9, esami.size(), "Dovrebbero esserci 9 esami caricati")
        );
    }

    @Test
    @DisplayName("Test aggiungiEsame")
    void testAggiungiEsame() {

        int sizePrima = sede.getEsami().size();
        sede.aggiungiEsame(LocalDate.now().plusDays(1),LocalTime.of(19, 0),"Esame Oncologico");
        int sizeDopo = sede.getEsami().size();

        assertAll(
                () -> assertEquals(sizePrima + 1, sizeDopo, "Dovrebbe esserci un esame in più"),
             // () -> assertTrue(sede.getEsami().containsValue(new Esame(LocalDate.now().plusDays(1),LocalTime.of(19, 0),"Esame Oncologico")),
               //         "L'esame aggiunto non è presente nella mappa"),
                () -> assertEquals("Esame Oncologico", sede.getEsami().values().stream()
                        .filter(e -> e.getData().equals(LocalDate.now().plusDays(1)) && e.getOrario().equals(LocalTime.of(19, 0)))
                        .findFirst().get().getNome(), "Il nome dell'esame non corrisponde")
        );
    }

    @Test
    @DisplayName("Test isOrarioDisponibile - orario disponibile")
    void testIsOrarioDisponibile_Disponibile() {
        LocalDate data = LocalDate.now().plusDays(2);
        LocalTime orario = LocalTime.of(16, 0);

        assertTrue(sede.isOrarioDisponibile(data, orario),
                "L'orario dovrebbe essere disponibile per una data senza esami");
    }

    @Test
    @DisplayName("Test isOrarioDisponibile - orario non disponibile")
    void testIsOrarioDisponibile_NonDisponibile() {
        LocalDate data = LocalDate.now();
        LocalTime orario = LocalTime.of(9, 30); // Sovrapposto con esame1 (9:00)

        assertFalse(sede.isOrarioDisponibile(data, orario),
                "L'orario non dovrebbe essere disponibile per sovrapposizione");
    }

  /*  @Test
    @DisplayName("Test isOrarioDisponibile - orario limite (90 minuti prima)")
    void testIsOrarioDisponibile_OrarioLimite() {
        LocalDate data = LocalDate.now();
        LocalTime orario = LocalTime.of(10, 31); // 1 minuto dopo la fine dell'intervallo di esame2 (10:30)

        assertTrue(sede.isOrarioDisponibile(data, orario),
                "L'orario dovrebbe essere disponibile essendo fuori dall'intervallo di 90 minuti");
    }*/


}
