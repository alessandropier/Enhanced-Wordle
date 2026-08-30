package it.uniba.app;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WordleFrame extends JFrame {

    private static int RIGHE;
    private static int COLONNE;
    
    private JLabel[][] celleGrid;
    
    // Tengono traccia di dove sta scrivendo l'utente
    private int rigaCorrente = 0;
    private int colonnaCorrente = 0;
    
    // IMPOSTATO A TRUE: All'avvio la tastiera è bloccata finché non si preme "NUOVA PARTITA"
    private boolean tastieraBloccata = true;
    private boolean hasWon = false;
    private boolean wasSecretWordShown = false;

    private Giocatore giocatore;
    private Paroliere paroliere;
    private Matrice matrice;

    public WordleFrame(Giocatore g, Paroliere p, Matrice m) {
        this.giocatore = g;
        this.paroliere = p;
        this.matrice = m;

        RIGHE = Controller.getMaxTentativi();
        COLONNE = Controller.getNumCaratteri();

        setTitle("Wordle Java");
        setSize(460, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. PANNELLO SUPERIORE CON I BOTTONI ---
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        panelBottoni.setBackground(new Color(54, 57, 58));

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
        panelGriglia.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        celleGrid = new JLabel[RIGHE][COLONNE];
        
        for (int i = 0; i < RIGHE; i++) {
            for (int j = 0; j < COLONNE; j++) {
                JLabel cella = new JLabel("", JLabel.CENTER);
                cella.setFont(new Font("SansSerif", Font.BOLD, 26));
                cella.setOpaque(true);
                cella.setBackground(Color.WHITE);
                cella.setForeground(new Color(30, 30, 30));
                cella.setBorder(BorderFactory.createLineBorder(new Color(211, 214, 218), 2));
                
                celleGrid[i][j] = cella;
                panelGriglia.add(cella);
            }
        }
        add(panelGriglia, BorderLayout.CENTER);

        // --- 3. GESTIONE EVENTI DEI BOTTONI ---
        btnNuovaPartita.addActionListener(e -> gestisciNuovaPartita());
        btnMostra.addActionListener(e -> gestisciMostraParola());
        btnEsci.addActionListener(e -> gestisciEsci());

        // --- 4. ASCOLTATORE DELLA TASTIERA ---
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

    private void styleButton(JButton btn) {
    btn.setFont(new Font("SansSerif", Font.BOLD, 12));
    btn.setFocusPainted(false);
    btn.setBackground(new Color(54, 57, 58)); // Grigio scuro moderno
    btn.setForeground(Color.WHITE);
    
    // Effetto "Mano" del Mouse
    btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); 
    btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
}

    /**
     * Gestisce il click sul bottone "NUOVA PARTITA"
     */
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

    /**
     * Gestisce il click sul bottone "MOSTRA PAROLA"
     */
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

    /**
     * Gestisce il click sul bottone "ESCI" con finestra di dialogo
     */
    private void gestisciEsci() {
        int scelta = JOptionPane.showConfirmDialog(
            this, 
            "Sei sicuro di voler uscire dal gioco?", 
            "Conferma uscita", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        // Se l'utente clicca su "Si" (YES_OPTION)
        if (scelta == JOptionPane.YES_OPTION) {

            // Mostro la parola segreta
            if(paroliere.getParolaSegreta() != null && !hasWon && !wasSecretWordShown)
                JOptionPane.showMessageDialog(this, "La parola segreta era: " + paroliere.getParolaSegreta(), "Uscita", JOptionPane.INFORMATION_MESSAGE);
            
            System.exit(0);
        }
        // Se l'utente clicca su "No", la finestra si chiude da sola e la partita continua

        // Impostiamo nuovamente il focus sulla matrice dei tentativi
        requestFocusInWindow();
    }

    /**
     * Resetta la griglia grafica e le variabili di stato per una nuova partita
     */
    private void resettaInterfacciaGrafica() {
        rigaCorrente = 0;
        colonnaCorrente = 0;
        tastieraBloccata = false; // Sblocca la tastiera

        for (int i = 0; i < RIGHE; i++) {
            for (int j = 0; j < COLONNE; j++) {
                celleGrid[i][j].setText("");
                celleGrid[i][j].setBackground(Color.WHITE);
                celleGrid[i][j].setForeground(new Color(30, 30, 30));
            }
        }
        requestFocusInWindow();
    }

    private void gestisciTastoPremuto(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char keyChar = e.getKeyChar();

        if (keyCode == KeyEvent.VK_ENTER) {
            inviaTentativo();
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            if (colonnaCorrente > 0) {
                colonnaCorrente--;
                celleGrid[rigaCorrente][colonnaCorrente].setText("");
            }
        } else if (Character.isLetter(keyChar) && colonnaCorrente < COLONNE) {
            char lettera = Character.toUpperCase(keyChar);
            celleGrid[rigaCorrente][colonnaCorrente].setText(String.valueOf(lettera));
            colonnaCorrente++;
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

        // Salviamo il tentativo fatto PRIMA di processarlo perché ci serve 
        // come indice per i controlli successivi
        int tentativoFatto = giocatore.getTentativi();

        // Eseguiamo il tentativo nel backend
        Controller.tentativo(giocatore, parolaInserita, paroliere, matrice);

        boolean tutteVerdi = true;

        // Aggiorniamo la riga corrente con i colori restituiti dalla matrice
        for (int j = 0; j < COLONNE; j++) {
            char lettera = matrice.getTentativiList().get(tentativoFatto).charAt(j);
            celleGrid[tentativoFatto][j].setText(String.valueOf(lettera));

            String codiceColoreANSI = matrice.getColoriList().get(tentativoFatto).get(j);

            if (codiceColoreANSI.equals("\u001B[42m")) { // Verde
                celleGrid[tentativoFatto][j].setBackground(new Color(106, 170, 100));
                celleGrid[tentativoFatto][j].setForeground(Color.WHITE);
            } else {
                tutteVerdi = false; // Se anche una sola lettera non è verde, non è vittoria piena
                if (codiceColoreANSI.equals("\u001B[103m")) { // Giallo
                    celleGrid[tentativoFatto][j].setBackground(new Color(201, 180, 88));
                    celleGrid[tentativoFatto][j].setForeground(Color.WHITE);
                } else { // Grigio
                    celleGrid[tentativoFatto][j].setBackground(new Color(120, 124, 126));
                    celleGrid[tentativoFatto][j].setForeground(Color.WHITE);
                }
            }
        }
        
        // Verifichiamo se la partita è terminata (Vittoria o Game Over sull'ultima riga)
        if (tutteVerdi) {
            tastieraBloccata = true; // Blocca la tastiera
            hasWon = true;
            JOptionPane.showMessageDialog(this, "Complimenti, hai indovinato la parola!", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
        } else if (tentativoFatto >= RIGHE - 1) {
            tastieraBloccata = true; // Blocca la tastiera
            wasSecretWordShown = true;
            JOptionPane.showMessageDialog(this, "Tentativi terminati! La parola era: " + paroliere.getParolaSegreta(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Avanziamo alla riga successiva
            rigaCorrente++;
            colonnaCorrente = 0;
        }
    }
}