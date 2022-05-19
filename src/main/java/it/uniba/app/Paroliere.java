package it.uniba.app;

/**<<Entity>>*/
/**La classe Paroliere gestisce l'entità paroliere in tutti i suoi aspetti */
public class Paroliere {
    private String parolaSegreta;

    /**Costruttore*/
    public Paroliere()
    {
        parolaSegreta=null;
    }

    /**Get parola segreta*/
    public String getParolaSegreta()
    {
        return parolaSegreta;
    }

    /**Set parola segreta*/
    public void setParolaSegreta(String p)
    {
        parolaSegreta = p;
    }

    
}