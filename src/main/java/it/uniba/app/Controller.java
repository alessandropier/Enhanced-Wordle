package it.uniba.app;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <<Controller>>
 * La classe Controller gestisce
 * il gioco in tutti i suoi aspetti e funzionalità.
 * */
public class Controller {
    /**Numero massimo di tentativi.*/
    private static final int MAXTENTATIVI = 6;
    /**Numero massimo di caratteri.*/
    private static final int NUMCARATTERI = 5;
    /**Indica se è in corso una partita. */
    private static boolean flagGioca = false;

    /**Costruttore. */
    protected Controller() {
    }

    /**
     * get numero massimo tentativi.
     * @return MAXTENTATIVI
     */
    public static int getMaxTentativi() {
        return MAXTENTATIVI;
    }
    /**
     * get numero massimo caratteri.
     * @return NUMCARATTERI
     */
    public static int getNumCaratteri() {
        return NUMCARATTERI;
    }

    /**Imposta una nuova parola segreta da indovinare.
     * @param nuovaParola parola da impostare come parola segreta
     * @param p paroliere
    */
    public static void nuova(String nuovaParola, final Paroliere p) {
        boolean flagLength = false;
        boolean flagCorrect = false;

        if (nuovaParola.length() < NUMCARATTERI) {
            flagLength = true;
            System.out.println("Parola segreta troppo corta."
             + " La parola deve contenere "
             + NUMCARATTERI + " lettere.");
        } else if (nuovaParola.length() > NUMCARATTERI) {
            flagLength = true;
            System.out.println("Parola segreta troppo lunga."
             + " La parola deve contenere "
             + NUMCARATTERI + " lettere.");
        }
        nuovaParola = nuovaParola.toUpperCase();
        for (int i = 0; i < NUMCARATTERI && !flagCorrect && !flagLength; i++) {
            if (nuovaParola.charAt(i) < 'A' || nuovaParola.charAt(i) > 'Z') {
                flagCorrect = true;
            }
        }
        if (flagCorrect) {
            System.out.println("Parola segreta non valida."
            + "La parola può contenere solo caratteri alfabetici.");
        }
        if (!flagLength && !flagCorrect) {
            System.out.println("OK!");
            p.setParolaSegreta(nuovaParola);
        }
    }

    /**Mostra la parola segreta impostata.
     * @param p paroliere
    */
    public static void mostra(final Paroliere p) {
        if (p.getParolaSegreta() == null) {
            System.out.println("Parola segreta non impostata.");
        } else {
            System.out.println("La parola segreta è "
             + p.getParolaSegreta() + ".");
        }
    }

    /**
     * Permette di iniziare una nuova partita.
     * @param g giocatore
     * @param p paroliere
     * @param m matrice
     */
    public static void gioca(final Giocatore g,
     final Paroliere p, final Matrice m) {
        if (p.getParolaSegreta() == null) {
            System.out.println("Parola segreta non impostata."
            + " Impossibile giocare.");
        } else if (g.getTentativi() == 0) {
            //Stampa della matrice dei tentativi
            m.stampaMatrice(MAXTENTATIVI, NUMCARATTERI);
            //FlagGioca=true per indicare che giocatore ha avviato una partita
            flagGioca = true;
        }
    }

    /**
     * Permette di effettuare un tentativo per indovinare la parola segreta.
     * @param g giocatore
     * @param s stringa tentativo
     * @param p paroliere
     * @param m matrice
     */
    public static void tentativo(final Giocatore g,
     String s, final Paroliere p, final Matrice m) {
        boolean flagCorrect = false;
        boolean flagLength = false;

        if (flagGioca) {
            if (g.getTentativi() < MAXTENTATIVI) {
                if (s.length() < NUMCARATTERI) {
                    System.out.println("Parola troppo corta."
                     + " La parola deve contenere "
                     + NUMCARATTERI + " lettere.");
                    flagLength = true;
                } else if (s.length() > NUMCARATTERI) {
                    System.out.println("Parola troppo lunga."
                     + " La parola deve contenere "
                     + NUMCARATTERI + " lettere.");
                    flagLength = true;
                } else {
                    s = s.toUpperCase();

                    for (int i = 0; i < NUMCARATTERI && !flagCorrect; i++) {
                        if (s.charAt(i) < 'A' || s.charAt(i) > 'Z') {
                            flagCorrect = true;
                        }
                    }
                    if (flagCorrect) {
                        System.out.println("Parola non valida. "
                        + "La parola può contenere solo caratteri alfabetici.");
                    }
                }
            } else {
                System.out.println("Numero massimo di tentativi raggiunto."
                 + " Avvia una nuova partita.");
                System.out.println("La parola segreta è "
                 + p.getParolaSegreta() + ".");
                p.setParolaSegreta(null);
                g.setTentativi(0);
                m.azzera(NUMCARATTERI);
                flagGioca = false;
            }
            // Verifica se la parola è stata indovinata
            if (s.equals(p.getParolaSegreta())) {
                System.out.println("Parola segreta indovinata in: "
                 + (g.getTentativi() + 1) + " tentativi");
                m.setTentativi(g.getTentativi(), s);
                m.impostaColore(new ArrayList<Integer>(
                    Arrays.asList(1, 1, 1, 1, 1)), g.getTentativi());

                m.stampaMatrice(MAXTENTATIVI, NUMCARATTERI);
                p.setParolaSegreta(null);
                g.setTentativi(0);
                m.azzera(NUMCARATTERI);
                flagGioca = false;
            } else if (!flagCorrect && !flagLength && flagGioca) {
                String parolaGiusta = p.getParolaSegreta();
                ArrayList<Integer> esito = new ArrayList<>(
                    Arrays.asList(2, 2, 2, 2, 2));
                contaVerdi(parolaGiusta, s, esito);
                for (int i = 0; i < NUMCARATTERI; i++) {
                    if (esito.get(i) != 1) {
                        if (contaOccorrenze(parolaGiusta, s.charAt(i))
                         - (contaOccorrenzeColore(parolaGiusta,
                         s.charAt(i), esito, 1)
                         + contaOccorrenzeColore(s, s.charAt(i),
                         esito, 0)) > 0) {
                            esito.set(i, 0);
                        }
                    }
                }

                m.setTentativi(g.getTentativi(), s);
                m.impostaColore(esito, g.getTentativi());
                m.stampaMatrice(MAXTENTATIVI, NUMCARATTERI);

                g.setTentativi(g.getTentativi() + 1);

                if (g.getTentativi() >= MAXTENTATIVI) {
                    System.out.println("Numero massimo di tentativi"
                     + " raggiunto. Avvia una nuova partita.");
                    System.out.println("La parola segreta è "
                     + p.getParolaSegreta() + ".");
                    p.setParolaSegreta(null);
                    g.setTentativi(0);
                    m.azzera(NUMCARATTERI);
                    flagGioca = false;
                }
            }
        } else {
            System.out.println("Digitare '/gioca' per iniziare una partita.");
        }
    }

    /**
     * Trova i caratteri uaguali tra la parola segreta e quella insierita
     *  dal giocatore nel tentativo, poi imposta nel arraylist esito
     *  in corrispondenza del carattere identico.
     * @param parolaGiusta parola segreta impostata nella partita
     * @param parolaUtente parola del tentativo
     * @param esito rappresenta la presenza di un carattere
     * */
    private static void contaVerdi(final String parolaGiusta,
     final String parolaUtente, final ArrayList<Integer> esito) {
        for (int i = 0; i < NUMCARATTERI; i++) {
            if (parolaGiusta.charAt(i) == parolaUtente.charAt(i)) {
                esito.set(i, 1);
            }
        }
    }

    /**
     * Data una parola e un char conta le sue occorenze.
     * @param parola parola da controllare
     * @param carattere char del quale contare le occorenza
     * @return numero occorenze
     * */
    private static int contaOccorrenze(final String parola,
     final char carattere) {
        int occorrenza = 0;
        for (int i = 0; i < parola.length(); i++)  {
            if (parola.charAt(i) == carattere) {
                occorrenza++;
            }
        }
        return occorrenza;
    }

    /**
     * Data una parola e un valore numerico che corrisponde
     * a un colore (1=verde, 0=giallo),
     * conta il numero di occorenze presenti in essa
     * in corrispondenza di esito.
     * @param parola parola da controllare
     * @param carattere char del quale contare occorenze
     * @param esito rappresenta la presenza di un carattere
     * @param val indica per quale colore viene effetuato il controllo
     * @return numero occorenze
     * */
    private static int contaOccorrenzeColore(final String parola,
     final char carattere, final ArrayList<Integer> esito, final int val) {
        int occorrenza = 0;
        for (int i = 0; i < parola.length(); i++) {
            if (parola.charAt(i) == carattere && esito.get(i) == val) {
                occorrenza++;
            }
        }
        return occorrenza;
    }

    /**
     * La funzione chiede conferma e in caso positivo
     * permette di abbandonare la partita in corso.
     * @param g giocatore
     * @param p paroliere
     * @param m matrice
     * */
    public static void abbandona(final Paroliere p, final Giocatore g,
     final Matrice m) {
        boolean flag = false;
        //Controllo sulla presenza di una partita in corso
        if ((p.getParolaSegreta() != null) && g.getTentativi() < MAXTENTATIVI) {
            do {
                flag = false;
                String prova = MyInput.leggiStringa("Abbandonare la partita?");
                //Chiede input e lo salva nella stringa "risposta"
                String risposta = prova.toUpperCase();

                if (risposta.equals("SI")) {
                    p.setParolaSegreta(null);
                    g.setTentativi(0);
                    m.azzera(NUMCARATTERI);
                    flagGioca = false;
                    System.out.println("Partita abbandonata con successo.");
                } else if (risposta.equals("NO")) {
                    System.out.println("Partita non abbandonata,"
                     + " in attesa di comandi o nuovo tentativo...");
                } else {
                    flag = true;
                    System.out.println("Inserire SI o NO.");
                }
            } while (flag);
        } else {
            System.out.println("Nessuna partita avviata.");
        }
    }

    /**
     * La funzione chiede conferma e
     * in caso positivo permette di chiudere l'applicazione.
     * @param p paroliere
     * @param g giocatore
     * */
    public static void esci(final Paroliere p, final Giocatore g) {
        boolean flag = false;
        do {
            flag = false;
            String prova = MyInput.leggiStringa("Uscire dal gioco?");
            //Chiede input e lo salva nella stringa "risposta"
            String risposta = prova.toUpperCase();
            if (risposta.equals("SI")) {
                p.setParolaSegreta(null);
                g.setTentativi(0);
                System.exit(0);
            } else if (risposta.equals("NO")) {
                System.out.println("In attesa di nuovi"
                 + " comandi o nuovo tentativo...");
            } else {
                flag = true;
                System.out.println("Inserire SI o NO.");
            }
        } while (flag);
    }

    /**
     * La funzione gestisce le esecuzioni dei comandi
     * del Paroliere e del Giocatore e i vari tentativi.
     * @param s comando
     * @param g giocatore
     * @param p paroliere
     * @param m matrice
     * */
    public static void wordle(String s, final Giocatore g,
     final Paroliere p, final Matrice m) {
        String comando;
        String tentativo = null;
        s = s.toLowerCase();
        int end = s.indexOf(" ", 0);
        comando = s;
        if (end != -1 && s.substring(0, end).equals("/nuova")) {
            comando = s.substring(0, end);
            tentativo = s.substring(end + 1);
        }
        switch (comando) {
            case "/nuova":
                if (tentativo != null) {
                    nuova(tentativo, p);
                } else {
                    System.out.println("Inserire una parola per il tentativo.");
                }
                break;
            case "/mostra":
                mostra(p);
                break;
            case "/help":
                Help.stampaHelp();
                break;
            case "/gioca":
                gioca(g, p, m);
                break;
            case "/abbandona":
                abbandona(p, g, m);
                break;
            case "/esci":
                esci(p, g);
                break;
            default:
                tentativo(g, s, p, m);
                break;
        }
    }
}
