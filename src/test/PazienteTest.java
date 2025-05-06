package test;

import main.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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


        @Test
        @DisplayName("Visualizzazione referti con referti presenti")
        void testVisualizzaReferti() {

            Esame esame = new Esame(LocalDate.now(), LocalTime.now(), "Esame Urine");
            Prenotazione prenotazione = new Prenotazione(esame, paziente);
            Referto referto = new Referto("REF123", LocalDate.now());
            referto.setReferto("Tutto ok");

            prenotazione.setReferto(referto);

            paziente.getPrenotazioniPaziente().put("PREN123", prenotazione);
            paziente.getRefertiCorrenti().put("REF123", referto);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            System.setOut(printStream);

            try {

                paziente.visualizzaRefertiAssociatiEsami();


                String output = outputStream.toString();
                assertTrue(output.contains("Referti associati agli esami per il paziente"), "Dovrebbe mostrare l'intestazione");
                assertTrue(output.contains("Esame: Esame Urine"), "Dovrebbe mostrare il nome dell'esame");
                assertTrue(output.contains("Referto Id: REF123"), "Dovrebbe mostrare l'ID del referto");
                assertTrue(output.contains("Risultato: Tutto ok"), "Dovrebbe mostrare il risultato del referto");

            } finally {
                // Ripristina l'output originale
                System.setOut(System.out);
            }
        }




        @Test
        @DisplayName("Stampa prenotazioni attive con prenotazioni")
        void testVisualizzaPrenotazioniAttive() {

            Esame esame=new Esame(LocalDate.now(), LocalTime.now(),"Esame Urine");
            Prenotazione prenotazione = new Prenotazione(esame,paziente);
            prenotazione.getEsame().prenotato();
            paziente.getPrenotazioniPaziente().put("PREN123", prenotazione);

            assertDoesNotThrow(() -> paziente.stampaPrenotazioniAttive());
        }

        @Test
        @DisplayName("Stampa prenotazioni attive senza prenotazioni")
        void testStampaPrenotazioniAttiveSenzaPrenotazioni() {
            assertDoesNotThrow(() -> paziente.stampaPrenotazioniAttive());
        }
    }


}