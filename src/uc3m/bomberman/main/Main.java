package uc3m.bomberman.main;

import java.util.Locale;

import edu.uc3m.game.GameBoardGUI;
import uc3m.bomberman.entity.*;
import uc3m.bomberman.map.*;

public class Main{
	public final static int DIMENSION = 17;
	public final static int NLEVEL = 15;
	public final static double FPS = 60.0;
	public final static double TPS = 30.0;
	public final static double BOMB_TIME = 2000;
	public final static double EXP_TIME = 1000;
	
	private static int NEXT_ID = 0;
	private static boolean running = true;
	
	public static final int nextId(){
		return ++NEXT_ID;
	}
	
	public static void main(String[] args) throws InterruptedException{
		
		Locale.setDefault(new Locale("en"));
		
		//Create board
		GameBoardGUI board = new GameBoardGUI(DIMENSION, DIMENSION);
		Game game = newGame("Bomberman", board, NLEVEL);

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
				if(deltaTimeTick > 1000.0/TPS){ //Esto son los ticks
					String newGameName = eventHandler(game, board);
					if(!newGameName.equals(""))
						game = newGame(newGameName, board, NLEVEL);
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
			String newGameName = eventHandler(game, board);
			if(!newGameName.equals("")){
				game = newGame(newGameName, board, NLEVEL);
				running = true;
			}
			//TODO esperar nuevo juego
		}while(true);
		
	}
	public static Game newGame(String name, GameBoardGUI board, int nLevels){
		Game game = new Game(DIMENSION, name, nLevels);
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
	/**
	 *
	 * @param game
	 * @param board
	 * @return If a new game has been ordered creating, it returns the name of the new game. Else, it returns ""
	 */
	public static String eventHandler(Game game, GameBoardGUI board){
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
				detonateAllBombs(game, board);
				break;
			default:
				if(action.contains("command ")){
					try{
						String command = action.substring(8);
						commandParser(game, board, command);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}else if(action.contains("new game ")){
					try{
						String gameName = action.substring(9);
						return gameName;
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		}
		return "";
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
				if(current instanceof Enemy){
					board.gb_println("Enemy "+current.getId()+" killed. +"+((Enemy) current).getScoreOnDeath()+" points.");
					game.getPlayer().addScore(((Enemy) current).getScoreOnDeath());
				}
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
		//TODO que la consola te diga que hay enemigos cuando no puedes cruzar la puerta
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
		board.gb_clearSprites();
		for(int ii = 0; ii < game.getEntities().length; ii++){
			board.gb_addSprite(game.getEntities()[ii].getId(), game.getEntities()[ii].getSprite(), true);	
		}		
	}
	public static void detonateAllBombs(Game game, GameBoardGUI board){
		for(int ii = 0; ii < game.getEntities().length; ii++){
			Entity current = game.getEntities()[ii];
			if(current instanceof Bomb){
				Bomb bomb = (Bomb) current;
				game.explodeAt(bomb.getPosition());
				game.getPlayer().bombExploded();
				removeEntity(game, board, bomb);
			}
		}
	}
	/**
	 * 
	 * @param game
	 * @param board
	 * @param command
	 * @return If a new game has been ordered creating, it returns the name of the new game. Else, it returns ""
	 */
	public static void commandParser(Game game, GameBoardGUI board, String command){
		switch(command){
		case "explore":
			game.getMap().toggleExploreMode();
			board.gb_println("Explore mode toggled");
			break;
		case "new":
			game.recreateCurrentLevel();
			board.gb_println("New map created");
			break;
		case "god":
			game.getPlayer().toggleGod();
			board.gb_println("God mode toggled");
			break;
		case "noclip":
			game.getPlayer().toggleClip();
			board.gb_println("Clip mode toggled");
			break;
		case "detonate":
			detonateAllBombs(game, board);
			board.gb_println("All placed bombs detonated");
			break;
		case "clear":
			board.gb_clearCommandBar();
			break;
		default:
			if(command.contains("level")){
				try{
					int level = Integer.parseInt(command.substring(5))-1;
					game.setMap(level);
					loadEntities(game, board);
					board.gb_println("Loaded level "+level);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		board.gb_clearCommandBar();
	}
}