package core;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionEvent;   
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import beans.Block;
//import beans.BreakableBlock;
import beans.Coin;
import beans.Enemy;
//import beans.GameObject;
//import beans.Goomba;
//import beans.Koopa;
//import beans.Mushroom;
import beans.Player;
import beans.PowerUp;
//import beans.QuestionBlock;
import logic.CollisionManager;
import logic.Level1;
import enums.GameState;

public class GamePanel extends JPanel implements Runnable{
	private Thread gameThread;
	private boolean running = false;
	private Player mario;
	private List<beans.Enemy> enemies;
	private List<beans.Coin> coins;
	private List<beans.PowerUp> powerUps; 
	private List<beans.Block> blocks;
	private final Object gameLoopLock = new Object();
	public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 200;
    private TileMap tileMap;
    private int cameraX;
    private int cameraY;
    private CollisionManager collisionManager;
    private GameState gameState;
    
	
	public GamePanel() {
		this.mario = new Player (100,112); //UNICO
	    this.powerUps = new ArrayList<>();
	    
	    
	    Level1 level1 = new Level1();
        this.enemies = level1.getEnemies();
        this.blocks = level1.getBlocks();
        this.coins = level1.getCoins();
		
		List<String> mapLayersPaths = new ArrayList<>();
		mapLayersPaths.add("/maps/marioTileset_background.csv"); 
        mapLayersPaths.add("/maps/marioTileset_sfondo.csv"); 
        mapLayersPaths.add("/maps/marioTileset_terreno.csv");
        
        String tilesImagesPath = "/tiles/";
        this.tileMap = new TileMap(mapLayersPaths, tilesImagesPath);
        
		
        
        cameraX = mario.getX() - GamePanel.WINDOW_WIDTH/2;
        cameraY = mario.getY() - GamePanel.WINDOW_HEIGHT/2;
        clampCamera();
        
        this.collisionManager = new CollisionManager();
        this.gameState = GameState.PLAYING;

	}
	
	public void startGame() {
        /*synchronized (gameLoopLock) { // Sincronizza la scrittura di 'running'
            running = true;
        }*/
		if (!running) {
			gameThread = new Thread(this);
			running = true;
			gameThread.start();
		}
        
        
        //avvio musica
        //this.frame.startBackgroundMusic();
    }

    // Metodo per fermare il gioco (utile per Game Over o Pausa)
    public void stopGame() {
        synchronized (gameLoopLock) { // Sincronizza la scrittura di 'running'
            running = false;
        }
        
        //stop musica
        //this.frame.stopBackgroundMusic();
    }

    @Override
    public void run() {
        // Il loop continuerà finché 'running' è true.
        // La lettura di 'running' deve essere sincronizzata.
        while (true) {
            boolean currentRunningState;
            synchronized (gameLoopLock) { // Sincronizza la lettura di 'running'
                currentRunningState = running;
            }

            if (!currentRunningState) {
                break; // Esci dal loop se 'running' è diventato false
            }

            update();
            repaint();

            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Game loop interrupted.");
                stopGame(); // Assicurati di fermare il gioco in caso di interruzione
                break;
            }
        }
    }

    
    
    private void update() {
    	if (gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
    		return;
    	}
    	
        int mapWidthPixels = tileMap.getCols() * TileMap.TILE_SIZE;
        int mapHeightPixels = tileMap.getRows() * TileMap.TILE_SIZE;

        //Update Player
        mario.update(mapWidthPixels, mapHeightPixels, tileMap);
        
        //verifica tile vittoria
        int marioCol = (mario.getX() + mario.getWidth() / 2) / TileMap.TILE_SIZE;
        int marioRow = (mario.getY() + mario.getHeight() / 2) / TileMap.TILE_SIZE;
        Tile currentTile = tileMap.getTile(1, marioRow, marioCol); // layer 0 o quello giusto

        if (currentTile != null && currentTile.getId() == 22) {
            gameState = GameState.WIN;
        }

        
        // Update Nemici e rimozione morti
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update(mapWidthPixels, mapHeightPixels, this.tileMap);
            if (!enemy.isAlive() && enemy.isRemovable()) {
                enemyIterator.remove();
            }
        }

        // collisioni nemici e mappa
        //collisionManager.checkPlayerTileCollisions(mario, tileMap);
        //collisionManager.checkPlayerEnemyCollisions(mario, enemies);
        
        
        //update blocchi
        for (Block block : blocks) {
            block.update(mapWidthPixels, mapHeightPixels, tileMap);
        }
        
        //collioni mario e blocchi
        //collisionManager.checkPlayerBlockCollisions(mario, blocks, coins, powerUps);
        //repaint();
        
        //update monete
        Iterator<Coin> coinIterator = coins.iterator();
        while(coinIterator.hasNext()) {
        	Coin coin = coinIterator.next();
            coin.update(mapWidthPixels, mapHeightPixels, tileMap);
            // Rimuovi la moneta se raccolta
            if (coin.isCollected()) {
                coinIterator.remove();
            }
        }
        
        //collisionManager.checkPlayerCoinCollisions(mario, coins);
        
        
        
        //update powerup esistenti
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp pu = powerUpIterator.next();
            pu.update(mapWidthPixels, mapHeightPixels, tileMap);

            // Controlla collisione con Mario
            //if (!pu.isCollected() && mario.getBounds().intersects(pu.getBounds())) {
               // pu.applyEffect(mario);
               // pu.setCollected(true);
            //}

            // Rimuovi power-up raccolto
            if (pu.isCollected()) {
                powerUpIterator.remove();
            }
        }
        
        collisionManager.checkAllCollisions(mario, enemies, blocks,coins,powerUps,tileMap);
        
        if (mario.getHp() == 0) {
        	stopGame();
        	gameState = GameState.GAME_OVER;
        	repaint();
        }
        
        // --- LOGICA DI CAMERA ---
        cameraX = mario.getX() - WINDOW_WIDTH / 2;
        cameraY = mario.getY() - WINDOW_HEIGHT / 2;
        clampCamera();
        
        //SwingUtilities.invokeLater(() -> this.repaint());
        repaint();
    }
    
    public void restartGame() {
        
    	mario.resetState();
    	enemies.clear();
        coins.clear();
        powerUps.clear();
        blocks.clear();
    	
        this.gameState = GameState.PLAYING;
        Level1 level1 = new Level1();
        enemies = level1.getEnemies();
        blocks = level1.getBlocks();
        coins = level1.getCoins();
        powerUps.clear();


       
        gameState = GameState.PLAYING;
        
        if (gameThread != null && gameThread.isAlive()) {
        	running = false;
        	try {
        		gameThread.join();
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        }
        	
        startGame(); // riavvia il thread
       
    }


    
	
    private void clampCamera() {
        int mapWidthPixels = tileMap.getCols() * TileMap.TILE_SIZE;
        int mapHeightPixels = tileMap.getRows() * TileMap.TILE_SIZE;
        
        //Centro camera su Mario
        int desiredCameraX = mario.getX() - WINDOW_WIDTH/2; 
        int minCameraX = 0;
        int maxCameraX = mapWidthPixels - WINDOW_WIDTH;
        
        // Limita orizzontalmente
        // Se la mappa è più piccola della finestra, centrala
        if (mapWidthPixels < GameFrame.WINDOW_WIDTH) {
            cameraX = maxCameraX / 2;
        } else { // Altrimenti, blocca ai bordi della mappa
            cameraX = Math.max(minCameraX, Math.min(desiredCameraX, Math.max(0, maxCameraX)));
        }
        
        
        int desiredCameraY = mario.getY() - WINDOW_HEIGHT / 2;
        int minCameraY = 0;
        int maxCameraY = mapHeightPixels - WINDOW_HEIGHT;
        
        // Limita verticalmente
        // Se la mappa è più piccola della finestra, centrala
        if (mapHeightPixels < WINDOW_HEIGHT) {
            //cameraY = maxCameraY / 2;
        	cameraY = 0;
        } else { // Altrimenti, blocca ai bordi della mappa
            cameraY = Math.max(minCameraY, Math.min(desiredCameraY, maxCameraY));
        }
    }
    
    private void drawScore(Graphics2D g2d) {
        int padding = 10; // distanza dai bordi
        int boxWidth = 95;
        int boxHeight = 40;

        // Sfondo del box
        g2d.setColor(new Color(0, 0, 0, 0)); // semi-trasparente
        g2d.fillRoundRect(padding, padding, boxWidth, boxHeight, 10, 10);


        // Testo del punteggio
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        g2d.drawString("Score: " + mario.getScore(), padding + 10, padding + 25);
    }
    
    private void drawLogo(Graphics2D g2d) {
    	Image imageIcon = null;
        java.net.URL logoUrl = getClass().getResource("/images/logo.png");
        if (logoUrl != null) {
            imageIcon = new ImageIcon(logoUrl).getImage();
        }
        
        if (imageIcon != null) {
            g2d.drawImage(imageIcon, getWidth() - 180, 10, 100, 50, null);
        } else {
        	System.out.println("logo non caricato");        }
    }

    
    public void drawWorld (Graphics2D g2d) {
    	// Calcola quali tile sono visibili
        int startCol = cameraX / TileMap.TILE_SIZE;
        int endCol = (cameraX + WINDOW_WIDTH) / TileMap.TILE_SIZE + 1;
        int startRow = cameraY / TileMap.TILE_SIZE;
        int endRow = (cameraY + WINDOW_HEIGHT) / TileMap.TILE_SIZE + 1;

        // Limita i cicli per non andare oltre i bordi effettivi della mappa
        if (startCol < 0) { 
        	startCol = 0;
        }
        if (endCol > tileMap.getCols()) {
        	endCol = tileMap.getCols();
        }
        if (startRow < 0) {
        	startRow = 0;
        }
        if (endRow > tileMap.getRows()) {
        	endRow = tileMap.getRows();
        }
        
        //System.out.println("DEBUG: CameraX finale: " + cameraX + ", MaxCameraX calcolato: " + (tileMap.getCols() * TileMap.TILE_SIZE - WINDOW_WIDTH + (2 * TileMap.TILE_SIZE))); // Assumendo 2 tile extra

        // Itera su ogni livello visibile
        for (int layerIndex = 0; layerIndex < tileMap.getNumLayers(); layerIndex++) {
            for (int row = startRow; row < endRow; row++) {
                for (int col = startCol; col < endCol; col++) {
                    Tile tile = tileMap.getTile(layerIndex, row, col);

                    // Disegna il tile solo se esiste e ha un'immagine
                    if (tile != null && tile.getImage() != null) {
                        BufferedImage img = tile.getImage();

                        // Calcola la posizione di disegno, sottraendo la posizione della camera
                        int drawX = (col * TileMap.TILE_SIZE) - cameraX;
                        int drawY = (row * TileMap.TILE_SIZE) - cameraY;

                        g2d.drawImage(img,
                                      drawX,
                                      drawY,
                                      TileMap.TILE_SIZE,
                                      TileMap.TILE_SIZE,
                                      null);
                    }
                }
            }
        }
        
        //blocchi
        for (Block block : blocks) {
        	block.draw(g2d, cameraX, cameraY);
        }
        
        
	    if(this.mario!= null) {
	    	mario.draw(g2d, cameraX, cameraY);
	    }
	    
	 // Disegna i nemici
	    for (Enemy enemy : enemies) {
	        enemy.draw(g2d, cameraX, cameraY);
	    }

	    // Disegna le monete
	    for (Coin coin : coins) {
	        coin.draw(g2d, cameraX, cameraY);
	    }

	    // Disegna i power-up
	    for (PowerUp powerUp : powerUps) {
	        powerUp.draw(g2d, cameraX, cameraY);
	    }
	    
	    drawScore(g2d);
	    drawLogo(g2d);

   }
		
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if(gameState == GameState.GAME_OVER) {
			
			
			//Overlay nero semitrasparente
			g2d.setColor(new Color (0, 0, 0, 180));
			g2d.fillRect(0, 0, getWidth(), getHeight());
			
			// Scritta Game Over
	        g2d.setColor(Color.RED);
	        g2d.setFont(new Font("Arial", Font.BOLD, 48));
	        g2d.drawString("GAME OVER", getWidth()/2 - 150, getHeight()/2);

	        // Messaggio per restart
	        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
	        g2d.setColor(Color.WHITE);
	        g2d.drawString("Premi Invio per ricominciare", getWidth()/2 - 140, getHeight()/2 + 50);

	        return; // non disegnare altro
		} else if (gameState == GameState.WIN) {
		    drawWorld(g2d);

		    // Overlay verde semi-trasparente
		    g2d.setColor(new Color(0, 150, 0, 150));
		    g2d.fillRect(0, 0, getWidth(), getHeight());

		    // Scritta di vittoria
		    g2d.setColor(Color.YELLOW);
		    g2d.setFont(new Font("Arial", Font.BOLD, 48));
		    g2d.drawString("HAI VINTO!", getWidth()/2 - 120, getHeight()/2);

		    g2d.setFont(new Font("Arial", Font.PLAIN, 24));
		    g2d.setColor(Color.WHITE);
		    g2d.drawString("Premi Invio per ricominciare", getWidth()/2 - 140, getHeight()/2 + 50);

		    return;
		} else {
			drawWorld(g2d);
		}
		
    }

	/**
	 * @return the gameState
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * @param gameState the gameState to set
	 */
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	/**
	 * @return the mario
	 */
	public Player getMario() {
		return mario;
	}

	/**
	 * @param mario the mario to set
	 */
	public void setMario(Player mario) {
		this.mario = mario;
	}
	

}
	
