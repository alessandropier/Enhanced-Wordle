package it.uniba.app;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <<Controller>>
 * La classe Controller gestisce
 * il gioco in tutti i suoi aspetti e funzionalità.
 * */
public final class Controller {
    /**Numero massimo di tentativi.*/
    private static final int MAXTENTATIVI = 6;
    /**Numero massimo di caratteri.*/
    private static final int NUMCARATTERI = 5;
    /**Indica se è in corso una partita. */
    private static boolean flagGioca = false;

    /**Costruttore. */
    private Controller() {
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
    /**
     * get di flagGioca.
     * @return flagGioca
     */
    public static boolean getFlagGioca() {
        return flagGioca;
    }

    /**Imposta una nuova parola segreta da indovinare.
     * @param nuovaParola parola da impostare come parola segreta
     * @param p paroliere
     * NOTA: questo codice serve a controllare che "nuovaParola" sia corretta
     * perché prima veniva inserita dall'utente, ora non più quindi può essere
     * snellito. Comunque, lo mantengo perché potrei 
     * voler inserire la possibilità
     * di aggiungere una parola (tramite GUI) ed 
     * inserirla nel PROPRIO file .txt
     * memorizzato localmente per personalizzare la propria esperienza di gioco
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
        g.setTentativi(0);
        if (p.getParolaSegreta() == null) {
            System.out.println("Parola segreta non impostata."
            + " Impossibile giocare.");
        } else if (g.getTentativi() == 0) {
            // Reset della matrice
            m.azzera(NUMCARATTERI);
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

                System.out.println("\u001B[32m" + "Complimenti! Hai indovinato la parola!" + "\u001B[0m");
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
                    System.out.println("La parola segreta era: " + "\u001B[1m" + "\u001B[31m" + p.getParolaSegreta() + "\n" + "\u001B[0m" + "\u001B[0m");
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
                System.out.println("Uscita in corso...");
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
    public static void wordle(final String s, final Giocatore g,
     final Paroliere p, final Matrice m) {
        String comando = s.toLowerCase();
        switch (comando) {
            case "/nuova":
                List<String> words = new ArrayList<>();
                
                // Carica il file tramite il ClassLoader per supportare l'esecuzione da file .jar
                InputStream inputStream = App.class.getClassLoader().getResourceAsStream("parole.txt");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    
                    if (inputStream == null) {
                        System.out.println("Errore: file delle parole non trovato nel JAR!");
                        return;
                    }

                    String linea_letta;
                    while ((linea_letta = reader.readLine()) != null) {
                        if (linea_letta.length() == 5) {
                            words.add(linea_letta);
                        }
                    }
                    // (to delete)
                    //System.out.println("🟢 SUCCESSO: Lette " + words.size() + " parole valide!");
                    } catch (Exception e) {
                        System.err.println("🔴 Errore nella lettura del file parole.txt: " + e.getMessage());
                    }

                // Controllo di sicurezza: se la lista è vuota, blocchiamo l'esecuzione prima del crash
                if (words.isEmpty()) {
                    System.err.println("Impossibile continuare: la lista delle parole è vuota!");
                    return;
                }

                // (to delete) Stampa delle Parole
                //System.out.println(words);

                // Generazione numero casuale
                Random random = new Random();
                int randomIndex = random.nextInt(words.size());

                // Prende la parola corrispondente all'indice generato
                String nuova_parola = words.get(randomIndex);

                // Parola assegnata al paroliere
                p.setParolaSegreta(nuova_parola);

                // (to delete) for quick development
                // System.out.println("La Parola Segreta è: " + p.getParolaSegreta());

                // Perfezionamento della parola con il metodo "nuova"
                nuova(nuova_parola, p);
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
