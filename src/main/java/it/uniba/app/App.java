package it.uniba.app;

/**
 * Main class of the application.
 */
public final class App {

    /**
     * Get a greeting sentence.
     *
     * @return the "Hello World!" string.
     */
    public String getGreeting() {
        return "Hello World!";
    }

    /**
     * Entrypoint of the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        System.out.println(new App().getGreeting());
        Help.StampaHelp();

        Giocatore g = new Giocatore();
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(), Controller.getNumCaratteri());

        do
        {
            Controller.wordle(MyInput.leggiStringa("Inserisci un comando o un tentativo"), g, p, m);
        }while(true);
    }
}
