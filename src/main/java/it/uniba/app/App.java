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
        return "Benvenuti in Wordle!";
    }

    /**
     * Entrypoint of the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        // ABILITA I COLORI ANSI SU WINDOWS (Windows 10/11)
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                // Questa chiamata abilità il processore ANSI integrato in Windows
                new ProcessBuilder("cmd", "/c", "echo off").inheritIO().start().waitFor();
                // In alternativa, per forzare l'abilitazione:
                Runtime.getRuntime().exec("reg add HKCU\\Console /v VirtualTerminalLevel /t REG_DWORD /d 1 /f");
            } catch (Exception e) {
                // Se non va resterà in bianco e nero
            }
        }
        
        System.out.println(new App().getGreeting());
        Help.stampaHelp();

        Giocatore g = new Giocatore();
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
                                Controller.getNumCaratteri());

        do {
            Controller.wordle(
                MyInput.leggiStringa("Inserisci un comando o un tentativo"),
                 g, p, m);
        } while (true);
    }
}
