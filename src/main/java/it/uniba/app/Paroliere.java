package it.uniba.app;

/**<<Entity>>*/
public class Paroliere {
    private String parolaSegreta;

    /**Costruttore*/
    public Paroliere()
    {
        parolaSegreta=null;
    }

    /**Set parola segreta*/
    public String getParolaSegreta()
    {
        return parolaSegreta;
    }

    /**Get parola segreta*/
    public void setParolaSegreta(String p)
    {
        parolaSegreta=p;
    }

    
}