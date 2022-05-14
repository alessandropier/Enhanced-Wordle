package it.uniba.app;

/**<<Entity>>*/
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
        parolaSegreta=p;
    }

    
}