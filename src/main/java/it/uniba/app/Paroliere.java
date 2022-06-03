package it.uniba.app;


/**
 * <<Entity>>
 * La classe Paroliere gestisce l'entità paroliere in tutti i suoi aspetti.
 */
public class Paroliere {
    /** Attributi della classe Paroliere. */
    private String parolaSegreta;

    /**
     * Costruttore.
     */
    public Paroliere() {
        parolaSegreta = null;
    }

    /**
     * Get parola segreta.
     * @return Parola segreta.
     */
    public String getParolaSegreta() {
        return parolaSegreta;
    }

    /**
     * Set parola segreta.
     * @param p Parola segreta inserita dall'utente.
     */
    public void setParolaSegreta(final String p) {
        parolaSegreta = p;
    }
}
