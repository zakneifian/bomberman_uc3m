package uc3m.bomberman.main;

import java.util.Locale;

import edu.uc3m.game.GameBoardGUI;
import uc3m.bomberman.entity.*;
import uc3m.bomberman.map.*;

public class Main{
	public final static int DIMENSION = 17;
	public final static double FPS = 60.0;
	public final static double BOMB_TIME = 3000;
	public final static double EXP_TIME = 1500;
	
	private static int NEXT_ID = 0;
	private static boolean running = true;
	
	public static final int nextId(){
		return ++NEXT_ID;
	}
	
	public static void main(String[] args) throws InterruptedException{
		
		Locale.setDefault(new Locale("en"));
		
		//Create board
		GameBoardGUI board = new GameBoardGUI(DIMENSION, DIMENSION);
		Game game = newGame("Bomberman", board);

		long time = System.currentTimeMillis();
		long timeTick = time;
		long deltaTime = 0;
		long deltaTimeTick = 0;
		do{
			while(running){
				if(deltaTime > 1000.0/FPS){ //Esto son los fps
					render(game, board);
					deltaTime = 0;
					time = System.currentTimeMillis();
				}
				if(deltaTimeTick > 1000.0/FPS){ //Esto son los ticks
					eventHandler(game, board);
					tickHandler(game, board);
					collisionHandler(game, board);
					checkAliveEntities(game, board);
					checkIfNextLevel(game, board);
					
					deltaTimeTick = 0;
					timeTick = System.currentTimeMillis();
				}
				deltaTime = System.currentTimeMillis()-time;
				deltaTimeTick = System.currentTimeMillis()-timeTick;
				//Let's not burn up the CPU
				Thread.sleep(25);
			}
			//TODO esperar nuevo juego
		}while(true);
		
	}
	public static Game newGame(String name, GameBoardGUI board){
		Game game = new Game(DIMENSION, name);
		prerender(game, board);
		return game;
	}
	public static void prerender(Game game, GameBoardGUI board){

		board.setVisible(true);
		loadEntities(game, board);
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
				board.gb_setSquareColor(ii, jj, 200, 255, 150);
				board.gb_setSquareImage(ii, jj, map.getSpriteAt(ii, jj));
			}
		}		
	}
	public static void renderStats(Game game, GameBoardGUI board){

		board.gb_setValueHealthMax(game.getPlayer().getMaxHp());
		board.gb_setValueHealthCurrent(game.getPlayer().getHp());
		board.gb_setValueAbility1(game.getPlayer().getRange());
		board.gb_setValueAbility2(game.getPlayer().getSpeed());
		board.gb_setValueLevel(game.getLevel()+1);
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
		for(int ii = entities.length - 1; ii > -1; ii--){ // que lo ultimo en ser renderizado sea el jugador
			Entity current = entities[ii];
			if(current.isAlive()){
				board.gb_setSpriteImage(current.getId(), current.getSprite());
				board.gb_moveSpriteCoord(current.getId(), current.getPosition().x, current.getPosition().y);
				board.gb_setSpriteVisible(current.getId(), current.isAlive());
			}
		}
	}
	public static void eventHandler(Game game, GameBoardGUI board){
		String action = board.gb_getLastAction().trim();
		game.setPlayerAction(action);
		if(action.length() > 0 && game.getPlayer().isAlive()){ 
			//if movement
			if (action.equals("up") || action.equals("down") || action.equals("left") || action.equals("right")) {
				game.getPlayer().setEntityDir(action);
				game.getPlayer().animateMovement(game.getPlayer().getSpritePhase(), game.getPlayer().getEntityDir());
			}
			switch(action){
			case "up":
				game.getPlayer().moveTowards(Direction.UP, game.getMap());
				if(game.getPlayer().collides(game.getMap()))
					game.getPlayer().moveTowards(Direction.DOWN, game.getMap());
				break;
			case "down":
				game.getPlayer().moveTowards(Direction.DOWN, game.getMap());
				if(game.getPlayer().collides(game.getMap()))
					game.getPlayer().moveTowards(Direction.UP, game.getMap());
				break;
			case "left":
				game.getPlayer().moveTowards(Direction.LEFT, game.getMap());
				if(game.getPlayer().collides(game.getMap()))
					game.getPlayer().moveTowards(Direction.RIGHT, game.getMap());
				break;
			case "right":
				game.getPlayer().moveTowards(Direction.RIGHT, game.getMap());
				if(game.getPlayer().collides(game.getMap()))
					game.getPlayer().moveTowards(Direction.LEFT, game.getMap());
				break;
			case "space":
				if (game.getPlayer().putBomb()) {
					addEntity(game, board, new Bomb(nextId(), game.getPlayer().getPosition().tenthsToUnits().unitsToTenths()));
					for (int ii = 0; ii < game.getEntities().length; ii++){
						Entity current = game.getEntities()[ii];
						if (current instanceof AntiBomberman) addEntity(game, board, new Bomb(nextId(), ((AntiBomberman) current).getPosition().tenthsToUnits().unitsToTenths()));
					}
				}
				break;
			case "tab":
				for(int ii = 0; ii < game.getEntities().length; ii++){
					Entity current = game.getEntities()[ii];
					if(current instanceof Bomb){
						Bomb bomb = (Bomb) current;
						game.explodeAt(bomb.getPosition());
						game.getPlayer().bombExploded();
						removeEntity(game, board, bomb);
					}
				}
				break;
			case "new  game  <player  name>":
				//TODO new game
			case "command  <thecommand>":
				//TODO comandos
			}
		}
	}
	public static void tickHandler(Game game, GameBoardGUI board){
		for(int ii = 0; ii < game.getEntities().length; ii++){
			Entity current = game.getEntities()[ii];
			//Handle the ticks
			if(current instanceof Bomb){
				Bomb bomb = (Bomb) current;
				if(bomb.tick() && !game.getPlayer().isRemote()){
					game.explodeAt(bomb.getPosition());
					game.getPlayer().bombExploded();
					removeEntity(game, board, bomb);
				}
			}else if(current instanceof Enemy){
				if (current instanceof AntiBomberman) {
					((AntiBomberman) current).moveEnemyAction(game, board.gb_getLastAction());
				}else
					((Enemy) current).moveEnemy(game);				
			}
		}
		
		game.getMap().tickTiles();
	}
	public static void collisionHandler(Game game, GameBoardGUI board){
		//Entity collision
		for (int ii = 0; ii < game.getEntities().length; ii++){
			Entity current = game.getEntities()[ii];
			for(int jj = 0; jj < game.getEntities().length; jj++){
				Entity another = game.getEntities()[jj];
				if(!current.equals(another) && current.isAlive() && another.isAlive() && current instanceof MovableEntity && current.collides(another)){
					MovableEntity movable = (MovableEntity) current;
					movable.onCollision(another);
				}
			}
		}
		//Map explosions
		for(int ii = 0; ii < game.getEntities().length; ii++){
			Entity current = game.getEntities()[ii];
			if(game.getMap().getTypeAt(current.getPosition().tenthsToUnits()).equals("explosion"))
				current.takeDamage(game.getPlayer().getDamage());
		}
		//Upgrades
		if(game.getMap().getTypeAt(game.getPlayer().getPosition().tenthsToUnits()).equals("upgrade")){
			game.getPlayer().upgrade(game.getMap().consumeUpgradeAt(game.getPlayer().getPosition().tenthsToUnits()));
		}
		
		//Enemy collision
		for (int ii = 0; ii < game.getEntities().length; ii++){
			Entity current = game.getEntities()[ii];
			for(int jj = 0; jj < game.getEntities().length; jj++){
				Entity another = game.getEntities()[jj];
				if(current instanceof MovableEntity && current.collides(another)){
					((MovableEntity) current).onCollision(another);
				}
			}
		}		
	}
	public static void checkAliveEntities(Game game, GameBoardGUI board){
		int enemyN = 0;
		for (int ii = 0; ii < game.getEntities().length; ii++){
			Entity current = game.getEntities()[ii];
			if (current instanceof Enemy && current.isAlive()) enemyN++;
			if (current instanceof Player && !current.isAlive()) {
				gameOver(game, board);				
			}
			if(!current.isAlive()){
				if(current instanceof Enemy)
					game.getPlayer().addScore(((Enemy) current).getScoreOnDeath());
				removeEntity(game, board, current);
			}
		}
		if (enemyN == 0) game.getPlayer().setEnemiesAlive(false);
		else game.getPlayer().setEnemiesAlive(true);
	}
	
	public static void checkIfNextLevel(Game game, GameBoardGUI board) {
		if (!game.getPlayer().getEnemiesAlive() && game.getPlayer().getOpenedDoorOnTouch()) {
			game.getPlayer().setOpenedDoorOnTouch(false);
			game.nextMap();
			loadEntities(game, board);
		}
	}
	public static void gameOver(Game game, GameBoardGUI board){
		for(int jj = 0; jj < 5; jj++){
			board.gb_setSpriteImage(game.getPlayer().getId(), "bomberman14"+(jj+1)+".png");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		board.gb_showMessageDialog("Game over!\nScore: "+game.getPlayer().getScore());
		running = false;
	}
	public static void loadEntities(Game game, GameBoardGUI board){
		for(int ii = 0; ii < game.getEntities().length; ii++){
			board.gb_addSprite(game.getEntities()[ii].getId(), game.getEntities()[ii].getSprite(), true);	
		}		
	}
}