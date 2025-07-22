package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

import core.TileMap;

public class Goomba extends Enemy {
	private Image goombaSmash;
	private long deathTime = 0;
	
	public static final int HEIGHT_GOOMBA = 3;
	public static final int WIDTH_GOOMBA = 5;
	public static final double GOOMBA_DAMAGE = 0.5; 
	public static final long SQUASH_DURATION = 1000;

	public Goomba(int x, int y, int width, int height, Image image) {
		super(x,y, WIDTH_GOOMBA, HEIGHT_GOOMBA, null);
		
		
		try {
			java.net.URL imageUrl = getClass().getResource("/images/goomba.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di goomba caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di goomba non trovata nel classpath: /images/goomba.png");
	            this.image = null;
	        }
	        
	     // Caricamento dell'immagine "smash" (se esiste)
            java.net.URL goombaSmashUrl = getClass().getResource("/images/stoppedGoomba.png"); 
            if (goombaSmashUrl != null) {
                this.goombaSmash = new ImageIcon(goombaSmashUrl).getImage(); 
            } else {
                System.err.println("ERRORE: Immagine Goomba Splash non trovata: /images/stoppedGoomba.png");
                this.goombaSmash = null;
            }
	    } catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di goomba: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	    }
	    
	}
	
	
	public void dealDamage (Player mario) {
		if (this.isAlive) {
			mario.takeDamage(GOOMBA_DAMAGE);	
		}
	}
	
	@Override
    public void die() {
        // Quando il Goomba muore (viene schiacciato):
        this.isAlive = false; // Imposta lo stato a non vivo (per il disegno)
        this.vel_x = 0;     // Ferma il movimento orizzontale
        this.vel_y = 0;     // Ferma la gravità (non deve più cadere)
        this.isOnGround = true; // Simula che sia "appiccicato" al terreno dopo essere schiacciato
        this.deathTime = System.currentTimeMillis(); // Registra il momento della morte
    }

	@Override
    public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
        // Se il Goomba è vivo, aggiorna normalmente
        if (this.isAlive && this.deathTime == 0) { // deathTime == 0 significa che non è stato schiacciato
            // Logica di movimento ereditata da Enemy
            if (isMovingLeft) {
                this.vel_x = -Enemy.VEL_X;
            } else if (isMovingRight) {
                this.vel_x = Enemy.VEL_X;
            } else {
                this.vel_x = 0;
            }
            super.update(mapWidthPixels, mapHeightPixels, tileMap);
        } else if (!this.isAlive && this.deathTime > 0) {
            // Se il Goomba è "schiacciato" (non vivo, ma con deathTime impostato),
            // controlla se è passato abbastanza tempo per farlo scomparire del tutto.
            long currentTime = System.currentTimeMillis();
            if (currentTime - deathTime >= SQUASH_DURATION) {
                this.isRemovable = true;
            }
            // Non chiamare super.update() qui, altrimenti la gravità continua ad applicarsi
            // e il Goomba "schiacciato" cadrebbe.
        }
        // Se isAlive è false e deathTime è 0, significa che è "morto" da GamePanel (es. caduto fuori mappa)
        // e quindi non va disegnato o aggiornato.
    }
	
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
}




