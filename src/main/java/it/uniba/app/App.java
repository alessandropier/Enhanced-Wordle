package it.uniba.app;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;

/**
 * Main class of the application.
 */
public final class App {

    public String getGreeting() {
        return "Benvenuti in Wordle!";
    }

    public static void main(final String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");

        // Abilitazione colori ANSI (opzionale se usi solo GUI)
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "echo off").inheritIO().start().waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(new App().getGreeting());

        // 1. Inizializza FlatLaf prima di tutto
        FlatDarkLaf.setup();

        // 2. Avvio della GUI e della logica tramite SwingUtilities
        SwingUtilities.invokeLater(() -> {
            // AVVIO GIOCO
            Giocatore g = new Giocatore();
            Paroliere p = new Paroliere();
            Matrice m = new Matrice(Controller.getMaxTentativi(), Controller.getNumCaratteri());

            // Avvia la versione Grafica
            apriInterfacciaGrafica(g, p, m);
        });
    }

    private static void apriInterfacciaGrafica(Giocatore g, Paroliere p, Matrice m) {
        WordleFrame finestra = new WordleFrame(g, p, m);
        finestra.setVisible(true);
    }
}