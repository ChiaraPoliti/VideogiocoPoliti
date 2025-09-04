package main;

import core.GameFrame;
/** 
 * Entry - Point del codice.
 * @param args
 */

public class Game {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GameFrame gf = new GameFrame();
            }
        });
    }     
}
