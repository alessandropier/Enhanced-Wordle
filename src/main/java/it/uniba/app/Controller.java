package it.uniba.app;
import java.util.*;

/**<<Controller>>*/
public class Controller {
    private final int maxTentativi = 6;
    private final int numCaratteri = 5;

    /**<<Costruttore>>*/
    public Controller()
    {}

    public int getMaxTentativi() 
    {
        return maxTentativi;
    }

    public int getNumCaratteri() 
    {
        return numCaratteri;
    }

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
        
        nuovaParola=nuovaParola.toUpperCase();

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
    public void Tentativo(Giocatore g, String s, Paroliere p, Matrice m)
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
                    s=s.toUpperCase();

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
            
            /** Verifica se la parola è stata indovinata */
            if(s.equals(p.getParolaSegreta()))
            {
                System.out.println("Parola segreta indovinata in: " + g.getTentativi() + " tentativi.");
                m.stampaMatrice(maxTentativi, numCaratteri);
            }
            else
            {
                String parolaGiusta = p.getParolaSegreta();
                ArrayList<Integer> esito = new ArrayList<>(Arrays.asList(2,2,2,2,2));
                contaVerdi(parolaGiusta, s, esito);
                for(int i=0; i<numCaratteri; i++)
                {
                    if(esito.get(i)!=1)
                    {
                        if(contaOccorrenze(parolaGiusta, s.charAt(i))-(contaOccorrenzeColore(parolaGiusta, s.charAt(i), esito, 1)+ contaOccorrenzeColore(s, s.charAt(i), esito, 0))>0)
                        {
                            esito.set(i, 0);
                        }
                    }
                }

                m.setTentativi(g.getTentativi(), s);
                m.impostaColore(esito, g.getTentativi());
                m.stampaMatrice(maxTentativi, numCaratteri);
            }
        }
        else 
        {
            System.out.println("Parola segreta mancante. Imposta una parola segreta per giocare.");
        }
    }

    /**Trova i caratteri uaguali tra la parola segreta e quella insierita dal giocatore nel tentativo, poi imposta nel arraylist esito in corrispondenza del carattere identico 1 */
    private void contaVerdi(String parolaGiusta, String parolaUtente, ArrayList<Integer> esito)
    {
        for(int i=0; i<numCaratteri; i++)
        {
            if(parolaGiusta.charAt(i) == parolaUtente.charAt(i))
            {
                esito.set(i, 1);
            }
        }
    }

    /**Data una parola e un char conta le sue occorenze */
    private int contaOccorrenze(String parola, char carattere)
    {
        int occorrenza=0;
        for(int i=0; i<parola.length(); i++)
        {
            if(parola.charAt(i)==carattere)
            {
                occorrenza++;
            }
        }
        return occorrenza;
    }

    /**Data una parola e un valore numerico che corrisponde a un colore (1=verde, 0=giallo), conta il numero di occorenze presenti in essa in corrispondenza di esito */
    private int contaOccorrenzeColore(String parola, char carattere, ArrayList<Integer> esito, int val)
    {
        int occorrenza=0;
        for(int i=0; i<parola.length(); i++)
        {
            if(parola.charAt(i)==carattere && esito.get(i)==val)
            {
                occorrenza++;
            }
        }
        return occorrenza;
    }
}