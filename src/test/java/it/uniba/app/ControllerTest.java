package it.uniba.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
/**
 * Classe di test per la classe Controller.
 */
public class ControllerTest {

    /**Stream di byte.*/
    private final ByteArrayOutputStream outContent =
    new ByteArrayOutputStream();
    /**Stream di output.*/
    private final PrintStream originalOut = System.out;

    /**Matrice Vuota aspettazione.*/
    private static final String MATRICEVUOTA =
    "[\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m]"
    + "\n[\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m]"
    + "\n[\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m]"
    + "\n[\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m]"
    + "\n[\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m]"
    + "\n[\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m]\n";

    /**Riga Piena aspettazione.
     * Value = aereo.
    */
    private static final String RIGAPIENA =
    "[\u001B[103m\u001B[30mA\u001B[40m\u001B[0m|"
    + "\033[97;107m\u001B[30mE\u001B[40m\u001B[0m|"
    + "\033[97;107m\u001B[30mR\u001B[40m\u001B[0m|"
    + "\033[97;107m\u001B[30mE\u001B[40m\u001B[0m|"
    + "\033[97;107m\u001B[30mO\u001B[40m\u001B[0m]\n";

    /**Riga Vuota aspettazione.*/
    private static final String RIGAVUOTA = "[\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m|"
    + "\u001b[30m \u001b[40m\u001b[0m|\u001b[30m \u001b[40m\u001b[0m]\n";

    /**Metodo setUpStreams.*/
    @BeforeEach
    public void setUpStreams() {
        this.outContent.reset(); 
        try {
            System.setOut(new PrintStream(this.outContent, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**Metodo restoreStreams.*/
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
             + " La parola deve contenere 5 lettere.\n";
            assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
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
        + " La parola deve contenere 5 lettere.\n";
        assertEquals(expectedOutput,
        outContent.toString("UTF-8").replaceAll("\r", ""));
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
        + "La parola può contenere solo caratteri alfabetici.\n";
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
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
        String expectedOutput  = "OK!\nLa parola segreta è PAOLA.\n";
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
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
        String expectedOutput  = "Parola segreta non impostata.\n";
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
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
        + " Impossibile giocare.\n";
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /**
     * Test 3 per la funzione gioca della classe Controller.
     * CASO: Parola impostata e numero tentativi = 0.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void giocaTest3() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.nuova("palla", p);
        Controller.gioca(new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));
        String expectedOutput  = "OK!\n" + MATRICEVUOTA;
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /*
     * Test 4 per la funzione gioca della classe Controller.
     * CASO: Parola impostata e numero tentativi > 0.
     * @throws UnsupportedEncodingException
     **/
    /*
    @Test*/
    /*
    public void giocaTest4() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Giocatore g = new Giocatore();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);
        Controller.tentativo(g, "aereo", p, m);
        Controller.gioca(g, p, m);
        String expectedOutput  = "OK!\n" + MATRICEVUOTA
        + RIGAPIENA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA;
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }*/

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
        
        // Verifica che la parola sia rimasta quella corretta e che flagGioca sia diventato false
        assertEquals("PALLA", p.getParolaSegreta());
        assertFalse(Controller.getFlagGioca());

        // Verifichiamo se l'output corrisponde alla parola trovata
        assertTrue(outContent.toString().contains("Complimenti! Hai indovinato la parola!"));
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
            6, g.getTentativi());
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
            6, g.getTentativi());
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

        String expectedOutput  = "OK!\n" + MATRICEVUOTA
        + RIGAPIENA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA
        + RIGAPIENA + RIGAPIENA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA
        + RIGAPIENA + RIGAPIENA + RIGAPIENA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA
        + RIGAPIENA + RIGAPIENA + RIGAPIENA + RIGAPIENA + RIGAVUOTA + RIGAVUOTA
        + RIGAPIENA + RIGAPIENA + RIGAPIENA + RIGAPIENA + RIGAPIENA + RIGAVUOTA
        + RIGAPIENA + RIGAPIENA + RIGAPIENA + RIGAPIENA + RIGAPIENA + RIGAPIENA
        + "Numero massimo di tentativi raggiunto. Avvia una nuova partita.\n"
        + "La parola segreta è PALLA.\n"
        + "Digitare '/gioca' per iniziare una partita.\n";

        try {
           assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
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
        String expectedOutput  = "OK!\n" + MATRICEVUOTA + "Parola troppo lunga."
        + " La parola deve contenere 5 lettere.\n";
        try {
           assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
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
        String expectedOutput  = "OK!\n" + MATRICEVUOTA + "Parola troppo corta."
        + " La parola deve contenere 5 lettere.\n";
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
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
        String expectedOutput = "OK!\n" + MATRICEVUOTA + "Parola non valida."
        + " La parola può contenere solo caratteri alfabetici.\n";
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
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
        String expectedOutput  = "Nessuna partita avviata.\n";
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }


    /**
     * Test 2 per la funzione abbandona della classe Controller.
     * CASO: Abbandono il gioco.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void abbandonaTest2() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in =
        new ByteArrayInputStream("SI".getBytes("UTF-8"));
        System.setIn(in);

        Controller.abbandona(p, g, m);
        String expectedOutput = "OK!\n"
        + MATRICEVUOTA
        + "Abbandonare la partita?: "
        + "La parola segreta era: " 
        + "\u001B[1m\u001B[31mPALLA\n\u001B[0m\u001B[0m\n"
        + "Partita abbandonata con successo.\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));

        //Reset System.in to its original
        System.setIn(sysInBackup);
    }

     /**
     * Test 2 per la funzione abbandona della classe Controller.
     * CASO: Non abbandono il gioco.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void abbandonaTest3() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.nuova("palla", p);
        Controller.gioca(g, p, m);
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in =
        new ByteArrayInputStream("NO".getBytes("UTF-8"));
        System.setIn(in);

        Controller.abbandona(p, g, m);
        String expectedOutput  = "OK!\n" + MATRICEVUOTA
        + "Abbandonare la partita?: " + "Partita non abbandonata,"
        + " in attesa di comandi o nuovo tentativo...\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));

        //Reset System.in to its original
        System.setIn(sysInBackup);
    }

     /**
     * Test 1 per la funzione esci della classe Controller.
     * CASO: Non esci il gioco.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void esciTest() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Giocatore g = new Giocatore();
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in =
        new ByteArrayInputStream("NO".getBytes("UTF-8"));
        System.setIn(in);

        Controller.esci(p, g);
        String expectedOutput  = "Uscire dal gioco?: " + "In attesa di nuovi"
        + " comandi o nuovo tentativo...\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));

        //Reset System.in to its original
        System.setIn(sysInBackup);
    }

    /**
     * Test 1 per la funzione wordle della classe Controller.
     * CASO: comando "/nuova palla"
     */
    @Test
    public void wordleTes1t() {
        Paroliere p = new Paroliere();
        Controller.wordle("/nuova palla", new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));

        assertNotEquals(
            "ebete", p.getParolaSegreta());
    }

    /*
     * Test 2 per la funzione wordle della classe Controller.
     * CASO: comando "/nuova" non seguito dalla parola segreta.
     * @throws UnsupportedEncodingException
     */
    /*
    @Test
    public void wordleTest2() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.wordle("/nuova", new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));

        assertEquals("OK!\nInserisci un comando o un tentativo: ",
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }*/

    /**
     * Test 3 per la funzione wordle della classe Controller.
     * CASO: comando "/help".
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest3() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.wordle("/help", new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));

        String expectedOutput  = "Il giocatore deve indovinare una parola"
        + "  di 5 lettere in 6 tentativi,"
        + " dopo ogni tentativo, i colori delle tessere "
        + " cambieranno per mostrare il feedback:\n"
        + "- di verde se la lettera è nella"
        + " parola segreta e nel posto giusto; \n"
        + "- di giallo se la lettera è nella"
        + " parola segreta ma nel posto sbagliato;\n"
        + "- di grigio se la lettera non"
        + " è nella parola segreta.\n"
        + "\nIl "
        + "\u001B[1m" + "GIOCATORE" + "\u001B[0m"
        + " ha a disposizione i seguenti comandi:\n"
        + " - /help\n"
        + " - /gioca\n"
        + " - /abbandona\n"
        + " - /esci\n"
        + "\nIl "
        + "\u001B[1m" + "PAROLIERE" + "\u001B[0m"
        + " ha a disposizione i seguenti comandi:\n"
        + " - /nuova <parola>\n"
        + " - /mostra\n"
        + "\u001B[31m" + "\u001B[1m" 
        + "\nnota: prima di iniziare la partita è necessario impostare una parola"
        + "\u001B[0m" + "\u001B[0m\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /**
     * Test 4 per la funzione wordle della classe Controller.
     * CASO: comando "/mostra" con parola segreta non impostata.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest4() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.wordle("/mostra", new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));

        String expectedOutput = "Parola segreta non impostata.\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /**
     * Test 5 per la funzione wordle della classe Controller.
     * CASO: comando "/mostra" con parola segreta impostata.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest5() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.nuova("palla", p);
        Controller.wordle("/mostra", new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));

        String expectedOutput = "OK!\nLa parola segreta è PALLA.\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /**
     * Test 6 per la funzione wordle della classe Controller.
     * CASO: comando "/gioca" con parola segreta non settata.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest6() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.wordle("/gioca", new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));

        String expectedOutput =
        "Parola segreta non impostata. Impossibile giocare.\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /**
     * Test 7 per la funzione wordle della classe Controller.
     * CASO: comando "/gioca" con parola segreta settata.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest7() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.nuova("palla", p);
        Controller.wordle("/gioca", new Giocatore(),
        p, new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri()));

        String expectedOutput = "OK!\n" + MATRICEVUOTA;

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /**
     * Test 8 per la funzione wordle della classe Controller.
     * CASO: comando "/tentativo" con partita avviata e tentativo sbagliato.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest8() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.nuova("palla", p);
        Giocatore g = new Giocatore();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Controller.gioca(g, p, m);
        Controller.wordle("aereo", g,
        p, m);

        String expectedOutput = "OK!\n" + MATRICEVUOTA
        + RIGAPIENA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA + RIGAVUOTA;

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /**
     * Test 9 per la funzione wordle della classe Controller.
     * CASO: comando "/tentativo" con partita avviata e tentativo corto.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest9() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.nuova("palla", p);
        Giocatore g = new Giocatore();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Controller.gioca(g, p, m);
        Controller.wordle("aere", g,
        p, m);

        String expectedOutput = "OK!\n" + MATRICEVUOTA + "Parola troppo corta."
        + " La parola deve contenere "
        + Controller.getNumCaratteri() + " lettere.\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /**
     * Test 10 per la funzione wordle della classe Controller.
     * CASO: comando "/tentativo" con partita avviata e tentativo lungo.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest10() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Controller.nuova("palla", p);
        Giocatore g = new Giocatore();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Controller.gioca(g, p, m);
        Controller.wordle("aereoo", g,
        p, m);

        String expectedOutput = "OK!\n" + MATRICEVUOTA + "Parola troppo lunga."
        + " La parola deve contenere "
        + Controller.getNumCaratteri() + " lettere.\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }

    /**
     * Test 11 per la funzione wordle della classe Controller.
     * CASO: comando "/esci" ma non esci il gioco.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest11() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Giocatore g = new Giocatore();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in =
        new ByteArrayInputStream("NO".getBytes("UTF-8"));
        System.setIn(in);

        Controller.wordle("/esci", g, p, m);
        String expectedOutput  = "Uscire dal gioco?: " + "In attesa di nuovi"
        + " comandi o nuovo tentativo...\n";

        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));

        //Reset System.in to its original
        System.setIn(sysInBackup);
    }

    /**
     * Test 12 per la funzione wordle della classe Controller.
     * CASO: comando "/abbandona" ma la partita non è stata avviata.
     * @throws UnsupportedEncodingException
     */
    @Test
    public void wordleTest12() throws UnsupportedEncodingException {
        Paroliere p = new Paroliere();
        Matrice m = new Matrice(Controller.getMaxTentativi(),
        Controller.getNumCaratteri());
        Giocatore g = new Giocatore();
        Controller.wordle("/abbandona", g, p, m);
        String expectedOutput  = "Nessuna partita avviata.\n";
        assertEquals(expectedOutput,
            outContent.toString("UTF-8").replaceAll("\r", ""));
    }
}
