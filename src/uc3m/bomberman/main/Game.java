package uc3m.bomberman.main;

import edu.uc3m.game.GameBoardGUI;
import uc3m.bomberman.entity.*;
import uc3m.bomberman.map.*;

public class Game{
	//private Player player;
	//private Entity[] onScreen;
	private Map map;
	private Entity[] entities;
	private Player player;
	private int level;
	private int enemyCount;
	
	public Game(int dim, String playerName){
		map = new Map(dim);
	}
	public Map getMap(){
		return map;
	}
}
