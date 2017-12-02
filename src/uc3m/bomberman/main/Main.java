package uc3m.bomberman.main;

import edu.uc3m.game.GameBoardGUI;
import uc3m.bomberman.entity.*;
import uc3m.bomberman.map.*;

public class Main{
	private final static int DIMENSION = 17;
	private final static double FPS = 60.0;
	
	private static int NEXT_ID = 0;
	public static final int nextId(){
		return ++NEXT_ID;
	}
	
	public static void main(String[] args) throws InterruptedException{
		
		Game game = new Game(DIMENSION, "hola");
		
		//Create board
		GameBoardGUI board = new GameBoardGUI(DIMENSION, DIMENSION);
		board.setVisible(true);
		for(int ii = 0; ii < game.getEntities().length; ii++){
			board.gb_addSprite(game.getEntities()[ii].getId(), game.getEntities()[ii].getSprite(), true);
		}
		
		//TODO ALEJANDRO: He hecho dos contadores: uno para los fps y otro para los ticks que harán los cálculos.
		//Así se pueden capar los fps jajaj (en realidad es para que la velocidad de los cálculos no dependa de la tasa de refresco de los gráficos)
		long time = System.currentTimeMillis();
		long timeTick = time;
		long deltaTime = 0;
		long deltaTimeTick = 0;
		
		while(true){ //TODO este boolean deberia ser una variable
			if(deltaTime > 1000.0/FPS){ //Esto son los fps
				render(game, board);
				deltaTime = 0;
				time = System.currentTimeMillis();
			}
			if(deltaTimeTick > 1000.0/FPS){ //Esto son los ticks
				movePlayer(game, board);
				deltaTimeTick = 0;
				timeTick = System.currentTimeMillis();
			}
			deltaTime = System.currentTimeMillis()-time;
			deltaTimeTick = System.currentTimeMillis()-timeTick;
			//Let's not burn up the CPU
			Thread.sleep(25);
		}
		
	}
	public static void render(Game game,GameBoardGUI board) {
		//render map		
		renderMap(game.getMap(), board);
		//renders all created entities
		renderEntities(game.getEntities(), board);
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
	/**
	 * Calls <code>{@link Game#addEntity(Entity)}</code> with <code>entity</code> as parameter and puts its sprite on the board.
	 * @param game Game where to add the <code>entity</code>
	 * @param board Board where to put the <code>entity</code>'s sprite
	 * @param entity Entity to add
	 */
	public static void addEntity(Game game, GameBoardGUI board, Entity entity){
		if(game.addEntity(entity))
			board.gb_addSprite(entity.getId(), entity.getSprite(), true);
	}
	/**
	 * Calls <code>{@link Game#removeEntity(Entity)}</code> with <code>entity</code> as parameter and HIDES its sprite on the board.
	 * The id is not removed.
	 * @param game
	 * @param board
	 * @param entity
	 */
	public static void removeEntity(Game game, GameBoardGUI board, Entity entity){
		if(game.removeEntity(entity.getId()))
			board.gb_setSpriteVisible(entity.getId(), false);
	}
	
	public static void renderEntities(Entity[] entities, GameBoardGUI board){
		for(int ii = 0; ii < entities.length; ii++){
			Entity current = entities[ii];
			if(current.isAlive()){
				board.gb_moveSpriteCoord(current.getId(), current.getPosition().x, current.getPosition().y);
				board.gb_setSpriteVisible(current.getId(), true);
			}
		}
	}
	public static void movePlayer(Game game, GameBoardGUI board){
		//TODO código de prueba. Mueve a bomberman hacia abajo hasta que se choque con algo
		game.getPlayer().moveTowards(Direction.DOWN);
		if(game.getPlayer().collides(game.getMap()))
			game.getPlayer().moveTowards(Direction.UP);
	}
}