package logic;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import beans.Player;
import core.GamePanel;
import enums.GameState;

public class InputManager implements KeyListener {
    private Player mario;
    private GamePanel gamePanel;

    public InputManager(Player mario, GamePanel gamePanel) {
        this.mario = mario;
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Non necessario per questo gioco, ma va implementato
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (gamePanel.getGameState() == GameState.PLAYING) {
	        if (keyCode == KeyEvent.VK_RIGHT) {
	            mario.setMovingRight(true);
	        } else if (keyCode == KeyEvent.VK_LEFT) {
	            mario.setMovingLeft(true);
	        } else if (keyCode == KeyEvent.VK_SPACE) {
	            mario.jump();
	        }
        } else if ((gamePanel.getGameState() == GameState.GAME_OVER || gamePanel.getGameState() == GameState.WIN) && keyCode == KeyEvent.VK_ENTER) {
            gamePanel.restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_RIGHT) {
            mario.setMovingRight(false);
        } else if (keyCode == KeyEvent.VK_LEFT) {
            mario.setMovingLeft(false);
        }
    }
}
