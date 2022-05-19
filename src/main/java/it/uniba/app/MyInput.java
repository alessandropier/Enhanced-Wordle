package it.uniba.app;
import java.io.*; 

/**La classe MyInput è stata progettata con l'obiettivo di semplificare le operazioni di input delle stringhe, affinchè la stringa rispetti i criteri definiti */
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