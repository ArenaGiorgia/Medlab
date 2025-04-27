package test;

import main.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@DisplayName("Test Suite per Medlab - Controller principale")
class MedlabTest {

    private Medlab medlab;
    private Paziente pazienteTest;
    private Sede sedeTest;

    @BeforeAll
    static void initAll() {
        System.out.println("Inizializzazione test suite Medlab");
    }

    @BeforeEach
    void init() {
        medlab = Medlab.getInstance();
        // Reset dello stato per isolamento test
        medlab.setPazienti(new HashMap<>());
        medlab.setPrenotazioni(new HashMap<>());
<<<<<<< HEAD
        medlab.setSedi(new ArrayList<>());
        // Configurazione fixture di test
        pazienteTest = new Paziente("Mario", "Rossi", LocalDate.of(1990, 5, 15), "MRORSS90E15F205A", "M", false);
        sedeTest = new Sede(1, "Policlinico Catania");
        medlab.getPazienti().put(pazienteTest.getCf(), pazienteTest);
        medlab.getSedi().add(sedeTest);
=======
        medlab.setPersonaleLaboratori(new HashMap<>());
>>>>>>> 277cb068dd8ea06584fccb57eea03a370f00e3aa
    }

    @AfterEach
    void tearDown() { //Pulizia dopo ogni test
        medlab.setPazienteCorrente(null);
        medlab.setSedeCorrente(null);
    }

    @AfterAll //Pulizia dopo ogni test
    static void tearDownAll() {
        System.out.println("Test suite Medlab completata");
    }

    // TEST SUITE - GESTIONE PAZIENTI
    @Nested
    // permette di raggruppare i test dentro classi interne (classi annidate) per organizzare meglio il file di test.
    @DisplayName("Test per gestione pazienti")
    class GestionePazientiTest {
        @Test
        @DisplayName("TC1 - Creazione nuovo paziente")
        void testNuovoPaziente() {
            String cf = "RSSGNN80A01H501P";
            medlab.nuovoPaziente("Giovanni", "Russo", LocalDate.of(1980, 1, 1), cf, "M", true);
            assertAll("Verifica attributi paziente",
                    () -> assertNotNull(medlab.getPazienteCorrente(), "Paziente corrente non impostato"),
                    () -> assertEquals(cf, medlab.getPazienteCorrente().getCf(), "Codice fiscale non corrisponde"),
                    () -> assertTrue(medlab.getPazienteCorrente().isCronico(), "Stato malato cronico errato")
            );
        }

        @Test
        @DisplayName("TC2 - Conferma paziente nella mappa")
        void testConfermaPaziente() {
            medlab.nuovoPaziente("Luigi", "Verdi", LocalDate.now(), "VRDLGU00L17G478F", "M", false);
            medlab.confermaPaziente();
            assertAll("Verifica stato dopo conferma",
                    () -> assertNull(medlab.getPazienteCorrente(), "Paziente corrente dovrebbe essere null"),
                    () -> assertTrue(medlab.getPazienti().containsKey("VRDLGU00L17G478F"), "Paziente non presente nella mappa")
            );
        }

        @Test
        @DisplayName("TC3 - Selezione paziente esistente")
        void testSelezionaPazienteEsistente() {
            Paziente risultato = medlab.selezionaPaziente(pazienteTest.getCf());
            assertAll("Verifica selezione paziente",
                    () -> assertNotNull(risultato, "Paziente non trovato"),
                    () -> assertEquals(pazienteTest, medlab.getPazienteCorrente(), "Paziente corrente non corrisponde")
            );
        }
    }

    // TEST SUITE - GESTIONE SEDI
    @Nested
    @DisplayName("TC4- Test per gestione sedi")
    class GestioneSediTest {
        @Test
        @DisplayName("TC4 - Creazione nuova sede")
        void testNuovaSede() {
            medlab.nuovaSede("Ospedale Garibaldi", 2);
            assertAll("Verifica attributi sede",
                    () -> assertNotNull(medlab.getSedeCorrente(), "Sede corrente non impostata"),
                    () -> assertEquals(2, medlab.getSedeCorrente().getCodice(), "Codice sede non corrisponde"),
                    () -> assertEquals("Ospedale Garibaldi", medlab.getSedeCorrente().getNome(), "Nome sede non corrisponde")
            );
        }

        @Test
        @DisplayName("TC5 - Conferma sede nella lista")
        void testConfermaSede() {
            medlab.nuovaSede("Ospedale Cannizzaro", 3);
            medlab.confermaSede();
            assertAll("Verifica stato dopo conferma",
                    () -> assertNull(medlab.getSedeCorrente(), "Sede corrente dovrebbe essere null"),
                    () -> assertEquals(2, medlab.getSedi().size(), "Numero sedi non corrisponde"),
                    () -> assertTrue(medlab.getSedi().stream().anyMatch(s -> s.getCodice() == 3), "Sede non presente nella lista")
            );
        }
    }

    @Test // TEST SUITE -PRENOTAZIONI
    @DisplayName("TC5 - Verifica prenotazioni massime giornaliere")
    void testPrenotazioniMaxPerGiorno() {
        medlab.setPazienteCorrente(pazienteTest);
        LocalDate dataTest = LocalDate.now().plusDays(1);
        // Creiamo 2 prenotazioni per lo stesso giorno
        Esame esame1 = new Esame(dataTest, LocalTime.of(9, 0), "Emocromo");
        Esame esame2 = new Esame(dataTest, LocalTime.of(11, 0), "Glicemia");
        Prenotazione p1 = new Prenotazione(esame1, pazienteTest);
        Prenotazione p2 = new Prenotazione(esame2, pazienteTest);
        pazienteTest.getPrenotazioni().put(p1.getCodice(), p1);
        pazienteTest.getPrenotazioni().put(p2.getCodice(), p2);

        assertTrue(medlab.PrenotazioniMaxPerGiorno(pazienteTest, dataTest),
                "Dovrebbe permettere terza prenotazione");
    }

    @Test // TC- ECCEZIONE CF DUPLICATO
    @DisplayName("TC6 - Verifica eccezione per CF duplicato")
    void testCodiceFiscaleDuplicato() {
        String cfEsistente = pazienteTest.getCf();
        assertThrows(IllegalArgumentException.class, () -> {
            medlab.nuovoPaziente("Nome", "Cognome", LocalDate.now(), cfEsistente, "M", false);
        }, "Dovrebbe lanciare eccezione per CF duplicato");
    }

    @Test //TEST CASE PER L'AUTENTIFICAZIONE
    @DisplayName("TC7 - Verifica autenticazione paziente")
    void testVerificaAccessoPaziente() {
        pazienteTest.setPassword("password123");
        String risultato = medlab.VerificaAccesso(pazienteTest.getCf(), "password123");
        assertEquals("paziente", risultato, "Tipo utente non corrisponde");
        assertEquals(pazienteTest, medlab.getPazienteCorrente(), "Paziente corrente non impostato");
    }

    @Test
    @DisplayName("TC8 - Verifica credenziali errate")
    void testVerificaAccessoFallito() {
        String risultato = medlab.VerificaAccesso("CFINESISTENTE", "password");
        assertEquals("credenziali errate", risultato, "Dovrebbe restituire credenziali errate");
        assertNull(medlab.getPazienteCorrente(), "Paziente corrente dovrebbe rimanere null");
    }

    @Test
    @DisplayName("TC9 - Verifica stato iniziale Medlab")
    void testStatoIniziale() {
        // Verify
        assertAll("Verifica stato iniziale",
                () -> assertNotNull(medlab.getAmministratore(), "Amministratore non inizializzato"),
                () -> assertTrue(medlab.getPazienti().containsKey(pazienteTest.getCf()), "Paziente test non presente"),
                () -> assertEquals(1, medlab.getSedi().size(), "Numero sedi iniziale errato")
        );
    }

    @Nested
    @DisplayName("TC10- Test per prenotazione esami")
    class PrenotazioneEsamiTest {
        @Test
        @DisplayName("TC11 - Prenotazione esame con successo")
        void testPrenotazioneEsameSuccesso() {
            medlab.setPazienteCorrente(pazienteTest);
            pazienteTest.getSedi().add(sedeTest);
            Esame esame = new Esame(LocalDate.now().plusDays(1), LocalTime.of(10, 0), "Esame Urine");
            sedeTest.getEsami().put(esame.getCodice(), esame);
            medlab.SelezionaEsame(esame);
            medlab.ConfermaEsame();
            assertAll("Verifica prenotazione",
                    () -> assertEquals(1, medlab.getPrenotazioni().size(), "Dovrebbe esserci 1 prenotazione"),
                    () -> assertEquals(1, pazienteTest.getPrenotazioni().size(), "Paziente dovrebbe avere 1 prenotazione"),
                    () -> assertTrue(esame.isPrenotato(), "Esame dovrebbe essere segnato come prenotato")
            );
        }

        @Test
        @DisplayName("TC12 - Tentativo prenotazione esame già prenotato")
        void testPrenotazioneEsameGiaPrenotato() {
            medlab.setPazienteCorrente(pazienteTest);
            pazienteTest.getSedi().add(sedeTest);
            Esame esame = new Esame(LocalDate.now().plusDays(1), LocalTime.of(10, 0), "Esame Urine");
            esame.prenotato();
            sedeTest.getEsami().put(esame.getCodice(), esame);
            assertThrows(IllegalStateException.class, () -> {
                medlab.SelezionaEsame(esame);
            }, "Dovrebbe lanciare eccezione per esame già prenotato");
        }

        @Test
        @DisplayName("TC13 - Annullamento prenotazione esame")
        void testAnnullamentoPrenotazione() {
            medlab.setPazienteCorrente(pazienteTest);
            pazienteTest.getSedi().add(sedeTest);
            Esame esame = new Esame(LocalDate.now().plusDays(1), LocalTime.of(10, 0), "Esame Urine");
            sedeTest.getEsami().put(esame.getCodice(), esame);
            medlab.SelezionaEsame(esame);
            medlab.ConfermaEsame();
            esame.annullaPrenotazione();
            assertAll("Verifica annullamento",
                    () -> assertFalse(esame.isPrenotato(), "Esame dovrebbe essere segnato come non prenotato"),
                    () -> assertEquals("Libero", esame.statoEsame(), "Stato esame dovrebbe essere 'Libero'")
            );
        }

        @Test
        @DisplayName("TC14 - Verifica stato iniziale esame")
        void testStatoInizialeEsame() {
            Esame esame = new Esame(LocalDate.now().plusDays(1), LocalTime.of(10, 0), "Esame Test");
            assertAll("Verifica stato iniziale",
                    () -> assertFalse(esame.isPrenotato(), "Esame dovrebbe essere libero inizialmente"),
                    () -> assertEquals("Libero", esame.statoEsame(), "Stato iniziale dovrebbe essere 'Libero'"),
                    () -> assertNotNull(esame.getCodice(), "Codice esame dovrebbe essere generato"),
                    () -> assertEquals(4, esame.getCodice().length(), "Codice esame dovrebbe essere di 4 caratteri")
            );
        }
    }


    @Nested
    @DisplayName("Test per gestione recensioni")
    class GestioneRecensioniTest {

        @Test
        @DisplayName("TC15 - Creazione recensione valida")
        void testCreaRecensioneValida() {
            // Setup
            medlab.setPazienteCorrente(pazienteTest);
            Prenotazione prenotazione = new Prenotazione(
                    new Esame(LocalDate.now(), LocalTime.now(), "Esame"),
                    pazienteTest
            );
            prenotazione.setStato(new StatoCompletato(prenotazione));
            pazienteTest.getPrenotazioni().put(prenotazione.getCodice(), prenotazione);
            pazienteTest.getSedi().add(sedeTest);

            // Exercise
            Recensione recensione = medlab.creaRecensione(pazienteTest, sedeTest);

            // Verify
            assertAll("Verifica recensione",
                    () -> assertNotNull(recensione, "Recensione non creata"),
                    () -> assertEquals(pazienteTest, recensione.getPaziente(), "Paziente non corrisponde"),
                    () -> assertEquals(sedeTest, recensione.getSede(), "Sede non corrisponde")
            );
        }

        @Test
        @DisplayName("TC16 - Tentativo recensione senza prenotazioni completate")
        void testCreaRecensioneSenzaPrenotazioni() {
            // Setup
            medlab.setPazienteCorrente(pazienteTest);

            // Exercise & Verify
            assertThrows(IllegalStateException.class, () -> {
                medlab.creaRecensione(pazienteTest, sedeTest);
            }, "Dovrebbe lanciare eccezione senza prenotazioni completate");
        }
    }


    @Nested
    @DisplayName("Test per generazione report")
    class GenerazioneReportTest {

        @Test
        @DisplayName("TC17 - Generazione report mensile")
        void testGeneraReportMensile() {
            // Setup
            Prenotazione p1 = new Prenotazione(new Esame(LocalDate.now(), LocalTime.now(), "Esame1"), pazienteTest);
            Prenotazione p2 = new Prenotazione(new Esame(LocalDate.now(), LocalTime.now(), "Esame2"), pazienteTest);
            medlab.getPrenotazioni().put(p1.getCodice(), p1);
            medlab.getPrenotazioni().put(p2.getCodice(), p2);

            // Exercise
            medlab.creaReport("mensile");

            // Verify
            assertAll("Verifica report",
                    () -> assertNotNull(medlab.getReportCorrente(), "Report non generato"),
                    () -> assertTrue(medlab.getReportCorrente() instanceof ReportMensile, "Tipo report errato")
            );
        }
    }


}