package it.uniba.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertNull;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Classe di test per la classe Controller.
 */
public class ControllerTest {
    /**
     * Testa la funzione getMaxTentativi della classe Controller.
     */
    @Test
    public void getMaxTentativiTest() {
        assertEquals(
            6, Controller.getMaxTentativi());
    }

    /**
     * Testa la funzione getNumCaratteri della classe Controller.
     */
    @Test
    public void getNumCaratteriTest() {
        assertEquals(
            5, Controller.getNumCaratteri());
    }

    /**
     * Test 1 per la funzione nuova della classe Controller.
     * CASO: Parola non corrispondente.
     */
    @Test
    public void nuovaTest1() {
        Paroliere p = new Paroliere();
        Controller.nuova("paolo", p);
        assertNotEquals(
            "ebete", p.getParolaSegreta());
    }

    /**
     * Test 2 per la funzione nuova della classe Controller.
     * CASO: Parola corrispondente.
     */
    @Test
    public void nuovaTest2() {
        Paroliere p = new Paroliere();
        Controller.nuova("palla", p);
        assertEquals(
            "PALLA", p.getParolaSegreta());
    }

    /**
     * Test 3 per la funzione nuova della classe Controller.
     * CASO: Parola impostata.
     */
    @Test
    public void nuovaTest3() {
        Paroliere p = new Paroliere();
        Controller.nuova("paolo", p);
        assertNotNull(p.getParolaSegreta(), () -> "Parola indovinata!");
    }

    /**
     * Test per la funzione gioca della classe Controller.
     */
    @Test
    public void giocaTest() {
        Paroliere p = new Paroliere();
        Controller.nuova("palla", p);
        Controller.gioca(new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));

        assertEquals(
            true, Controller.getFlagGioca());
    }

    /**
     * Test 1 per la funzione tentativo della classe Controller.
     * CASO: Parola indovinata al primo tentativo
     */
    @Test
    public void tentativoTest1() {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);

        Controller.tentativo(g, "aereo", p, m);
        Controller.tentativo(g, "palla", p, m);
        assertNull(p.getParolaSegreta(), () -> "Parola indovinata!");
    }

    /**
     * Test 2 per la funzione tentativo della classe Controller.
     * CASO: Parola non indovinata fino al terzo tentativo
     */
    @Test
    public void tentativoTest2() {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);

        Controller.tentativo(g, "aereo", p, m);
        Controller.tentativo(g, "sasso", p, m);
        Controller.tentativo(g, "bomba", p, m);

        assertEquals(
            3, g.getTentativi());
    }

    /**
     * Test 3 per la funzione tentativo della classe Controller.
     * CASO: Max Tentativi raggiunto senza indovinare
     */
    @Test
    public void tentativoTest3() {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);

        for (int i = 0; i < Controller.getMaxTentativi(); i++) {
            Controller.tentativo(g, "aereo", p, m);
        }

        assertEquals(
            0, g.getTentativi());
    }

     /**
     * Test 4 per la funzione tentativo della classe Controller.
     * CASO: Max Tentativi - 1 raggiunto senza indovinare
     */
    @Test
    public void tentativoTest4() {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);

        for (int i = 0; i < Controller.getMaxTentativi() - 1; i++) {
            Controller.tentativo(g, "aereo", p, m);
        }

        assertEquals(
            Controller.getMaxTentativi() - 1, g.getTentativi());
    }

     /**
     * Test 5 per la funzione tentativo della classe Controller.
     * CASO: Max Tentativi raggiunto senza indovinare
     */
    @Test
    public void tentativoTest5() {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);

        for (int i = 0; i < Controller.getMaxTentativi(); i++) {
            Controller.tentativo(g, "aereo", p, m);
        }

        assertEquals(
            0, g.getTentativi());
    }

    /**
     * Test per la funzione wordle della classe Controller.
     * CASO: comando "/nuova palla"
     */
    @Test
    public void wordleTest() {
        Paroliere p = new Paroliere();
        Controller.wordle("/nuova palla", new Giocatore(),
        p, new Matrice(6, 5));

        assertNotEquals(
            "ebete", p.getParolaSegreta());
    }
}
