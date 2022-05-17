package it.uniba.app;
import java.io.*; //le librerie(PACKAGE) contengono classi. 
public class MyInput
{
    public static String leggiStringa(String msg) //metodo di classe
    {
        BufferedReader tastiera=new BufferedReader(new InputStreamReader(System.in)); 
        //classe del package java.io, SI FA COSì PER NON OCCUPARE SPAZIO INUTILE PER IL READER
        boolean err;
        String aux=null; //aux: variabile oggetto di classe String che contiene un indirizzo, quello del primo carattere della stringa. Per questo posso anche inizializzarla null
        do
        {
            err=false;
            System.out.print(msg+": ");            
            try
            {
                aux=tastiera.readLine();
            }
            catch(Exception e)
            {
                err=true;
                System.out.println("Errore dell'input dei dati");
            }
        } while(err);
        return aux;
    }
}