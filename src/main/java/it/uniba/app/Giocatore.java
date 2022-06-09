package it.uniba.app;

/**
 * <<Entity>>
 * La classe Giocatore gestisce l'entità giocatore in tutti i suoi aspetti.
*/
public class Giocatore {
    /** Attributi della classe Giocatore. */
    private int tentativi;

    /**Costruttore.*/
    public Giocatore() {
        tentativi = 0;
    }

    /**
     * Set numero tentativi.
     * @return Numero di tentativi.
     */
    public int getTentativi() {
        return tentativi;
    }

    /**
     * Get numero tentativi.
     * @param t Numero di tentativi.
     */
    public void setTentativi(final int t) {
        tentativi = t;
    }
}
