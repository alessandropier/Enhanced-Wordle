package it.uniba.app;

/**<<Boundary>>
 * La classe Help stampa a video il menu di aiuto.
*/

public class Help {

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

        System.out.print("Se sei ");
        System.out.print("\033[0;1m" + "GIOCATORE");
        System.out.println(" hai a disposizione i seguenti comandi:");
        System.out.println(" - /help");
        System.out.println(" - /gioca");
        System.out.println(" - /abbandona");
        System.out.println(" - /esci\n");
        System.out.print("Se sei ");
        System.out.print("\033[0;1m" + "PAROLIERE");
        System.out.println(" hai a disposizione i seguenti comandi:");
        System.out.println(" - /nuova <parola>");
        System.out.println(" - /mostra");
    }
}
