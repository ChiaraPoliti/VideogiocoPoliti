
package logic;

import java.awt.Rectangle;
//import java.util.ArrayList;
import java.util.List;
import beans.Player;
import core.TileMap;
//import enums.blockType;
import beans.Enemy;
import beans.GameObject;
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

    /**
     * Gestisce le collisioni tra il giocatore e le piastrelle solide della mappa.
     */
    public void checkPlayerTileCollisions(Player mario, TileMap tileMap) {
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
   }
    
    public void checkEnemyTileCollisions(Enemy enemy, TileMap tileMap) {
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
        
   }

    
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
                // Collisione tra Mario e un nemico rilevata
                // Se Mario sta scendendo e il suo piede è sopra il nemico
                if (mario.getVel_y() > 0 && mario.getBounds().y + mario.getHeight() < enemy.getBounds().y + enemy.getHeight()) {
                    enemy.die();
                    mario.setVel_y(-10); // Piccolo rimbalzo per feedback
                } else {
                    mario.takeDamage(1); // Mario viene danneggiato
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
					if (spawned != null && spawned instanceof PowerUp) {
						powerUps.add((PowerUp) spawned);
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
				powerUp.setCollected(true);
			}
		}
	
	}
}

	
	
