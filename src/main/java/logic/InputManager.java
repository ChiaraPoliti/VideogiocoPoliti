package logic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import beans.Player;
import core.GamePanel;
import enums.GameState;

/**
 * Classe che gestisce gli input dall'utente, sia i comandi di gioco che quello di riavvio del gioco
 * implementa l'interfaccia KeyListener
 */
public class InputManager implements KeyListener {
    private Player mario;
    private GamePanel gamePanel;

    public InputManager(Player mario, GamePanel gamePanel) {
        this.mario = mario;
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Non necessario per questo gioco, ma deve essere implementato
    }

    /**
     * Metodo per la gestione dei tasti premuti
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode(); //definisco una variabile per il codice del tasto premuto
        if (gamePanel.getGameState() == GameState.PLAYING) { //se lo stato del gioco è PLAYING
	        if (keyCode == KeyEvent.VK_RIGHT) { //e se il tasto premuto è la freccia dx
	            mario.setMovingRight(true); //mario si muove a destra
	        } else if (keyCode == KeyEvent.VK_LEFT) { //se il tasto è la freccia a sx
	            mario.setMovingLeft(true); //marioo va a sx
	        } else if (keyCode == KeyEvent.VK_SPACE) { //se si preme la barra spaziatrice
	            mario.jump(); //salta
	        }
	        //altrimenti, se lo stato è WIN o GAME_OVER E si preme l'Enter
        } else if ((gamePanel.getGameState() == GameState.GAME_OVER || gamePanel.getGameState() == GameState.WIN) && keyCode == KeyEvent.VK_ENTER) { 
            gamePanel.restartGame(); //riavvio il gioco
        }
    }

    /**
     * Metodo per la gestione dei tasti rilasciati
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode(); //definisco una variabile sul codice del tasto rilasciato
        if (keyCode == KeyEvent.VK_RIGHT) { //se rilascio il tasto freccia dx
            mario.setMovingRight(false); //mario non cammina più verso dx
        } else if (keyCode == KeyEvent.VK_LEFT) { //se rilascio la freccia sx
            mario.setMovingLeft(false); //mario non cammina più a sinistra
        }
    }
}
