package main;

import core.GameFrame;

public class Game {
	/** 
	 * Entry - Point del codice.
	 * @param args
	 */

	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GameFrame gf = new GameFrame();
            }
        });
    }

}
