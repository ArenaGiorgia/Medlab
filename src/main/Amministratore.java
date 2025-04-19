package main;

import java.util.ArrayList;
import java.util.List;

public class Amministratore implements RecensioneObserver {
    private String codiceFiscale;
    private String password;
    private List<Recensione> recensioniNonLette;

public Amministratore(){
    this.codiceFiscale ="a"; //settato al codice fiscale di alessio
    this.password = "a"; //password setta di default da me
    this.recensioniNonLette= new ArrayList<>();
}

    public String getCodiceFiscale() {
        return codiceFiscale;
    }
    public boolean verificaPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public void update(Recensione recensione) {
        recensioniNonLette.add(recensione);
        System.out.println(
                "[NOTIFICA AMMINISTRATORE] Nuova recensione da " +
                        recensione.getPaziente().getNome() + " " +
                        recensione.getPaziente().getCognome()
        );
    }
    // Metodo per visualizzare le recensioni non lette
    public void visualizzaRecensioniNonLette() {
        if (recensioniNonLette.isEmpty()) {
            System.out.println("Nessuna nuova recensione.");
            return;
        }

        System.out.println("📩 RECENSIONI NON LETTE:");
        recensioniNonLette.forEach(r -> {
            System.out.println("──────");
            System.out.println(r);
        });
        recensioniNonLette.clear(); // Resetta la lista dopo la visualizzazione
    }

}
