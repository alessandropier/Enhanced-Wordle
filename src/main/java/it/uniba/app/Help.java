package it.uniba.app;

/**<<Boundary>>
 * La classe Help stampa a video il menu di aiuto.
*/

public final class Help {

    // Costanti per rendere il codice più pulito (puoi metterle in cima alla classe)
    final static String ANSI_RESET = "\u001B[0m";
    final static String ANSI_BOLD = "\u001B[1m";

    /**Costruttore. */
    private Help() {
    }

    /** Stampa il menu di aiuto. */
    public static void stampaHelp() {
        System.out.print("Il giocatore deve indovinare una parola");
        System.out.print("  di 5 lettere in 6 tentativi,");
        System.out.print(" dopo ogni tentativo, i colori delle tessere ");
        System.out.println(" cambieranno per mostrare il feedback:");
        System.out.print("- di verde se la lettera è nella");
        System.out.println(" parola segreta e nel posto giusto; ");
        System.out.print("- di giallo se la lettera è nella");
        System.out.println(" parola segreta ma nel posto sbagliato;");
        System.out.print("- di grigio se la lettera non");
        System.out.println(" è nella parola segreta.");

        System.out.print("\nIl ");
        System.out.print(ANSI_BOLD + "GIOCATORE" + ANSI_RESET);
        System.out.println(" ha a disposizione i seguenti comandi:");
        System.out.println(" - /help");
        System.out.println(" - /gioca");
        System.out.println(" - /abbandona");
        System.out.println(" - /esci");

        System.out.print("\nIl ");
        System.out.print(ANSI_BOLD + "PAROLIERE" + ANSI_RESET);
        System.out.println(" ha a disposizione i seguenti comandi:");
        System.out.println(" - /nuova <parola>");
        System.out.println(" - /mostra");

        System.out.println("\u001B[31m" + ANSI_BOLD + "\nnota: prima di " +
        "iniziare la partita è necessario impostare una parola" + 
        "\u001B[0m" + ANSI_RESET);
    }
}
