package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe che definisce la schemrata iniziale del gioco, col bottone di avvio
 */
public class StartPanel extends JPanel {
	private GameFrame gameFrame;
	private Image background;
	
    public StartPanel(GameFrame gameFrame) {
    	this.gameFrame = gameFrame;
        this.setLayout(new GridBagLayout()); // usato per migliorare l'estetica del bottone
        GridBagConstraints gbc = new GridBagConstraints(); //vincolo per la collocazione del bottone
        gbc.insets = new Insets(120, 0, 0, 0); //collocazione
        this.setBackground(Color.DARK_GRAY); //colore sfondo 
        
     // Carica l'immagine di sfondo
        try {
            background = new ImageIcon(getClass().getResource("/images/background.png")).getImage();
        } catch (Exception e) {
            System.err.println("Immagine di sfondo non trovata.");
        }
        
        //genera un bottone per iniziare con le sue caratteristiche estetiche
        JButton startButton = new JButton("Inizia il gioco");
        startButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(new Color (192, 100, 16)); // Colore marrone
        startButton.setOpaque(true); //opacità
        startButton.setBorderPainted(true); //bordo visibile
        
        this.add(startButton, gbc); //aggiungo al panello
        
        //aggiungo listener al bottone
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(GamePanel.this, "Inizia il gioco!", "Avviso", JOptionPane.INFORMATION_MESSAGE);
                //System.out.println("Inizia  il gioco!");
                //startGame();
            	gameFrame.startGame();
            }
        });
    }
    
    /**
     * Disegna lo sfondo
     */
   @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}