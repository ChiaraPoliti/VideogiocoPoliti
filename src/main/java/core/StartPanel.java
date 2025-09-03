package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import core.GameFrame;

public class StartPanel extends JPanel {
	private GameFrame gameFrame;
	private Image background;
	
    public StartPanel(GameFrame gameFrame) {
    	this.gameFrame = gameFrame;
        this.setLayout(new GridBagLayout()); // usato per migliorare l'estetica del bottone
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(120, 0, 0, 0); 
        this.setBackground(Color.DARK_GRAY);
        
     // Carica l'immagine di sfondo (sostituisci "background.png" con il nome del tuo file)
        try {
            background = new ImageIcon(getClass().getResource("/images/background.png")).getImage();
        } catch (Exception e) {
            System.err.println("Immagine di sfondo non trovata.");
        }
        
        JButton startButton = new JButton("Inizia il gioco");
        startButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(new Color (192, 100, 16)); // Colore oro
        startButton.setOpaque(true);
        startButton.setBorderPainted(true);
        
        this.add(startButton, gbc);
        
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//ORA MESSAGGIO PER CAPIRE, POI DA MODIFICARE
                //JOptionPane.showMessageDialog(GamePanel.this, "Inizia il gioco!", "Avviso", JOptionPane.INFORMATION_MESSAGE);
                //System.out.println("Inizia  il gioco!");
                //startGame();
            	gameFrame.startGame();
            }
        });
    }
    
 // Sovrascrive paintComponent per disegnare l'immagine di sfondo
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}