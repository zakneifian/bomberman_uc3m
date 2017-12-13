package uc3m.bomberman.main;

import java.util.Locale;

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
		
		Locale.setDefault(new Locale("en"));
		Game game = new Game(DIMENSION, "hola");
		
		//Create board
		GameBoardGUI board = new GameBoardGUI(DIMENSION, DIMENSION);
		prerender(game, board);
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
				eventHandler(game, board);
				deltaTimeTick = 0;
				timeTick = System.currentTimeMillis();
			}
			deltaTime = System.currentTimeMillis()-time;
			deltaTimeTick = System.currentTimeMillis()-timeTick;
			//Let's not burn up the CPU
			Thread.sleep(25);
		}
		
	}
	public static void prerender(Game game, GameBoardGUI board){
		
		board.setVisible(true);
		for(int ii = 0; ii < game.getEntities().length; ii++){
			board.gb_addSprite(game.getEntities()[ii].getId(), game.getEntities()[ii].getSprite(), true);	
		}		
		board.gb_setTextAbility1("Bomb range");
		board.gb_setTextAbility2("Player speed");
		board.gb_setTextPointsDown("Bombs");
		board.gb_setTextPointsUp("Score");	
		board.gb_setTextPlayerName(game.getPlayer().getName());
		board.gb_setPortraitPlayer("White_Bomberman_R.png");
	}
	public static void render(Game game,GameBoardGUI board) {
		//render map		
		renderMap(game.getMap(), board);
		//renders all created entities
		renderEntities(game.getEntities(), board);
		//renders right board
		renderStats(game, board);
	}
	public static void renderMap(Map map, GameBoardGUI board){
		board.gb_repaintBoard();
		for(int ii = 0; ii < map.getDimensions().x; ii++){
			for(int jj = 0; jj < map.getDimensions().y; jj++){
				switch(map.getTypeAt(ii, jj)){
				case "green":
					board.gb_setSquareColor(ii, jj, 200, 255, 150);
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
	public static void renderStats(Game game, GameBoardGUI board){

		board.gb_setValueHealthMax(game.getPlayer().getMaxHp());
		board.gb_setValueHealthCurrent(game.getPlayer().getHp());
		board.gb_setValueAbility1(game.getPlayer().getRange());
		board.gb_setValueAbility2(game.getPlayer().getSpeed());
		//board.gb_setValueLevel(level);
		board.gb_setValuePointsDown(game.getPlayer().getBombs());
		board.gb_setValuePointsUp(game.getPlayer().getScore());
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
				board.gb_addSprite(current.getId(), current.getSprite(), true);
				board.gb_moveSpriteCoord(current.getId(), current.getPosition().x, current.getPosition().y);
				board.gb_setSpriteVisible(current.getId(), true);
			}
		}
	}
	public static void eventHandler(Game game, GameBoardGUI board){
		//TODO código de prueba. Mueve a bomberman hacia abajo hasta que se choque con algo
		String action = board.gb_getLastAction().trim();
		if(action.length() > 0){ 
			//if movement
			if (action.equals("up") || action.equals("down") || action.equals("left") || action.equals("right")) {
				game.getPlayer().setEntityDir(action);
				game.getPlayer().animateMovement(game.getPlayer().getSpritePhase(), game.getPlayer().getEntityDir());
			}
			switch(action){
			case "up":
				game.getPlayer().moveTowards(Direction.UP);
				if(game.getPlayer().collides(game.getMap())){
					game.getPlayer().moveTowards(Direction.DOWN);
				}
				break;
			case "down":
				game.getPlayer().moveTowards(Direction.DOWN);
				if(game.getPlayer().collides(game.getMap())){
					game.getPlayer().moveTowards(Direction.UP);
				}
				break;
			case "left":
				game.getPlayer().moveTowards(Direction.LEFT);
				if(game.getPlayer().collides(game.getMap())){
					game.getPlayer().moveTowards(Direction.RIGHT);
				}
				break;
			case "right":
				game.getPlayer().moveTowards(Direction.RIGHT);
				if(game.getPlayer().collides(game.getMap())){
					game.getPlayer().moveTowards(Direction.LEFT);
				}
				break;
			}
		}
	}
}