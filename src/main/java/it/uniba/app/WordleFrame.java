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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
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

    private JButton btnCambiaLunghezza;

    private JButton btnHint;
    private boolean hintUtilizzato = false;

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

    // Tasti bloccati dall'hint
    private final java.util.Set<Character> tastiOscuratiHint = new java.util.HashSet<>();
    private final java.util.Set<Character> tastiHintSpeciali = new java.util.HashSet<>();

    // Bottone cambia lingua
    private JButton btnCambiaLingua;

    public WordleFrame(final Giocatore g, final Paroliere p, final Matrice m) {
        this.giocatore = g;
        this.paroliere = p;
        this.matrice = m;

        // CONTROLLO LINGUA AL PRIMO AVVIO
        if (Controller.getLingua() == null) {
            String[] lingueDisponibili = Controller.getLingueDisponibili();
            String linguaScelta = (String) JOptionPane.showInputDialog(
                this, // usa 'this' come riferimento della finestra principale
                "Seleziona la lingua iniziale / Select initial language:",
                "Primo Avvio - Selezione Lingua",
                JOptionPane.QUESTION_MESSAGE,
                null,
                lingueDisponibili,
                "ITA"
            );

        Controller.setLingua((linguaScelta != null && !linguaScelta.trim().isEmpty()) ? linguaScelta : "ITA");
    }

        COLONNE = Controller.getNumCaratteri();
        calcolaRigheInBaseAllaLunghezza();

        setTitle("Wordle Java");
        setSize(Math.max(620, 400 + (COLONNE * 55)), 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // --- 1. PANNELLO SUPERIORE CON I BOTTONI E IL TOGGLE ---
        panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));

        btnNuovaPartita = new JButton("NUOVA");
        btnMostra = new JButton("ARRENDITI");
        btnEsci = new JButton("ESCI");
        btnAggiungi = new JButton("AGGIUNGI PAROLA");
        tglModalita = new JToggleButton("Notte");

        btnCambiaLunghezza = new JButton("Lunghezza: " + COLONNE);
        styleButton(btnCambiaLunghezza);
        btnCambiaLunghezza.setPreferredSize(new Dimension(130, 32));
        btnCambiaLunghezza.addActionListener(e -> mostraDialogSelezioneLunghezza());
        panelBottoni.add(btnCambiaLunghezza);

        styleButton(btnNuovaPartita);
        styleButton(btnMostra);
        styleButton(btnEsci);
        styleButton(btnAggiungi);
        styleButton(tglModalita);

        btnAggiungi.setPreferredSize(new Dimension(120, 32));
        tglModalita.setPreferredSize(new Dimension(55, 32));

        panelBottoni.add(btnNuovaPartita);
        panelBottoni.add(btnMostra);
        panelBottoni.add(btnEsci);

        separatore = new JPanel();
        separatore.setPreferredSize(new Dimension(2, 32));
        panelBottoni.add(separatore);

        panelBottoni.add(tglModalita);
        panelBottoni.add(btnAggiungi);
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

        // Configurazione Bottone Aiuto (icona 'i')
        btnAiuto = new JButton("i") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getModel().isRollover() ? new Color(41, 128, 185) : new Color(52, 152, 219));
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth("i")) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString("i", x, y);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {}
        };
        btnAiuto.setPreferredSize(new Dimension(32, 32));
        btnAiuto.setFocusPainted(false);
        btnAiuto.setBorderPainted(false);
        btnAiuto.setContentAreaFilled(false);
        btnAiuto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAiuto.setToolTipText("Aiuto & Regole");
        btnAiuto.addActionListener(e -> mostraAiuto(false));

        // Configurazione Bottone Hint (Lampadina 💡)
        btnHint = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (!isEnabled()) {
                    g2.setColor(new Color(150, 150, 150)); // Grigio se disabilitato
                } else {
                    g2.setColor(getModel().isRollover() ? new Color(241, 196, 15) : new Color(243, 156, 18)); // Toni oro/giallo
                }
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                String simbolo = "💡";
                int x = (getWidth() - fm.stringWidth(simbolo)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(simbolo, x, y);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {}
        };
        btnHint.setPreferredSize(new Dimension(32, 32));
        btnHint.setFocusPainted(false);
        btnHint.setBorderPainted(false);
        btnHint.setContentAreaFilled(false);
        btnHint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHint.setToolTipText("Richiedi Indizio (Utilizzabile una sola volta)");
        btnHint.addActionListener(e -> gestisciHint());

        // Configurazione bottone cambio lingua
        btnCambiaLingua = new JButton("Lingua: " + Controller.getLingua());
        styleButton(btnCambiaLingua);
        btnCambiaLingua.setPreferredSize(new Dimension(120, 32));
        btnCambiaLingua.addActionListener(e -> mostraDialogCambioLingua());
        panelBottoni.add(btnCambiaLingua);

        // --- CONTAINER IN BASSO CON HINT A SINISTRA E AIUTO A DESTRA E CAMBIO LINGUA AL CENTRO ---
        JPanel panelInferioreExtra = new JPanel(new BorderLayout());
        panelInferioreExtra.setOpaque(false);
        panelInferioreExtra.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 15));

        JPanel panelHintContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelHintContainer.setOpaque(false);
        panelHintContainer.add(btnHint);

        JPanel panelLinguaContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelLinguaContainer.setOpaque(false);
        panelLinguaContainer.add(btnCambiaLingua);

        JPanel panelAiutoContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelAiutoContainer.setOpaque(false);
        panelAiutoContainer.add(btnAiuto);

        panelInferioreExtra.add(panelHintContainer, BorderLayout.WEST);
        panelInferioreExtra.add(panelLinguaContainer, BorderLayout.CENTER);
        panelInferioreExtra.add(panelAiutoContainer, BorderLayout.EAST);

        panelSud = new JPanel(new BorderLayout());
        panelSud.add(lblParolaSegreta, BorderLayout.NORTH);
        panelSud.add(panelTastiera, BorderLayout.CENTER);
        panelSud.add(panelInferioreExtra, BorderLayout.SOUTH);
        add(panelSud, BorderLayout.SOUTH);

        // --- 4. GESTIONE EVENTI DEI BOTTONI ---
        btnNuovaPartita.addActionListener(e -> gestisciNuovaPartita());
        btnMostra.addActionListener(e -> gestisciMostraParola());
        btnEsci.addActionListener(e -> gestisciEsci());
        btnAggiungi.addActionListener(e -> gestisciAggiungiParola());

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
            + "<tr><td style='width: 160px; font-weight: bold; vertical-align: top;'>Lunghezza:</td><td>Scegli la lunghezza della parola da indovinare.</td></tr>"
            + "<tr><td style='width: 160px; font-weight: bold; vertical-align: top;'>NUOVA:</td><td>Avvia una nuova partita.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>ARRENDITI:</td><td>Rivela la parola segreta.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>ESCI:</td><td>Chiude l'applicazione.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>Notte / Giorno:</td><td>Alterna il tema grafico e salva automaticamente la preferenza.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>AGGIUNGI PAROLA:</td><td>Permette l'inserimento di una nuova parola nel dizionario.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>AIUTO (icona i):</td><td>Apre questa schermata con le regole e la guida.</td></tr>"
            + "<tr><td style='font-weight: bold; vertical-align: top;'>HINT (icona 💡):</td><td>Se possibile, fornisce un aiuto all'utente (utilizzabile solo una volta a partita).</td></tr>"
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
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

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

        // --- GESTIONE SICURA DELLA TASTIERA ---
        for (java.awt.Component comp : panelTastiera.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(coloreSfondo);
                for (java.awt.Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JButton) {
                        JButton btn = (JButton) subComp;
                        String testoBtn = btn.getText();
                        Color bgAttuale = btn.getBackground();

                        // Verifichiamo se il bottone corrisponde a una lettera dell'alfabeto gestita nei nostri dizionari
                        Character letteraCorrispondente = null;
                        if (testoBtn != null && testoBtn.length() == 1) {
                            char c = testoBtn.charAt(0);
                            if (tastiVirtuali.containsKey(c)) {
                                letteraCorrispondente = c;
                            }
                        }

                        // Se è un tasto funzione speciale (es. "INVIO", "CANC", o simboli)
                        if (letteraCorrispondente == null) {
                            btn.setBackground(coloreBottoniBg);
                            btn.setForeground(coloreBottoniFg);
                            btn.setBorder(BorderFactory.createLineBorder(coloreBordo, 1));
                        } 
                        // Se è un tasto lettera dell'alfabeto
                        else {
                            // 1. Se il tasto ha un colore di tentativo standard (verde, giallo, grigio tentativo), lo manteniamo
                            if (bgAttuale.equals(new Color(106, 170, 100)) ||
                                bgAttuale.equals(new Color(201, 180, 88)) ||
                                bgAttuale.equals(new Color(120, 124, 126))) {
                                // Lascia inalterato
                            } 
                            // 2. Se il tasto è stato oscurato dall'Hint 1
                            else if (tastiOscuratiHint.contains(letteraCorrispondente)) {
                                // Notte: Grigio molto scuro (quasi nero) per staccare dai tasti normali
                                // Giorno: Grigio tenue neutro
                                Color colBgHint = isNotte ? new Color(28, 30, 33) : new Color(215, 218, 222);
                                
                                // Testo sbiadito (faded) per dare l'effetto "tasto disabilitato/cancellato"
                                Color colFgHint = isNotte ? new Color(110, 115, 120) : new Color(130, 135, 140);
                                Color colBordoHint = isNotte ? new Color(40, 43, 46) : new Color(190, 193, 196);

                                btn.setBackground(colBgHint);
                                btn.setForeground(colFgHint);
                                btn.setBorder(BorderFactory.createLineBorder(colBordoHint, 1));
                            }
                            // 3. Se il tasto fa parte degli hint speciali (lettera iniziale o presente)
                            else if (tastiHintSpeciali.contains(letteraCorrispondente)) {
                                // Mantiene il colore speciale già assegnato senza perdersi
                                btn.setForeground(Color.WHITE);
                                btn.setBorder(BorderFactory.createLineBorder(coloreBordo, 1));
                            } 
                            // 4. Tasti normali neutri
                            else {
                                btn.setBackground(coloreBottoniBg);
                                btn.setForeground(coloreBottoniFg);
                                btn.setBorder(BorderFactory.createLineBorder(coloreBordo, 1));
                            }
                        }
                    }
                }
            }
        }

        AbstractButton[] bottoniSuperiori = {btnNuovaPartita, btnMostra, btnEsci, btnAggiungi, btnCambiaLunghezza, btnCambiaLingua};
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
        matrice.azzera(RIGHE, COLONNE);
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
        // reset 
        hintUtilizzato = false;
        btnHint.setEnabled(true);
        btnHint.repaint();
        
        // --- PULIZIA DEGLI HINT PRECEDENTI ---
        tastiOscuratiHint.clear();
        tastiHintSpeciali.clear();
        // ---

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

        // CONTROLLO: Verifica se la parola fa parte delle parole consentite (Unione dei 3 dizionari)
        // dizionario 1: parole_N.txt (soluzioni)
        // dizionario 2: parole_extra_N.txt (soluzioni aggiunte dall'utente)
        // dizionario 3: parole_consentite_N.txt (altre parole NON soluzioni ammesse dal sistema)

        // Mostriamo il pop-up di errore solo se la parola NON è soluzione e NON è consentita
        if (!Controller.esisteParola(parolaInserita) && !Controller.isConsentita(parolaInserita)) {
            JOptionPane.showMessageDialog(
                this, 
                "La parola inserita non è presente nell'elenco delle parole consentite!", 
                "Parola non valida", 
                JOptionPane.ERROR_MESSAGE
            );
            return; // viene mostrato il pop-up e l'utente può correggere il tentativo
        }

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
            if (tastiOscuratiHint.contains(lettera))
            {
                // non fare niente perché significa che quelle lettere sono già state oscurate da un hint
            } else if (tastoBtn != null) {
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

            // inizio: Verifica se la parola è composta tutta dalla stessa lettera
            boolean tutteUguali = true;
            char primaLettera = parola.charAt(0);
            for (int i = 1; i < parola.length(); i++) {
                if (parola.charAt(i) != primaLettera) {
                    tutteUguali = false;
                    break;
                }
            }

            if (tutteUguali) {
                JOptionPane.showMessageDialog(this, "Davvero!? La stessa lettera? Mi dispiace ciccio, non è possibile!", "Parola non valida", JOptionPane.WARNING_MESSAGE);
                requestFocusInWindow();
                return;
            }
            // fine

            boolean salvata = Controller.aggiungiParolaExtra(parola);
            if (salvata) {
                JOptionPane.showMessageDialog(this, "Parola aggiunta con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "La parola è già presente nel dizionario (interno o extra)!", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        }
        requestFocusInWindow();
    }

    private void mostraDialogSelezioneLunghezza() {
        String[] opzioni = {"5", "6", "7", "8", "9"};
        String sceltaCorrente = String.valueOf(COLONNE);
        
        String scelta = (String) JOptionPane.showInputDialog(
            this,
            "Seleziona la lunghezza della parola:",
            "Cambia Dimensione",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opzioni,
            sceltaCorrente
        );

        if (scelta != null) {
            int nuovaLunghezza = Integer.parseInt(scelta);
            if (nuovaLunghezza != COLONNE) {
                Controller.setNumCaratteri(nuovaLunghezza);
                COLONNE = Controller.getNumCaratteri();
                btnCambiaLunghezza.setText("Lunghezza: " + COLONNE);
                ricostruisciGriglia();
                gestisciNuovaPartita();
            }
        }
        requestFocusInWindow();
    }

    private void ricostruisciGriglia() {
        remove(panelGriglia);

        // Aggiorna COLONNE prendendole dal back-end
        // e le RIGHE di conseguenza
        COLONNE = Controller.getNumCaratteri();
        calcolaRigheInBaseAllaLunghezza();

        panelGriglia = new JPanel(new GridLayout(RIGHE, COLONNE, 6, 6));
        panelGriglia.setBorder(BorderFactory.createEmptyBorder(5, 40, 5, 40));

        celleGrid = new JLabel[RIGHE][COLONNE];
        boolean isNotte = tglModalita.isSelected();
        Color coloreBordoCella = isNotte ? BORDO_CELLA_NOTTE : BORDO_CELLA_GIORNO;
        Color coloreSfondo = isNotte ? SFONDO_NOTTE : SFONDO_GIORNO;

        panelGriglia.setBackground(coloreSfondo);

        for (int i = 0; i < RIGHE; i++) {
            for (int j = 0; j < COLONNE; j++) {
                JLabel cella = new JLabel("", JLabel.CENTER);
                cella.setFont(new Font("SansSerif", Font.BOLD, 26));
                cella.setOpaque(true);
                cella.setBackground(Color.WHITE);
                cella.setForeground(new Color(30, 30, 30));
                cella.setBorder(BorderFactory.createLineBorder(coloreBordoCella, 2));

                celleGrid[i][j] = cella;
                panelGriglia.add(cella);
            }
        }

        add(panelGriglia, BorderLayout.CENTER);

        int nuovaLarghezza = Math.max(620, 400 + (COLONNE * 55));
        setSize(nuovaLarghezza, RIGHE > 6 ? 980 : 950); 
        setLocationRelativeTo(null);

        revalidate();
        repaint();
    }

    /**
     * Imposta automaticamente i tentativi standard in base alla lunghezza della parola.
     * Regola tipica: 
     * - 5 lettere = 6 tentativi
     * - 6-7 lettere = 7 tentativi
     * - 8-9 lettere = 9 tentativi (o a scelta)
     */
    private void calcolaRigheInBaseAllaLunghezza() {
        if (COLONNE == 5) {
            RIGHE = 6;
        } else if (COLONNE == 6 || COLONNE == 7) {
            RIGHE = 7;
        } else if (COLONNE >= 8) {
            RIGHE = 9;
        }

        Controller.setMaxTentativi(RIGHE);
    }

    /*
        Should be easily moved to the Controller.java class but at the moment it's good here.
        This method heavily interacts with the GUI; therefore, considering the project,
        it's reasonable to leave it here for the moment. Anyway, the 'heavy' logic could be 
        easily moved to the Controller class using appropriate 'getter' and 'setter' methods.
     */
    private void gestisciHint() {
        if (hintUtilizzato || tastieraBloccata) {
            return;
        }

        String parolaSegreta = paroliere.getParolaSegreta();
        if (parolaSegreta == null || parolaSegreta.isEmpty()) {
            return;
        }

        java.util.Random rand = new java.util.Random();
        
        // 1. Verifica se la prima lettera è stata già trovata (verde nella prima colonna)
        char primaLettera = parolaSegreta.charAt(0);
        boolean primaLetteraGiaTrovata = false;
        for (int i = 0; i < rigaCorrente; i++) {
            if (i < matrice.getColoriList().size() && !matrice.getColoriList().get(i).isEmpty()) {
                String coloreANSI = matrice.getColoriList().get(i).get(0);
                if (coloreANSI.equals("\u001B[42m") && celleGrid[i][0].getText().equalsIgnoreCase(String.valueOf(primaLettera))) {
                    primaLetteraGiaTrovata = true;
                    break;
                }
            }
        }

        // 2. Calcola le lettere conosciute/escluse per verificare la disponibilità dell'HINT 1
        java.util.Set<Character> lettereConosciuteHint1 = new java.util.HashSet<>(tastiHintSpeciali);
        lettereConosciuteHint1.addAll(tastiOscuratiHint);
        for (int i = 0; i < rigaCorrente; i++) {
            if (i < matrice.getTentativiList().size() && i < matrice.getColoriList().size()) {
                String tentativo = matrice.getTentativiList().get(i);
                java.util.List<String> colori = matrice.getColoriList().get(i);
                for (int j = 0; j < tentativo.length(); j++) {
                    if (j < colori.size()) {
                        lettereConosciuteHint1.add(tentativo.charAt(j));
                    }
                }
            }
        }

        java.util.List<Character> lettereErrateIgnotite = new java.util.ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            if (parolaSegreta.indexOf(c) == -1 && !lettereConosciuteHint1.contains(c)) {
                lettereErrateIgnotite.add(c);
            }
        }

        // 3. Calcola le lettere conosciute (verdi/gialle) per verificare la disponibilità dell'HINT 3
        java.util.Set<Character> lettereConosciuteHint3 = new java.util.HashSet<>(tastiHintSpeciali);
        for (int i = 0; i < rigaCorrente; i++) {
            if (i < matrice.getTentativiList().size() && i < matrice.getColoriList().size()) {
                String tentativo = matrice.getTentativiList().get(i);
                java.util.List<String> colori = matrice.getColoriList().get(i);
                for (int j = 0; j < tentativo.length(); j++) {
                    if (j < colori.size()) {
                        String col = colori.get(j);
                        if (col.equals("\u001B[42m") || col.equals("\u001B[103m")) {
                            lettereConosciuteHint3.add(tentativo.charAt(j));
                        }
                    }
                }
            }
        }

        java.util.List<Character> letterePresentiIgnotite = new java.util.ArrayList<>();
        for (int i = 0; i < parolaSegreta.length(); i++) {
            char c = parolaSegreta.charAt(i);
            if (!letterePresentiIgnotite.contains(c) && !lettereConosciuteHint3.contains(c)) {
                letterePresentiIgnotite.add(c);
            }
        }

        // 4. Costruiamo dinamicamente la lista degli hint validi PRIMA di estrarre
        java.util.List<Integer> hintDisponibili = new java.util.ArrayList<>();
        
        if (!lettereErrateIgnotite.isEmpty()) {
            hintDisponibili.add(1); // Valido se ci sono ancora lettere errate ignote da poter escludere
        }
        
        if (!primaLetteraGiaTrovata) {
            hintDisponibili.add(2); // Valido solo se la prima lettera non è stata trovata
        }
        
        if (!letterePresentiIgnotite.isEmpty()) {
            hintDisponibili.add(3); // Valido solo se ci sono ancora lettere presenti ignote da mostrare
        }

        // for debugging
        // System.out.println("HINTS DISPONIBILI: " + hintDisponibili);

        if (hintDisponibili.isEmpty()) {
            boolean isNotte = tglModalita.isSelected();
            String messaggioNessunHint = "<b>Nessun Indizio Disponibile!</b><br><br>"
                    + "Hai già scoperto o escluso tutte le informazioni possibili.<br>"
                    + "Non ci sono hint applicabili in questo momento!";
            
            mostraGraficaHintDialog(messaggioNessunHint, isNotte);
            
            // Manteniamo comunque bloccato il bottone dell'hint se non ci sono più opzioni
            hintUtilizzato = true;
            btnHint.setEnabled(false);
            btnHint.repaint();
            requestFocusInWindow();
            return;
        }

        // 5. Estraiamo casualmente l'hint scegliendo SOLO tra quelli realmente disponibili
        int tipoHint = hintDisponibili.get(rand.nextInt(hintDisponibili.size()));

        String messaggioDialogo = "";
        boolean isNotte = tglModalita.isSelected();

        switch (tipoHint) {
            case 1:
                // --- HINT 1: Esclusione di lettere errate ignote ---
                java.util.Collections.shuffle(lettereErrateIgnotite, rand);

                int conteggioScartate = 0;
                for (char c : lettereErrateIgnotite) {
                    JButton tasto = tastiVirtuali.get(c);
                    if (tasto != null) {
                        tastiOscuratiHint.add(c);
                        
                        Color colBgHint = isNotte ? new Color(28, 30, 33) : new Color(215, 218, 222);
                        Color colFgHint = isNotte ? new Color(110, 115, 120) : new Color(130, 135, 140);
                        
                        tasto.setBackground(colBgHint);
                        tasto.setForeground(colFgHint);
                        
                        conteggioScartate++;
                        if (conteggioScartate == 3) break;
                    }
                }
                
                // Fallback se le lettere ignote rimaste sono meno di 3
                if (conteggioScartate < 3) {
                    java.util.List<Character> alfabetoList = new java.util.ArrayList<>();
                    for (char c = 'A'; c <= 'Z'; c++) {
                        if (parolaSegreta.indexOf(c) == -1 && !tastiOscuratiHint.contains(c)) {
                            alfabetoList.add(c);
                        }
                    }
                    java.util.Collections.shuffle(alfabetoList, rand);
                    for (char c : alfabetoList) {
                        JButton tasto = tastiVirtuali.get(c);
                        if (tasto != null) {
                            tastiOscuratiHint.add(c);
                            Color colBgHint = isNotte ? new Color(28, 30, 33) : new Color(215, 218, 222);
                            Color colFgHint = isNotte ? new Color(110, 115, 120) : new Color(130, 135, 140);
                            tasto.setBackground(colBgHint);
                            tasto.setForeground(colFgHint);
                            conteggioScartate++;
                            if (conteggioScartate == 3) break;
                        }
                    }
                }

                messaggioDialogo = "<b>Potere della Lampadina: Esclusione!</b><br><br>"
                        + "Il sistema ha analizzato la parola e ha oscurato <b>nuove lettere</b> "
                        + "che non fanno parte della parola segreta.";
                break;

            case 2:
                // --- HINT 2: Svelare la lettera iniziale ---
                tastiHintSpeciali.add(primaLettera);
                
                JButton tastoIniziale = tastiVirtuali.get(primaLettera);
                if (tastoIniziale != null) {
                    tastoIniziale.setBackground(new Color(41, 128, 185));
                    tastoIniziale.setForeground(Color.WHITE);
                }

                messaggioDialogo = "<b>Potere della Lampadina: Lettera Iniziale!</b><br><br>"
                        + "La parola segreta inizia con la lettera: <span style='font-size: 16pt; color: #3498DB;'><b>" + primaLettera + "</b></span><br>"
                        + "<i>È stata evidenziata in blu sulla tastiera!</i>";
                break;

            case 3:
                // --- HINT 3: Mostrare una lettera presente ma NON ANCORA CONOSCIUTA ---
                java.util.Collections.shuffle(letterePresentiIgnotite, rand);
                char letteraCasuale = letterePresentiIgnotite.get(0);
                
                tastiHintSpeciali.add(letteraCasuale);
                JButton tastoPresente = tastiVirtuali.get(letteraCasuale);
                if (tastoPresente != null) {
                    tastoPresente.setBackground(new Color(212, 172, 13));
                    tastoPresente.setForeground(Color.WHITE);
                }

                messaggioDialogo = "<b>Potere della Lampadina: Indizio di Presenza!</b><br><br>"
                        + "Fai attenzione: la parola segreta contiene sicuramente la lettera: "
                        + "<span style='font-size: 16pt; color: #D4AC0D;'><b>" + letteraCasuale + "</b></span><br>"
                        + "<i>È stata evidenziata in giallo speciale sulla tastiera!</i>";
                break;
        }

        mostraGraficaHintDialog(messaggioDialogo, isNotte);

        hintUtilizzato = true;
        btnHint.setEnabled(false);
        btnHint.repaint();
        requestFocusInWindow();
    }

    private void mostraGraficaHintDialog(String htmlTesto, boolean isNotte) {
        Color bgColore = isNotte ? SFONDO_NOTTE : Color.WHITE;
        
        javax.swing.JDialog hintDialog = new javax.swing.JDialog(this, "💡 Indizio Strategico Casuale", true);
        hintDialog.setLayout(new BorderLayout());

        JPanel panelContenuto = new JPanel(new BorderLayout(15, 15));
        panelContenuto.setBackground(bgColore);
        panelContenuto.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblIcona = new JLabel("💡", JLabel.CENTER);
        lblIcona.setFont(new Font("SansSerif", Font.PLAIN, 40));
        
        String contenutoHtml = "<html><div style='text-align: center; font-family: SansSerif; font-size: 13pt; color: " + (isNotte ? "#DCDCDC" : "#222222") + ";'>"
                + htmlTesto
                + "</div></html>";

        JLabel lblTesto = new JLabel(contenutoHtml);

        panelContenuto.add(lblIcona, BorderLayout.NORTH);
        panelContenuto.add(lblTesto, BorderLayout.CENTER);

        JButton btnOk = new JButton("Ho capito, grazie!");
        styleButton(btnOk);
        btnOk.setPreferredSize(new Dimension(160, 35));
        btnOk.addActionListener(e -> hintDialog.dispose());

        JPanel panelPulsante = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelPulsante.setBackground(bgColore);
        panelPulsante.add(btnOk);

        hintDialog.add(panelContenuto, BorderLayout.CENTER);
        hintDialog.add(panelPulsante, BorderLayout.SOUTH);
        hintDialog.pack();
        hintDialog.setLocationRelativeTo(this);
        hintDialog.setVisible(true);
    }

    private String mostraDialogCambioLingua() {
    String[] lingueDisponibili = Controller.getLingueDisponibili();
    String linguaCorrente = Controller.getLingua();
    
    // Pannello
    String nuovaLingua = (String) JOptionPane.showInputDialog(
        this,
        "Seleziona la nuova lingua:",
        "Cambia Lingua",
        JOptionPane.QUESTION_MESSAGE,
        null,
        lingueDisponibili,
        linguaCorrente
    );

    // Controllo sulla lingua
    if (nuovaLingua != null && !nuovaLingua.equals(linguaCorrente)) {
        // Aggiorna la lingua in "Controller" e salva nelle Preferences
        Controller.setLingua(nuovaLingua);
        btnCambiaLingua.setText("Lingua: " + nuovaLingua);

        // Ricarica la partita con i dizionari della nuova lingua
        gestisciNuovaPartita();

        JOptionPane.showMessageDialog(
            this,
            "Lingua cambiata con successo in " + nuovaLingua + "! Il sistema è stato ricaricato.",
            "Ricaricamento completato",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    requestFocusInWindow();
    return nuovaLingua;
}
}