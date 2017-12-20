package uc3m.bomberman.main;

import uc3m.bomberman.entity.*;
import uc3m.bomberman.map.*;

/**
 * This class stores and handles all the information needed to execute the game, being helped by other classes (see below).
 * @author Daniel Alcaide Nombela, Alejandro Martínez Riera
 * @see <code>{@link Map}</code>
 * @see <code>{@link Entity}</code>
 * @see <code>{@link Player}</code>
 */
public class Game{
	private static final long MIN_TIME = 5*60*1000; //3 minutes
	
	private Map[] map = new Map[15];
	private Entity[] entities;
	private Player player;
	private int level = 0;
	private String playerAction = "";
	private long mapTime = 0;
	
	/**
	 * This constructor creates a game with <code>nLevels</code> number of <code>dim</code> x <code>dim</code> maps (levels), and creates a <code>{@link Player}</code> with name <code>playerName</code>
	 * @param dim dimension of the map
	 * @param playerName name of the player
	 * @param nLevels number of levels
	 */
	public Game(int dim, String playerName, int nLevels){		
		if(playerName == null || playerName.equals(""))
			playerName = "Bomberman";
		
		player = new Player(Main.nextId(), playerName);
		
		if(nLevels > 0)
			map = new Map[nLevels];
		map[0] = new Map(Main.DIMENSION, 0, player.getPosition().tenthsToUnits());
		for(int ii = 1; ii < map.length; ii++){
			map[ii] = new Map(Main.DIMENSION, ii, map[ii-1].getDoorSquare());
		}
		
		entities = new Entity[1];
		entities[0] = player;
		mapTime = System.currentTimeMillis();
		
		spawnEnemies();
	}
	/**
	 * @return Current selected level
	 */
	public Map getMap(){
		return map[level];
	}
	/**
	 * This method regenerates the current selected level
	 */
	public void recreateCurrentLevel(){
		if(level == 0)
			map[level] = new Map(Main.DIMENSION, level, player.getPosition());
		else
			map[level] = new Map(Main.DIMENSION, level, map[level-1].getDoorSquare());
		clearEntities();
		spawnEnemies();
	}
	/**
	 * This method selects the next map
	 * @return If no more levels are available, -1; if there is one more level, it returns the score associated to the time bonus (1 point per second, up to 3 minutes)
	 */
	public int nextMap(){
		if(++level >= map.length){
			level--;
			return -1;
		}
		
		mapTime = 0;
		clearEntities();
		spawnEnemies();

		if(MIN_TIME -(System.currentTimeMillis() - mapTime)  > 0){
			int bonus = (int) (MIN_TIME -(System.currentTimeMillis() - mapTime))/1000;
			player.addScore(bonus);
			return bonus;
		}
		return 0;
	}
	/**
	 * Sets the selected level to <code>level</code>
	 * @param level
	 * @return true if the level exists; false otherwise
	 */
	public boolean setMap(int level){
		if(level < 0 || level >= map.length){
			return false;
		}
		this.level = level;
		clearEntities();
		spawnEnemies();
		return true;
	}
	/**
	 * Adds an entity to <code>{@link #entities}</code> such that there are no null pointers in it and there are no repeated ids.
	 * @param entity
	 * @return true if the entity has been added succesfully, false otherwise
	 */
	public boolean addEntity(Entity entity){
		if(!entity.isAlive())
			return false;
		for(int ii = 0; ii < entities.length; ii++){
			if(entities[ii].getId() == entity.getId())
				return false;
		}		
		//Crea una nueva array con la nueva entidad
		Entity[] arr = new Entity[entities.length+1];
		System.arraycopy(entities, 0, arr, 0, entities.length);
		arr[arr.length-1] = entity;
		entities = arr;
		return true;
	}
	/**
	 * Removes an entity from <code>{@link #entities}</code> such that there are no null pointers left in it and the id's do not repeat
	 * @param id
	 * @return true if the entity exists and has been removed; false otherwise
	 */
	public boolean removeEntity(int id){
		for(int ii = 1; ii < entities.length; ii++){
			if(entities[ii].getId() == id){
				entities[ii].kill();
				Entity[] arr = new Entity[entities.length-1];
				System.arraycopy(entities, 0, arr, 0, ii);
				System.arraycopy(entities, ii+1, arr, ii, arr.length-ii);
				entities = arr;
				return true;
			}
		}
		return false;
	}
	/**
	 * Removes all entities from the array
	 */
	public void clearEntities(){
		Entity[] aux = new Entity[1];
		aux[0] = player;
		entities = aux;
	}
	/**
	 * Adds entities according to the blueprint provided by the selected map
	 * @see <code>{@link Map#getEnemiesPos()}</code>
	 */
	public void spawnEnemies() {
		for (int ii = 0; ii < getMap().getEnemiesPos().length; ii++) {
			for (int jj = 0; jj < getMap().getEnemiesPos()[ii].length; jj++) {
				if(getMap().getEnemiesPos()[ii][jj] != null){
					if (getMap().getEnemiesPos()[ii][jj].equals("balloon")) {
						addEntity(new Balloon(Main.nextId(), new Coordinates(ii*10, jj*10)));
					}
					else if (getMap().getEnemiesPos()[ii][jj].equals("slime")) {
						addEntity(new Slime(Main.nextId(), new Coordinates(ii*10, jj*10)));
					}
					else if (getMap().getEnemiesPos()[ii][jj].equals("anti")) {
						addEntity(new AntiBomberman(Main.nextId(), new Coordinates(ii*10, jj*10)));
					}
				}
			}
		}
	}
	/**
	 * Creates an explosion with center <code>bombPos</code> and range that of the player
	 * @param bombPos
	 * @see <code>{@link Map#setExplosionAt(Coordinates, String)}</code> is used to set the explosions in the appropriate tiles
	 */
	public void explodeAt(Coordinates bombPos){
		int explosionLength = getPlayer().getRange();		
		Coordinates square = bombPos.tenthsToUnits();
		//Center 
		getMap().setTypeAt(square, "explosion");
		boolean wallN = false, wallW = false, wallS = false, wallE = false;
		Coordinates[][] arrExplosive = new Coordinates[4][explosionLength]; // N, W, S, E
		for (int ii = 1; ii < explosionLength; ii++) {
			//Setting all the possible coordinates in all directions (but center)
			arrExplosive[0][ii] = new Coordinates(square.x, square.y + ii); //North
			arrExplosive[1][ii] = new Coordinates(square.x - ii, square.y); //West
			arrExplosive[2][ii] = new Coordinates(square.x, square.y - ii); //South
			arrExplosive[3][ii] = new Coordinates(square.x + ii, square.y); //East
			//Taking into account the limits of the walls
			if (!wallN && ii > 0 && getMap().getTypeAt(arrExplosive[0][ii]).equals("wall")) wallN = true;
			if (!wallW && ii < getMap().getDimensions().x && getMap().getTypeAt(arrExplosive[1][ii]).equals("wall")) wallW = true;
			if (!wallS && ii < getMap().getDimensions().y && getMap().getTypeAt(arrExplosive[2][ii]).equals("wall")) wallS = true;
			if (!wallE && ii > 0 && getMap().getTypeAt(arrExplosive[3][ii]).equals("wall")) wallE = true;
			//Setting explosion types
			getMap().setExplosionAt(square, "c");
			if (!wallN) {
				if      (ii != explosionLength - 1) getMap().setExplosionAt(arrExplosive[0][ii], "v");//Vertical
				else if (ii == explosionLength - 1) getMap().setExplosionAt(arrExplosive[0][ii], "s");//South
			}
			if (!wallW) {
				if      (ii != explosionLength - 1) getMap().setExplosionAt(arrExplosive[1][ii], "h");//Horizontal
				else if (ii == explosionLength - 1) getMap().setExplosionAt(arrExplosive[1][ii], "w");//West
			}
			if (!wallS) {
				if      (ii != explosionLength - 1) getMap().setExplosionAt(arrExplosive[2][ii], "v");//Vertical
				else if (ii == explosionLength - 1) getMap().setExplosionAt(arrExplosive[2][ii], "n");//North
			}
			if (!wallE) {
				if      (ii != explosionLength - 1) getMap().setExplosionAt(arrExplosive[3][ii], "h");//Horizontal
				else if (ii == explosionLength - 1) getMap().setExplosionAt(arrExplosive[3][ii], "e");//East
			}
		}
		
	}


	/**
	 * @return a <code>{@link Entity}</code> array with every Entity currently active on the map.
	 */
	public Entity[] getEntities(){
		return entities;
	}
	/**
	 * @return the <code>{@link Player}</code> object
	 */
	public Player getPlayer(){
		return player;
	}
	/**
	 * @return the current level
	 */
	public int getLevel(){
		return level;
	}
	/**
	 * @return the current player keyboard press/action
	 */
	public String getPlayerAction(){
		return playerAction;
	}
	/**
	 * Sets the current player keyboard press/action
	 * @param action
	 */
	public void setPlayerAction(String action){
		playerAction = action;
	}
}

