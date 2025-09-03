
package logic;

import java.awt.Rectangle;
//import java.util.ArrayList;
import java.util.List;
import beans.Player;
import core.TileMap;
//import enums.blockType;
import beans.Enemy;
import beans.GameObject;
import beans.Koopa;
import beans.PowerUp;
import beans.Block;
import beans.Coin;

public class CollisionManager {

	public void checkAllCollisions(Player mario, List<Enemy> enemies, List<Block> blocks, List<Coin> coins, List<PowerUp> powerUps, TileMap tileMap) {
        // 1. Controlla le collisioni del giocatore con la mappa
        checkPlayerTileCollisions(mario, tileMap);

        // 2. Controlla le collisioni del giocatore con i nemici
        checkPlayerEnemyCollisions(mario, enemies);

        // 3. Controlla le collisioni del giocatore con i blocchi
        checkPlayerBlockCollisions(mario, blocks, coins, powerUps);

        // 4. Controlla le collisioni del giocatore con monete e power-up
        checkPlayerCoinCollisions(mario, coins);
        checkPlayerPowerUpCollisions(mario, powerUps);

        // 5. Aggiungi i controlli per le collisioni dei nemici con la mappa
        for (Enemy enemy : enemies) {
            checkEnemyTileCollisions(enemy, tileMap);
        }
    }

	
	/*public void checkPlayerTileCollisions(Player mario, TileMap tileMap) {
	    // Calcola la posizione futura di Mario basandoti sulla velocità attuale
	    int nextX = mario.getX() + mario.getVel_x();
	    int nextY = mario.getY() + mario.getVel_y();
	    
	    // Rettangolo di collisione per la prossima posizione
	    Rectangle nextBounds = new Rectangle(nextX, nextY, mario.getWidth(), mario.getHeight());

	    // --- Gestione della collisione orizzontale ---
	    int leftTile = nextBounds.x / TileMap.TILE_SIZE;
	    int rightTile = (nextBounds.x + nextBounds.width - 1) / TileMap.TILE_SIZE;
	    int topTile = nextBounds.y / TileMap.TILE_SIZE;
	    int bottomTile = (nextBounds.y + nextBounds.height - 1) / TileMap.TILE_SIZE;

	    if (mario.getVel_x() < 0) { // Movimento a sinistra
	        for (int row = topTile; row <= bottomTile; row++) {
	            if (tileMap.isTileSolid(leftTile, row)) {
	                // Mario si ferma al bordo sinistro del blocco
	                mario.setX((leftTile + 1) * TileMap.TILE_SIZE);
	                mario.setVel_x(0);
	                break;
	            }
	        }
	    } else if (mario.getVel_x() > 0) { // Movimento a destra
	        for (int row = topTile; row <= bottomTile; row++) {
	            if (tileMap.isTileSolid(rightTile, row)) {
	                // Mario si ferma al bordo destro del blocco
	                mario.setX(rightTile * TileMap.TILE_SIZE - mario.getWidth());
	                mario.setVel_x(0);
	                break;
	            }
	        }
	    }

	    // --- Gestione della collisione verticale ---
	    nextBounds.setLocation(mario.getX(), nextY); // Ricalcola i limiti con la posizione X corretta

	    topTile = nextBounds.y / TileMap.TILE_SIZE;
	    bottomTile = (nextBounds.y + nextBounds.height - 1) / TileMap.TILE_SIZE;
	    leftTile = nextBounds.x / TileMap.TILE_SIZE;
	    rightTile = (nextBounds.x + nextBounds.width - 1) / TileMap.TILE_SIZE;

	    if (mario.getVel_y() > 0) { // Caduta
	        for (int col = leftTile; col <= rightTile; col++) {
	            if (tileMap.isTileSolid(col, bottomTile)) {
	                mario.setVel_y(0);
	                mario.setOnGround(true);
	                mario.setJumping(false);
	                mario.setY(bottomTile * TileMap.TILE_SIZE - mario.getHeight());
	                break;
	            }
	        }
	    } else if (mario.getVel_y() < 0) { // Salto
	        for (int col = leftTile; col <= rightTile; col++) {
	            if (tileMap.isTileSolid(col, topTile)) {
	                mario.setVel_y(0);
	                mario.setJumping(false); 
	                mario.setY((topTile + 1) * TileMap.TILE_SIZE);
	                break;
	            }
	        }
	    }
	}*/
	
	public void checkPlayerTileCollisions(Player mario, TileMap tileMap) {
	    // Calcola la posizione futura di Mario basandoti sulla velocità attuale
	    int nextX = mario.getX() + mario.getVel_x();
	    int nextY = mario.getY() + mario.getVel_y();
	    
	    // Inizializza le posizioni finali
	    int finalX = nextX;
	    int finalY = nextY;

	    // --- Gestione della collisione orizzontale ---
	    Rectangle nextXBounds = new Rectangle(nextX, mario.getY(), mario.getWidth(), mario.getHeight());
	    
	    int leftTile = nextXBounds.x / TileMap.TILE_SIZE;
	    int rightTile = (nextXBounds.x + nextXBounds.width - 1) / TileMap.TILE_SIZE;
	    int topTile = nextXBounds.y / TileMap.TILE_SIZE;
	    int bottomTile = (nextXBounds.y + nextXBounds.height - 1) / TileMap.TILE_SIZE;
	    
	    if (mario.getVel_x() < 0) { // Movimento a sinistra
	        for (int row = topTile; row <= bottomTile; row++) {
	            if (tileMap.isTileSolid(leftTile, row)) {
	                mario.setVel_x(0);
	                finalX = (leftTile + 1) * TileMap.TILE_SIZE;
	                break;
	            }
	        }
	    } else if (mario.getVel_x() > 0) { // Movimento a destra
	        for (int row = topTile; row <= bottomTile; row++) {
	            if (tileMap.isTileSolid(rightTile, row)) {
	                mario.setVel_x(0);
	                finalX = rightTile * TileMap.TILE_SIZE - mario.getWidth();
	                break;
	            }
	        }
	    }

	    // --- Gestione della collisione verticale ---
	    Rectangle nextYBounds = new Rectangle(mario.getX(), nextY, mario.getWidth(), mario.getHeight());

	    topTile = nextYBounds.y / TileMap.TILE_SIZE;
	    bottomTile = (nextYBounds.y + nextYBounds.height - 1) / TileMap.TILE_SIZE;
	    leftTile = nextYBounds.x / TileMap.TILE_SIZE;
	    rightTile = (nextYBounds.x + nextYBounds.width - 1) / TileMap.TILE_SIZE;

	    if (mario.getVel_y() > 0) { // Caduta
	        for (int col = leftTile; col <= rightTile; col++) {
	            if (tileMap.isTileSolid(col, bottomTile)) {
	                mario.setVel_y(0);
	                mario.setOnGround(true);
	                mario.setJumping(false);
	                finalY = bottomTile * TileMap.TILE_SIZE - mario.getHeight();
	                break;
	            }
	        }
	    } else if (mario.getVel_y() < 0) { // Salto
	        for (int col = leftTile; col <= rightTile; col++) {
	            if (tileMap.isTileSolid(col, topTile)) {
	                mario.setVel_y(0);
	                mario.setJumping(false); 
	                finalY = (topTile + 1) * TileMap.TILE_SIZE;
	                break;
	            }
	        }
	    }

	    // Aggiorna la posizione finale di Mario dopo tutti i controlli
	    mario.setX(finalX);
	    mario.setY(finalY);
	}
	
	//MIMO FUNZIONA
    /**
     * Gestisce le collisioni tra il giocatore e le piastrelle solide della mappa.
     */
    /*public void checkPlayerTileCollisions(Player mario, TileMap tileMap) {
    	this.checkMapCollision(mario.getBounds(), tileMap);
        // Salva le posizioni future per la collisione
        int nextX = (int) (mario.getX() + mario.getVel_x());
        int nextY = (int) (mario.getY() + mario.getVel_y());

        // Collisioni orizzontali
        Rectangle futureBoundsX = new Rectangle(nextX, mario.getY(), mario.getWidth(), mario.getHeight());
        if (checkMapCollision(futureBoundsX, tileMap)) {
            mario.setVel_x(0);
        }

        // Collisioni verticali
        Rectangle futureBoundsY = new Rectangle(mario.getX(), nextY, mario.getWidth(), mario.getHeight());
        if (checkMapCollision(futureBoundsY, tileMap)) {
        	if (mario.getVel_y() > 0) {
        		int collisionRow = (nextY + mario.getHeight()) / TileMap.TILE_SIZE;
        		mario.setY(collisionRow * TileMap.TILE_SIZE - mario.getHeight());
                mario.setOnGround(true);
                mario.setJumping(false);
        	} else if (mario.getVel_y()<0) {
        		int collisionRow = nextY / TileMap.TILE_SIZE;
        		//mario.setOnGround(false);
        		mario.setY((collisionRow +1) * TileMap.TILE_SIZE);
        		//mario.setVel_y(0);
        	}
        	mario.setVel_y(0);
        }
   }*/
    
	public void checkEnemyTileCollisions(Enemy enemy, TileMap tileMap) {
	    // Gestione della collisione verticale
	    Rectangle enemyBoundsY = enemy.getBounds();
	    
	    // Calcola le coordinate delle piastrelle (tile) con cui l'oggetto potrebbe collidere
	    int topTile = enemyBoundsY.y / TileMap.TILE_SIZE;
	    int bottomTile = (enemyBoundsY.y + enemyBoundsY.height) / TileMap.TILE_SIZE;
	    int leftTile = enemyBoundsY.x / TileMap.TILE_SIZE;
	    int rightTile = (enemyBoundsY.x + enemyBoundsY.width - 1) / TileMap.TILE_SIZE;

	    boolean collisionY = false;
	    for (int col = leftTile; col <= rightTile; col++) {
	        if (enemy.getVel_y() > 0) { // Caduta
	            if (tileMap.isTileSolid(col, bottomTile)) {
	                collisionY = true;
	                enemy.setVel_y(0);
	                enemy.setOnGround(true);
	                enemy.setY(bottomTile * TileMap.TILE_SIZE - enemy.getHeight());
	                break;
	            }
	        } else if (enemy.getVel_y() < 0) { // Salto (improbabile per i nemici, ma buona pratica)
	            if (tileMap.isTileSolid(col, topTile)) {
	                collisionY = true;
	                enemy.setVel_y(0);
	                enemy.setY((topTile + 1) * TileMap.TILE_SIZE);
	                break;
	            }
	        }
	    }
	    
	    // Aggiorna lo stato "onGround" anche se non c'è una collisione
	    if (!collisionY) {
	        enemy.setOnGround(false);
	    }
	    
	    // Gestione della collisione orizzontale
	    Rectangle enemyBoundsX = enemy.getBounds();

	    topTile = enemyBoundsX.y / TileMap.TILE_SIZE;
	    bottomTile = (enemyBoundsX.y + enemyBoundsX.height - 1) / TileMap.TILE_SIZE;
	    leftTile = enemyBoundsX.x / TileMap.TILE_SIZE;
	    rightTile = (enemyBoundsX.x + enemyBoundsX.width) / TileMap.TILE_SIZE;

	    for (int row = topTile; row <= bottomTile; row++) {
	        if (enemy.getVel_x() < 0) { // Movimento a sinistra
	            if (tileMap.isTileSolid(leftTile, row)) {
	                enemy.setMovingLeft(!enemy.isMovingLeft());
	                enemy.setMovingRight(!enemy.isMovingRight());
	                enemy.setX((leftTile + 1) * TileMap.TILE_SIZE);
	                break;
	            }
	        } else if (enemy.getVel_x() > 0) { // Movimento a destra
	            if (tileMap.isTileSolid(rightTile, row)) {
	                enemy.setMovingLeft(!enemy.isMovingLeft());
	                enemy.setMovingRight(!enemy.isMovingRight());
	                enemy.setX(rightTile * TileMap.TILE_SIZE - enemy.getWidth());
	                break;
	            }
	        }
	    }

	    // Aggiungiamo un controllo extra per far girare i nemici
	    // quando raggiungono il bordo di una piattaforma
	    int footX = enemy.isMovingRight() ? enemy.getX() + enemy.getWidth() + 1 : enemy.getX() - 1;
	    int footY = enemy.getY() + enemy.getHeight() + 1; // Un pixel sotto i piedi
	    
	    int footTileX = footX / TileMap.TILE_SIZE;
	    int footTileY = footY / TileMap.TILE_SIZE;

	    if (!tileMap.isTileSolid(footTileX, footTileY) && enemy.isOnGround()) {
	        enemy.setMovingLeft(!enemy.isMovingLeft());
	        enemy.setMovingRight(!enemy.isMovingRight());
	    }
	}
    
    //MIO FUNZIONA
    /*public void checkEnemyTileCollisions(Enemy enemy, TileMap tileMap) {
    	this.checkMapCollision(enemy.getBounds(), tileMap);
        // Salva le posizioni future per la collisione
        int nextX = (int) (enemy.getX() + enemy.getVel_x());
        int nextY = (int) (enemy.getY() + enemy.getVel_y());

        // Collisioni orizzontali
        Rectangle futureBoundsX = new Rectangle(nextX, enemy.getY(), enemy.getWidth(), enemy.getHeight());
        if (checkMapCollision(futureBoundsX, tileMap)) {
        	enemy.setVel_x(0);
        }

        // Collisioni verticali
        Rectangle futureBoundsY = new Rectangle(enemy.getX(), nextY, enemy.getWidth(), enemy.getHeight());
        if (checkMapCollision(futureBoundsY, tileMap)) {
        	if (enemy.getVel_y() > 0) {
        		int collisionRow = (nextY + enemy.getHeight()) / TileMap.TILE_SIZE;
        		enemy.setY(collisionRow * TileMap.TILE_SIZE - enemy.getHeight());
        		enemy.setOnGround(true);
        	} else if (enemy.getVel_y()<0) {
        		int collisionRow = nextY / TileMap.TILE_SIZE;
        		//mario.setOnGround(false);
        		enemy.setY((collisionRow +1) * TileMap.TILE_SIZE);
        		//mario.setVel_y(0);
        	}
        	enemy.setVel_y(0);
        }
        
   }*/

    
    /**
     * Controlla se un rettangolo si scontra con una piastrella solida.
     */
    public boolean checkMapCollision(Rectangle bounds, TileMap tileMap) {
        int startCol = bounds.x / TileMap.TILE_SIZE;
        int endCol = (bounds.x + bounds.width) / TileMap.TILE_SIZE;
        int startRow = bounds.y / TileMap.TILE_SIZE;
        int endRow = (bounds.y + bounds.height) / TileMap.TILE_SIZE;

        // Limita i cicli entro i confini della mappa
        startCol = Math.max(0, startCol);
        endCol = Math.min(tileMap.getCols() - 1, endCol);
        startRow = Math.max(0, startRow);
        endRow = Math.min(tileMap.getRows() - 1, endRow);

        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {
                if (tileMap.isTileSolid(c, r)) {
                    Rectangle tileBounds = new Rectangle(c * TileMap.TILE_SIZE, r * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
                    if (bounds.intersects(tileBounds)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gestisce le collisioni tra il giocatore e i nemici.
     */
    public void checkPlayerEnemyCollisions(Player mario, List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && mario.getBounds().intersects(enemy.getBounds())) {
            	if (mario.getVel_y() > 0 && mario.getBounds().y + mario.getHeight() < enemy.getBounds().y + enemy.getHeight()) {
            		if (enemy instanceof Koopa ) {
	            		Koopa koopa = (Koopa) enemy;
	            		koopa.die();
	            		//koopa.standBy();
            		} else {
            			enemy.die();
            			mario.setVel_y(-10);
            		}
            	} else {
            		mario.takeDamage(1);
            	}
            }
        }
   }
            	
            	
            
    
    /**
     * Gestisce le collisioni tra Mario e i blocchi speciali (rompibili, domanda, ecc).
     */
    
    public void checkPlayerBlockCollisions(Player mario, List<Block> blocks, List<Coin> coins, List<PowerUp> powerUps) {
    	for (Block block : blocks) {
			
			Rectangle headBox = mario.getHeadBox();
			Rectangle triggerBox = block.getTriggerBox();
			//Rectangle blockBounds = block.getBounds();
		    
			//collisione da sotto
			if (headBox.intersects(triggerBox)) { 
				boolean hittingFromBelow = mario.getVel_y() < 0;
				if (hittingFromBelow) {
					GameObject spawned = block.hit();
					//System.out.println("Blocco colpito" + intersection);
					if (spawned != null) {
						if (spawned instanceof PowerUp) {
						powerUps.add((PowerUp) spawned);
						} else if (spawned instanceof Coin) {
						coins.add((Coin)spawned);
						}
					}

					mario.setVel_y(0);
					mario.setY(block.getY() + block.getHeight());
					//
					continue;
				}
			}
			
			//collisione da sopra (gravità - piattaforma)
			Rectangle feetBox = new Rectangle(mario.getX(), mario.getY() + mario.getHeight() - 2, mario.getWidth(), 10);
		    Rectangle blockTop = new Rectangle(block.getX(), block.getY(), block.getWidth(), 10);

			
	        //if (feetBox.intersects(blockBounds) && mario.getVel_y() >= 0) {
	        if (feetBox.intersects(blockTop) && mario.getVel_y() >= 0) {
	            mario.setVel_y(0);
	            mario.setY(block.getY() - mario.getHeight());
	            mario.setOnGround(true);
	            mario.setJumping(false);
	        }
			
		}
    }
    	
	public void checkPlayerCoinCollisions(Player mario, List<Coin> coins){
		for (Coin coin : coins) {
			if (!coin.isCollected() && mario.getBounds().intersects(coin.getBounds())) {
				coin.collect();
				mario.setScore(mario.getScore()+coin.getValue());
			}
		}
	
	}
	
	public void checkPlayerPowerUpCollisions(Player mario, List<PowerUp> powerUps){
		for (PowerUp powerUp : powerUps) {
			if (!powerUp.isCollected() && mario.getBounds().intersects(powerUp.getBounds())) {
				powerUp.applyEffect(mario);
				powerUp.setCollected(true);
				
			}
		}
	
	}
}

	
	
