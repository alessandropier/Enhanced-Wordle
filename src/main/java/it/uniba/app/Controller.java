package it.uniba.app;
import java.util.*;

/**<<Controller>>*/
/**La classe Controller gestisce il gioco in tutti i suoi aspetti e funzionalità.*/
public class Controller {
    private static final int maxTentativi = 6;
    private static final int numCaratteri = 5;
    private static boolean flagGioca = false;

    public static int getMaxTentativi() 
    {
        return maxTentativi;
    }

    public static int getNumCaratteri() 
    {
        return numCaratteri;
    }

    /**Imposta una nuova parola segreta da indovinare */
    public static void Nuova(String nuovaParola, Paroliere p)
    {
        boolean flagLength = false, flagCorrect=false;

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

        for(int i=0; i<numCaratteri && !flagCorrect && !flagLength; i++)
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
    public static void Mostra(Paroliere p)
    {
        if(p.getParolaSegreta()==null){
            System.out.println("Parola segreta non impostata.");
        }else{
            System.out.println("La parola segreta è "+p.getParolaSegreta()+".");
        }
    }

    /**Permette di iniziare una nuova partita */
    public static void Gioca(Giocatore g, Paroliere p, Matrice m)
    {
        if(p.getParolaSegreta()==null)
        {
            System.out.println("Parola segreta non impostata. Impossibile giocare.");
        }
        else
        if(g.getTentativi() == 0)
        {
            /**Stampa della matrice dei tentativi*/    
            m.stampaMatrice(maxTentativi, numCaratteri);
            flagGioca = true; /** Flag di gioco = true per indicare che il giocatore ha avviato una partita*/
        }
    }

    /**Permette di effettuare un tentativo per indovinare la parola segreta */
    public static void Tentativo(Giocatore g, String s, Paroliere p, Matrice m)
    {
        boolean flagCorrect = false;
        boolean flagLength = false;

        if(flagGioca)
        {
            if(g.getTentativi()<maxTentativi)
            {
                if(s.length()<numCaratteri)  
                {
                    System.out.println("Parola troppo corta. La parola deve contenere " + numCaratteri + " lettere.");
                    flagLength = true;
                }         
                else if(s.length()>numCaratteri)
                {
                    System.out.println("Parola troppo lunga. La parola deve contenere " + numCaratteri + " lettere.");
                    flagLength = true;
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
                System.out.println("La parola segreta è " + p.getParolaSegreta() + ".");
                p.setParolaSegreta(null);
                g.setTentativi(0);
                m.azzera(numCaratteri);
                flagGioca = false;
            }
            
            /** Verifica se la parola è stata indovinata */
            if(s.equals(p.getParolaSegreta()))
            {
                System.out.println("Parola segreta indovinata in: " + (g.getTentativi() + 1) + " tentativi");
                m.setTentativi(g.getTentativi(), s);
                m.impostaColore(new ArrayList<Integer>(Arrays.asList(1,1,1,1,1)), g.getTentativi());

                m.stampaMatrice(maxTentativi, numCaratteri);
                p.setParolaSegreta(null);
                g.setTentativi(0);
                m.azzera(numCaratteri);
                flagGioca = false;
            }
            else if(!flagCorrect && !flagLength && flagGioca)
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

                g.setTentativi(g.getTentativi() + 1);

                if(g.getTentativi() >= maxTentativi)
                {
                    System.out.println("Numero massimo di tentativi raggiunto. Avvia una nuova partita.");
                    System.out.println("La parola segreta è " + p.getParolaSegreta() + ".");
                    p.setParolaSegreta(null);
                    g.setTentativi(0);
                    m.azzera(numCaratteri);
                    flagGioca = false;
                }
            }
        }
        else 
        {
            System.out.println("Digitare '/gioca' per iniziare una partita.");
        }
    }

    /**Trova i caratteri uaguali tra la parola segreta e quella insierita dal giocatore nel tentativo, poi imposta nel arraylist esito in corrispondenza del carattere identico 1 */
    private static void contaVerdi(String parolaGiusta, String parolaUtente, ArrayList<Integer> esito)
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
    private static int contaOccorrenze(String parola, char carattere)
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

    /**Data una  @param parola e un valore numerico che corrisponde a un colore (1=verde, 0=giallo), conta il numero di occorenze presenti in essa in corrispondenza di esito */
    private static int contaOccorrenzeColore(String parola, char carattere, ArrayList<Integer> esito, int val)
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

    /**La funzione chiede conferma e in caso positivo permette di abbandonare la partita in corso*/
    public static void abbandona(Paroliere p, Giocatore g, Matrice m)
    {
        boolean flag=false;
        if((p.getParolaSegreta()!=null) && g.getTentativi()<maxTentativi) /**Controllo sulla presenza di una partita in corso */
        {
            do
            {
                flag=false;
                String prova = MyInput.leggiStringa("Abbandonare la partita?");
                //Chiede input e lo salva nella stringa "risposta"
                String risposta=prova.toUpperCase();

                if(risposta.equals("SI"))
                {
                    p.setParolaSegreta(null);
                    g.setTentativi(0);
                    m.azzera(numCaratteri);
                    flagGioca = false;
                    System.out.println("Partita abbandonata con successo.");
                }
                else if(risposta.equals("NO"))
                {
                    System.out.println("Partita non abbandonata, in attesa di comandi o nuovo tentativo...");
                }
                    else
                    {
                        flag=true;
                        System.out.println("Inserire SI o NO.");
                    }
            }while(flag);   
        }
        else 
        {
            System.out.println("Nessuna partita avviata.");
        }
    }

    /**La funzione chiede conferma e in caso positivo permette di chiudere l'applicazione */
    public static void esci(Paroliere p, Giocatore g)
    {
        boolean flag=false;
        
        do
        {
            flag=false;
            String prova = MyInput.leggiStringa("Uscire dal gioco?");
            //Chiede input e lo salva nella stringa "risposta"
            String risposta=prova.toUpperCase();

            if(risposta.equals("SI"))
            {
                p.setParolaSegreta(null);
                g.setTentativi(0);
                System.exit(0);
            }
            else if(risposta.equals("NO"))
            {
                System.out.println("In attesa di nuovi comandi o nuovo tentativo...");
            }
                else
                {
                    flag=true;
                    System.out.println("Inserire SI o NO.");
                }
        }while(flag);   
    }

    /**La funzione gestisce le esecuzioni dei comandi del Paroliere e del Giocatore e i vari tentativi */
    public static void wordle(String s, Giocatore g, Paroliere p, Matrice m)
    {
        String comando;
        String tentativo = null;

        s = s.toLowerCase();
        int end = s.indexOf(" ", 0);
        comando = s;
        
        if (end != -1 && s.substring(0, end).equals("/nuova")) 
        {
            comando = s.substring(0, end);
            tentativo = s.substring(end + 1);
        }
        
        switch(comando)
        {
            case "/nuova":
                if(tentativo != null){
                    Nuova(tentativo, p);
                }
                else{
                    System.out.println("Inserire una parola per il tentativo.");
                }
                break;
            
            case "/mostra":
                Mostra(p);
                break;
            
            case "/help":
                Help.stampaHelp();
                break;

            case "/gioca":
                Gioca(g, p, m);
                break;

            case "/abbandona":
                abbandona(p, g, m);
                break;

            case "/esci":
                esci(p, g);
                break;
            
            default:
                Tentativo(g, s, p, m);
                break;
        }
    }
}