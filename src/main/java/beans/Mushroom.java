package beans;

import javax.swing.ImageIcon;

/** Classe che definisce il fungo,
 * estensione della classe PowerUp.
 */
public class Mushroom extends PowerUp {
	public Mushroom(int x, int y) {
		super(x, y);
		
		//estraggo l'immagine
		try {
			java.net.URL imageUrl = getClass().getResource("/tiles/tile_7.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di fungo caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di fungo non trovata nel classpath: /tiles/tile_7.png");
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
		if (!isCollected) { //se non è già stato raccolto
			mario.toBig(); // fa crescere Mario
			this.isCollected = true; // si setta come raccolto
		}	
	}	
}

