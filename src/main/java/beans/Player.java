package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import core.TileMap;

/**
 * Classe che definisce il giocatore
 */
public class Player extends MovableGameObject {
	private boolean isBig;
	private boolean isJumping;
	private boolean isRunning;
	private int score;
	private int hp;
	
	private static final int MAX_VEL_X = 3;
	private static final int MAX_FALL_SPEED = 15;
	private static final int NORMAL_HEIGHT = 20;
	private static final int NORMAL_WIDTH = 24;
	private static final int BIG_HEIGHT = 25;
	private static final int BIG_WIDTH = 30;
	private static final int JUMP_VEL = -13;
	
	public Player(int x, int y) {
		super(x,y,NORMAL_WIDTH, NORMAL_HEIGHT, null, 0, 0);
		this.isBig = false;
		this.isJumping = false;
		this.isRunning = false;
		this.score = 0;
		this.hp = 1;
		
		//estraggo le immagini
		try {
			java.net.URL imageUrl = getClass().getResource("/images/mario.png"); //lascia questa immagine per proporzioni migliori
	        if (imageUrl != null) {
	        	this.image = new ImageIcon(imageUrl).getImage(); // Assegna all'immagine ereditata
	            System.out.println("Immagine di Mario caricata con successo da: " + imageUrl);
	        } else {
	            System.err.println("ERRORE: Immagine di Mario non trovata nel classpath: /images/mario.png");
	            this.image = null;
	        }
	    } catch (Exception e) {
	        System.err.println("Eccezione durante il caricamento dell'immagine di Mario: " + e.getMessage());
	        e.printStackTrace();
	        this.image = null;
	    }
	}
	
	/**
	 * Metodo che gestisce il moto del giocatore
	 * Le collisioni sono gestite nel CollisionManager in logic
	 */
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
	    //Moto orizzontale
		if (isMovingLeft) { //se si sposta a sx
	    	vel_x = -MAX_VEL_X; //aggiorno x con MRU
	    } else if (isMovingRight) { //se si sposta a dx
	        vel_x = MAX_VEL_X; // aggiorno con MRU
	    } else { //altrimenti
	    	vel_x = 0; //fermo
	    }
	    
	    //Gravità e moto verticale
	    this.vel_y += g; //gravità
	    if (this.vel_y > MAX_FALL_SPEED) { //gravità che aumenta la velocità fino al suo massimo
	        this.vel_y = MAX_FALL_SPEED;
	    }
	    
	    // Controlli per i limiti della mappa
	    if (this.x < 0) { //se è troppo indietro
	    	this.x = 0; //lo riporto all'origine
	    }
	    
	    if (this.x + this.width > mapWidthPixels) { //se fuori bordo destro
	    	this.x = mapWidthPixels - this.width; //lo riporta indietro della sua larghezza
	    }
	    if (this.y > mapHeightPixels) { //se troppo in basso
	        this.hp = 0; // muore
	    }
	}
	
	/**
	 * Gestisce il salto
	 */
	public void jump() {
		if (isOnGround) { //può saltare solo se tocca terreno
			this.setVel_y(JUMP_VEL); //fissa velocità verticale
			this.isJumping = true;
			this.setOnGround(false);
		}
	}
	
	/**
	 * Gestisce il rimbalzo per il salto sui nemici
	 */
	public void bounce() {
		this.setVel_y(-8);
	}
	
	/**
	 * Acquisisce il valore della moneta per aumentare il punteggio
	 **/
	public void getCoin(Coin coin) {
		this.score += coin.getValue();
	}
	
	/**
	 * Fa crescere le misure di Mario.
	 **/
	public void toBig() {
		if (!isBig) { //se non è già grande
			this.isBig = true;
			this.width = BIG_WIDTH; // modifico misure
			this.height = BIG_HEIGHT;
			if (this.hp == 1) {
				this.hp ++; //aggiungo una vita
			}
		}
	}
	
	/**
	 * Fa decrescere le dimensioni di Mario.
	 **/
	public void toSmall() {
		if (this.isBig) {
			this.isBig = false;
			this.width = NORMAL_WIDTH; // aggiorno le misure
			this.height = NORMAL_HEIGHT;
			if (this.hp == 2) { //vita in meno
				this.hp --;
			}
		}
	}
	
	
	/**
	 * Gestisce il danno che si subisce
	 * @param malus è il danno che subisce 
	 **/
	public void takeDamage(double malus) {
		this.hp -= malus;
		if (this.hp <=0) {
			System.out.println("Game Over!");
			this.die();
		}else if (isBig) {
			this.toSmall();
		}
	}

	/**
	 * definisce lo stato di morte
	 */
	public void die() {
		this.hp = 0;
		System.out.println("GAME OVER");
	}
	
	/**
	 * Resetta lo stato di mario per garantire che il gioco si possa riavviare con le stesse condizioni iniziali del livello1
	 */
	public void resetState() {
	    this.x = 100; // Posizione X iniziale
	    this.y = 112; // Posizione Y iniziale
	    this.height = Player.NORMAL_HEIGHT;
	    this.width = Player.NORMAL_WIDTH;
	    this.vel_x = 0;
	    this.vel_y = 0;
	    this.isMovingLeft = false;
	    this.isMovingRight = false;
	    this.isOnGround = false;
	    this.isBig = false;
	    this.isRunning = false;
	    this.isJumping = false;
	    this.hp = 1; 
	    this.score = 0;
	   
	}
	
	/**
	 * Acquisisce un rettangolo sopra la testa per gestire meglio le collisioni
	 * @return il rettangolo di spazio
	 */
	public Rectangle getHeadBox() {
		return new Rectangle(this.x, this.y, this.width, 8);  
	}
	
	/**
	 * Metodo che disegna il giocatore con le immagini acquisite nel costruttore
	 */
	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int screenX = x - cameraX;
        int screenY = y - cameraY;
        //System.out.println("DEBUG Mario: worldX=" + x + ", worldY=" + y + 
                //" | cameraX=" + cameraX + ", cameraY=" + cameraY +
                //" | screenX=" + screenX + ", screenY=" + screenY +
                //" | width=" + width + ", height=" + height);
		if (this.image != null) {
			g.drawImage(image, screenX, screenY, width, height, null);
		} else {
			g.setColor(Color.BLUE);
			g.fillRect (screenX,screenY,width, height);
		
		}
		
	}

	//GETTER E SETTER
	public boolean isBig() {
		return isBig;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public int getScore() {
		return score;
	}

	public int getHp() {
		return hp;
	}

	public static int getMaxVelX() {
		return MAX_VEL_X;
	}

	public static int getMaxFallSpeed() {
		return MAX_FALL_SPEED;
	}

	public static int getNormalHeight() {
		return NORMAL_HEIGHT;
	}

	public static int getNormalWidth() {
		return NORMAL_WIDTH;
	}

	public static int getBigHeight() {
		return BIG_HEIGHT;
	}

	public static int getBigWidth() {
		return BIG_WIDTH;
	}

	public static int getJumpVel() {
		return JUMP_VEL;
	}

	public void setBig(boolean isBig) {
		this.isBig = isBig;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
}
