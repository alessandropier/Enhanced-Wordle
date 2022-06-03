package it.uniba.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GiocatoreTest {

    /**
     * Testa la funzione setTentativi della classe Giocatore.
     */
    @Test
    public void setTentativiTest() {
        Giocatore g = new Giocatore();
        g.setTentativi(6);
        assertEquals(
            6, g.getTentativi());
    }

    /**
     * Testa la funzione getParolaSegreta della classe Paroliere.
     */
    @Test
    public void getTentativiTest() {
        Giocatore g = new Giocatore();
        assertEquals(0, g.getTentativi());
    }
}