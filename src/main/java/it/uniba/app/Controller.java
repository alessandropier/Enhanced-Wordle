package it.uniba.app;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <<Controller>>
 * La classe Controller gestisce
 * il gioco in tutti i suoi aspetti e funzionalità.
 * */
public final class Controller {
    /**Numero massimo di tentativi.*/
    private static int MAXTENTATIVI = 6;
    /**Numero massimo di caratteri.*/
    private static int NUMCARATTERI = 5;
    /**Indica se è in corso una partita. */
    private static boolean flagGioca = false;
    /**Variabile per la generazione del numero
     *  ranom per la parola del Paroliere.*/
    private static final Random RANDOM = new Random();

    /** Cache in memoria per evitare di rileggere i file delle parole consentite ad ogni tentativo. 
     *  La chiave è la lunghezza della parola (es. 5, 6...), il valore è il set delle parole. */
    private static final java.util.Map<Integer, java.util.Set<String>> CACHE_CONSENTITE = new java.util.HashMap<>();

    /**Costruttore. */
    private Controller() {
    }

    /**
     * Imposta la lunghezza dei caratteri desiderata per la partita.
     * @param num numero di caratteri (es. da 5 a 9)
     */
    public static void setNumCaratteri(final int num) {
        if (num >= 5 && num <= 9) {
            NUMCARATTERI = num;

            // PRE-CARICAMENTO: Popola la cache in anticipo in background
            // così quando l'utente farà il primo tentativo sarà già tutto in memoria e evitiamo
            // qualsiasi rischio di micro-lag (già improbabili).
            caricaCacheConsentite(NUMCARATTERI);
        }
    }

    /**
     * Imposta il numero massimo di tentativi desiderato per la partita.
     * @param num numero di tentativi (es. da 6 a 10)
     */
    public static void setMaxTentativi(final int num) {
        if (num >= 6 && num <= 10) {
            MAXTENTATIVI = num;
        }
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
            m.azzera(MAXTENTATIVI, NUMCARATTERI);
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
                ArrayList<Integer> esitoVittoria = new ArrayList<>(NUMCARATTERI);
                for (int i = 0; i < NUMCARATTERI; i++) {
                    esitoVittoria.add(1);
                }
                m.impostaColore(esitoVittoria, g.getTentativi());

                m.stampaMatrice(MAXTENTATIVI, NUMCARATTERI);
                //p.setParolaSegreta(null);
                //g.setTentativi(0);
                //m.azzera(NUMCARATTERI);
                flagGioca = false;

                System.out.println("\u001B[32m" + "Complimenti! Hai indovinato la parola!" + "\u001B[0m");
            } else if (!flagCorrect && !flagLength && flagGioca) {
                String parolaGiusta = p.getParolaSegreta();
                ArrayList<Integer> esito = new ArrayList<>(NUMCARATTERI);
                for (int i = 0; i < NUMCARATTERI; i++) {
                    esito.add(2);
                }
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
                    //p.setParolaSegreta(null);
                    //g.setTentativi(0);
                    //m.azzera(NUMCARATTERI);
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
                    m.azzera(MAXTENTATIVI, NUMCARATTERI);
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
                
                // 1. Carica il file dinamico in base alla lunghezza (es. parole_5.txt, parole_6.txt)
                String nomeFileInterno = "parole_" + NUMCARATTERI + ".txt";
                InputStream inputStream = App.class.getClassLoader().getResourceAsStream(nomeFileInterno);
                
                if (inputStream == null) {
                    System.out.println("Errore: file " + nomeFileInterno + " non trovato nel JAR!");
                    return;
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    String linea_letta;
                    while ((linea_letta = reader.readLine()) != null) {
                        linea_letta = linea_letta.trim().toUpperCase();
                        if (linea_letta.length() == NUMCARATTERI) {
                            words.add(linea_letta);
                        }
                    }
                    // (to delete)
                    // System.out.println("🟢 SUCCESSO: Lette " + words.size() + " parole valide!");
                } catch (Exception e) {
                    System.err.println("Errore nella lettura del file " + nomeFileInterno + ": " + e.getMessage());
                }

                // 2. Carica il file esterno specifico per la lunghezza (es. parole_extra_5.txt)
                java.io.File extraFile = getFileParoleExtra();
                if (extraFile.exists()) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(extraFile), StandardCharsets.UTF_8))) {
                        String linea_letta;
                        while ((linea_letta = reader.readLine()) != null) {
                            linea_letta = linea_letta.trim().toUpperCase();
                            if (linea_letta.length() == NUMCARATTERI && linea_letta.matches("[A-Z]+")) {
                                if (!words.contains(linea_letta)) {
                                    words.add(linea_letta);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Errore nella lettura del file extra: " + e.getMessage());
                    }
                }

                // (to delete) for testing
                //System.out.println("🟢 SUCCESSO: Lette " + words.size() + " parole valide!");
                //System.out.println(words);

                // Controllo di sicurezza: se la lista è vuota, blocchiamo l'esecuzione prima del crash
                if (words.isEmpty()) {
                    System.err.println("Impossibile continuare: la lista delle parole è vuota!");
                    return;
                }

                // (to delete) Stampa delle Parole
                //System.out.println(words);

                int randomIndex = RANDOM.nextInt(words.size());
                String nuova_parola = words.get(randomIndex);
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

    /**
     * Controlla se una parola esiste già nel file interno o nel file extra.
     * @param parola parola da verificare
     * @return true se esiste, false altrimenti
     */
    public static boolean esisteParola(String parola) {
        if (parola == null) {
            return false;
        }
        parola = parola.trim().toUpperCase();
        List<String> tutteLeParole = new ArrayList<>();

        // 1. Controlla nel file interno dinamico (es. parole_5.txt)
        String nomeFileInterno = "parole_" + NUMCARATTERI + ".txt";
        InputStream inputStream = App.class.getClassLoader().getResourceAsStream(nomeFileInterno);
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    linea = linea.trim().toUpperCase();
                    if (linea.length() == NUMCARATTERI) {
                        tutteLeParole.add(linea);
                    }
                }
            } catch (Exception ignored) {}
        }

        // 2. Controlla nel file esterno specifico (es. parole_extra_5.txt)
        java.io.File extraFile = getFileParoleExtra();
        if (extraFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(extraFile), StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    linea = linea.trim().toUpperCase();
                    if (linea.length() == NUMCARATTERI) {
                        tutteLeParole.add(linea);
                    }
                }
            } catch (Exception ignored) {}
        }

        return tutteLeParole.contains(parola);
    }

    /**
     * Aggiunge una parola personalizzata salvandola nel file locale "parole_extra.txt"
     * solo se non è già presente in nessun dizionario.
     * @param nuovaParola parola inserita dall'utente
     * @return true se aggiunta con successo, false se già esiste o non valida
     */
    public static boolean aggiungiParolaExtra(String nuovaParola) {
    if (nuovaParola == null) {
        return false;
    }
    nuovaParola = nuovaParola.trim().toUpperCase();

    // Validazione lunghezza e caratteri
    if (nuovaParola.length() != NUMCARATTERI) {
        return false;
    }
    for (int i = 0; i < NUMCARATTERI; i++) {
        if (nuovaParola.charAt(i) < 'A' || nuovaParola.charAt(i) > 'Z') {
            return false;
        }
    }

    // Verifica preliminare di duplicazione su entrambi i file
    if (esisteParola(nuovaParola)) {
        return false;
    }

    java.io.File extraFile = getFileParoleExtra();
    boolean needsNewline = false;

    // 1. Controlliamo se l'ultimo carattere del file non è un invio
    if (extraFile.exists() && extraFile.length() > 0) {
        try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(extraFile, "r")) {
            raf.seek(extraFile.length() - 1);
            int lastByte = raf.read();
            if (lastByte != '\n' && lastByte != '\r') {
                needsNewline = true; // Manca l'invio alla fine
            }
        } catch (Exception e) {
            // problema di lettura -> ignoriamo o gestiamo
        }
    }

    // 2. Scrittura in append su "parole_extra.txt" in codifica UTF-8
    try (java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(
            new java.io.FileOutputStream(extraFile, true), StandardCharsets.UTF_8);
         java.io.PrintWriter pw = new java.io.PrintWriter(writer)) {
        
        // Se l'utente è entrato nel file per cambiare qualcosa e si è dimenticato
        // l'invio, me ne occupo io aggiungendolo
        if (needsNewline) {
            pw.println();
        }
        
        pw.println(nuovaParola);
        return true;
    } catch (Exception e) {
        System.err.println("Errore nel salvataggio della parola extra: " + e.getMessage());
        return false;
    }
}

/**
     * Restituisce il file "parole_extra.txt" posizionato nella cartella 
     * dei dati utente del sistema operativo (es. AppData/Roaming/.wordle_data).
     * @return File di parole extra
     */
    private static java.io.File getFileParoleExtra() {
        String userHome = System.getProperty("user.home");
        java.io.File dir = new java.io.File(userHome, ".wordle_data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Nome del file extra dipendente dalla lunghezza corrente
        return new java.io.File(dir, "parole_extra_" + NUMCARATTERI + ".txt");
    }

    /**
     * Verifica se una parola è presente nel dizionario delle parole consentite,
     * sfruttando una cache in memoria per le performance.
     * @param parola parola da verificare
     * @return true se consentita, false altrimenti
     */
    public static boolean isConsentita(String parola) {
        if (parola == null) {
            return false;
        }
        parola = parola.trim().toUpperCase();
        int lunghezza = parola.length();
        
        // Assicuriamoci che la cache per questa lunghezza esista (fallback di sicurezza)
        // altrimenti la carichiamo
        if (!CACHE_CONSENTITE.containsKey(lunghezza)) {
            caricaCacheConsentite(lunghezza);
        }
        
        // Controllo istantaneo in O(1)
        return CACHE_CONSENTITE.get(lunghezza).contains(parola);
    }

    /**
     * Carica in anticipo il file delle parole consentite nella cache per una data lunghezza.
     * @param lunghezza lunghezza della parola
     */
    private static void caricaCacheConsentite(int lunghezza) {
        // Se è già presente in cache, evitiamo di ricaricarlo inutilmente
        if (CACHE_CONSENTITE.containsKey(lunghezza)) {
            return;
        }

        java.util.Set<String> setParole = new java.util.HashSet<>();
        String nomeFile = "parole_consentite_" + lunghezza + ".txt";
        
        InputStream inputStream = App.class.getClassLoader().getResourceAsStream(nomeFile);
        if (inputStream != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    linea = linea.trim().toUpperCase();
                    if (linea.length() == lunghezza) {
                        setParole.add(linea);
                    }
                }
            } catch (IOException e) {
                System.err.println("Errore lettura file consentite " + nomeFile + ": " + e.getMessage());
            }
        } else {
            System.err.println("File delle parole consentite non trovato nelle risorse: " + nomeFile);
        }
        
        // Salviamo il set nella cache statica
        CACHE_CONSENTITE.put(lunghezza, setParole);
    }
}
