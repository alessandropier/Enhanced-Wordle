package it.uniba.app;

/**<<Controller>>*/
public class Controller {
    private final int maxTentativi=6;
    private final int numCaratteri=5;

    /**Costruttore*/
    public Controller()
    {}

    /**Imposta una nuova parola segreta da indovinare */
    public void Nuova(String nuovaParola, Paroliere p)
    {
        boolean flagLength=false, flagCorrect=false;

        if(nuovaParola.length()<numCaratteri)  
        {
            flagLength=true;
            System.out.println("Parola segreta troppo corta. La parola deve contenere " + numCaratteri + " lettere.");
        }
            
        else if(nuovaParola.length()>numCaratteri)
        {
            flagLength=true;
            System.out.println("Parola segreta troppo lunga. La parola deve contenere " + numCaratteri + " lettere.");
        }
        
        nuovaParola.toUpperCase();

        for(int i=0; i<numCaratteri && !flagCorrect; i++)
        {
            if(nuovaParola.charAt(i)<'A' || nuovaParola.charAt(i)>'Z')
            {
                flagCorrect=true;
            }
        }
        if(flagCorrect)
            System.out.println("Parola segreta non valida. La parola può contenere solo caratteri alfabetici.");
        if(!flagLength && !flagCorrect)
        {
            System.out.println("OK!");
            p.setParolaSegreta(nuovaParola);
        }
    }

    /**Mostra la parola segreta impostata */
    public void Mostra(Paroliere p)
    {
        if(p.getParolaSegreta()==null){
            System.out.println("Parola segreta non impostata.");
        }else{
            System.out.println("La parola segreta è "+p.getParolaSegreta()+".");
        }
    }

    /**Permette di iniziare una nuova partita */
    public void Gioca(Giocatore g, Paroliere p, Matrice m)
    {
        if(p.getParolaSegreta().equals(null))
        {
            System.out.println("Parola segreta non impostata. Impossibile giocare.");
        }
        else
        if(g.getTentativi() == 0)
        {
            /**Stampa della matrice dei tentativi*/     
            m.stampaMatrice(maxTentativi, numCaratteri);
        }
    }

    /**Permette di effettuare un tentativo per indovinare la parola segreta */
    public void Tentativo(Giocatore g, String s, Paroliere p)
    {
        if(!p.getParolaSegreta().equals(null))
        {
            if(g.getTentativi()<maxTentativi)
            {
                boolean flagCorrect=false;

                if(s.length()<numCaratteri)  
                {
                    System.out.println("Parola troppo corta. La parola deve contenere " + numCaratteri + " lettere.");
                }         
                else if(s.length()>numCaratteri)
                {
                    System.out.println("Parola troppo lunga. La parola deve contenere " + numCaratteri + " lettere.");
                }
                else
                {
                    s.toUpperCase();

                    for(int i=0; i<numCaratteri && !flagCorrect; i++)
                    {
                        if(s.charAt(i)<'A' || s.charAt(i)>'Z')
                        {
                            flagCorrect=true;
                        }
                    }
                    if(flagCorrect)
                    System.out.println("Parola non valida. La parola può contenere solo caratteri alfabetici.");
                }
            }
            else
            {
                System.out.println("Numero massimo di tentativi raggiunto. Avvia una nuova partita.");
            }
        }
        else 
        {
            System.out.println("Parola segreta mancante. Imposta una parola segreta per giocare.");
        }
        /**Implementati i controlli sulla correttezza della parola inserita e sullo stato del gioco
         * Da sviluppare: 
         * Verifica se la parola è stata indovinata;
         * Colori delle lettere;
         * Implementazione della stampa a video.
        */
         
    }
}

