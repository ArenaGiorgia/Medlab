package test;

import main.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@DisplayName("Test Suite per Medlab - Controller principale")
class MedlabTest {

    private Medlab medlab;
    private Paziente pazienteTest;
    private Sede sedeTest;
    private Amministratore amministratoreTest;

    @BeforeAll
    static void initAll() {
        System.out.println("Inizializzazione test Medlab");
    }

    @BeforeEach
    void testInit() {
        medlab = Medlab.getInstance();
        medlab.setPazienti(new HashMap<>());
        medlab.setPrenotazioni(new HashMap<>());
        medlab.setSedi(new ArrayList<>());
        pazienteTest = new Paziente("Mario", "Rossi", LocalDate.of(1990, 5, 15), "MRORSS90E15F205A", "M", false);
        sedeTest = new Sede(1, "Policlinico Catania");
        medlab.getPazienti().put(pazienteTest.getCf(), pazienteTest);
        medlab.getSedi().add(sedeTest);
        amministratoreTest=new Amministratore();
        medlab.setPazienteCorrente(pazienteTest);
        medlab.setPersonaleLaboratori(new HashMap<>());

    }

    @AfterEach
    void tearDown() {
        medlab.setPazienteCorrente(null);
        medlab.setSedeCorrente(null);
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Test suite Medlab completata");
    }
 //UC1
    @Nested // permette di raggruppare i test dentro classi interne (classi annidate) per organizzare meglio il file di test.
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


    @Test
    @DisplayName("Test registrazione sede con input valido")
    void testRegistrazioneSedeValida() {

        String inputSimulato = "1\n";
        InputStream inputStream = new ByteArrayInputStream(inputSimulato.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        medlab.RegistrazioneSede();

        assertTrue(pazienteTest.getSedi().contains(sedeTest), "La sede dovrebbe essere associata al paziente");

        String output = outputStream.toString();
        assertTrue(output.contains("assegnata a: Mario Rossi"), "L'output dovrebbe confermare l'assegnazione");
    }

    @Test // TEST SUITE -PRENOTAZIONI
    @DisplayName("TC5 - Verifica prenotazioni massime giornaliere")
    void testPrenotazioniMaxPerGiorno() {
        medlab.setPazienteCorrente(pazienteTest);
        LocalDate dataTest = LocalDate.now().plusDays(1);
        Esame esame1 = new Esame(dataTest, LocalTime.of(9, 0), "Emocromo");
        Esame esame2 = new Esame(dataTest, LocalTime.of(11, 0), "Glicemia");
        Esame esame3 = new Esame(dataTest, LocalTime.of(12, 0), "Urine");
        Prenotazione p1 = new Prenotazione(esame1, pazienteTest);
        Prenotazione p2 = new Prenotazione(esame2, pazienteTest);
        Prenotazione p3 = new Prenotazione(esame3, pazienteTest);
        pazienteTest.getPrenotazioni().put(p1.getCodice(), p1);
        pazienteTest.getPrenotazioni().put(p2.getCodice(), p2);
        pazienteTest.getPrenotazioni().put(p3.getCodice(), p3);

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
    @DisplayName("TC9 - Verifica stato iniziale Medlab")
    void testStatoIniziale() {

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
        @DisplayName("TC13 - Verifica stato iniziale esame")
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

// UC9
@Test
@DisplayName("Test visualizzaSediRecensibili ")
void testVisualizzaSediRecensibili() {
    medlab.setPazienteCorrente(pazienteTest);
    Sede sede1 = new Sede(1, "Roma");
    Sede sede2 = new Sede(2, " Milano");

    medlab.confermaSede(sede1);
    medlab.confermaSede(sede2);
    Esame esame1 = new Esame(LocalDate.now(), LocalTime.now(), "Esame sangue");
    sede1.getEsami().put(esame1.getCodice(), esame1);
    esame1.prenotato();
    Prenotazione prenotazione1 = new Prenotazione(esame1, pazienteTest);
    prenotazione1.setStato(new StatoCompletato(prenotazione1));
    Esame esame2 = new Esame(LocalDate.now(), LocalTime.now(), "Esame Urine");
    sede2.getEsami().put(esame2.getCodice(), esame2);
    esame2.prenotato();
    Prenotazione prenotazione2 = new Prenotazione(esame2, pazienteTest);
    prenotazione2.setStato(new StatoCompletato(prenotazione2));
    pazienteTest.getPrenotazioniPaziente().put(prenotazione1.getCodice(),prenotazione1);
    pazienteTest.getPrenotazioniPaziente().put(prenotazione2.getCodice(),prenotazione2);

    List<Sede> risultato = medlab.visualizzaSediRecensibili(pazienteTest);
    assertAll("Verifica sedi recensibili",
            () -> assertEquals(2, risultato.size(), "Dovrebbero esserci 2 sedi recensibili"),
            () -> assertTrue(risultato.contains(sede1), "Dovrebbe contenere sede1"),
            () -> assertTrue(risultato.contains(sede2), "Dovrebbe contenere sede2")
    );
}
   @Test // caso completo in cui viene testato il funzionamento del observer
   @DisplayName("TC17 - Test completo lasciaRecensione ")
   void testLasciaRecensione() {
       medlab.setPazienteCorrente(pazienteTest);
       medlab.addObserver(amministratoreTest);
       Esame esame = new Esame(LocalDate.now(), LocalTime.now(), "Esame sangue");
       esame.prenotato();

       Prenotazione prenotazione = new Prenotazione(esame, pazienteTest);
       prenotazione.setStato(new StatoCompletato(prenotazione));
       pazienteTest.getPrenotazioni().put(prenotazione.getCodice(), prenotazione);
       pazienteTest.getSedi().add(sedeTest);

       String simulatedInput = String.join("\n",
               "1",            // scelta sede
               "5",            // stelle
               "Ottimo servizio"  // commento
       ) + "\n";

       InputStream originalIn = System.in;
       try {
           System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
           Scanner testScanner = new Scanner(System.in);

           medlab.lasciaRecensione(testScanner);

           List<Recensione> recensioni = amministratoreTest.getRecensioniNonLette();
           assertEquals(1, recensioni.size(), "Dovrebbe esserci una recensione");
           Recensione recensione = recensioni.get(0);

           assertAll("Verifica dettagli recensione",
                   () -> assertEquals(pazienteTest, recensione.getPaziente(), "Paziente errato"),
                   () -> assertEquals(sedeTest, recensione.getSede(), "Sede errata"),
                   () -> assertEquals(5, recensione.getValutazione(), "Numero stelle errato"),
                   () -> assertEquals("Ottimo servizio", recensione.getCommento(), "Commento errato")
           );
       } finally {
           System.setIn(originalIn);
       }
   }


    @Nested
    @DisplayName("Test per generazione report")
    class GenerazioneReportTest {

        @Test
        @DisplayName("TC17 - Generazione report mensile")
        void testGeneraReportMensile() {

            Prenotazione p1 = new Prenotazione(new Esame(LocalDate.now(), LocalTime.now(), "Esame1"), pazienteTest);
            Prenotazione p2 = new Prenotazione(new Esame(LocalDate.now(), LocalTime.now(), "Esame2"), pazienteTest);
            medlab.getPrenotazioni().put(p1.getCodice(), p1);
            medlab.getPrenotazioni().put(p2.getCodice(), p2);

            medlab.creaReport("mensile");

            assertAll("Verifica report",
                    () -> assertNotNull(medlab.getReportCorrente(), "Report non generato"),
                    () -> assertTrue(medlab.getReportCorrente() instanceof ReportMensile, "Tipo report errato")
            );
        }
    }



    @Test //UC10
    void testNuovoEsameAggiunto() {
        medlab.setSedeCorrente(sedeTest);
        int esamiIniziali = sedeTest.getEsami().size();
        medlab.nuovoEsame(LocalDate.of(2025, 6, 1), LocalTime.of(10, 0), "Esame Oncologico");
        assertEquals(esamiIniziali+1, sedeTest.getEsami().size());
        assertNull(medlab.getSedeCorrente());
    }
  //UC11
    @Test
    @DisplayName("Test inserimento personale laboratorio")
    void testInserisciPersonaleLab() {
        medlab.inserisciPersonaleLab("CF123", "Pippo", "Punzo", sedeTest);
        PersonaleLaboratorio personale = medlab.getPersonaleLaboratorioCorrente();

        assertAll("Verifica proprietà personale",
                () -> assertEquals("CF123", personale.getCf()),
                () -> assertEquals("Pippo", personale.getNome()),
                () -> assertEquals("Punzo", personale.getCognome()),
                () -> assertEquals(sedeTest, personale.getSede())
        );
    }
    @Test
    @DisplayName("Test conferma personale laboratorio")
    void testConfermaPersonaleLab() {
        medlab.inserisciPersonaleLab("CF123", "Pippo", "Punzo", sedeTest);
        medlab.confermaPersonaleLab();

        assertNull(medlab.getPersonaleLaboratorioCorrente(),
                "Il personale corrente dovrebbe essere null dopo la conferma");
        assertTrue(medlab.getPersonaleLaboratori().containsKey("CF123"),
                "Il personale dovrebbe essere stato aggiunto alla mappa");
    }

}