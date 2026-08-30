package it.uniba.app;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class WordleFrame extends JFrame {

    private static int RIGHE;
    private static int COLONNE;

    private JLabel[][] celleGrid;
    private JLabel lblParolaSegreta;
    private final Map<Character, JButton> tastiVirtuali = new HashMap<>();

    private JPanel panelBottoni;
    private JPanel panelGriglia;
    private JPanel panelTastiera;
    private JPanel panelSud;
    private JButton btnNuovaPartita;
    private JButton btnMostra;
    private JButton btnEsci;
    private JToggleButton tglModalita;
    private JButton btnAggiungi;
    private JButton btnAiuto;
    private JPanel separatore;

    private int rigaCorrente = 0;
    private int colonnaCorrente = 0;

    private boolean tastieraBloccata = true;
    private boolean hasWon = false;
    private boolean wasSecretWordShown = false;

    private final Giocatore giocatore;
    private final Paroliere paroliere;
    private final Matrice matrice;

    // Palette Notte
    private final Color SFONDO_NOTTE = new Color(48, 52, 55);
    private final Color BOTTONE_NOTTE = new Color(65, 70, 75);
    private final Color BORDO_BOTTONE_NOTTE = new Color(95, 100, 105);
    private final Color BORDO_CELLA_NOTTE = new Color(70, 75, 80);

    // Palette Giorno
    private final Color SFONDO_GIORNO = new Color(240, 240, 240);
    private final Color BOTTONE_GIORNO = Color.WHITE;
    private final Color TESTO_BOTTONE_GIORNO = new Color(30, 30, 30);
    private final Color BORDO_BOTTONE_GIORNO = new Color(180, 180, 180);
    private final Color BORDO_CELLA_GIORNO = new Color(200, 204, 208);

    public WordleFrame(final Giocatore g, final Paroliere p, final Matrice m) {
        this.giocatore = g;
        this.paroliere = p;
        this.matrice = m;

        RIGHE = Controller.getMaxTentativi();
        COLONNE = Controller.getNumCaratteri();

        setTitle("Wordle Java");
        setSize(620, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // --- 1. PANNELLO SUPERIORE CON I BOTTONI E IL TOGGLE ---
        panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));

        btnNuovaPartita = new JButton("NUOVA");
        btnMostra = new JButton("ARRENDITI");
        btnEsci = new JButton("ESCI");
        btnAggiungi = new JButton("AGGIUNGI PAROLA");
        btnAiuto = new JButton("AIUTO");
        tglModalita = new JToggleButton("Notte");

        styleButton(btnNuovaPartita);
        styleButton(btnMostra);
        styleButton(btnEsci);
        styleButton(btnAggiungi);
        styleButton(btnAiuto);
        styleButton(tglModalita);

        btnAggiungi.setPreferredSize(new Dimension(120, 32));
        btnAiuto.setPreferredSize(new Dimension(80, 32));
        tglModalita.setPreferredSize(new Dimension(55, 32));

        panelBottoni.add(btnNuovaPartita);
        panelBottoni.add(btnMostra);
        panelBottoni.add(btnEsci);

        separatore = new JPanel();
        separatore.setPreferredSize(new Dimension(2, 32));
        panelBottoni.add(separatore);

        panelBottoni.add(tglModalita);
        panelBottoni.add(btnAggiungi);
        panelBottoni.add(btnAiuto);
        add(panelBottoni, BorderLayout.NORTH);

        // --- 2. GRIGLIA DI TENTATIVI ---
        panelGriglia = new JPanel(new GridLayout(RIGHE, COLONNE, 6, 6));
        panelGriglia.setBorder(BorderFactory.createEmptyBorder(5, 40, 5, 40));

        celleGrid = new JLabel[RIGHE][COLONNE];

        for (int i = 0; i < RIGHE; i++) {
            for (int j = 0; j < COLONNE; j++) {
                JLabel cella = new JLabel("", JLabel.CENTER);
                cella.setFont(new Font("SansSerif", Font.BOLD, 26));
                cella.setOpaque(true);
                cella.setBackground(Color.WHITE);
                cella.setForeground(new Color(30, 30, 30));
                cella.setBorder(BorderFactory.createLineBorder(BORDO_CELLA_NOTTE, 2));

                celleGrid[i][j] = cella;
                panelGriglia.add(cella);
            }
        }
        add(panelGriglia, BorderLayout.CENTER);

        // --- 3. LABEL PAROLA SEGRETA E TASTIERA VIRTUALE IN BASSO ---
        lblParolaSegreta = new JLabel("", JLabel.CENTER);
        lblParolaSegreta.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblParolaSegreta.setVisible(false);
        lblParolaSegreta.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        panelTastiera = creaPannelloTastiera();

        panelSud = new JPanel(new BorderLayout());
        panelSud.add(lblParolaSegreta, BorderLayout.NORTH);
        panelSud.add(panelTastiera, BorderLayout.CENTER);
        add(panelSud, BorderLayout.SOUTH);

        // --- 4. GESTIONE EVENTI DEI BOTTONI ---
        btnNuovaPartita.addActionListener(e -> gestisciNuovaPartita());
        btnMostra.addActionListener(e -> gestisciMostraParola());
        btnEsci.addActionListener(e -> gestisciEsci());
        btnAggiungi.addActionListener(e -> gestisciAggiungiParola());
        btnAiuto.addActionListener(e -> mostraAiuto(false));

        tglModalita.addActionListener(e -> {
            boolean isNotte = tglModalita.isSelected();
            Preferences prefs = Preferences.userNodeForPackage(WordleFrame.class);
            prefs.putBoolean("dark_mode", isNotte);

            if (isNotte) {
                tglModalita.setText("Notte");
                applicaTema(true);
            } else {
                tglModalita.setText("Giorno");
                applicaTema(false);
            }
            requestFocusInWindow();
        });

        Preferences prefs = Preferences.userNodeForPackage(WordleFrame.class);
        boolean savedDarkMode = prefs.getBoolean("dark_mode", true);

        tglModalita.setSelected(savedDarkMode);
        tglModalita.setText(savedDarkMode ? "Notte" : "Giorno");
        applicaTema(savedDarkMode);

        // --- 5. ASCOLTATORE DELLA TASTIERA ---
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!tastieraBloccata) {
                    gestisciTastoPremuto(e);
                }
            }
        });

        setFocusable(true);
        controllaMostraAiutoAllAvvio();
    }

    private void controllaMostraAiutoAllAvvio() {
        Preferences prefs = Preferences.userNodeForPackage(WordleFrame.class);
        boolean nonMostrarePiu = prefs.getBoolean("nonMostrareAiuto", false);

        if (!nonMostrarePiu) {
            javax.swing.SwingUtilities.invokeLater(() -> mostraAiuto(true));
        }
    }

    private void mostraAiuto(boolean isAvvio) {
        boolean isNotte = tglModalita.isSelected();

        Color bgColore = isNotte ? SFONDO_NOTTE : Color.WHITE;
        Color fgColore = isNotte ? new Color(220, 220, 220) : new Color(34, 34, 34);
        Color bordoColore = isNotte ? BORDO_BOTTONE_NOTTE : BORDO_BOTTONE_GIORNO;
        Color bottoniBg = isNotte ? BOTTONE_NOTTE : BOTTONE_GIORNO;

        String textColorHex = isNotte ? "#DCDCDC" : "#222222";
        String h3ColorHex = isNotte ? "#5DADE2" : "#1b4f72";

        String messaggioHtml = "<html><body style='width: 580px; font-family: SansSerif; font-size: 14pt; color: " + textColorHex + "; padding: 0px;'>"
            + "<h3 style='color: " + h3ColorHex + "; font-size: 17pt; margin-top: 0px; margin-bottom: 6px;'>1. Regole di Wordle</h3>"
            + "<p style='margin-top: 0px; margin-bottom: 8px;'>L'obiettivo è indovinare la parola segreta nel minor numero di tentativi possibili. "
            + "Dopo ogni tentativo, ciascuna casella verrà colorata per darti un indizio sulla parola segreta.</p>"
            + "<table style='width: 100%; font-family: SansSerif; font-size: 14pt; color: " + textColorHex + "; margin-bottom: 10px;'>"
            + "<tr><td style='width: 30px; color: #6AAA64; font-size: 20pt; vertical-align: top;'>&#9632;</td><td><b>Verde</b>: La lettera è corretta e si trova nella posizione giusta.</td></tr>"
            + "<tr><td style='color: #C9B458; font-size: 20pt; vertical-align: top;'>&#9632;</td><td><b>Giallo</b>: La lettera è presente nella parola ma in una posizione errata.</td></tr>"
            + "<tr><td style='color: #787C7E; font-size: 20pt; vertical-align: top;'>&#9632;</td><td><b>Grigio</b>: La lettera non è presente nella parola segreta.</td></tr>"
            + "</table>"
            + "<h3 style='color: " + h3ColorHex + "; font-size: 17pt; margin-top: 12px; margin-bottom: 6px;'>2. Guida all'Interfaccia e Bottoni</h3>"
            + "<table style='width: 100%; font-family: SansSerif; font-size: 14pt; color: " + textColorHex + ";'>"
            + "<tr><td style='width: 160px; font-weight: bold; vertical-align: top;'>NUOVA:</td><td>Avvia una nuova partita.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>ARRENDITI:</td><td>Rivela la parola segreta.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>ESCI:</td><td>Chiude l'applicazione.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>Notte / Giorno:</td><td>Alterna il tema grafico e salva automaticamente la preferenza.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>AGGIUNGI PAROLA:</td><td>Permette l'inserimento di una nuova parola nel dizionario.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>AIUTO:</td><td>Apre questa schermata con le regole e la guida.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>Tastiera:</td><td>Digita le lettere, premi <b>INVIO</b> per confermare o <b>⌫</b> per cancellare.</td></tr>"
            + "</table>"
            + "</body></html>";

        javax.swing.JDialog dialog = new javax.swing.JDialog(this, "Regole del Gioco & Guida UI", true);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(bgColore);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTesto = new JLabel(messaggioHtml);
        panel.add(lblTesto, BorderLayout.CENTER);

        JCheckBox checkBox = null;
        if (isAvvio) {
            checkBox = new JCheckBox("Non mostrare più questo messaggio all'avvio");
            checkBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
            checkBox.setForeground(fgColore);
            checkBox.setBackground(bgColore);
            checkBox.setFocusPainted(false);
            panel.add(checkBox, BorderLayout.SOUTH);
        }

        JPanel panelSudDialog = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSudDialog.setBackground(bgColore);
        panelSudDialog.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JButton btnOk = new JButton("OK");
        styleButton(btnOk);
        btnOk.setBackground(bottoniBg);
        btnOk.setForeground(fgColore);
        btnOk.setBorder(BorderFactory.createLineBorder(bordoColore, 1));
        btnOk.addActionListener(e -> dialog.dispose());
        panelSudDialog.add(btnOk);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(panelSudDialog, BorderLayout.SOUTH);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(bordoColore, 2));

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (isAvvio && checkBox != null && checkBox.isSelected()) {
            Preferences prefs = Preferences.userNodeForPackage(WordleFrame.class);
            prefs.putBoolean("nonMostrareAiuto", true);
        }

        requestFocusInWindow();
    }

    private JPanel creaPannelloTastiera() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 0, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));

        String[] riga1 = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
        String[] riga2 = {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
        String[] riga3 = {"INVIO", "Z", "X", "C", "V", "B", "N", "M", "⌫"};

        panel.add(creaRigaTasti(riga1));
        panel.add(creaRigaTasti(riga2));
        panel.add(creaRigaTasti(riga3));

        return panel;
    }

    private JPanel creaRigaTasti(String[] lettere) {
        JPanel rigaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 4));

        for (String s : lettere) {
            JButton btn = new JButton(s);
            btn.setFont(new Font("SansSerif", Font.BOLD, 15));
            btn.setFocusPainted(false);

            if (s.equals("INVIO") || s.equals("⌫")) {
                btn.setPreferredSize(new Dimension(75, 55));
            } else {
                btn.setPreferredSize(new Dimension(46, 55));
            }

            btn.addActionListener(e -> {
                if (!tastieraBloccata) {
                    gestisciInputVirtuale(s);
                }
            });

            if (s.length() == 1) {
                tastiVirtuali.put(s.charAt(0), btn);
            }

            rigaPanel.add(btn);
        }
        return rigaPanel;
    }

    private void styleButton(AbstractButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(100, 32));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    private void applicaTema(boolean isNotte) {
        Color coloreSfondo = isNotte ? SFONDO_NOTTE : SFONDO_GIORNO;
        Color coloreBottoniBg = isNotte ? BOTTONE_NOTTE : BOTTONE_GIORNO;
        Color coloreBottoniFg = isNotte ? Color.WHITE : TESTO_BOTTONE_GIORNO;
        Color coloreBordo = isNotte ? BORDO_BOTTONE_NOTTE : BORDO_BOTTONE_GIORNO;
        Color coloreBordoCella = isNotte ? BORDO_CELLA_NOTTE : BORDO_CELLA_GIORNO;

        getContentPane().setBackground(coloreSfondo);
        panelBottoni.setBackground(coloreSfondo);
        panelGriglia.setBackground(coloreSfondo);
        panelTastiera.setBackground(coloreSfondo);
        panelSud.setBackground(coloreSfondo);

        lblParolaSegreta.setForeground(isNotte ? Color.WHITE : TESTO_BOTTONE_GIORNO);

        if (separatore != null) {
            separatore.setBackground(coloreBordo);
        }

        for (int i = 0; i < RIGHE; i++) {
            for (int j = 0; j < COLONNE; j++) {
                celleGrid[i][j].setBorder(BorderFactory.createLineBorder(coloreBordoCella, 2));
            }
        }

        for (java.awt.Component comp : panelTastiera.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(coloreSfondo);
                for (java.awt.Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JButton) {
                        JButton btn = (JButton) subComp;
                        Color bgAttuale = btn.getBackground();
                        if (!bgAttuale.equals(new Color(106, 170, 100)) &&
                            !bgAttuale.equals(new Color(201, 180, 88)) &&
                            !bgAttuale.equals(new Color(120, 124, 126))) {
                            btn.setBackground(coloreBottoniBg);
                            btn.setForeground(coloreBottoniFg);
                            btn.setBorder(BorderFactory.createLineBorder(coloreBordo, 1));
                        }
                    }
                }
            }
        }

        AbstractButton[] bottoniSuperiori = {btnNuovaPartita, btnMostra, btnEsci, btnAggiungi, btnAiuto};
        for (AbstractButton b : bottoniSuperiori) {
            b.setBackground(coloreBottoniBg);
            b.setForeground(coloreBottoniFg);
            b.setBorder(BorderFactory.createLineBorder(coloreBordo, 1));
        }

        if (isNotte) {
            tglModalita.setBackground(new Color(38, 42, 45));
            tglModalita.setForeground(Color.WHITE);
        } else {
            tglModalita.setBackground(coloreBottoniBg);
            tglModalita.setForeground(coloreBottoniFg);
        }
        tglModalita.setBorder(BorderFactory.createLineBorder(coloreBordo, 1));
    }

    private void gestisciNuovaPartita() {
        matrice.azzera(COLONNE);
        giocatore.setTentativi(0);
        paroliere.setParolaSegreta(null);
        hasWon = false;
        wasSecretWordShown = false;

        Controller.wordle("/nuova", giocatore, paroliere, matrice);
        Controller.wordle("/gioca", giocatore, paroliere, matrice);

        resettaInterfacciaGrafica();
        JOptionPane.showMessageDialog(this, "Nuova partita avviata! Inizia a digitare.", "Nuova Partita", JOptionPane.INFORMATION_MESSAGE);
    }

    private void gestisciMostraParola() {
        if (tastieraBloccata) {
            return;
        }
        Controller.wordle("/mostra", giocatore, paroliere, matrice);
        String parolaSegreta = paroliere.getParolaSegreta();
        tastieraBloccata = true;
        wasSecretWordShown = true;

        lblParolaSegreta.setText(parolaSegreta);
        lblParolaSegreta.setVisible(true);
    }

    private void gestisciEsci() {
        int scelta = JOptionPane.showConfirmDialog(
            this,
            "Sei sicuro di voler uscire dal gioco?",
            "Conferma uscita",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (scelta == JOptionPane.YES_OPTION) {
            if (paroliere.getParolaSegreta() != null && !hasWon && !wasSecretWordShown) {
                JOptionPane.showMessageDialog(this, "La parola segreta era: " + paroliere.getParolaSegreta(), "Uscita", JOptionPane.INFORMATION_MESSAGE);
            }
            System.exit(0);
        }

        requestFocusInWindow();
    }

    private void resettaInterfacciaGrafica() {
        rigaCorrente = 0;
        colonnaCorrente = 0;
        tastieraBloccata = false;

        lblParolaSegreta.setText("");
        lblParolaSegreta.setVisible(false);

        boolean isNotte = tglModalita.isSelected();
        Color coloreBordoCella = isNotte ? BORDO_CELLA_NOTTE : BORDO_CELLA_GIORNO;

        for (int i = 0; i < RIGHE; i++) {
            for (int j = 0; j < COLONNE; j++) {
                celleGrid[i][j].setText("");
                celleGrid[i][j].setBackground(Color.WHITE);
                celleGrid[i][j].setForeground(new Color(30, 30, 30));
                celleGrid[i][j].setBorder(BorderFactory.createLineBorder(coloreBordoCella, 2));
            }
        }

        Color defaultBg = isNotte ? BOTTONE_NOTTE : BOTTONE_GIORNO;
        Color defaultFg = isNotte ? Color.WHITE : TESTO_BOTTONE_GIORNO;
        Color defaultBordo = isNotte ? BORDO_BOTTONE_NOTTE : BORDO_BOTTONE_GIORNO;

        for (JButton btn : tastiVirtuali.values()) {
            btn.setBackground(defaultBg);
            btn.setForeground(defaultFg);
            btn.setBorder(BorderFactory.createLineBorder(defaultBordo, 1));
        }

        requestFocusInWindow();
    }

    private void gestisciTastoPremuto(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char keyChar = e.getKeyChar();

        if (keyCode == KeyEvent.VK_ENTER) {
            inviaTentativo();
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            cancellaLettera();
        } else if (Character.isLetter(keyChar) && colonnaCorrente < COLONNE) {
            char upperChar = Character.toUpperCase(keyChar);
        
            // Controlliamo che il char rientri strettamente tra 'A' e 'Z'
            // evitiamo le lettere accentate ò, à, è, ù
            if (upperChar >= 'A' && upperChar <= 'Z') {
                aggiungiLettera(upperChar);
            }
        }
    }

    private void gestisciInputVirtuale(String comando) {
        if (comando.equals("INVIO")) {
            inviaTentativo();
        } else if (comando.equals("⌫")) {
            cancellaLettera();
        } else if (colonnaCorrente < COLONNE) {
            aggiungiLettera(comando.charAt(0));
        }
        requestFocusInWindow();
    }

    private void aggiungiLettera(char lettera) {
        celleGrid[rigaCorrente][colonnaCorrente].setText(String.valueOf(lettera));
        colonnaCorrente++;
    }

    private void cancellaLettera() {
        if (colonnaCorrente > 0) {
            colonnaCorrente--;
            celleGrid[rigaCorrente][colonnaCorrente].setText("");
        }
    }

    private void inviaTentativo() {
        if (colonnaCorrente < COLONNE) {
            JOptionPane.showMessageDialog(this, "La parola deve essere di " + COLONNE + " lettere!", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < COLONNE; j++) {
            sb.append(celleGrid[rigaCorrente][j].getText());
        }
        String parolaInserita = sb.toString();

        int tentativoFatto = giocatore.getTentativi();
        Controller.tentativo(giocatore, parolaInserita, paroliere, matrice);

        boolean tutteVerdi = true;

        for (int j = 0; j < COLONNE; j++) {
            char lettera = matrice.getTentativiList().get(tentativoFatto).charAt(j);
            celleGrid[tentativoFatto][j].setText(String.valueOf(lettera));

            String codiceColoreANSI = matrice.getColoriList().get(tentativoFatto).get(j);
            Color coloreCella;
            int prioritaColore = 0;

            if (codiceColoreANSI.equals("\u001B[42m")) { // Verde
                coloreCella = new Color(106, 170, 100);
                prioritaColore = 3;
            } else {
                tutteVerdi = false;
                if (codiceColoreANSI.equals("\u001B[103m")) { // Giallo
                    coloreCella = new Color(201, 180, 88);
                    prioritaColore = 2;
                } else { // Grigio
                    coloreCella = new Color(120, 124, 126);
                    prioritaColore = 1;
                }
            }

            celleGrid[tentativoFatto][j].setBackground(coloreCella);
            celleGrid[tentativoFatto][j].setForeground(Color.WHITE);

            JButton tastoBtn = tastiVirtuali.get(lettera);
            if (tastoBtn != null) {
                Color coloreAttuale = tastoBtn.getBackground();
                boolean aggiorna = true;
                if (coloreAttuale.equals(new Color(106, 170, 100))) {
                    aggiorna = false;
                } else if (coloreAttuale.equals(new Color(201, 180, 88)) && prioritaColore < 3) {
                    aggiorna = false;
                }

                if (aggiorna) {
                    tastoBtn.setBackground(coloreCella);
                    tastoBtn.setForeground(Color.WHITE);
                }
            }
        }

        if (tutteVerdi) {
            tastieraBloccata = true;
            hasWon = true;
            JOptionPane.showMessageDialog(this, "Complimenti, hai indovinato la parola!", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
        } else if (tentativoFatto >= RIGHE - 1) {
            tastieraBloccata = true;
            wasSecretWordShown = true;

            lblParolaSegreta.setText(paroliere.getParolaSegreta());
            lblParolaSegreta.setVisible(true);

            JOptionPane.showMessageDialog(this, "Tentativi terminati!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else {
            rigaCorrente++;
            colonnaCorrente = 0;
        }
    }

    private void gestisciAggiungiParola() {
        String input = JOptionPane.showInputDialog(
            this,
            "Inserisci una nuova parola di " + COLONNE + " lettere da aggiungere nel sistema:",
            "Aggiungi Parola Personalizzata",
            JOptionPane.QUESTION_MESSAGE
        );

        if (input != null && !input.trim().isEmpty()) {
            String parola = input.trim().toUpperCase();

            if (parola.length() != COLONNE) {
                JOptionPane.showMessageDialog(this, "La parola deve essere di esattamente " + COLONNE + " lettere!", "Errore", JOptionPane.ERROR_MESSAGE);
                requestFocusInWindow();
                return;
            }

            if (!parola.matches("[A-Z]+")) {
                JOptionPane.showMessageDialog(this, "La parola deve contenere solo lettere dell'alfabeto!", "Caratteri non validi", JOptionPane.ERROR_MESSAGE);
                requestFocusInWindow();
                return;
            }

            boolean salvata = Controller.aggiungiParolaExtra(parola);
            if (salvata) {
                JOptionPane.showMessageDialog(this, "Parola aggiunta con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "La parola è già presente nel dizionario (interno o extra)!", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        }
        requestFocusInWindow();
    }
}