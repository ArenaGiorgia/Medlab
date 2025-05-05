package test;

import main.Amministratore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmministratoreTest {
    @Test
    @DisplayName("Test costruttore Amministratore")
    void testCostruttoreAmministratore() {
        Amministratore amministratore = new Amministratore();
        assertEquals("a", amministratore.getCodiceFiscale(), "Il codice fiscale dovrebbe essere 'a'");
        assertTrue(amministratore.verificaPassword("a"), "La password predefinita dovrebbe essere corretta");
    }

}
