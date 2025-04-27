package test;

import javafx.beans.value.ObservableBooleanValue;
import main.*;
import org.junit.jupiter.api.*;

import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

class PersonaleLaboratorioTest {
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
        esame.prenotato(); // Aggiungi questa linea
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
    @DisplayName("Test visualizza lista esami prenotati senza esami")
    void testVisualizzaListaEsamiPrenotatiSenzaEsami() {
        // Rimuovo la prenotazione di test
        paziente.getPrenotazioniPaziente().clear();
        boolean result = personale.visualizzaListaEsamiPrenotati();
        assertFalse(result);
    }

    @Test
    @DisplayName("Test seleziona prenotazione esistente")
    void testSelezionaPrenotazioneEsistente() {
        String codicePrenotazione = prenotazione.getCodice(); // Usa il codice reale
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
    void testInserisciStatoDaInAttesaACompletato() {
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
        Paziente result = personale.selezionaPazienteProxy("CF_INESISTENTE");
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

    @Test
    @DisplayName("Test completo aggiornaReferto - Flusso principale")
    void testAggiornaReferto() {
        // 1. Configurazione iniziale
        prenotazione.setStato(new StatoCompletato(prenotazione));
        Referto referto = new Referto("REF1", LocalDate.now());
        prenotazione.setReferto(referto);

        // 2. Simula l'input utente completo:
        //    - CF paziente
        //    - Codice prenotazione
        //    - Descrizione referto
        String inputSimulato = String.join("\n",
                paziente.getCf(),         // CF paziente (flusso 2)
                prenotazione.getCodice(), // Codice prenotazione (flusso 4)
                "Risultato negativo"   // Descrizione referto (flusso inserisciReferto)
                // Conferma (flusso confermaReferto)
        );

        InputStream inputStream = new ByteArrayInputStream(inputSimulato.getBytes());
        System.setIn(inputStream);

        // 3. Esecuzione
        try {
            personale.aggiornaReferto(); // Chiama il metodo che sta leggendo l'input
        } catch (Exception e) {
            e.printStackTrace(); // Aggiungi la gestione delle eccezioni per capire meglio l'errore
            fail("Test failed due to unexpected exception");
        }

        // 4. Verifiche
        // 4.1 Verifica che il referto sia stato aggiornato
        assertEquals("Risultato negativo", referto.getRisultato());

        // 4.2 Verifica che sia stato associato alla prenotazione
        assertSame(referto, prenotazione.getReferto());

        // 4.3 Verifica che sia nel paziente
        assertTrue(paziente.getRefertiCorrenti().containsValue(referto));

        // 4.4 Verifica che il referto corrente sia stato resettato
        assertNull(personale.getRefertoCorrente());

        // 5. Pulizia
        System.setIn(System.in); // Ripristina il flusso di input originale
    }


    @Test
    @DisplayName("Test inserisci referto con input simulato")
    void testInserisciRefertoConInputSimulato() {
        // Prepara referto corrente
        Referto referto = new Referto("REF1", LocalDate.now());
        personale.setRefertoCorrente(referto);

        // Simula input utente
        String inputSimulato = "Risultato dell'esame negativo\n";
        InputStream inputStream = new ByteArrayInputStream(inputSimulato.getBytes());
        System.setIn(inputStream);

        // Esegui
        personale.inserisciReferto();

        // Verifica
        assertEquals("Risultato dell'esame negativo", referto.getRisultato());
        System.setIn(System.in);
    }


}

// Classe mock per PazienteProvider per isolare i test
class PazienteProviderMock implements PazienteProvider {
    private Map<String, Paziente> pazienti = new HashMap<>();

    public void addPaziente(Paziente paziente) {
        pazienti.put(paziente.getCf(), paziente);
    }

    public void clearPazienti() {
        pazienti.clear();
    }

    @Override
    public List<Paziente> getAllPazienti() {
        return new ArrayList<>(pazienti.values());
    }

    @Override
    public Paziente getPazienteByCF(String cf) {
        return pazienti.get(cf);
    }
}
