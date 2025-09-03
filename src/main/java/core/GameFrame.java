package core; 
import javax.swing.JButton;
import javax.swing.JFrame;

import beans.Player;
import enums.GameState;
import logic.InputManager;
import logic.Level1;
//import audio.MusicPlayer;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.KeyListener;


public class GameFrame extends JFrame { 
/**
 * Classe che serve per la creazione di una finestra, che conterrà la grafica del gioco.
 */
	private GamePanel gamePanel; //dove verrà disegnato il gioco
	private StartPanel startPanel;
	//private JButton startGame;
	//private MusicPlayer mp;
	
	public static final String NAME = "Super Mario Bros";
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 200;
	

	public GameFrame() {
		this.setPreferredSize(new Dimension (WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setTitle(NAME);
		this.setResizable(false);
		//this.setVisible(true);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		
		this.startPanel = new StartPanel(this);
		this.add(startPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//this.gamePanel = new GamePanel ();
		//add(gamePanel);
		//this.gamePanel.setFocusable(true);
        //this.gamePanel.requestFocusInWindow();
		
        
        //avvio musica
        //this.mp = new MusicPlayer("resources/videobackground.wav");
		
		
		/*JButton startButton = new JButton("Inizia il Gioco");
        startButton.setBounds(350, 150, 150, 30);
        startButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // Nascondi il bottone
                startButton.setVisible(false);
                
                // Avvia il GamePanel
                startGame();
            }
        });*/
	}
	
	public void startGame() {
	    // Metodo chiamato dal bottone in StartPanel
	    this.remove(startPanel); // Rimuovi il pannello di avvio
	    this.gamePanel = new GamePanel(); // Crea il pannello del gioco
	    this.add(gamePanel); // Aggiungilo alla finestra

	    // Avvia l'input listener e il focus
	    InputManager im = new InputManager(gamePanel.getMario(), this.gamePanel);
	    this.gamePanel.addKeyListener(im);
	    this.gamePanel.setFocusable(true);
	    this.gamePanel.requestFocusInWindow();
	    
	    this.revalidate();
	    this.repaint();
	    
	    this.gamePanel.startGame();
	}

    
    
	
	// avvia musica
   /* public void startBackgroundMusic() {
        if (mp != null) {
            mp.playLoop();
        }
    }

    // ferma musica
    public void stopBackgroundMusic() {
        if (mp != null) {
            mp.stop();
        }
    }*/
	
	
	

}

