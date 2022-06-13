package it.uniba.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Classe di test per la classe Controller.
 */
public class ControllerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private final String matriceVuota = "[[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m]\n" +
    "[[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m]\n" +
    "[[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m]\n" +
    "[[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m]\n" +
    "[[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m]\n" +
    "[[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m|[30m [40m[0m]\n";

    private final String rigaPiena = "[[43m[30mA[40m[0m|[97;107m[30mE[40m[0m|[97;107m[30mR[40m[0m|[97;107m[30mE[40m[0m|[97;107m[30mO[40m[0m]\n";
    private final String rigaVuota = "[\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m]\n";

    @BeforeEach
    public void setUpStreams() {
            System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

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
     * Test 4 per la funzione nuova della classe Controller.
     * CASO: Lunghezza parola > NUMCARATTERI.
     */
    @Test
    public void nuovaTest4() {
        Paroliere p = new Paroliere();
        Controller.nuova("paoloo", p);
        try {
            String expectedOutput  = "Parola segreta troppo lunga."
             + " La parola deve contenere 5 lettere.\n" ;
            assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 

    /**
     * Test 5 per la funzione nuova della classe Controller.
     * CASO: Lunghezza parola < NUMCARATTERI.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void nuovaTest5() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.nuova("paol", p);
        String expectedOutput  = "Parola segreta troppo corta."
        + " La parola deve contenere 5 lettere.\n" ;
        assertEquals(expectedOutput, 
        outContent.toString("UTF-8").replaceAll("\r", "") );
    }

    /**
     * Test 6 per la funzione nuova della classe Controller.
     * CASO: Nella parola sono presenti caratteri non alfabetici.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void nuovaTest6() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.nuova("paol6", p);
        String expectedOutput  = "Parola segreta non valida."
        + "La parola può contenere solo caratteri alfabetici.\n" ;
        assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
    }

    
    /**
     * Test 1 per la funzione mostra della classe Controller.
     * CASO: Parola impostata.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void mostraTest1() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.nuova("paola", p);
        Controller.mostra(p);
        String expectedOutput  = "OK!\nLa parola segreta è PAOLA.\n" ;
        assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
    }


    /**
     * Test 2 per la funzione mostra della classe Controller.
     * CASO: Parola non impostata.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void mostraTest2() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.mostra(p);
        String expectedOutput  = "Parola segreta non impostata.\n" ;
        assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
    }


    /**
     * Test 1 per la funzione gioca della classe Controller.
     */
    @Test
    public void giocaTest1() {
        Paroliere p = new Paroliere();
        Controller.nuova("palla", p);
        Controller.gioca(new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));

        assertEquals(
            true, Controller.getFlagGioca());
    }


    /**
     * Test 2 per la funzione gioca della classe Controller.
     * CASO: Parola non impostata.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void giocaTest2() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.gioca(new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));
        String expectedOutput  = "Parola segreta non impostata."
        + " Impossibile giocare.\n" ;
        assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
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

        for (int i = 0; i <= Controller.getMaxTentativi(); i++) {
            Controller.tentativo(g, "aereo", p, m);
        }

        assertEquals(
            0, g.getTentativi());
    }

     /**
     * Test 5.1 per la funzione tentativo della classe Controller.
     * CASO: Max Tentativi raggiunto senza indovinare + controllo stampa
     */
    @Test
    public void tentativoTest51() {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);

        for (int i = 0; i <= Controller.getMaxTentativi(); i++) {
            Controller.tentativo(g, "aereo", p, m);
        }

        String expectedOutput  = "OK!\n" + matriceVuota
        + rigaPiena + rigaVuota + rigaVuota + rigaVuota + rigaVuota + rigaVuota
        + rigaPiena + rigaPiena + rigaVuota + rigaVuota + rigaVuota + rigaVuota
        + rigaPiena + rigaPiena + rigaPiena + rigaVuota + rigaVuota + rigaVuota
        + rigaPiena + rigaPiena + rigaPiena + rigaPiena + rigaVuota + rigaVuota
        + rigaPiena + rigaPiena + rigaPiena + rigaPiena + rigaPiena + rigaVuota
        + rigaPiena + rigaPiena + rigaPiena + rigaPiena + rigaPiena + rigaPiena
        +"Numero massimo di tentativi raggiunto."
        + " Avvia una nuova partita.\n" + "La parola segreta è PALLA.\n" +
        "Digitare '/gioca' per iniziare una partita.\n";
       
        try {
           assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


     /**
     * Test 6 per la funzione tentativo della classe Controller.
     * CASO: Lunghezza parola > NUMCARATTERI.
     */
    @Test
    public void tentativoTest6() {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);
        Controller.tentativo(g, "aereoo", p, m);
        String expectedOutput  = "OK!\n" + matriceVuota + "Parola troppo lunga."
        + " La parola deve contenere 5 lettere.\n" ;
        try {
           assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test 7 per la funzione tentativo della classe Controller.
     * CASO: Lunghezza parola < NUMCARATTERI.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void tentativoTest7() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);
        Controller.tentativo(g, "aere", p, m);
        String expectedOutput  = "OK!\n" + matriceVuota + "Parola troppo corta."
        + " La parola deve contenere 5 lettere.\n" ;
        assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
    }

    /**
     * Test 8 per la funzione tentativo della classe Controller.
     * CASO: Nella parola sono presenti caratteri non alfabetici.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void tentativoTest8() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);
        Controller.tentativo(g, "aere6", p, m);
        String expectedOutput  ="OK!\n" + matriceVuota + "Parola non valida."
        + " La parola può contenere solo caratteri alfabetici.\n" ;
        assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
    }


    /**
     * Test 1 per la funzione abbandona della classe Controller.
     * CASO: La partita non è stata avviata.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void abbandonaTest1() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.abbandona(p, g, m);
        String expectedOutput  = "Nessuna partita avviata.\n" ;
        assertEquals(expectedOutput, 
            outContent.toString("UTF-8").replaceAll("\r", "") );
    }


    /**
     * Test 2 per la funzione abbandona della classe Controller.
     * CASO: Abbandono il gioco.
     * @throws UnsupportedEncodingException
     */
   /*  @Test
    public void abbandonaTest2() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.gioca(g, p, m);
        Controller.abbandona(p, g, m);
        InputStream sysInBackup = System.in; 
        ByteArrayInputStream in = new ByteArrayInputStream("My string".getBytes());
        System.setIn(in);
        in.read();
        

        //Reset System.in to its original
        System.setIn(sysInBackup);
        //assertEquals(expectedOutput, 
          //  outContent.toString("UTF-8").replaceAll("\r", "") );
    }
    */

     /**
     * Test 2 per la funzione abbandona della classe Controller.
     * CASO: Non abbandono il gioco.
     * @throws UnsupportedEncodingException
     */
   /*  @Test
    public void abbandonaTest3() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.gioca(g, p, m);
        Controller.abbandona(p, g, m);
        String expectedOutput  = "Nessuna partita avviata.\n" ;
       // assertEquals(expectedOutput, 
         //   outContent.toString("UTF-8").replaceAll("\r", "") );
    }
*/

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
