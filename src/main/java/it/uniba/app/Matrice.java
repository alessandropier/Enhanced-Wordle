package it.uniba.app;

import java.util.ArrayList;

/** <<Boundary>> La classe Matrice stampa a video la matrice dei tentativi. */
public class Matrice {
    /** Attributi della classe Matrice. */
    private ArrayList<String> tentativi;
    private ArrayList<ArrayList<String>> colori;
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_GRAY_BACKGROUND = "\033[97;107m";
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";

    /**
     * Costruttore.
     * @param maxT Numero massimo di tentativi.
     * @param maxC Numero massimo di caratteri per parola.
     */
    public Matrice(final int maxT, final int maxC) {
        tentativi = new ArrayList<>();
        for (int i = 0; i < maxT; i++) {
            tentativi.add("     ");
        }
        colori = new ArrayList<>(maxT);
        for (int i = 0; i < maxT; i++) {
            colori.add(new ArrayList<String>(maxC));
            for (int j = 0; j < maxC; j++) {
                colori.get(i).add("");
            }
        }
    }

    /**
     * Stampa della matrice.
     * @param maxT Numero massimo di tentativi.
     * @param maxC Numero massimo di caratteri per parola.
     */
    public void stampaMatrice(final int maxT, final int maxC) {
        int size = 0;

        for (int i = 0; i < maxT; i++) {
            System.out.print("[");
            for (int j = 0; j < maxC; j++) {
                System.out.print(colori.get(i).get(j));
                System.out.print(ANSI_BLACK + this.tentativi.get(size).charAt(j));
                System.out.print(ANSI_BLACK_BACKGROUND + ANSI_RESET);
                if (j != maxC - 1) {
                    System.out.print("|");
                }
            }
            size++;
            System.out.println("]");
        }
    }

    /**
     * Memorizza il colore dello sfondo per ogni carattere di ogni tentativo.
     * @param esito Array per memorizzare la presenza dei caratteri.
     * @param tentativo Numero del tentativo.
     */
    public void impostaColore(final ArrayList<Integer> esito, final int tentativo) {
        for (int i = 0; i < esito.size(); i++) {
            if (esito.get(i) == 1) {
                colori.get(tentativo).set(i, ANSI_GREEN_BACKGROUND);
            }
            else if (esito.get(i) == 0) {
                colori.get(tentativo).set(i, ANSI_YELLOW_BACKGROUND);
            }
            else {
                colori.get(tentativo).set(i, ANSI_GRAY_BACKGROUND);
            }
        }
    }

    /**
     * Set tentavi, memorizza la parola inserita all'n-esimo tentativo.
     * @param tentativo Numero del tentativo.
     * @param parola Parola inserita come tentativo.
     */
    public void setTentativi(final int tentativo, final String parola) {
        tentativi.set(tentativo, parola);
    }

    /**
     * Permette di azzerare i tentativi nella matrice.
     * @param maxC Numero massimo di caratteri per parola.
     */
    public void azzera(final int maxC) {

        for (int i = 0; i < tentativi.size(); i++) {
            tentativi.set(i, "     ");
        }
        for (int i = 0; i < colori.size(); i++) {
            for (int j = 0; j < maxC; j++) {
                colori.get(i).set(j, "");
            }
        }
    }
}
