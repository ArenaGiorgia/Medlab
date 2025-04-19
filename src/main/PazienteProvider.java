import java.util.List;

public interface PazienteProvider {
    Paziente getPazienteByCF(String cf);
    List<Paziente> getAllPazienti();
}
