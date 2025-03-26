import java.util.Comparator;
import java.util.Map;

public class EsameComparator implements Comparator<String> {
    private final Map<String, Esame> esamiMap;

    public EsameComparator(Map<String, Esame> esamiMap) {
        this.esamiMap = esamiMap;
    }

    @Override
    public int compare(String codice1, String codice2) {
        Esame e1 = esamiMap.get(codice1);
        Esame e2 = esamiMap.get(codice2);

        if (e1 == null || e2 == null) return 0;  // Evita errori su chiavi non valide

        int nomeCompare = e1.getNome().compareTo(e2.getNome());
        if (nomeCompare != 0) return nomeCompare;

        int dataCompare = e1.getData().compareTo(e2.getData());
        if (dataCompare != 0) return dataCompare;

        return e1.getOrario().compareTo(e2.getOrario());
    }
}