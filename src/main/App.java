package main;

import java.util.Scanner;
public class App {
    public static void main(String[] args) {
        Medlab sistema = Medlab.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== MEDLAB =====");
            System.out.println("1. Accedi");
            System.out.println("2. Esci");
            System.out.print("Scegli un'opzione: ");

            int scelta = 0;

            while (scelta < 1 || scelta > 2) {
                try {
                    scelta = Integer.parseInt(scanner.nextLine());
                    if (scelta < 1 || scelta > 2) {
                        System.out.println("Opzione non valida. Inserisci un numero tra 1 e 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore: Devi inserire un numero intero!");
                    scelta = 0;
                }
            }

            if (scelta == 2) {
                System.out.println("Chiusura del programma");
                System.exit(0);
            }

            System.out.print("Inserisci il codice fiscale: ");
            String codiceFiscale = scanner.nextLine();
            System.out.print("Inserisci la password: ");
            String password = scanner.nextLine();
            String ruolo = sistema.VerificaAccesso(codiceFiscale, password);

            if (ruolo.equals("amministratore")) {
                System.out.println("Accesso amministratore effettuato!");

                while (true) {
                    System.out.println("\n===== MENU AMMINISTRATORE =====");
                    System.out.println("1. Aggiungi paziente");
                    System.out.println("2. Modifica paziente");
                    System.out.println("3. Elimina paziente");
                    System.out.println("4. Inserimento nuova sede");
                    System.out.println("5. Modifica sede");
                    System.out.println("6. Elimina sede");
                    System.out.println("7. Gestisci esami");
                    System.out.println("8. Gestisci personale laboratorio");
                    System.out.println("9. Generazione report");
                    System.out.println("10. Vedi recensioni");
                    System.out.println("11. Logout");
                    System.out.print("Scegli un'opzione: ");

                    scelta = 0;
                    while (scelta < 1 || scelta > 11) {
                        try {
                            scelta = Integer.parseInt(scanner.nextLine());
                            if (scelta < 1 || scelta > 11) {
                                System.out.println("Opzione non valida. Inserisci un numero tra 1 e 10.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Errore: Devi inserire un numero intero!");
                            scelta = 0;
                        }
                    }

                    if (scelta == 11) {
                        System.out.println("Logout amministratore...");
                        sistema.logout();
                        break;
                    }

                    switch (scelta) {
                        case 1:
                            sistema.aggiungiPaziente();
                            break;
                        case 2:
                            sistema.modificaPazienteAmministratore();
                            break;
                        case 3:
                            sistema.eliminaPaziente();
                            break;
                        case 4:
                            sistema.aggiungiSede();
                            break;
                        case 5:
                            sistema.modificaSedeAmministratore();
                            break;
                        case 6:
                            sistema.eliminaSede();
                            break;
                        case 7:
                            System.out.println("Gestione esami...");
                            sistema.aggiungiNuovoEsame();
                            break;
                        case 8:
                            System.out.println("Gestione personale laboratorio...");
                            sistema.aggiungiPersonale();
                            break;
                        case 9:
                            System.out.println("Generazione report...");
                            break;
                        case 10:

                            sistema.getAmministratore().visualizzaRecensioniNonLette();
                            System.out.println("TUTTE LE RECENSIONI..");
                            sistema.visualizzaRecensioni();

                            break;
                    }
                }
            } else if (ruolo.equals("paziente")) {
                System.out.println("Accesso paziente effettuato!");

                while (true) {
                    System.out.println("\n===== MENU PAZIENTE =====");
                    System.out.println("1. Registrazione nuova sede");
                    System.out.println("2. main.Prenotazione esame");
                    System.out.println("3. Visualizza referto");
                    System.out.println("4. Modifica dati personali");
                    System.out.println("5. Inserisci recensione");
                    System.out.println("6. Visualizza prenotazioni");
                    System.out.println("7. Logout");
                    System.out.print("Scegli un'opzione: ");

                    scelta = 0;
                    while (scelta < 1 || scelta > 7) {
                        try {
                            scelta = Integer.parseInt(scanner.nextLine());
                            if (scelta < 1 || scelta > 7) {
                                System.out.println("Opzione non valida. Inserisci un numero tra 1 e 7.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Errore: Devi inserire un numero intero!");
                            scelta = 0;
                        }
                    }
                    if (scelta == 7) {
                        System.out.println("Logout paziente...");
                        sistema.logout();
                        break;
                    }

                    switch (scelta) {
                        case 1:
                            sistema.RegistrazioneSede();
                            break;
                        case 2:
                            sistema.PrenotazioneEsame();
                            break;
                        case 3:
                            System.out.println("Visualizzazione referto...");
                            break;
                        case 4:
                            System.out.println("Modifica dati personali...");
                            break;
                        case 5:
                            System.out.println("Inserisci recensione...");
                            sistema.lasciaRecensione();
                            break;
                        case 6:
                            //  sistema.visualizzaPrenotazioniPaziente();
                            break;
                    }
                }
            } else if (ruolo.equals("personale")) {
                 System.out.println("Accesso personale di laboratorio effettuato!");

                    while (true) {
                        System.out.println("\n===== MENU PERSONALE LABORATORIO =====");
                        System.out.println("1. Aggiungi nuovo referto");
                        System.out.println("2. Modifica referto");
                        System.out.println("3. Elimina referto");
                        System.out.println("4. Visualizza pazienti prenotati di un esame");
                        System.out.println("5. Logout");
                        System.out.print("Scegli un'opzione: ");

                        scelta = 0;
                        while (scelta < 1 || scelta > 5) {
                            try {
                                scelta = Integer.parseInt(scanner.nextLine());
                                if (scelta < 1 || scelta > 5) {
                                    System.out.println("Opzione non valida. Inserisci un numero tra 1 e 5.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Errore: Devi inserire un numero intero!");
                                scelta = 0;
                            }
                        }
                        if (scelta == 5) {
                            System.out.println("Logout paziente...");
                            sistema.logout();
                            break;
                        }

                        switch (scelta) {
                            case 1: sistema.aggiungiReferto();
                                break;

                            case 2: sistema.aggiornaReferto();
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                        }
                    }
            } else System.out.println("Errore: Credenziali errate");
        }
    }
}
