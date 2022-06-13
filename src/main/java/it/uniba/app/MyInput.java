package it.uniba.app;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * <<Boundary>>
 * La classe MyInput
 * è stata progettata con l'obiettivo di semplificare
 * le operazioni di input delle stringhe,
 * affinchè la stringa rispetti i criteri definiti. */
public final class MyInput {

    /**Costruttore. */
    private MyInput() {
    }

    /**
     * Permette di leggere stringhe in input da tastiera.
     * @param msg Messaggio stampato a video prima di ricevere input.
     * @return Stringa letta in input.
     */
    public static String leggiStringa(final String msg) {
        BufferedReader tastiera = new BufferedReader(
                                    new InputStreamReader(
                                        System.in, StandardCharsets.UTF_8));
        boolean err;
        String aux = null;
        do {
            err = false;
            System.out.print(msg + ": ");
            try {
                aux = tastiera.readLine();
            } catch (Exception e) {
                err = true;
                System.out.println("Errore dell'input dei dati");
            }
        } while (err);
        return aux;
    }
}
