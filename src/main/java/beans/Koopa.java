package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import core.TileMap;

public class Koopa extends Enemy {
	private boolean isInShell;
	private Image koopaSmash;
	private long shellStartTime;
	
	private static final int WIDTH_KOOPA = 25;
	private static final int HEIGHT_KOOPA = 30;
	private static final int WIDTH_SHELL = 16;
	private static final int HEIGHT_SHELL = 16;
    private static final long SHELL_DURATION = 1000;
    //private static final double KOOPA_DAMAGE = 0.5;
    private static final double KOOPA_DAMAGE = 1;
    
	
	public Koopa (int x, int y) {
		super (x, y, WIDTH_KOOPA, HEIGHT_KOOPA, null);
		this.isInShell = false;
		this.shellStartTime = 0;
		
		//estraggo le immagini
		try {
			java.net.URL imageUrl = getClass().getResource("/tiles/tile_10.png");
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di koopa caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di koopa non trovata nel classpath: /tiles/tile_10.png");
	            this.image = null;
	        }
	        
	     // Caricamento dell'immagine "smash" (se esiste)
            java.net.URL koopaSmashUrl = getClass().getResource("/tiles/tile_9.png"); 
            if (koopaSmashUrl != null) {
                this.koopaSmash = new ImageIcon(koopaSmashUrl).getImage(); 
            } else {
                System.err.println("ERRORE: Immagine Guscio Koopa non trovata: /tiles/tile_9.png");
                this.koopaSmash = null;
            }
	    } catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di Guscio Koopa: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	    }
	}

	/**
	 * Metodo che definisce il moto del Koopa
	 */
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
	    if (this.isAlive && this.isInShell) { //se koopa è vivo e nel guscio
	        long currentTime = System.currentTimeMillis(); //salvo il tempo
	        if (currentTime - shellStartTime >= SHELL_DURATION) { //se il tempo attuale è più grande del momento di schiacciamento + durata di guscio
	            this.isRemovable = true; //si può rimuovere
	            this.isAlive = false; // muore
	        }
	    } else if (this.isAlive && !isInShell) { //se è vivo e non è nel guscio
	        super.update(mapWidthPixels, mapHeightPixels, tileMap);// è un normale nemico --> metodo update() di Enemy
	    }
	} 
		
	/**
	 * Metodo che applica il danno a Mario se il Koopa è vivo
	 */
	@Override
	public void dealDamage(Player mario) {
		if (this.isAlive) {
			mario.takeDamage(KOOPA_DAMAGE);// danno a mario = meno vita
		}
	}
	
	/**
	 * Metodo che implementa lo stato del Koopa nel guscio.
	 * In questo modo non è pericoloso (solo se colpito da sopra) e rimane vivo per qualche secondo, prima di morire e sparire
	 */
	public void toShell() {
		if (!this.isInShell) { // se non è nel guscio
			this.isDangerous = false; //setto tutto
			this.isMovingLeft = false;
			this.isMovingRight = false;
			this.isInShell = true;
			this.isRemovable = false;
			this.y += 14; // abbasso la sua posizione per farlo apparire 'a terra'
			this.vel_x = 0;
			this.vel_y = 0;
			this.height = HEIGHT_SHELL;
			this.width = WIDTH_SHELL;
			shellStartTime = System.currentTimeMillis(); //scatta il timer per la sparizione
		}	
	}
	
	/**
	 * Metodo che definisce la morte diretta
	 */
	@Override
	public void die() {
		this.isAlive = false;
		this.isInShell = true;
		this.isDangerous = false;
		this.isRemovable = true;
		this.vel_x = 0;
		this.vel_y = 0;
	}
	
	
	/**
	 * Metodo che disegna il koopa con le immagini importate dal costruttore
	 */
	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int screenX = this.x - cameraX;
		int screenY = this.y - cameraY;
		
		if (this.isAlive) {
			if (this.isInShell) {
				if (koopaSmash != null) {
					g.drawImage(koopaSmash, screenX, screenY, this.width, this.height, null);
				}else {
					g.setColor(Color.GREEN.darker());
					g.fillRect (this.x, this.y, this.width, this.height);
				}
			} else {
				if (this.image != null) {
					g.drawImage(this.image, screenX, screenY, this.width, this.height, null);
				} else {
					g.setColor(Color.GREEN);
					g.fillRect (this.x, this.y, this.width, this.height);
				}
			}
			
		} 
	}

	//Sono due metodi scritti per eventualmente amplicare il progetto, lasciando che il koopa torni al suo stato normale dopo il tempo previsto 
	//e che muoia solo se colpito di nuovo
	//NON USATI
	
	/*public void toNormalSize() {
		if (this.isInShell) {
			this.isDangerous = true;
			this.isInShell = false;
			this.isMovingRight = false;
			this.isMovingLeft = true;
			this.vel_x = Enemy.VEL_X;
			this.height = HEIGHT_KOOPA;
			this.width = WIDTH_KOOPA;
			shellStartTime = 0;
		}
	}
	
	 public void upgradeStatus() {
	     if (isInShell) {
	    	 long currentTime = System.currentTimeMillis();
	         if (currentTime - shellStartTime <= SHELL_DURATION) {
	            toNormalSize();
	         }
	     }
	 }*/
	
	//GETTER E SETTER
	
	public boolean isInShell() {
		return isInShell;
	}

	public Image getKoopaSmash() {
		return koopaSmash;
	}

	public long getShellStartTime() {
		return shellStartTime;
	}

	public static int getWidthKoopa() {
		return WIDTH_KOOPA;
	}

	public static int getHeightKoopa() {
		return HEIGHT_KOOPA;
	}

	public static int getWidthShell() {
		return WIDTH_SHELL;
	}

	public static int getHeightShell() {
		return HEIGHT_SHELL;
	}

	public static long getShellDuration() {
		return SHELL_DURATION;
	}

	public static double getKoopaDamage() {
		return KOOPA_DAMAGE;
	}

	public void setInShell(boolean isInShell) {
		this.isInShell = isInShell;
	}

	public void setKoopaSmash(Image koopaSmash) {
		this.koopaSmash = koopaSmash;
	}

	public void setShellStartTime(long shellStartTime) {
		this.shellStartTime = shellStartTime;
	}
}