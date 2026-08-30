package it.uniba.app;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
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
    private Map<Character, JButton> tastiVirtuali = new HashMap<>();
    
    private int rigaCorrente = 0;
    private int colonnaCorrente = 0;
    
    private boolean tastieraBloccata = true;
    private boolean hasWon = false;
    private boolean wasSecretWordShown = false;

    private Giocatore giocatore;
    private Paroliere paroliere;
    private Matrice matrice;

    // Sfondo grigio scuro / effetto notte
    private final Color COLORE_SFONDO = new Color(48, 52, 55);

    public WordleFrame(Giocatore g, Paroliere p, Matrice m) {
        this.giocatore = g;
        this.paroliere = p;
        this.matrice = m;

        RIGHE = Controller.getMaxTentativi();
        COLONNE = Controller.getNumCaratteri();

        setTitle("Wordle Java");
        setSize(500, 720); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 0 spazi orizzontali e verticali tra i componenti del BorderLayout
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(COLORE_SFONDO);

        // --- 1. PANNELLO SUPERIORE CON I BOTTONI ---
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        panelBottoni.setBackground(COLORE_SFONDO);

        JButton btnNuovaPartita = new JButton("NUOVA PARTITA");
        JButton btnMostra = new JButton("MOSTRA PAROLA");
        JButton btnEsci = new JButton("ESCI");

        styleButton(btnNuovaPartita);
        styleButton(btnMostra);
        styleButton(btnEsci);

        panelBottoni.add(btnNuovaPartita);
        panelBottoni.add(btnMostra);
        panelBottoni.add(btnEsci);
        add(panelBottoni, BorderLayout.NORTH);

        // --- 2. GRIGLIA di TENTATIVI ---
        JPanel panelGriglia = new JPanel(new GridLayout(RIGHE, COLONNE, 6, 6));
        panelGriglia.setBackground(COLORE_SFONDO); 
        panelGriglia.setBorder(BorderFactory.createEmptyBorder(5, 40, 5, 40));
        
        celleGrid = new JLabel[RIGHE][COLONNE];
        
        for (int i = 0; i < RIGHE; i++) {
            for (int j = 0; j < COLONNE; j++) {
                JLabel cella = new JLabel("", JLabel.CENTER);
                cella.setFont(new Font("SansSerif", Font.BOLD, 26));
                cella.setOpaque(true);
                cella.setBackground(Color.WHITE);
                cella.setForeground(new Color(30, 30, 30));
                cella.setBorder(BorderFactory.createLineBorder(new Color(70, 75, 80), 2));
                
                celleGrid[i][j] = cella;
                panelGriglia.add(cella);
            }
        }
        add(panelGriglia, BorderLayout.CENTER);

        // --- 3. TASTIERA VIRTUALE IN BASSO ---
        JPanel panelTastiera = creaPannelloTastiera();
        add(panelTastiera, BorderLayout.SOUTH);

        // --- 4. GESTIONE EVENTI DEI BOTTONI ---
        btnNuovaPartita.addActionListener(e -> gestisciNuovaPartita());
        btnMostra.addActionListener(e -> gestisciMostraParola());
        btnEsci.addActionListener(e -> gestisciEsci());

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
        JPanel panelTastiera = new JPanel();
        panelTastiera.setLayout(new GridLayout(3, 1, 0, 5));
        panelTastiera.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));
        panelTastiera.setBackground(COLORE_SFONDO);

        String[] riga1 = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
        String[] riga2 = {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
        String[] riga3 = {"INVIO", "Z", "X", "C", "V", "B", "N", "M", "⌫"};

        panelTastiera.add(creaRigaTasti(riga1));
        panelTastiera.add(creaRigaTasti(riga2));
        panelTastiera.add(creaRigaTasti(riga3));

        return panelTastiera;
    }

    private JPanel creaRigaTasti(String[] lettere) {
        JPanel rigaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        rigaPanel.setBackground(COLORE_SFONDO);

        for (String s : lettere) {
            JButton btn = new JButton(s);
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE); 
            btn.setForeground(new Color(50, 50, 50));
            btn.setBorder(BorderFactory.createLineBorder(new Color(110, 115, 120), 1));
            
            if (s.equals("INVIO") || s.equals("⌫")) {
                btn.setPreferredSize(new Dimension(55, 45));
            } else {
                btn.setPreferredSize(new Dimension(38, 45));
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

    private void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE); 
        btn.setForeground(new Color(50, 50, 50));
        btn.setBorder(BorderFactory.createLineBorder(new Color(110, 115, 120), 1));
        btn.setPreferredSize(new Dimension(130, 35));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
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
        JOptionPane.showMessageDialog(this, "La parola segreta era: " + parolaSegreta, "Resa", JOptionPane.INFORMATION_MESSAGE);
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

        for (int i = 0; i < RIGHE; i++) {
            for (int j = 0; j < COLONNE; j++) {
                celleGrid[i][j].setText("");
                celleGrid[i][j].setBackground(Color.WHITE);
                celleGrid[i][j].setForeground(new Color(30, 30, 30));
            }
        }

        for (JButton btn : tastiVirtuali.values()) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(50, 50, 50));
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
            JOptionPane.showMessageDialog(this, "Tentativi terminati! La parola era: " + paroliere.getParolaSegreta(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else {
            rigaCorrente++;
            colonnaCorrente = 0;
        }
    }
}