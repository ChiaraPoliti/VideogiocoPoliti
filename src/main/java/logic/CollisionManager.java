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

    /**
     * Controlla tutte le collisioni tra gli oggetti di gioco.
     */
    /*public void checkAllCollisions(Player mario, List<Enemy> enemies, List<Block> blocks, List<Coin> coins, List<PowerUp> powerUps, TileMap tileMap) {
        // Controlla le collisioni tra Mario e le piastrelle solide
        checkPlayerTileCollisions(mario, tileMap);

        // Controlla le collisioni tra Mario e i nemici
        checkPlayerEnemyCollisions(mario, enemies);
        
        //controllo collisioni tra Mario e blocchi
        checkPlayerBlockCollisions(mario, blocks, coins, powerUps);
        
        
    }*/

    /**
     * Gestisce le collisioni tra il giocatore e le piastrelle solide della mappa.
     */
    public void checkPlayerTileCollisions(Player mario, TileMap tileMap) {
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
    /*public void checkPlayerBlockCollisions(Player mario, List<Block> blocks) {
        for (Block block : blocks) {
            if (block.isHit()) continue; // se già rotto o vuoto, ignora

            if (mario.getBounds().intersects(block.getBounds())) {
                
                // Mario colpisce dal basso
                boolean hittingFromBelow = 
                    mario.getVel_y() < 0 && mario.getY() > block.getY();

                if (hittingFromBelow) {
                	block.hit();
                    /*if (block.getType () == blockType.BREAKABLE) {
                        block.hit();  // lo segni come distrutto
                    } else if (block.getType() == blockType.QUESTION) {
                        block.activate();    // rilascia item / moneta
                    }*/

                    // stoppa salto e sistema posizione
                  //.setVel_y(0);
                  //  mario.setY(block.getY() + block.getHeight());
              //  }

                // se serve puoi gestire anche collisioni sopra/lato
           // }
        //}
  //  } */
    
    public void checkPlayerBlockCollisions(Player mario, List<Block> blocks, List<Coin> coins, List<PowerUp> powerUps) {
    		for (Block block : blocks) {
    			if (block.isHit()) {
    				continue;
    			}
    			if (mario.getBounds().intersects(block.getBounds())) {
    				boolean hittingFromBelow = mario.getVel_y() < 0 && mario.getY() > block.getY();
    				
    				if (hittingFromBelow) {
    					GameObject spawned = block.hit();
    					if (spawned != null) {
    						if (spawned instanceof Coin) {
    							coins.add((Coin) spawned);
    						} else if (spawned instanceof PowerUp){
    							powerUps.add((PowerUp) spawned);
    						}
    					}

    					mario.setVel_y(0);
    					mario.setY(block.getY() + block.getHeight());
    				}
    			}
    		}
    }



}
