package it.uniba.app;

import java.util.ArrayList;

/** <<Boundary>> */
public class Matrice {
    private ArrayList<String> Tentativi;
    private ArrayList<ArrayList<String>> colori;
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_GRAY_BACKGROUND = "\033[97;107m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";

    /**Costruttore */
    public Matrice(int maxT, int maxC) {
        Tentativi = new ArrayList<>();
        for (int i = 0; i < maxT; i++) {
            Tentativi.add("     ");
        }
        colori=new ArrayList<>(maxT);
        for (int i = 0; i < maxT; i++) {
            colori.add( new ArrayList<String>(maxC));
            for(int j=0; j < maxC; j++)
            {
                colori.get(i).add("");
            }
        }
    
    }

    /**Stampa della matrice */
    public void stampaMatrice(int maxT, int maxC) 
    {
        int size = 0;

        for (int i = 0; i < maxT; i++) 
        {
            System.out.print("[");
            for (int j = 0; j < maxC; j++) 
            {
                System.out.print(colori.get(i).get(j));
                System.out.print(this.Tentativi.get(size).charAt(j)+ANSI_BLACK_BACKGROUND);
                if (j != maxC - 1) 
                {
                    System.out.print("|");
                }
            }
            size++;
            System.out.println("]");
        }
    }

    /**Memorizza il colore dello sfondo per ogni carattere di ogni tentativo */
    public void impostaColore(ArrayList<Integer> esito, int tentativo)
    {
        for(int i=0; i<esito.size(); i++)
        {
            if(esito.get(i)==1)
            {
                colori.get(tentativo).set(i, ANSI_GREEN_BACKGROUND);
            }
            else if(esito.get(i)==0)
            {
                colori.get(tentativo).set(i, ANSI_YELLOW_BACKGROUND);
            }
            else
            {
                colori.get(tentativo).set(i, ANSI_GRAY_BACKGROUND);
            }

        }
    }

    /**Set tentavi, memorizza la parola inserita all'n-esimo tentativo */
    public void setTentativi(int tentativo, String parola) {
        Tentativi.set(tentativo, parola);
    }
}
