package it.uniba.app;

import java.util.ArrayList;

/** <<Boundary>> */
public class Matrice {
    private ArrayList<String> Tentativi;

    public Matrice(int maxT) {
        Tentativi = new ArrayList<>();
        for (int i = 0; i < maxT; i++) {
            Tentativi.add("     ");
        }
    }

    public void stampaMatrice(int maxT, int maxC) 
    {
        int size = 0;

        for (int i = 0; i < maxT; i++) 
        {
            System.out.print("[");
            for (int j = 0; j < maxC; j++) 
            {
                System.out.print(this.Tentativi.get(size).charAt(j));
                if (j != maxC - 1) 
                {
                    System.out.print("|");
                }
            }
            size++;
            System.out.println("]");
        }
    }
}
