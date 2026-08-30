package it.uniba.app;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
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

public class WordleFrame extends JFrame {

    private static int RIGHE;
    private static int COLONNE;

    private JLabel[][] celleGrid;
    private JLabel lblParolaSegreta; // Etichetta per mostrare la parola segreta
    private Map<Character, JButton> tastiVirtuali = new HashMap<>();

    private JPanel panelBottoni;
    private JPanel panelGriglia;
    private JPanel panelTastiera;
    private JPanel panelSud; // Pannello contenitore per etichetta parola e tastiera
    private JButton btnNuovaPartita;
    private JButton btnMostra;
    private JButton btnEsci;
    private JToggleButton tglModalita;
    private JButton btnAggiungi;
    private JPanel separatore; // Lineetta separatrice tra bottoni di gioco e di sistema

    private int rigaCorrente = 0;
    private int colonnaCorrente = 0;

    private boolean tastieraBloccata = true;
    private boolean hasWon = false;
    private boolean wasSecretWordShown = false;

    private Giocatore giocatore;
    private Paroliere paroliere;
    private Matrice matrice;

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

    public WordleFrame(Giocatore g, Paroliere p, Matrice m) {
        this.giocatore = g;
        this.paroliere = p;
        this.matrice = m;

        RIGHE = Controller.getMaxTentativi();
        COLONNE = Controller.getNumCaratteri();

        setTitle("Wordle Java");
        setSize(580, 870);
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
        tglModalita.setSelected(true);

        styleButton(btnNuovaPartita);
        styleButton(btnMostra);
        styleButton(btnEsci);
        styleButton(btnAggiungi);
        styleButton(tglModalita);

        // Dimensione bottone "Aggiungi"
        btnAggiungi.setPreferredSize(new Dimension(120, 32));

        // Dimensione bottone (Giorno/Notte)
        tglModalita.setPreferredSize(new Dimension(55, 32));

        panelBottoni.add(btnNuovaPartita);
        panelBottoni.add(btnMostra);
        panelBottoni.add(btnEsci);

        // Separatore visivo tra azioni di gioco e comandi di sistema
        separatore = new JPanel();
        separatore.setPreferredSize(new Dimension(2, 32)); // Stessa altezza dei bottoni
        panelBottoni.add(separatore);

        panelBottoni.add(tglModalita);
        panelBottoni.add(btnAggiungi);
        add(panelBottoni, BorderLayout.NORTH);

        // --- 2. GRIGLIA di TENTATIVI ---
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
        lblParolaSegreta.setVisible(false); // Nascosta all'avvio
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

        // Gestione dinamica del cambio tema tramite il Toggle Button
        tglModalita.addActionListener(e -> {
            if (tglModalita.isSelected()) {
                tglModalita.setText("Notte");
                applicaTema(true);
            } else {
                tglModalita.setText("Giorno");
                applicaTema(false);
            }
            requestFocusInWindow();
        });

        // Di base parte in modalità Notte
        applicaTema(true);

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

        // Aggiorna colore testo label parola segreta in base al tema
        lblParolaSegreta.setForeground(isNotte ? Color.WHITE : TESTO_BOTTONE_GIORNO);

        // Aggiorna il colore del separatore in base al tema
        if (separatore != null) {
            separatore.setBackground(coloreBordo);
        }

        // Aggiorna dinamicamente i bordi delle celle della griglia
        for (int i = 0; i < RIGHE; i++) {
            for (int j = 0; j < COLONNE; j++) {
                celleGrid[i][j].setBorder(BorderFactory.createLineBorder(coloreBordoCella, 2));
            }
        }

        // Aggiorna la tastiera virtuale
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

        // Aggiorna i bottoni superiori standard
        AbstractButton[] bottoniSuperiori = {btnNuovaPartita, btnMostra, btnEsci, btnAggiungi};
        for (AbstractButton b : bottoniSuperiori) {
            b.setBackground(coloreBottoniBg);
            b.setForeground(coloreBottoniFg);
            b.setBorder(BorderFactory.createLineBorder(coloreBordo, 1));
        }

        // Gestione specifica per il Toggle Button della modalità
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
            if(paroliere.getParolaSegreta() != null && !hasWon && !wasSecretWordShown)
                JOptionPane.showMessageDialog(this, "La parola segreta era: " + paroliere.getParolaSegreta(), "Uscita", JOptionPane.INFORMATION_MESSAGE);

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
            aggiungiLettera(Character.toUpperCase(keyChar));
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
                if (coloreAttuale.equals(new Color(106, 170, 100))) aggiorna = false; 
                else if (coloreAttuale.equals(new Color(201, 180, 88)) && prioritaColore < 3) aggiorna = false; 

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