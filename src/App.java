import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Medlab sistema= Medlab.getInstance();

        Scanner scanner = new Scanner(System.in);
        /* sistema.visualizzaPazienti(); */

        while (true) {
            System.out.println("\n===== MEDLAB =====");
            System.out.print("Inserisci il codice fiscale: ");
            String codice = scanner.nextLine();
            System.out.print("Inserisci la password: ");
            String password = scanner.nextLine();

            // Verifica delle credenziali di MedLab in base al ruolo
            String ruolo = sistema.VerificaAccesso(codice, password);

            switch (ruolo) {
                case "amministratore":

                    System.out.println("Accesso amministratore: " + sistema.getAmministratore() + " effettuato! ");
                    menuAmministratore(scanner, sistema);
                    break;
                case "paziente":
                    System.out.println("Accesso paziente effettuato!");
                    menuPaziente(scanner,sistema);
                    break;

              /*  case "personale":
                    System.out.println("Accesso personale di laboratorio effettuato!");
                    menuPersonale(scanner);
                    break; */

                default:
                    System.out.println("Credenziali errate. Riprova.");
            }

        }
    }
    private static void menuAmministratore(Scanner scanner, Medlab sistema) {
        int scelta;
        do {
            System.out.println("\n===== MENU AMMINISTRATORE =====");
            System.out.println("1. Gestisci pazienti");
            System.out.println("2. Gestisci sedi");
            System.out.println("3. Gestisci esami");
            System.out.println("4. Gestisci personale laboratorio");
            System.out.println("5. Generazione report");
            System.out.println("6. Logout");
            System.out.print("Scegli un'opzione: ");
            scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    sistema.aggiungiPaziente();

                    break;
                case 2:
                    sistema.aggiungiSede();

                    break;
                case 3:
                    System.out.println("esami");
                    break;
                case 4:
                    System.out.println("Personale laboratorio");
                    break;
                case 5:
                    System.out.println("Report");
                    break;
                case 6:
                    System.out.println("Logout effettuato");
                   return;

                default:
                    System.out.println("Opzione non valida.");
            }
        } while (true);
    }

    // Menu Paziente
    private static void menuPaziente(Scanner scanner, Medlab sistema) {
        int scelta;
        do {
            System.out.println("\n===== MENU PAZIENTE =====");
            System.out.println("1. Registrazione sede laboratorio");
            System.out.println("2. Prenotazione esame");
            System.out.println("3. Visualizza il proprio referto");
            System.out.println("4. Modifica i dati personali");
            System.out.println("5. Inserisci recensione");
            System.out.println("6. Visualizza prenotazioni attive");
            System.out.println("7. Logout");
            System.out.print("Scegli un'opzione: ");
            scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:

                    sistema.visualizzaSedi();
                    sistema.selezionaSedePerPaziente();
                    sistema.toString();
                    //  System.out.println("Registrato");
                    break;
                case 2:
                    System.out.println("Prenotato");
                    break;
                case 3:
                    System.out.println("Referto");
                    break;
                case 4:
                    System.out.println("dati");
                    break;
                case 5:
                    System.out.println("recensione");
                    break;
                case 6:
                    System.out.println("prenotazioni attive");
                    break;
                case 7:
                    System.out.println("Logout effettuato");

                   return;
                default:
                    System.out.println("Opzione non valida.");
            }
        } while (true);
    }

    // Menu Personale di Laboratorio
   /* private static void menuPersonale(Scanner scanner){
        int scelta;
        do {
            System.out.println("\n===== MENU PERSONALE DI LABORATORIO =====");
            System.out.println("1. Registra nuovi esami");
            System.out.println("2. Consulta report esami");
            System.out.println("3. Logout");
            System.out.print("Scegli un'opzione: ");
            scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    System.out.println("Registrazione nuovi esami");
                    break;
                case 2:
                    System.out.println("Consultazione report esami");
                    break;
                case 3:
                    System.out.println("Logout effettuato");
                    System.exit(0);
                break;
                default:
                    System.out.println("Opzione non valida.");
            }
        } while (true);
    }  */
}
