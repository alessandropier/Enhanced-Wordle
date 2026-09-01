package it.uniba.app;

import java.util.ArrayList;
import java.util.Arrays;

/** <<Boundary>> La classe Matrice stampa a video la matrice dei tentativi. */
public class Matrice {
    /**Array che memorizza i tentativi inseriti dal giocatore .*/
    private ArrayList<String> tentativi;
    /**Array che memorizza in corrispondenza per ogni char di
     * ogni tentativo il colore corrispondente.
     * */
    private ArrayList<ArrayList<String>> colori;
    /**Colore background verde. */
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    /**Colore background giallo. */
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[103m";
    /**Colore background grigio. */
    private static final String ANSI_GRAY_BACKGROUND = "\033[97;107m";
    /**Colore background nero. */
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    /**Colore reset testo (bianco). */
    private static final String ANSI_RESET = "\u001B[0m";
    /**Colore testo nero. */
    private static final String ANSI_BLACK = "\u001B[30m";

    /**
     * Costruttore.
     * @param maxT Numero massimo di tentativi.
     * @param maxC Numero massimo di caratteri per parola.
     */
    public Matrice(final int maxT, final int maxC) {
        tentativi = new ArrayList<>();
        char[] spaziInizialiArr = new char[maxC];
        Arrays.fill(spaziInizialiArr, ' ');
        String spaziIniziali = new String(spaziInizialiArr);

        for (int i = 0; i < maxT; i++) {
            tentativi.add(spaziIniziali);
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
        // Usa il minimo tra maxT e la dimensione reale delle liste per evitare IndexOutOfBoundsException
        int righeDaStampare = Math.min(maxT, Math.min(tentativi.size(), colori.size()));

        for (int i = 0; i < righeDaStampare; i++) {
            System.out.print("[");
            for (int j = 0; j < maxC; j++) {
                // Controllo di sicurezza sui colori
                if (i < colori.size() && j < colori.get(i).size()) {
                    System.out.print(colori.get(i).get(j));
                }
                
                // Stampa del carattere del tentativo in sicurezza
                if (i < tentativi.size() && j < tentativi.get(i).length()) {
                    System.out.print(ANSI_BLACK + this.tentativi.get(i).charAt(j));
                } else {
                    System.out.print(ANSI_BLACK + ' ');
                }
                
                System.out.print(ANSI_BLACK_BACKGROUND + ANSI_RESET);
                if (j != maxC - 1) {
                    System.out.print("|");
                }
            }
            System.out.println("]");
        }
    }

    /**
     * Memorizza il colore dello sfondo per ogni carattere di ogni tentativo.
     * @param esito Array per memorizzare la presenza dei caratteri.
     * @param tentativo Numero del tentativo.
     */
    public void impostaColore(final ArrayList<Integer> esito,
     final int tentativo) {
        for (int i = 0; i < esito.size(); i++) {
            if (tentativo < colori.size() && i < colori.get(tentativo).size()) {
                if (esito.get(i) == 1) {
                    colori.get(tentativo).set(i, ANSI_GREEN_BACKGROUND);
                } else if (esito.get(i) == 0) {
                    colori.get(tentativo).set(i, ANSI_YELLOW_BACKGROUND);
                } else {
                    colori.get(tentativo).set(i, ANSI_GRAY_BACKGROUND);
                }
            }
        }
    }

    /**
     * Set tentavi, memorizza la parola inserita all'n-esimo tentativo.
     * @param tentativo Numero del tentativo.
     * @param parola Parola inserita come tentativo.
     */
    public void setTentativi(final int tentativo, final String parola) {
        if (tentativo >= 0 && tentativo < tentativi.size()) {
            tentativi.set(tentativo, parola);
        }
    }

    /**
     * Permette di azzerare e ridimensionare i tentativi e i colori nella matrice.
     * @param maxT Numero massimo di tentativi (righe).
     * @param maxC Numero massimo di caratteri per parola (colonne).
     */
    public void azzera(final int maxT, final int maxC) {
        char[] spaziVuotiArr = new char[maxC];
        Arrays.fill(spaziVuotiArr, ' ');
        String spaziVuoti = new String(spaziVuotiArr);

        // Ricrea completamente la lista dei tentativi con le nuove righe e colonne
        tentativi.clear();
        for (int i = 0; i < maxT; i++) {
            tentativi.add(spaziVuoti);
        }
        
        // Ricrea completamente la matrice dei colori con le nuove dimensioni (maxT x maxC)
        colori.clear();
        for (int i = 0; i < maxT; i++) {
            ArrayList<String> rigaColori = new ArrayList<>();
            for (int j = 0; j < maxC; j++) {
                rigaColori.add("");
            }
            colori.add(rigaColori);
        }
    }

    /**
     * Restituisce la lista dei tentativi inseriti.
     * @return ArrayList di stringhe contenente i tentativi.
     */
    public ArrayList<String> getTentativiList() {
        return this.tentativi;
    }

    /**
     * Restituisce la matrice dei colori ANSI associati.
     * @return Matrice dei colori.
     */
    public ArrayList<ArrayList<String>> getColoriList() {
        return this.colori;
    }
}