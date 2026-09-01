package it.uniba.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;

import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;

/**
 * Classe di test per la classe MyInput.
 */
public class MyInputTest {

    /** Stream di byte per l'output. */
    private final ByteArrayOutputStream outContent =
    new ByteArrayOutputStream();
    /** Stream di output originale. */
    private final PrintStream originalOut = System.out;
    /** Stream di input originale. */
    private final java.io.InputStream originalIn = System.in;

    /** Metodo setUpStreams. */
    @BeforeEach
    public void setUpStreams() {
        this.outContent.reset();
        try {
            System.setOut(new PrintStream(this.outContent, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /** Metodo restoreStreams. */
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    /**
     * Testa la lettura corretta di una stringa tramite MyInput.
     */
    @Test
    public void testLeggiStringaSuccesso() {
        String inputSimulato = "testo_prova\n";
        System.setIn(new ByteArrayInputStream(inputSimulato.getBytes(StandardCharsets.UTF_8)));

        String risultato = MyInput.leggiStringa("Inserisci");
        assertEquals("testo_prova", risultato);
    }

    /**
     * Testa la gestione dell'eccezione (blocco catch) in MyInput.
     */
    @Test
    public void testLeggiStringaEccezione() {
        // Creiamo un InputStream personalizzato che lancia un'eccezione la prima volta,
        // e poi restituisce -1 (fine stream) per far terminare il ciclo do-while.
        InputStream faultyInput = new InputStream() {
            private boolean throwError = true;

            @Override
            public int read() throws java.io.IOException {
                if (throwError) {
                    throwError = false;
                    throw new java.io.IOException("Errore I/O simulato");
                }
                return -1; // Fine dello stream per uscire dal do-while
            }
        };

        System.setIn(faultyInput);

        MyInput.leggiStringa("Inserisci");

        // Verifica che sia entrato nel catch stampando l'errore
        assertTrue(outContent.toString().contains("Errore dell'input dei dati"));
    }

    /**
     * Test per verificare la corretta lettura di una stringa tramite MyInput.leggiStringa.
     */
    @Test
    public void testLeggiStringa() {
        // Salviamo il System.in originale
        InputStream sysInBackup = System.in;
        
        // Simuliamo l'inserimento della stringa "test" seguita da un invio (\n)
        String inputSimulato = "test\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inputSimulato.getBytes());
        System.setIn(in);

        // Chiamiamo il metodo
        String risultato = MyInput.leggiStringa("Inserisci qualcosa");

        // Verifichiamo che la stringa letta sia corretta
        assertEquals("test", risultato);

        // Ripristiniamo il System.in originale
        System.setIn(sysInBackup);
    }

    /**
     * Test per coprire il costruttore privato di MyInput (pattern utility class).
     * Questo serve a raggiungere il 100% di coverage sulla classe.
     */
    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<MyInput> constructor = MyInput.class.getDeclaredConstructor();
        // Rendiamo accessibile il costruttore privato
        constructor.setAccessible(true);
        
        // Verifichiamo che sia privato
        assert(Modifier.isPrivate(constructor.getModifiers()));
        
        // Istanziamo la classe via reflection per coprire la riga del costruttore privato
        MyInput instance = constructor.newInstance();
        assertNotNull(instance);
    }
}