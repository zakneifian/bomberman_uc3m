package uc3m.bomberman.main;

import edu.uc3m.game.GameBoardGUI;

public class Game{
	//private Player player;
	//private Entity[] onScreen;
	private Map map;
	private GameBoardGUI board;
	private int level;
	private int enemyCount;
	
	public Game(int dim, String playerName){
		board = new GameBoardGUI(dim, dim);
		board.setVisible(true);
		
	}
}
