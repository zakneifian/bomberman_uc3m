package uc3m.bomberman.main;

import java.util.Locale;

import edu.uc3m.game.GameBoardGUI;
import uc3m.bomberman.entity.*;
import uc3m.bomberman.map.*;

/**
 * This is the main class of the game. It follows a structured programming paradigm to perform all the actions the game needs to be executed
 * @see <code>{@link Game}</code>
 * @see <code>{@link GameBoardGUI}</code>
 * @see <code>{@link Entity}</code>
 * @author Daniel Alcaide Nombela, Alejandro Martínez Riera
 * @since December 12th, 2017
 * @version 0.1
 */
public class Main{
	public final static int DIMENSION = 17;
	public final static int NLEVEL = 15;
	public final static double FPS = 60.0;
	public final static double TPS = 30.0;
	public final static double BOMB_TIME = 2000;
	public final static double EXP_TIME = 1000;
	
	private static int NEXT_ID = 0;
	private static boolean running = true;
	
	/**
	 * This method generates a new ID for a new entity. This is the method that should be used when assigning id's to new entities
	 * @return The id of the next entity to add
	 */
	public static final int nextId(){
		return ++NEXT_ID;
	}
	/**
	 * Main method; this method creates a <code>{@link Game}</code> object (with default player name "Bomberman") and a <code>{@link GameBoardGUI}</code> object,
	 * which will be later used throughout the execution of the game; it will execute ticks and renders in a periodic fashion: 30 ticks, 60 frames (renders) per second; first ticks, then renders.
	 * The runtime of this program can be divided into two processes: ticks and renders
	 * When the program makes the calculations it needs to run the game (i.e., move entities, check collisions, etc.), we say it has done a tick; 
	 * when it prints the result of those calculations on the board (<code>{@link GameBoardGUI}</code>), it has done a render.
	 * It will keep doing so as long as the game is running (the game can be stopped by the player's death, the user starting a new game or the player winning the game)
	 * @see <code>{@link #render(Game, GameBoardGUI)}</code>
	 * @see <code>{@link #tick(Game, GameBoardGUI)}</code>
	 * @see <code>{@link #newGame(String, GameBoardGUI, int)}</code>
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException{
		//Localization
		Locale.setDefault(new Locale("en"));
		
		//Create board
		GameBoardGUI board = new GameBoardGUI(DIMENSION, DIMENSION);
		//Create game
		Game game = newGame("Bomberman", board, NLEVEL);

		long time = System.currentTimeMillis();
		long timeTick = time;
		long deltaTime = 0;
		long deltaTimeTick = 0;
		
		
		do{
			//This loop is the game execution
			while(running){
				if(deltaTimeTick > 1000.0/TPS){ //Ticks
					String newGameName = tick(game, board);
					//Check if a new game has been ordered
					if(!newGameName.equals(""))
						game = newGame(newGameName, board, NLEVEL);
					//Reset timer
					deltaTimeTick = 0;
					timeTick = System.currentTimeMillis();
				}
				if(deltaTime > 1000.0/FPS){ //Fps
					render(game, board);
					//Reset timer
					deltaTime = 0;
					time = System.currentTimeMillis();
				}
				//Update timers
				deltaTime = System.currentTimeMillis()-time;
				deltaTimeTick = System.currentTimeMillis()-timeTick;
				//Let's not burn up the CPU
				Thread.sleep(10);
			}
			//Check if a new game has been ordered
			String newGameName = eventHandler(game, board);
			if(!newGameName.equals("")){
				game = newGame(newGameName, board, NLEVEL);
				running = true;
			}
		}while(true);
		
	}
	/**
	 * This method will create a new <code>{@link Game}</code> and loads its entities into the board.
	 * @param name Name of the player of the new game
	 * @param board <code>{@link GameBoardGUI}</code> object to show the game on
	 * @param nLevels Number of levels the game will have
	 * @return The created <code>{@link Game}</code> object
	 * @see <code>{@link Game}</code>
	 * @see <code>{@link #loadEntities(Game, GameBoardGUI)}</code>
	 */
	public static Game newGame(String name, GameBoardGUI board, int nLevels){
		Game game = new Game(DIMENSION, name, nLevels);
		prerender(game, board);
		loadEntities(game, board);
		return game;
	}
	/**
	 * This method performs the ticks of the game, i.e., it will perform all the calculations the game needs when running.
	 * It will, in the following order: 1)Send the tick to all the "tickable" entities (entities that act when a tick is done, i.e.: bomb, etc.)
	 * 2)Read the user's input 
	 * 3)Check the collisions of every entity with each other 
	 * 4)Check whether the registered entities in the game are alive or not, and kill them appropriately 
	 * 5)Check whether the player has advanced to the next level or nor<P>
	 * @param game <code>{@link Game}</code> to be used
	 * @param board <code>{@link GameBoardGUI}</code> to be used
	 * @return <code>{@link Main#eventHandler(Game, GameBoardGUI)}</code>
	 * @see <code>{@link #tickHandler(Game, GameBoardGUI)}</code>
	 * @see <code>{@link #eventHandler(Game, GameBoardGUI)}</code>
	 * @see <code>{@link #collisionHandler(Game, GameBoardGUI)}</code>
	 * @see <code>{@link #checkAliveEntities(Game, GameBoardGUI)}</code>
	 * @see <code>{@link #checkIfNextLevel(Game, GameBoardGUI)}</code>
	 */
	public static String tick(Game game, GameBoardGUI board){
		//Send ticks
		tickHandler(game, board);
		//Check user's events
		String newGameName = eventHandler(game, board);
		//Check collisions
		collisionHandler(game, board);
		//Check that entities are alive
		checkAliveEntities(game, board);
		//Advance level if it should
		checkIfNextLevel(game, board);
		return newGameName;
	}
	/**
	 * This method sends a tick to all the entities and tiles in <code>game</code> that need it.
	 * The entities that need a tick are enemies and bombs. 
	 * Bombs will perform a tick and enemies will move. If a bomb has exploded, it will create its explosion and remove it from <code>game</code> and <code>board</code>.
	 * The tiles are ticked through the method referenced below.
	 * @param game
	 * @param board
	 * @see <code>{@link #removeEntity(Game, GameBoardGUI, Entity)}</code>
	 * @see <code>{@link Bomb#tick()}</code>
	 * @see <code>{@link Enemy#moveEnemy(Game)}</code>
	 * @see <code>{@link Game#getEntities()}</code>
	 * @see <code>{@link Map#tickTiles()}</code> - method that ticks tiles
	 */
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
				((Enemy) current).moveEnemy(game);				
			}
		}		
		game.getMap().tickTiles();
	}
	/**
	 * This method reads the last action sent to <board> and acts accordingly; it also gives <code>game</code> that last action to store it.
	 * If any of the movement arrows have been pressed, it moves the player. If the space has been pressed, it places a bomb where the player is in case the player has enough bombs.
	 * If tab has been pressed and the player has the proper upgrade, it will explode all bombs. Else, if a new game has been ordered, it will return its name. Else, if the action is a command,
	 * it will parse it.
	 * @param game
	 * @param board
	 * @return If a new game has been ordered creating, it returns the name of the new game. Else, it returns "" empty <code>String</code>
	 * @see <code>{@link #commandParser(Game, GameBoardGUI, String)}</code>
	 * @see <code>{@link #detonateAllBombs(Game, GameBoardGUI)}</code>
	 * @see <code>{@link Player}</code>
	 * @see <code>{@link Entity#collides(Map)}</code>
	 * @see <code>{@link GameBoardGUI#gb_getLastAction()}</code>
	 */
	public static String eventHandler(Game game, GameBoardGUI board){
		String action = board.gb_getLastAction().trim();
		//Store last action
		game.setPlayerAction(action);
		if(action.length() > 0){ 
			if (game.getPlayer().isAlive()) {
				//if movement
				if (action.equals("up") || action.equals("down") || action.equals("left") || action.equals("right")) {
					//Movement animation
					game.getPlayer().setEntityDir(action);
					game.getPlayer().animateMovement(game.getPlayer().getSpritePhase(), game.getPlayer().getEntityDir());
				}
				switch(action){
				//Move player
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
					//Place bombs
				case "space":
					if (game.getPlayer().putBomb()) {
						addEntity(game, board, new Bomb(nextId(), game.getPlayer().getPosition().tenthsToUnits().unitsToTenths()));
					}
					break;
					//Detonate remote
				case "tab":
					if(game.getPlayer().isRemote())
						detonateAllBombs(game, board);
					break;
				default:
					if(action.contains("command ")){
						//Commands
						try{
							String command = action.substring(8);
							commandParser(game, board, command);
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}else if(action.contains("new game ")){
						//New game button
						try{
							String gameName = action.substring(9);
							return gameName;
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}
			}
		}
		return "";
	}
	/**
	 * This method checks the collisions of every entity with every other and collides them invoking the proper method; it also checks the collisions (i.e.: standing on special tiles) of those entities with the map
	 * When two entities are in the same cell, they collide. If one of those entities is movable, the method <code>{@link MovableEntity#onCollision(Entity)}</code> is called.
	 * Then, this method checks if any entity is standing  on an explosion, in which case it will be damaged.
	 * It then checks if the player is standing on an upgrade, and it it is, then the upgrade is consumed.
	 * @param game
	 * @param board
	 * @see <code>{@link Game#getEntities()}</code>
	 * @see <code>{@link Entity#collides(Entity)}</code>
	 * @see <code>{@link MovableEntity#onCollision(Entity)}</code>
	 * @see <code>{@link Player#onCollision(Entity)}</code>
	 */
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
			if(game.getMap().getTypeAt(current.getPosition().tenthsToUnits()).equals("explosion")){
				current.takeDamage(game.getPlayer().getDamage());
				if(current instanceof Player)
					board.gb_println("You are harming yourself!");
			}
		}
		//Upgrades
		if(game.getMap().getTypeAt(game.getPlayer().getPosition().tenthsToUnits()).equals("upgrade")){
			game.getPlayer().upgrade(game.getMap().consumeUpgradeAt(game.getPlayer().getPosition().tenthsToUnits()));
		}	
	}
	/**
	 * Checks if all registered entities are alive, and removes those that are not from both <code>game</code> and <code>board</code>
	 * @param game
	 * @param board
	 * @see <code>{@link #removeEntity(Game, GameBoardGUI, Entity)}</code>
	 * @see <code>{@link #gameOver(Game, GameBoardGUI)}</code> - is called if the player has died
	 */
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
					board.gb_println("Enemy "+current+" killed. +"+((Enemy) current).getScoreOnDeath()+" points.");
					game.getPlayer().addScore(((Enemy) current).getScoreOnDeath());
				}
				removeEntity(game, board, current);
			}
		}
		if (enemyN == 0) game.getPlayer().setEnemiesAlive(false);
		else game.getPlayer().setEnemiesAlive(true);
	}
	/**
	 * This method will check whether the player has openned the door or not, and if it has, load a new map, and its entities.
	 * If the next level could not be loaded, it will mean we have arrived at the end, and so the game will end with the winning screen.
	 * @param game
	 * @param board
	 * @see <code>{@link #win(Game, GameBoardGUI)}</code>
	 * @see <code>{@link Player#getOpenedDoorOnTouch()}</code>
	 * @see <code>{@link #loadEntities(Game, GameBoardGUI)}</code>
	 */
	public static void checkIfNextLevel(Game game, GameBoardGUI board) {
		if (game.getPlayer().getDoorOnTouch()) {
			if (!game.getPlayer().getEnemiesAlive()) {
				int bonus = game.nextMap();
				if(bonus == -1)
					win(game, board);
				else{
					board.gb_println("You headed into the next level. Bonus score: "+bonus+" points.");
					loadEntities(game, board);
				}
			}
			else board.gb_println("Door closed; there are enemies alive!");
			game.getPlayer().setDoorOnTouch(false);
		}
	}
	/**
	 * Loads the menu and sets <code>board</code> visible
	 * @param game
	 * @param board
	 */
	public static void prerender(Game game, GameBoardGUI board){
		board.setVisible(true);
		board.gb_setTextAbility1("Bomb range");
		board.gb_setTextAbility2("Player speed");
		board.gb_setTextPointsDown("Bombs");
		board.gb_setTextPointsUp("Score");	
		board.gb_setTextPlayerName(game.getPlayer().getName());
		board.gb_setPortraitPlayer("White_Bomberman_R.png");
	}
	/**
	 * This method will print all the graphic information of the <code>game</code> on the <code>board</code>. It first will print the map and its tiles;
	 * then it will place the entities in their correct positions (note that the sprites have to be loaded before rendering). Then the stats will be updated
	 * @param game
	 * @param board
	 * @see <code>{@link #renderMap(Map, GameBoardGUI)}</code>
	 * @see <code>{@link #renderEntities(Entity[], GameBoardGUI)}</code>
	 * @see <code>{@link #renderStats(Game, GameBoardGUI)}</code>
	 */
	public static void render(Game game,GameBoardGUI board) {
		//render map		
		renderMap(game.getMap(), board);
		//renders all created entities
		renderEntities(game.getEntities(), board);
		//renders right board
		renderStats(game, board);
	}
	/**
	 * Sets the sprite of each tile to the appropriate according to the current level of <code>game</code>
	 * @param map
	 * @param board
	 * @see <code>{@link Map}</code>
	 */
	public static void renderMap(Map map, GameBoardGUI board){
		board.gb_repaintBoard();
		for(int ii = 0; ii < map.getDimensions().x; ii++){
			for(int jj = 0; jj < map.getDimensions().y; jj++){
				board.gb_setSquareColor(ii, jj, 200, 255, 150);
				board.gb_setSquareImage(ii, jj, map.getSpriteAt(ii, jj));
			}
		}		
	}
	/**
	 * This method updates the data shown in the side menu
	 * @param game
	 * @param board
	 */
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
	 * This method updates the corresponding sprites  on <code>board</code> of the entities in <code>entities</code>. Note that the corresponding
	 * sprites must be already loaded
	 * @param entities 
	 * @param board 
	 * @see <code>{@link #loadEntities(Game, GameBoardGUI)}</code>
	 */
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
	 * The sprite is not removed.
	 * @param game
	 * @param board
	 * @param entity
	 */
	public static void removeEntity(Game game, GameBoardGUI board, Entity entity){
		if(game.removeEntity(entity.getId()))
			board.gb_setSpriteVisible(entity.getId(), false);
	}
	/**
	 * Ends the game animating the players death and showing a game over message
	 * @param game
	 * @param board
	 */
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
	/**
	 * Ends the <code>game</code> showing a win message
	 * @param game
	 * @param board
	 */
	public static void win(Game game, GameBoardGUI board){
		board.gb_showMessageDialog("You won!\nScore: "+game.getPlayer().getScore());
		running = false;
	}
	/**
	 * Loads the sprites corresponding to the entities of the <code>game</code>
	 * @param game
	 * @param board
	 * @see <code>{@link Game#getEntities()}</code>
	 */
	public static void loadEntities(Game game, GameBoardGUI board){
		board.gb_clearSprites();
		for(int ii = 0; ii < game.getEntities().length; ii++){
			board.gb_addSprite(game.getEntities()[ii].getId(), game.getEntities()[ii].getSprite(), true);	
		}		
	}
	/**
	 * This method destroys all bombs of the game and places explosions in the corresponding positions
	 * @param game
	 * @param board
	 * @see <code>{@link #removeEntity(Game, GameBoardGUI, Entity)}</code>
	 */
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
	 * Takes a command and acts accordingly. A list of the commands is specified in the report.
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
			loadEntities(game, board);
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
		case "killall":
			for(Entity e : game.getEntities()){
				removeEntity(game, board, e);
			}
			board.gb_println("Entities cleared");
			break;
		case "clear":
			board.gb_clearConsole();
			break;
		case "win":
			win(game, board);
			break;
		default:
			if(command.contains("level")){
				try{
					int level = Integer.parseInt(command.substring(5))-1;
					if(game.setMap(level)){
						loadEntities(game, board);
						board.gb_println("Loaded level "+ (level + 1));
					}else
						board.gb_println("Level "+ (level + 1) +" does not exist");
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}else{
				board.gb_println("Command not found!");
			}
		}
		board.gb_clearCommandBar();
	}
}