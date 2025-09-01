package beans;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import core.TileMap;

public class Koopa extends Enemy {
	private boolean isDangerous;
	private boolean isInShell;
	private Image koopaSmash;
	
	public static final int WIDTH_KOOPA = 25;
	public static final int HEIGHT_KOOPA = 30;
	public static final int WIDTH_SHELL = 4;
	public static final int HEIGHT_SHELL = 4;
    private static final long SHELL_DURATION = 5000;
    public static final double KOOPA_DAMAGE = 0.5;
    
    public long shellStartTime = 0;
    
	
	public Koopa (int x, int y) {
		super (x, y, WIDTH_KOOPA, HEIGHT_KOOPA, null);
		this.isDangerous = true;
		this.isInShell = false;
		
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

	@Override
	public void dealDamage(Player mario) {
		if (this.isAlive) {
			mario.takeDamage(KOOPA_DAMAGE);
		}
	}
	
	@Override
	public void die() {
		this.isAlive = true;
		this.isInShell = true;
		this.isDangerous = false;
		this.isRemovable = false;
		this.vel_x = 0;
		this.vel_y = 0;
	}
	
	public void sparito() {
		this.isAlive = false;
		this.isRemovable = true;
		this.isInShell = false;
		this.isDangerous = false;
		this.isRemovable = true;
		
	}
	
	
/*
	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
	    if (!isAlive && !isInShell) {
	        // Koopa morto da caduta fuori mappa, non fare nulla
	        return;
	    }

	    // --- Logica per uscire dallo shell dopo durata ---
	    if (isInShell) {
	        long currentTime = System.currentTimeMillis();
	        if (currentTime - shellStartTime >= SHELL_DURATION) {
	            toNormalSize();
	        }
	    }

	    // --- Controllo collisione terreno/muri ---
	    int footY = y + height;
	    int frontX = isMovingRight ? x + width + 1 : x - 1;
	    int frontCol = frontX / TileMap.TILE_SIZE;
	    //int footRow = footY / TileMap.TILE_SIZE;

	    boolean groundAhead = tileMap.isTileSolid(frontCol, (footY + 1) / TileMap.TILE_SIZE);
	    boolean wallAhead = tileMap.isTileSolid(frontCol, y / TileMap.TILE_SIZE) || tileMap.isTileSolid(frontCol, (y + height / 2) / TileMap.TILE_SIZE);

	    // Inversione direzione se muro o buco
	    if (wallAhead || !groundAhead) {
	        if (isMovingRight) { isMovingRight = false; isMovingLeft = true; }
	        else { isMovingRight = true; isMovingLeft = false; }
	    }

	    // --- Movimento ---
	    if (isInShell && vel_x != 0) {
	        // Guscio calciato: muove con vel_x
	        x += vel_x;
	    } else if (!isInShell) {
	        // Koopa normale
	        vel_x = isMovingLeft ? -VEL_X : VEL_X;
	        x += vel_x;
	    }

	    // --- Gravità ---
	    if (!isOnGround) {
	        vel_y += g;  // applica gravità
	    }
	    y += vel_y;

	    // Check collisione con pavimento
	    int bottomRow = (y + height) / TileMap.TILE_SIZE;
	    if (tileMap.isTileSolid(x / TileMap.TILE_SIZE, bottomRow) ||
	            tileMap.isTileSolid((x + width - 1) / TileMap.TILE_SIZE, bottomRow)) {
	            y = bottomRow * TileMap.TILE_SIZE - height;
	            vel_y = 0;
	            isOnGround = true;
	        } else {
	            isOnGround = false;
	        }

	        // --- Caduta fuori mappa ---
	        if (y > mapHeightPixels + 100) {
	            die();
	        }
	    }*/


	

	@Override
	public void update(int mapWidthPixels, int mapHeightPixels, TileMap tileMap) {
	    if (!this.isAlive) {
	        // Se morto, potresti voler gestire la rimozione come per Goomba
	        // o semplicemente non aggiornarlo più
	        return;
	    }

	    // Logica per tornare allo stato normale dal guscio
	    upgradeStatus();

	    // Logica di movimento basata sullo stato
	    if (isInShell) {
	        // Se nel guscio e sta rotolando (vel_x != 0), continua a muoversi
	        // Altrimenti, è fermo nel guscio, non si muove.
	        // La vel_x verrebbe impostata esternamente dal CollisionManager quando Mario "calcia" il guscio.
	        if (this.vel_x != 0) {
	            super.update(mapWidthPixels, mapHeightPixels, tileMap); // Applica movimento e gravità
	        } else {
	            // È nel guscio fermo, applica solo gravità se non a terra
	            if (!isOnGround) {
	                this.vel_y += g;
	            }
	            this.y += this.vel_y;
	            // TODO: Aggiungi qui un controllo collisioni verticali base per il guscio fermo
	        }
	    } else {
	        // Stato normale: si muove come un normale nemico
	        if (isMovingLeft) {
	            this.vel_x = -Enemy.VEL_X;
	        } else if (isMovingRight) {
	            this.vel_x = Enemy.VEL_X;
	        } else {
	            this.vel_x = 0;
	        }
	        super.update(mapWidthPixels, mapHeightPixels, tileMap);
	    }

	    // Gestione della caduta dalla mappa
	    if (this.y > mapHeightPixels + 100) {
	        this.die();
	    }
	}

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
		
	public void toShell() {
		if (!this.isInShell) {
			this.isDangerous = false;
			this.isMovingLeft = false;
			this.isMovingRight = false;
			this.isInShell = true;
			this.vel_x = 0;
			this.height = HEIGHT_SHELL;
			this.width = WIDTH_SHELL;
			shellStartTime = System.currentTimeMillis();
		}	
	}
	
	public void toNormalSize() {
		if (this.isInShell) {
			this.isDangerous = true;
			this.isInShell = false;
			this.isMovingRight = false;
			this.isMovingLeft = true;
			this.vel_x = this.VEL_X;
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
	 }
	 
	 

}
