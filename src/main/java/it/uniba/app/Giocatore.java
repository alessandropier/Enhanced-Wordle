package it.uniba.app;

/**<<Entity>>*/
public class Giocatore {
    private int tentativi;
    
    /**Costruttore*/
    public Giocatore()      
    {
        tentativi=0;
    }

    /**Set numero tentativi*/
    public int getTentativi()
    {
        return tentativi;
    }

    /**Get numero tentativi*/
    public void setTentativi(int t)
    {
        tentativi=t;
    }

    /**Incrementa il numero di tentativi*/
    public void incrementaTentativi()
    {
        setTentativi(getTentativi()+1);
    }
}
