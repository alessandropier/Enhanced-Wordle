package it.uniba.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ParoliereTest {

    /**
     * Testa la funzione setParolaSegreta della classe Paroliere.
     */
    @Test
    public void setParolaSegretaTest() {
        Paroliere p = new Paroliere();
        p.setParolaSegreta("palle");
        assertEquals(
            "palle", p.getParolaSegreta());
    }

    /**
     * Testa la funzione getParolaSegreta della classe Paroliere.
     */
    @Test
    public void getParolaSegretaTest() {
        Paroliere p = new Paroliere();
        assertNull(p.getParolaSegreta());
    }
}