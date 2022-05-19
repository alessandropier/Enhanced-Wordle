package it.uniba.app;
/**<<noECB>> */
/**La classe Help stampa a video il menu di aiuto */
public final class Help {
    
    public static void StampaHelp(){
        System.out.println("Il giocatore deve indovinare una parola di 5 lettere in 6 tentativi, dopo ogni tentativo, i colori delle tessere cambieranno per mostrare il feedback:");
        System.out.println(" - di verde se la lettera è nella parola segreta e nel posto giusto;\n - di giallo se la lettera è nella parola segreta ma nel posto sbagliato;\n - di grigio se la lettera non è nella parola segreta.\n");
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