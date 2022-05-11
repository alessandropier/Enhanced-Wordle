package it.uniba.app;

public class Controller {
    private final int maxTentativi=6;
    private final int numCaratteri=5;

    /**Costruttore*/
    public Controller()
    {}

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
}

