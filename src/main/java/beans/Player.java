package beans;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import core.TileMap;

public class Player extends MovableGameObject {
	/**
	 * @author Chiara
		Classe che definisce il giocatore, 
	 */
	private boolean isBig = false;
	private boolean isJumping = false;
	private boolean isRunning = false;
	private int score;
	private int hp;
	
	//public static final double g = 1.0;
	public static final int MAX_VEL_X = 5;
	public static final int NORMAL_HEIGHT = 60;
	public static final int NORMAL_WIDTH = 45;
	public static final int BIG_HEIGHT = 80;
	public static final int BIG_WIDTH = 55;
	public static final int JUMP_VEL = -15;
	
	public Player(int x, int y) {
		super(x,y,NORMAL_WIDTH, NORMAL_HEIGHT, null, 0, 0);
		this.score = 0;
		this.hp = 1;
		
		try {
			java.net.URL imageUrl = getClass().getResource("/images/mario.png");
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
	 * Definisce il danno che si subisce. 
	 **/
	public void takeDamage(double malus) {
		this.hp -= malus;
		if (this.hp <=0) {
			System.out.println("Game Over!");
		}else if (isBig) {
			this.toSmall();
		}
	}
	
	/**
	 * Fa crescere le misure di Mario.
	 **/
	public void toBig() {
		this.isBig = true;
		this.y -= (this.BIG_HEIGHT - this.NORMAL_HEIGHT);
		this.width = BIG_WIDTH;
		this.height = BIG_HEIGHT;
	}
	
	/**
	 * Diminuisce le dimensioni di Mario.
	 **/
	public void toSmall() {
		this.isBig = false;
		this.y -= (this.BIG_HEIGHT - this.NORMAL_HEIGHT);
		this.width = NORMAL_WIDTH;
		this.height = NORMAL_HEIGHT;
	}
	
	/**
	 * Coordina il salto.
	 **/
	public void jump() {
		if (isOnGround) {
			this.vel_y = JUMP_VEL;
			this.isJumping = true;
			this.isOnGround = false;
		}
	}
	
	/**
	 * Acquisisce il valore della moneta per aumentare il punteggio
	 **/
	public void getCoin(Coin coin) {
		this.score += coin.getValue();
	}
	
	/**
	 * Riceve il potenziamento fungo e applica il metodo di crescita
	 **/
	public void getMushroom (Mushroom mushroom) {
		this.toBig();
	}
	
	
	/**
	 * Aggiorna il movimento di Mario.
	 **/
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
		if (isMovingRight) {
			this.vel_x = MAX_VEL_X; 
		} else if (isMovingLeft) {
			this.vel_x = -MAX_VEL_X;
		}else {
			this.vel_x = 0;
		}
		
		super.update(mapWidthPixels, mapHeightPixels, tileMap);
		
		if (isOnGround && isJumping) {
            isJumping = false;
        }
        
        if (this.y > mapHeightPixels + 100) {
            System.out.println("Mario è caduto fuori dalla mappa! Game Over o Reset.");
            // Implementa qui la logica di game over/reset
        }
		
	}

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

	/**
	 * @return the isBig
	 */
	public boolean isBig() {
		return isBig;
	}

	/**
	 * @return the isJumping
	 */
	public boolean isJumping() {
		return isJumping;
	}

	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * @return the isOnGround
	 */
	public boolean isOnGround() {
		return isOnGround;
	}

	/**
	 * @return the isMovingLeft
	 */
	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	/**
	 * @return the isMovingRight
	 */
	public boolean isMovingRight() {
		return isMovingRight;
	}

	/**
	 * @return the vel_x
	 */
	public int getVel_x() {
		return vel_x;
	}

	/**
	 * @return the vel_y
	 */
	public int getVel_y() {
		return vel_y;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * @return the g
	 */
	public static double getG() {
		return g;
	}

	/**
	 * @return the maxVelX
	 */
	public static int getMaxVelX() {
		return MAX_VEL_X;
	}

	/**
	 * @return the normalHeight
	 */
	public static int getNormalHeight() {
		return NORMAL_HEIGHT;
	}

	/**
	 * @return the normalWidth
	 */
	public static int getNormalWidth() {
		return NORMAL_WIDTH;
	}

	/**
	 * @return the bigHeight
	 */
	public static int getBigHeight() {
		return BIG_HEIGHT;
	}

	/**
	 * @return the bigWidth
	 */
	public static int getBigWidth() {
		return BIG_WIDTH;
	}

	/**
	 * @return the jumpVel
	 */
	public static int getJumpVel() {
		return JUMP_VEL;
	}

	/**
	 * @param isBig the isBig to set
	 */
	public void setBig(boolean isBig) {
		this.isBig = isBig;
	}

	/**
	 * @param isJumping the isJumping to set
	 */
	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	/**
	 * @param isRunning the isRunning to set
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	/**
	 * @param isOnGround the isOnGround to set
	 */
	public void setOnGround(boolean isOnGround) {
		this.isOnGround = isOnGround;
	}

	/**
	 * @param isMovingLeft the isMovingLeft to set
	 */
	public void setMovingLeft(boolean isMovingLeft) {
		this.isMovingLeft = isMovingLeft;
	}

	/**
	 * @param isMovingRight the isMovingRight to set
	 */
	public void setMovingRight(boolean isMovingRight) {
		this.isMovingRight = isMovingRight;
	}

	/**
	 * @param vel_x the vel_x to set
	 */
	public void setVel_x(int vel_x) {
		this.vel_x = vel_x;
	}

	/**
	 * @param vel_y the vel_y to set
	 */
	public void setVel_y(int vel_y) {
		this.vel_y = vel_y;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @param hp the hp to set
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	
	
}
