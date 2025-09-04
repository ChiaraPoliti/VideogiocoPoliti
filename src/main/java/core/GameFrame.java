package core; 
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import logic.InputManager;


/**
 * Classe che serve per la creazione di una finestra, che conterrà la grafica del gioco.
 */
public class GameFrame extends JFrame { 
	private GamePanel gamePanel; //dove verrà disegnato il gioco
	private StartPanel startPanel;
	
	public static final String NAME = "Super Mario Bros";
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 200;
	

	public GameFrame() {
		this.setPreferredSize(new Dimension (WINDOW_WIDTH, WINDOW_HEIGHT)); //dimensioni finestra
		this.setTitle(NAME); //nome (in alto a sx piccolo)
		this.setResizable(false); //non si può allargare
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //se si preme su X della finestra, chiude definitivamente app
		
		this.startPanel = new StartPanel(this);
		this.add(startPanel);
		this.pack(); //ridimensiona tutte le componenti
		this.setLocationRelativeTo(null); //centra sullo schermo del pc
		this.setVisible(true); //visibile
	
        //avvio musica
        //this.mp = new MusicPlayer("resources/videobackground.wav");
	}
	
	public void startGame() {
	    // Metodo chiamato dal bottone in StartPanel
	    this.remove(startPanel); // Rimuovi il pannello di avvio
	    this.gamePanel = new GamePanel(); // Crea il pannello del gioco
	    this.add(gamePanel); // aggiunto alla finestra

	    // Avvia l'input listener e il focus
	    InputManager im = new InputManager(gamePanel.getMario(), this.gamePanel);
	    this.gamePanel.addKeyListener(im);
	    this.gamePanel.setFocusable(true);
	    this.gamePanel.requestFocusInWindow(); //+ focus
	    
	    this.revalidate(); //ricalcola layout
	    this.repaint(); //ridisegna
	    
	    this.gamePanel.startGame();
	}

	//Tentativo di aggiunta di sottofondo musicale
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

