
package logic;

import java.awt.Rectangle;
import java.util.List;
import beans.Player;
import core.TileMap;
import beans.Enemy;
import beans.GameObject;
import beans.Koopa;
import beans.PowerUp;
import beans.Block;
import beans.Coin;

public class CollisionManager {

	/**
	 * Metodo che attiva i check di tutte le collisioni in un colpo solo
	 * @param mario, giocatore
	 * @param enemies, tutti i nemici del livello
	 * @param blocks, tutti i blocchi
	 * @param coins, tutte le monete
	 * @param powerUps, tutti i potenziamenti
	 * @param tileMap, la mappa
	 */
	public void checkAllCollisions(Player mario, List<Enemy> enemies, List<Block> blocks, List<Coin> coins, List<PowerUp> powerUps, TileMap tileMap) {
        // 1. Controlla le collisioni del giocatore con la mappa
        checkPlayerTileCollisions(mario, tileMap);
        
        // 2. Aggiungi i controlli per le collisioni dei nemici con la mappa
        for (Enemy enemy : enemies) {
            checkEnemyTileCollisions(enemy, tileMap);
        }

        // 3. Controlla le collisioni del giocatore con i nemici
        checkPlayerEnemyCollisions(mario, enemies);

        // 4. Controlla le collisioni del giocatore con i blocchi
        checkPlayerBlockCollisions(mario, blocks, coins, powerUps);

        // 5. Controlla le collisioni del giocatore con monete
        checkPlayerCoinCollisions(mario, coins);
        
        // 6. Controlla le collisioni del giocatore con i powerUp
        checkPlayerPowerUpCollisions(mario, powerUps);
    }

	
	
	/**
	 * Collisioni Mario-Mappa
	 * @param mario
	 * @param tileMap
	 */
	public void checkPlayerTileCollisions(Player mario, TileMap tileMap) {
		//PROMEMORIA: 
		//RETTANGOLO SI CALCOLA DALL'ANGOLO IN ALTO A SINISTRA SEMPRE
		
	    //Calcolo le posizioni successive con MRU
	    int nextX = mario.getX() + mario.getVel_x();
	    int nextY = mario.getY() + mario.getVel_y();
	    
	    //Inizializzo due variabili inizialmente poste uguali alle posizioni successive
	    int finalX = nextX;
	    int finalY = nextY;

	    //COLLISIONI ORIZZONTALI CON LA MAPPA
	    //Trovo l'area che appena segue Mario (orizzontalmente)
	    Rectangle nextXBounds = new Rectangle(nextX, mario.getY(), mario.getWidth(), mario.getHeight());
	    
	    //.X e .y in un rettangolo sono le coordinate x/y del punto in alto a sinistra
	    int leftTile = nextXBounds.x / TileMap.TILE_SIZE; //estraggo la tile appena a sinistra
	    int rightTile = (nextXBounds.x + nextXBounds.width - 1) / TileMap.TILE_SIZE; //estraggo la tile di destra
	    int topTile = nextXBounds.y / TileMap.TILE_SIZE; //estraggo la tile sopra
	    int bottomTile = (nextXBounds.y + nextXBounds.height - 1) / TileMap.TILE_SIZE; //estraggo la tile sotto
	    
	    if (mario.getVel_x() < 0) { //se mario va a sx
	        for (int row = topTile; row <= bottomTile; row++) { // per ogni riga che va da quella in alto a quella in basso del rettangolo nuovo di mario
	            if (tileMap.isTileSolid(leftTile, row)) { //se la tile è solida
	                mario.setVel_x(0); //mario si ferma
	              //la posizione finale di mario è esattamente quella dell'ultima tile +1 (ritorno alla tile precedente) (*size per convertirla in pixel)
	                finalX = (leftTile + 1) * TileMap.TILE_SIZE; 
	                break;
	            }
	        }
	    } else if (mario.getVel_x() > 0) { // se mario si muove a dx
	    	for (int row = topTile; row <= bottomTile; row++) { // per ogni riga che va da quella in alto a quella in basso del rettangolo nuovo di mario
	            if (tileMap.isTileSolid(rightTile, row)) { //se la tile è solida
	                mario.setVel_x(0); //mario si ferma
	                //la posizione finale di mario è esattamente quella dell'ultima tile (*size per convertirla in pixel) meno la larghezza di mario (ci deve stare)
	                finalX = rightTile * TileMap.TILE_SIZE - mario.getWidth();  
	                break;
	            }
	        }
	    }

	    //COLLISIONI VERTICALI CON LA MAPPA
	    //Trovo l'area che appena segue Mario (verticalmente)
	    Rectangle nextYBounds = new Rectangle(mario.getX(), nextY, mario.getWidth(), mario.getHeight());
	    
	    //riassegno le variabili delle ultime tile a quelle interessanti ora (quindi le ultime in un moto verticale sopra/sotto)
	    topTile = nextYBounds.y / TileMap.TILE_SIZE; //tile sopra del rettangolo nuovo
	    bottomTile = (nextYBounds.y + nextYBounds.height - 1) / TileMap.TILE_SIZE; //tile sotto (-1 per far avvenire l'intersezione ed è legato all'asse delle ordinate positivo verso il basso)
	    leftTile = nextYBounds.x / TileMap.TILE_SIZE; //tile di sinistra 
	    rightTile = (nextYBounds.x + nextYBounds.width - 1) / TileMap.TILE_SIZE; //tile di destra

	    if (mario.getVel_y() > 0) { //se mario cade 
	        for (int col = leftTile; col <= rightTile; col++) { //per ogni colonna che va da sinistra a destra
	            if (tileMap.isTileSolid(col, bottomTile)) { //se la tile è solida
	                mario.setVel_y(0); //mario si ferma
	                mario.setOnGround(true); //tocca terra
	                mario.setJumping(false); // non sta saltando
	                finalY = bottomTile * TileMap.TILE_SIZE - mario.getHeight(); //nuova posizione finale è tile sotto (*size per pixel) - l'altezza
	                break;
	            }
	        }
	    } else if (mario.getVel_y() < 0) { // se mario salta
	        for (int col = leftTile; col <= rightTile; col++) { //per ogni colonna da sx a dx
	            if (tileMap.isTileSolid(col, topTile)) { // se la tile è solida
	                mario.setVel_y(0); //mario si ferma
	                mario.setJumping(false); //smette di saltare (così torna giù)
	                finalY = (topTile + 1) * TileMap.TILE_SIZE; //posizione finale è tile alta + 1 (*size per pixel)
	                break;
	            }
	        }
	    }

	    //Fisso le nuove posizioni di mario, dopo aver controllato le collisioni
	    mario.setX(finalX);
	    mario.setY(finalY);
	}
	
	/**
	 * Collisioni Nemici-Mappa
	 * @param enemy
	 * @param tileMap
	 */
	public void checkEnemyTileCollisions(Enemy enemy, TileMap tileMap) {
		//COLLISIONI VERTICALI CON LA MAPPA
	    //recupero il rettangolo dello spazio del nemico (la sua posizione è aggiornata in update())
	    Rectangle enemyBoundsY = enemy.getBounds();
	    
	    //recupero delle tile degli angoli
	    int topTile = enemyBoundsY.y / TileMap.TILE_SIZE; //sopra 
	    int bottomTile = (enemyBoundsY.y + enemyBoundsY.height) / TileMap.TILE_SIZE; //sotto
	    int leftTile = enemyBoundsY.x / TileMap.TILE_SIZE; //sinistra
	    int rightTile = (enemyBoundsY.x + enemyBoundsY.width - 1) / TileMap.TILE_SIZE; //destra

	    boolean collisionY = false; //variabile per collisioni su Y
	    for (int col = leftTile; col <= rightTile; col++) { //per ogni colonan da sx a dx
	        if (enemy.getVel_y() > 0) { // se cade
	            if (tileMap.isTileSolid(col, bottomTile)) { //se la tile è solida
	                collisionY = true; //fisso a true
	                enemy.setVel_y(0); //fermo il nemico
	                enemy.setOnGround(true); //sta a terra
	                enemy.setY(bottomTile * TileMap.TILE_SIZE - enemy.getHeight()); //fisso nuova posizione (tile sotto - altezza)
	                break;
	            }
	        } else if (enemy.getVel_y() < 0) { // se salta (per eventuali nuovi nemici)
	            if (tileMap.isTileSolid(col, topTile)) { //se la tile è solida)
	                collisionY = true; //true
	                enemy.setVel_y(0); // si ferma
	                enemy.setY((topTile + 1) * TileMap.TILE_SIZE); // nuova posizione (quella alta +1)
	                break;
	            }
	        }
	    }
	    
	    // Aggiorno collisionY in ogni caso
	    if (!collisionY) {
	        enemy.setOnGround(false);
	    }
		
		//COLLISIONI ORIZZONTALI CON LA MAPPA
		//recupero il rettangolo dello spazio del nemico (la sua posizione è aggiornata in update())
	    Rectangle enemyBoundsX = enemy.getBounds();

	    //recupero delle tile degli angoli
	    topTile = enemyBoundsX.y / TileMap.TILE_SIZE; //sopra
	    bottomTile = (enemyBoundsX.y + enemyBoundsX.height - 1) / TileMap.TILE_SIZE; //sotto (devo sommare l'altezza del nemico, -1 per far avvenire l'intersezione)
	    leftTile = enemyBoundsX.x / TileMap.TILE_SIZE; //sinistra
	    rightTile = (enemyBoundsX.x + enemyBoundsX.width) / TileMap.TILE_SIZE; //destra (x + larghezza nemico)

	    for (int row = topTile; row <= bottomTile; row++) { //per ogni riga da sopra a sotto
	        if (enemy.getVel_x() < 0) { //se il nemico si muove a sx
	            if (tileMap.isTileSolid(leftTile, row)) { //se la tile è solida
	                enemy.setMovingLeft(!enemy.isMovingLeft()); //il nemico inverte il suo moto
	                enemy.setMovingRight(!enemy.isMovingRight());
	                enemy.setX((leftTile + 1) * TileMap.TILE_SIZE); //fissa la nuova x a quella di sinistra +1 (verso destra) (torna indietro)
	                break;
	            }
	        } else if (enemy.getVel_x() > 0) { //se si muove a dx
	            if (tileMap.isTileSolid(rightTile, row)) { //se la tile è solida
	                enemy.setMovingLeft(!enemy.isMovingLeft()); //inverte moto
	                enemy.setMovingRight(!enemy.isMovingRight());
	                enemy.setX(rightTile * TileMap.TILE_SIZE - enemy.getWidth()); //fissa la nuova x a tile di destra - larghezza) e non alla successiva
	                break;
	            }
	        }
	    }

	    // se i nemici trovano ostacolo invertono il loro moto
	    //variabili dei piedi
	    //piede sinistro: se si muove a destra, è uguale a x + larghezza +1, altrimenti a x -1
	    int footX = enemy.isMovingRight() ? enemy.getX() + enemy.getWidth() + 1 : enemy.getX() - 1;
	    //piede destro: y + altezza +1 sotto i piedi
	    int footY = enemy.getY() + enemy.getHeight() + 1;
	    
	    //recupero le rispettive tile
	    int footTileX = footX / TileMap.TILE_SIZE;
	    int footTileY = footY / TileMap.TILE_SIZE;

	    if (!tileMap.isTileSolid(footTileX, footTileY) && enemy.isOnGround()) { //se è solida e il nemico è a terra
	        enemy.setMovingLeft(!enemy.isMovingLeft()); //inverto moto
	        enemy.setMovingRight(!enemy.isMovingRight());
	    }
	}
    
	/**
     * Collisioni Mario/Nemici
     * @param mario
     * @param enemies
     */
    public void checkPlayerEnemyCollisions(Player mario, List<Enemy> enemies) {
        for (Enemy enemy : enemies) { // per ogni nemico
            if (enemy.isAlive() && mario.getBounds().intersects(enemy.getBounds())) { // se il nemico è vivo e c'è intersezione con mario
            	if (mario.getVel_y() > 0 && mario.getBounds().y + mario.getHeight() < enemy.getBounds().y + enemy.getHeight()) { //se mario sta cadendo e la sua base è minore della base del nemico
            		if (enemy instanceof Koopa ) { //se è koopa
	            		Koopa koopa = (Koopa) enemy; //cast
	            		koopa.toShell(); // koopa in guscio
	            		mario.setVel_y(-10); // rimbalzo
            		} else { //in altri casi
            			enemy.die(); // nemico muore
            			mario.setVel_y(-10); // rimbalzo
            		}
            	} else { //se mario non ci cade sopra 
            		enemy.dealDamage(mario); // viene danneggiato
            	}
            }
        }
   }
	   
    
    /**
     * Collisioni Mario/Blocchi
     * @param mario
     * @param blocks
     * @param coins
     * @param powerUps
     */
    
    public void checkPlayerBlockCollisions(Player mario, List<Block> blocks, List<Coin> coins, List<PowerUp> powerUps) {
    	for (Block block : blocks) { //per ogni blocco
			//recupero i rettangoli di testa di mario e di zona di colpo del blocco
			Rectangle headBox = mario.getHeadBox();
			Rectangle triggerBox = block.getTriggerBox();
		    
			//collisione da sotto (unico modo per attivare i blocchi)
			if (headBox.intersects(triggerBox)) { //se la testa interseca il blocco
				//boolean hittingFromBelow = mario.getVel_y() < 0; //
				if (mario.getVel_y() < 0) { //se mario salta
					GameObject spawned = block.hit(); // creo oggetto dal metodo del blocco 
					//System.out.println("Blocco colpito" + intersection);
					if (spawned != null) { //se non è nullo, capisco il tipo di oggetto e lo aggiungo alla lista giusta
						if (spawned instanceof PowerUp) {  
						powerUps.add((PowerUp) spawned); 
						} else if (spawned instanceof Coin) {
						coins.add((Coin)spawned);
						}
					}

					mario.setVel_y(0); // in ogni caso fermo la salita di mario
					mario.setY(block.getY() + block.getHeight()); // nuova posizione a quella della fine del blocco 
					continue;
				}
			}
			
			//collisione da sopra (gravità - piattaforma)
			//recupero i rettangoli piedi mario e parte alta del blocco
			Rectangle feetBox = new Rectangle(mario.getX(), mario.getY() + mario.getHeight() - 2, mario.getWidth(), 10);
		    Rectangle blockTop = new Rectangle(block.getX(), block.getY(), block.getWidth(), 10);

			
	        
	        if (feetBox.intersects(blockTop) && mario.getVel_y() >= 0) { //se si intersecano e mario sta cadendo
	            mario.setVel_y(0); //fermo
	            mario.setY(block.getY() - mario.getHeight()); // nuova posizione di mario sopra il blocco
	            mario.setOnGround(true); // a terra
	            mario.setJumping(false); // non sta saltando
	        }
			
		}
    }
    	
    /**
     * Collisioni Mario/Monete
     * @param mario
     * @param coins
     */
	public void checkPlayerCoinCollisions(Player mario, List<Coin> coins){
		for (Coin coin : coins) { // per ogni moneta 
			if (!coin.isCollected() && mario.getBounds().intersects(coin.getBounds())) { //se non raccolta e che iterseca mario
				coin.collect(); // raccolgo
				mario.getCoin(coin); //aggiorno punteggio
			}
		}
	
	}
	
	/**
	 * Collisioni Mario/PowerUp
	 * @param mario
	 * @param powerUps
	 */
	public void checkPlayerPowerUpCollisions(Player mario, List<PowerUp> powerUps){
		for (PowerUp powerUp : powerUps) { //per ogni powerup
			if (!powerUp.isCollected() && mario.getBounds().intersects(powerUp.getBounds())) { //se non raccolto e che interseca mario
				powerUp.applyEffect(mario); // apllico il suo effetto
				powerUp.setCollected(true); // fisso a raccolto
				
			}
		}
	}
}

	
	
