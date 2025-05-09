package test;

import main.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

class PersonaleLabTest {
    private PersonaleLaboratorio personale;
    private Sede sede;
    private PazienteProviderMock pazienteProvider;
    private Paziente paziente;
    private Prenotazione prenotazione;
    private Esame esame;

    @BeforeEach
    void setUp() {
        // Setup iniziale comune a tutti i test
        sede = new Sede(1, "Catania");
        personale = new PersonaleLaboratorio("CF123", "Mario", "Rossi", sede);
        pazienteProvider = new PazienteProviderMock();
        personale.setPazienteProvider(pazienteProvider);
        paziente = new Paziente("Luca", "Bianchi", LocalDate.of(1990, 5, 15) ,"CF456", "M",true);
        pazienteProvider.addPaziente(paziente);
        esame = new Esame( LocalDate.now(), LocalTime.now(),"Esame Urine");
        prenotazione = new Prenotazione( esame, paziente);
        prenotazione.setStato(new StatoInAttesa(prenotazione));
        esame.prenotato();
        paziente.getPrenotazioniPaziente().put(prenotazione.getCodice(),prenotazione);

    }

    @AfterEach
    void tearDown() {
        // Pulizia dopo ogni test
        personale = null;
        sede = null;
        pazienteProvider = null;
        paziente = null;
        prenotazione = null;
        esame = null;
    }

    @Test
    @DisplayName("Test costruttore e getter di base")
    void testCostruttoreEGetter() {
        assertEquals("CF123", personale.getCf());
        assertEquals("Mario", personale.getNome());
        assertEquals("Rossi", personale.getCognome());
        assertEquals(sede, personale.getSede());
        assertNotNull(personale.getReferti());
        assertTrue(personale.getReferti().isEmpty());
    }

    @Test
    @DisplayName("Test verifica password corretta")
    void testVerificaPasswordCorretta() {
        assertTrue(personale.verificaPassword("CF123"));
    }

    @Test
    @DisplayName("Test verifica password errata")
    void testVerificaPasswordErrata() {
        assertFalse(personale.verificaPassword("passwordErrata"));
    }

    @Test
    @DisplayName("Test lista pazienti della sede")
    void testListaPazientiDellaSede() {
        List<Paziente> pazienti = personale.listaPazientiDellaSede();
        assertNotNull(pazienti);
        assertEquals(1, pazienti.size());
        assertEquals(paziente, pazienti.get(0));
    }

    @Test
    @DisplayName("Test visualizza lista esami prenotati con esami presenti")
    void testVisualizzaListaEsamiPrenotatiConEsami() {
        boolean result = personale.visualizzaListaEsamiPrenotati();
        assertTrue(result);
    }

    @Test
    @DisplayName("Test visualizza lista esami prenotati senza esami") // QUANDO NON CI SONO DEGLI ESAMI IN ATTESA DI REFERTO
    void testVisualizzaListaEsamiPrenotatiSenzaEsami() {

        paziente.getPrenotazioniPaziente().clear();
        boolean result = personale.visualizzaListaEsamiPrenotati();
        assertFalse(result);
    }
//UC5
    @Test
    @DisplayName("Test seleziona prenotazione esistente")
    void testSelezionaPrenotazioneEsistente() {
        String codicePrenotazione = prenotazione.getCodice();
        Prenotazione result = personale.selezionaPrenotazione(codicePrenotazione);
        assertNotNull(result);
        assertEquals(prenotazione, result);
    }

    @Test
    @DisplayName("Test seleziona prenotazione inesistente")
    void testSelezionaPrenotazioneInesistente() {
        Prenotazione result = personale.selezionaPrenotazione("CODICE_INESISTENTE");
        assertNull(result);
    }

    @Test
    @DisplayName("Test inserisci stato da InAttesa a Completato")
    void testInserisciStato() {
        personale.inserisciStato(prenotazione);
        assertTrue(prenotazione.getStato() instanceof StatoCompletato);
    }

    @Test
    @DisplayName("Test visualizza pazienti associati alla sede con pazienti")
    void testVisualizzaPazientiAssociatiAllaSedeConPazienti() {
        boolean result = personale.visualizzaPazientiAssociatiAllaSede();
        assertTrue(result);
    }

    @Test
    @DisplayName("Test visualizza pazienti associati alla sede senza pazienti")
    void testVisualizzaPazientiAssociatiAllaSedeSenzaPazienti() {
        pazienteProvider.clearPazienti();
        boolean result = personale.visualizzaPazientiAssociatiAllaSede();
        assertFalse(result);
    }

    @Test
    @DisplayName("Test seleziona paziente esistente")
    void testSelezionaPazienteEsistente() {
        Paziente result = personale.selezionaPazienteProxy("CF456");
        assertNotNull(result);
        assertEquals(paziente, result);
    }

    @Test
    @DisplayName("Test seleziona paziente inesistente")
    void testSelezionaPazienteInesistente() {
        Paziente result = personale.selezionaPazienteProxy("CF5437");
        assertNull(result);
    }

    @Test
    @DisplayName("Test visualizza prenotazioni confermate con prenotazioni")
    void testVisualizzaPrenotazioniConfermateConPrenotazioni() {
        prenotazione.setStato(new StatoCompletato(prenotazione));
        boolean result = personale.visualizzaPrenotazioniConfermate(paziente);
        assertTrue(result);
    }

    @Test
    @DisplayName("Test visualizza prenotazioni confermate senza prenotazioni")
    void testVisualizzaPrenotazioniConfermateSenzaPrenotazioni() {
        boolean result = personale.visualizzaPrenotazioniConfermate(paziente);
        assertFalse(result);
    }
//UC6
// Test per gestione referti
@Test
@DisplayName("Test inserisci referto ")
void testInserisciReferto() {
    String input = "Tutto Ok";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    Referto referto = new Referto("REF1", LocalDate.now());
    personale.setRefertoCorrente(referto);
    personale.inserisciReferto(new Scanner(System.in));

    assertEquals("Tutto Ok", referto.getRisultato());
    System.setIn(System.in);
}
    @Test
    @DisplayName("Test conferma referto")
    void testConfermaReferto() {
        Referto referto = new Referto("REF1", LocalDate.now());
        String input = "Tutto Ok";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        personale.setRefertoCorrente(referto);
        referto.setReferto(input);
        personale.confermaReferto(paziente);

        assertTrue(paziente.getRefertiCorrenti().containsKey("REF1"));
        assertEquals("Tutto Ok", referto.getRisultato());
        assertNull(personale.getRefertoCorrente());
    }
    @Test
    @DisplayName("Test Aggiorna Referto") //aggiorna la descrizione di un referto in modo completo con un input da tastiera
    void testAggiornaReferto() {

        prenotazione.setStato(new StatoCompletato(prenotazione));
        Referto referto = new Referto("REF1", LocalDate.now());
        prenotazione.setReferto(referto);

        String allInputs = String.join("\n",
                paziente.getCf(),         // CF paziente
                prenotazione.getCodice(), // Codice prenotazione
                "Risultato negativo", // Descrizione referto
                "" // Conferma finale
        ) + "\n";
        System.out.println("Test Input: " + allInputs);

        InputStream originalIn = System.in;

        try {

            System.setIn(new ByteArrayInputStream(allInputs.getBytes()));

            personale.aggiornaReferto();

          assertEquals("Risultato negativo", referto.getRisultato());
          assertSame(referto, prenotazione.getReferto());
           assertTrue(paziente.getRefertiCorrenti().containsValue(referto));
            assertNull(personale.getRefertoCorrente());
        } finally {

            System.setIn(originalIn);
        }
    }


}



