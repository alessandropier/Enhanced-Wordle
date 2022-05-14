package it.uniba.app;
import java.util.ArrayList;
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
        /** Verifica se la parola è stata indovinata */
        if(s.equals(p.getParolaSegreta()))
        {
            System.out.println("Parola segreta indovinata in: "+g.getTentativi()+" tentativi.");
            /**stampa matrice */
        }
        else
        {
            String parolaGiusta=p.getParolaSegreta();
            ArrayList<Integer> conta=new ArrayList<>(numCaratteri);
            ArrayList<Integer> esito=new ArrayList<>(numCaratteri);
            contaCaratteri(parolaGiusta, conta);
            contaVerdi(parolaGiusta, s, conta, esito);
            for(int i=0; i<numCaratteri; i++) /**i: controllo parola paroliere */
            {
                if(conta.get(i)>0)
                {
                    for(int j=0; j<numCaratteri; j++) /**j: controllo parola utente */
                    {
                        if(esito.get(j)!=1)
                        {
                            if(s.charAt(j)!=parolaGiusta.charAt(i))
                                esito.set(i, -1);
                            else
                                esito.set(i, 0);
                        }
                    }
                }
            }
        }
        /**Implementati i controlli sulla correttezza della parola inserita e sullo stato del gioco
         * Da sviluppare: 
         * Colori delle lettere;
         * Implementazione della stampa a video.
        */
         
    }
    private void contaCaratteri(String parolaGiusta, ArrayList<Integer> conta){}
    private void contaVerdi(String parolaGiusta, String parolaUtente, ArrayList<Integer> conta, ArrayList<Integer> esito)
    {
        for(int i=0; i<numCaratteri; i++)
        {
            if(parolaGiusta.charAt(i)==parolaUtente.charAt(i))
            {
                esito.set(i, 1);
                decrementa(parolaGiusta.charAt(i), conta);
            }
        }
    }
    private void decrementa(char carattere, ArrayList<Integer> conta){}
}

