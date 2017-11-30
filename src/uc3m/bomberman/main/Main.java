package uc3m.bomberman.main;

import edu.uc3m.game.GameBoardGUI;
import uc3m.bomberman.map.Map;

public class Main{
	public static void main(String[] args){
		int dim = 17;
		Game game = new Game(dim, "hola");
		
		//Create board
		GameBoardGUI board = new GameBoardGUI(dim, dim);
		board.setVisible(true);
		
		while(true){
			render(game, board);
		}
		
	}
	public static void render(Game game,GameBoardGUI board) {
		//render map
		renderMap(game.getMap(), board);
	}
	public static void renderMap(Map map, GameBoardGUI board){
		for(int ii = 0; ii < map.getDimensions().x; ii++){
			for(int jj = 0; jj < map.getDimensions().y; jj++){
				switch(map.getTypeAt(ii, jj)){
				case "green":
					board.gb_setSquareColor(ii, jj, 127, 255, 127);
					break;
				case "wall":
					board.gb_setSquareImage(ii, jj, "wall.gif");
					break;
				case "brick":
					board.gb_setSquareImage(ii, jj, "bricks.gif");
					break;
				}
			}
		}		
	}
}