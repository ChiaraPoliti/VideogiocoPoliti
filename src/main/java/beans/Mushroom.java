package beans;

import javax.swing.ImageIcon;

/** 
 * @author Chiara
 */
public class Mushroom extends PowerUp {
	/** Classe che definisce il fungo,
	 * estensione della classe PowerUp.
	 */


	
	public Mushroom(int x, int y) {
		super(x, y);
		
		
		try {
			java.net.URL imageUrl = getClass().getResource("/images/fungo.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di fungo caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di fungo non trovata nel classpath: /images/fungo.png");
	            this.image = null;
	        }
		}catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di fungo: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	     }   
		}
	

	@Override
	public void applyEffect(Player mario) {
		if (!isCollected) {
			mario.toBig();
			this.isCollected = true;
		}	
	}

		
}

