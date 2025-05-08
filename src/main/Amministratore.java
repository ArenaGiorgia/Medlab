package main;

import java.util.*;


public class
Amministratore implements Observer {
    private  String codiceFiscale;
    private  String password;
    private final List<Recensione> recensioniNonLette ;
    private boolean notified;


    public Amministratore(){
    this.codiceFiscale ="a";
    this.password = "a"; //password settata di default
    this.recensioniNonLette= new ArrayList<>();
}

    public String getCodiceFiscale() {
        return codiceFiscale;
    }
    public List<Recensione> getRecensioniNonLette() {
        return recensioniNonLette;
    }
    public boolean verificaPassword(String password) {
        return this.password.equals(password);
    }
    public boolean isNotified() {
        return notified;
    }

    public void update(Observable o, Object arg) {

        if (arg instanceof Recensione) {
            Recensione recensione = (Recensione) arg;
            recensioniNonLette.add(recensione);
            notified=true;
            System.out.println(
                    "[NOTIFICA AMMINISTRATORE] Nuova recensione da " +
                            recensione.getPaziente().getNome() + " " +
                            recensione.getPaziente().getCognome()
            );
        }
    }
    public void aggiungiRecensioneNonLetta(Recensione recensione) {
        if (!recensioniNonLette.contains(recensione)) {
            recensioniNonLette.add(recensione);
        }
    }
    public void visualizzaRecensioniNonLette() {
        if (recensioniNonLette.isEmpty()) {
            System.out.println("Nessuna nuova recensione.");
            return;
        }
        System.out.println("=== RECENSIONI===");
        recensioniNonLette.forEach(r -> {
            System.out.println("──────");
            System.out.println(r);
            r.marcaComeLetta();
        });


    }

}


