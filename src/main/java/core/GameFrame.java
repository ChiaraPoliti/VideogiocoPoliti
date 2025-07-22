package core; 
import javax.swing.JFrame;
import java.awt.Dimension;


public class GameFrame extends JFrame { 
/**
 * Classe che serve per la creazione di una finestra, che conterrà la grafica del gioco.
 */
	private GamePanel gamePanel; //dove verrà disegnato il gioco
	
	public static final String NAME = "Super Mario Bros";
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 200;
	

	public GameFrame() {
		this.setPreferredSize(new Dimension (WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setTitle(NAME);
		this.setResizable(false);
		this.setVisible(true);
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		
		this.gamePanel = new GamePanel ();
		add(gamePanel);
		this.gamePanel.setFocusable(true);
        this.gamePanel.requestFocusInWindow();
		
        
 
		
	}


}

