package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import core.TileMap;

public class Goomba extends Enemy {
	private Image goombaSmash;
	private long deathTime;
	
	private static final int HEIGHT_GOOMBA = 15;
	private static final int WIDTH_GOOMBA = 20;
	//private static final double GOOMBA_DAMAGE = 0.5; 
	private static final double GOOMBA_DAMAGE = 1;
	private static final long SQUASH_DURATION = 1000;

	public Goomba(int x, int y) {
		super(x,y, WIDTH_GOOMBA, HEIGHT_GOOMBA, null);
		this.deathTime = 0;
		
		//estraggo le immagini
		try {
			java.net.URL imageUrl = getClass().getResource("/tiles/tile_8.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di goomba caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di goomba non trovata nel classpath: /tiles/tile_8.png");
	            this.image = null;
	        }
	        
	     // Caricamento dell'immagine "smash" (se esiste)
            java.net.URL goombaSmashUrl = getClass().getResource("/tiles/tile_23.png"); 
            if (goombaSmashUrl != null) {
                this.goombaSmash = new ImageIcon(goombaSmashUrl).getImage(); 
            } else {
                System.err.println("ERRORE: Immagine Goomba Splash non trovata: /tiles/tile_23.png");
                this.goombaSmash = null;
            }
	    } catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di goomba: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	    }
	}
	
	/**
	 * Gestione del moto del Goomba: in Enemy c'è metodo generale, qui metodo specifico
	 */
	@Override
    public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
        // Se il Goomba è vivo, aggiorna normalmente
        if (this.isAlive && this.deathTime == 0) { // se è vivo e non schiacciato(deathTime == 0 --> non è stato schiacciato)
            // Logica di movimento ereditata da Enemy
            super.update(mapWidthPixels, mapHeightPixels, tileMap); //si comporta esattamente con un nemico normale
        } else if (!this.isAlive && this.deathTime > 0) { // se non è vivo ed è stato schiacciato
            long currentTime = System.currentTimeMillis(); //tengo conto del tempo attuale
            if (currentTime - deathTime >= SQUASH_DURATION) { //se il tempo attuale è più grande del momento di schiacciamento + durata dello schiacciamento
                this.isRemovable = true; // il goomba può sparire
            }
        }
    }
	
	/**
	 * Metodo che definisce il danno del Goomba a Mario
	 */
	public void dealDamage (Player mario) {
		if (this.isAlive) { //se Goomba è vivo
			mario.takeDamage(GOOMBA_DAMAGE); //Applica il danno a Mario (abbasa vita)	
		}
	}
	
	/**
	 * Metodo che definisce la morte: a differenza di quello di enemy, non si setta subito isRemovable a True
	 */
	@Override
    public void die() {
        this.isAlive = false; //setto il nuovo stato (morto)
        this.vel_x = 0;     // non cammina più
        this.vel_y = 0;     // non cade più
        this.isOnGround = true; // è a terra
        this.deathTime = System.currentTimeMillis(); // salvo il momento in cui è stato schiacciato per il timer di sparizione
	}
	
	/**
	 * metodo che disegna limmagine di goomba dalle importazioni nel costruttore
	 */
	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int screenX = this.x - cameraX;
		int screenY = this.y - cameraY;
		
		if (this.isAlive) {
			if (this.image != null) {
				g.drawImage(this.image, screenX, screenY, this.width, this.height, null);
			} else {
				g.setColor(Color.BLACK);
				g.fillRect (this.x, this.y, this.width, this.height);
			
			}
		} else {
			if (goombaSmash != null) {
				g.drawImage(goombaSmash, screenX, screenY, this.width, this.height/2, null);
			}else {
				g.setColor(Color.DARK_GRAY);
				g.fillRect (this.x, this.y, this.width, this.height);
			
			}
		}
	}

	//GETTER E SETTER
	public Image getGoombaSmash() {
		return goombaSmash;
	}

	public long getDeathTime() {
		return deathTime;
	}

	public static int getHeightGoomba() {
		return HEIGHT_GOOMBA;
	}

	public static int getWidthGoomba() {
		return WIDTH_GOOMBA;
	}

	public static double getGoombaDamage() {
		return GOOMBA_DAMAGE;
	}

	public static long getSquashDuration() {
		return SQUASH_DURATION;
	}

	public void setGoombaSmash(Image goombaSmash) {
		this.goombaSmash = goombaSmash;
	}

	public void setDeathTime(long deathTime) {
		this.deathTime = deathTime;
	}
}

