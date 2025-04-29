package test;

import main.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;


import static org.junit.jupiter.api.Assertions.*;

class PazienteTest {

    private Paziente paziente;
    private Sede sede;
    private PersonaleLaboratorio personale;


    @BeforeEach
    void setUp() {
        paziente = new Paziente("Mario", "Rossi", LocalDate.of(1980, 1, 1),"CF123","M",true);
        sede=new Sede(0,"Misterbianco");
    personale= new PersonaleLaboratorio("CF66","Pino","Punzo",sede);
    }

    @Nested
    @DisplayName("Test gestione password")
    class PasswordTest {

        @Test
        @DisplayName("Password inizializzata correttamente al CF")
        void testPasswordIniziale() {
            assertEquals("CF123", paziente.getPassword());
        }

        @Test
        @DisplayName("Modifica password con successo")
        void testSetPassword() {
            String nuovaPassword = "nuovaPassword123";
            paziente.setPassword(nuovaPassword);
            assertEquals(nuovaPassword, paziente.getPassword());
        }

        @Test
        @DisplayName("Verifica password corretta")
        void testVerificaPasswordCorretta() {
            assertTrue(paziente.verificaPassword(paziente.getPassword()));
        }

        @Test
        @DisplayName("Verifica password errata")
        void testVerificaPasswordErrata() {
            assertFalse(paziente.verificaPassword("passwordErrata"));
        }
    }

    @Nested
    @DisplayName("Test gestione prenotazioni e referti")
    class PrenotazioniERefertiTest {

        @Test
        @DisplayName("Aggiunta prenotazione")
        void testAggiuntaPrenotazione() {
            Esame esame=new Esame(LocalDate.now(), LocalTime.now(),"Esame Urine");
            Prenotazione prenotazione = new Prenotazione(esame,paziente);
            String codicePrenotazione = "PREN123";
            paziente.getPrenotazioniPaziente().put(codicePrenotazione, prenotazione);

            assertTrue(paziente.getPrenotazioniPaziente().containsKey(codicePrenotazione));
            assertEquals(prenotazione, paziente.getPrenotazioniPaziente().get(codicePrenotazione));
        }

        @Test // non mi convince  -----
        @DisplayName("Visualizzazione referti con referti presenti")
        void testVisualizzaRefertiConReferti() {
            // Setup semplice: creo esame, prenotazione, referto
            Esame esame = new Esame(LocalDate.now(), LocalTime.now(), "Esame Urine");
            Prenotazione prenotazione = new Prenotazione(esame, paziente);
            Referto referto = new Referto("REF123", LocalDate.now());

            // Associo referto alla prenotazione
            prenotazione.setReferto(referto);

            // Aggiungo la prenotazione e il referto al paziente
            paziente.getPrenotazioniPaziente().put("PREN123", prenotazione);
            paziente.getRefertiCorrenti().put("REF123", referto);

            // Verifica: il metodo deve semplicemente funzionare senza lanciare errori
            assertDoesNotThrow(() -> paziente.visualizzaRefertiAssociatiEsami());
        }


        @Test
        @DisplayName("Stampa prenotazioni attive con prenotazioni")
        void testStampaPrenotazioniAttiveConPrenotazioni() {
            // Setup
            Esame esame=new Esame(LocalDate.now(), LocalTime.now(),"Esame Urine");
            Prenotazione prenotazione = new Prenotazione(esame,paziente);
            prenotazione.getEsame().prenotato();
            paziente.getPrenotazioniPaziente().put("PREN123", prenotazione);

            // Test
            assertDoesNotThrow(() -> paziente.stampaPrenotazioniAttive());
        }

        @Test
        @DisplayName("Stampa prenotazioni attive senza prenotazioni")
        void testStampaPrenotazioniAttiveSenzaPrenotazioni() {
            assertDoesNotThrow(() -> paziente.stampaPrenotazioniAttive());
        }
    }


}