package test;

import main.Amministratore;
import main.Paziente;
import main.Recensione;
import main.Sede;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Observable;

import static org.junit.jupiter.api.Assertions.*;

public class AmministratoreTest {
    @Test
    @DisplayName("Test costruttore Amministratore")
    void testCostruttoreAmministratore() {
        Amministratore amministratore = new Amministratore();
        assertEquals("a", amministratore.getCodiceFiscale(), "Il codice fiscale dovrebbe essere 'a'");
        assertTrue(amministratore.verificaPassword("a"), "La password predefinita dovrebbe essere corretta");
    }
    @Test
    @DisplayName("Test verifica password")
    void testVerificaPassword() {
        Amministratore amministratore = new Amministratore();
        assertTrue(amministratore.verificaPassword("a"), "La password dovrebbe essere corretta");
        assertFalse(amministratore.verificaPassword("wrongPassword"), "La password dovrebbe essere errata");
    }

    @Test
    @DisplayName("Verifica Update")
    void testUpdate() {

        Amministratore amministratore=new Amministratore();
        Paziente paziente = new Paziente("Mario", "Rossi", LocalDate.of(1980, 1, 1),"CF123","M",true);
        Sede sede = new Sede(01, "Catania");
        Recensione recensione = new Recensione(paziente, sede, 5, "Ottimo servizio");

        amministratore.update(new Observable(), recensione);

        assertFalse(amministratore.getRecensioniNonLette().isEmpty());
        assertEquals(1, amministratore.getRecensioniNonLette().size());
        assertEquals(recensione, amministratore.getRecensioniNonLette().get(0));
    }
    @Test
    @DisplayName("Test visualizza recensioni non lette")
    void testVisualizzaRecensioniNonLette() {
        Amministratore amministratore = new Amministratore();
        Paziente paziente = new Paziente("Mario", "Rossi", LocalDate.of(1980, 1, 1),"CF123","M",true);
        Sede sede = new Sede(01, "Catania");
        Recensione recensione = new Recensione(paziente, sede, 5, "Ottimo servizio");

        amministratore.aggiungiRecensioneNonLetta(recensione);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        amministratore.visualizzaRecensioniNonLette();

        String output = outputStream.toString();
        assertTrue(output.contains("=== RECENSIONI==="), "Dovrebbe mostrare l'intestazione");
        assertTrue(output.contains("Ottimo servizio"), "Dovrebbe mostrare la recensione");


        List<Recensione> recensioniNonLette = amministratore.getRecensioniNonLette();
        assertTrue(recensioniNonLette.get(0).toString().contains("[VISTA]"), "La recensione dovrebbe essere marcata come letta");

    }

    @Test
    @DisplayName("Test aggiungi recensione non letta")
    void testAggiungiRecensioneNonLetta() {
        Amministratore amministratore = new Amministratore();
        Paziente paziente = new Paziente("Mario", "Rossi", LocalDate.of(1980, 1, 1),"CF123","M",true);
        Sede sede = new Sede(01, "Catania");
        Recensione recensione = new Recensione(paziente, sede, 5, "Ottimo servizio");

        amministratore.aggiungiRecensioneNonLetta(recensione);

        List<Recensione> recensioniNonLette = amministratore.getRecensioniNonLette();
        assertEquals(1, recensioniNonLette.size(), "La recensione dovrebbe essere aggiunta");
        assertEquals(recensione, recensioniNonLette.get(0), "La recensione non è correttamente aggiunta");

        amministratore.aggiungiRecensioneNonLetta(recensione);

        assertEquals(1, recensioniNonLette.size(), "La recensione non dovrebbe essere aggiunta più di una volta");
    }


}
