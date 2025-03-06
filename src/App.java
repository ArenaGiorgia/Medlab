public class App {
    public static void main(String[] args) {
        Medlab sistema= Medlab.getInstance();

        sistema.aggiungiPaziente();
        sistema.confermaPaziente();
        sistema.visualizzaPazienti();
    }

}
