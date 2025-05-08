package test;

import main.Paziente;
import main.PazienteProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PazienteProviderMock implements PazienteProvider { // Classe mock per PazienteProvider per isolare i test
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