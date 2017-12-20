package uc3m.bomberman.map;

import uc3m.bomberman.entity.Entity;
import uc3m.bomberman.entity.Player;

/**
 * This class holds all the methods regarding map manipulation such as map generation. A map being a <code>{@link Tile}</code> array with <code>{@link Coordinates}</code>
 * @author Daniel Alcaide Nombela
 * @author Alejandro Martínez Riera
 * @see <code>{@link Tile}</code>
 * @see <code>{@link Entity}</code>
 * @see <code>{@link Player}</code>
 */

public class Map{
	public static final int NBRICKS = 50;
	
	private Tile[][] map;
	private String[][] upgrades;
	private String[][] enemies;
	private Coordinates dim;
	
	private boolean explore = false;
	/**
	 * Full constructor
	 * 
	 * @param dim
	 * @param level
	 * @param restrictedSquare
	 */
	public Map(int dim, int level, Coordinates restrictedSquare){
		this.dim = new Coordinates(dim, dim);
		createMap(getPlayerPersonalSpace(restrictedSquare), level);
	}
	
	/**
	 * Generates the map with its <code>{@link Tile}</code>s, <code>{@link Upgrade}</code>s and <code>{@link Entity}</code> for certain level,
	 * taking into account that if there exist a previous level, bricks won't be
	 * generated around the door's position.
	 * @param playerPersonalSpace
	 * @param level
	 */
	private void createMap(Coordinates[] playerPersonalSpace, int level){
		map = new Tile[dim.x][dim.y];
		upgrades = new String[dim.x][dim.y];
		enemies = new String[dim.x][dim.y];
		//Create green cells
		for(int ii = 0; ii < map.length; ii++){
			for(int jj = 0; jj < map[ii].length; jj++){
				map[ii][jj] = new Tile("green");
			}
		}
		//Create Walls
		for(int ii = 0; ii < map.length; ii++){
			for(int jj = 0; jj < map[ii].length; jj++){
				if (ii == 0 || jj == 0 || ii == map.length - 1 || jj == map[ii].length - 1 || (ii % 2 == 0 && jj % 2 == 0))
				map[ii][jj] = new Tile("wall");
			}
		}		
		//Create Bricks
		int brickCount = 0;
		while ( brickCount < NBRICKS) {
			//System.out.println(brickCount + " brick placed");
			for(int ii = 0; ii < map.length; ii++){
				for(int jj = 0; jj < map[ii].length; jj++){
					//if there is no bricks or walls we will set a brick with a given probability of dim*dim
					if (!isInPlayerPersonalSpace(playerPersonalSpace, new Coordinates(ii, jj)) && brickCount < NBRICKS && (map[ii][jj] == null || map[ii][jj].getType().equals("green")) && (Math.random() < 2.0/(dim.x*dim.y - brickCount - 1))) {
						map[ii][jj] = new Tile("brick");
						brickCount++; 
					}
				}
			}
		}
		//determine the number of upgrades of each type depending on the level
		genUpgrades(level);
		genEnemies(level);
	}

	/**
	 * sends ticks to the appropiate tiles
	 * @param x
	 * @param y
	 */
	public void tickTiles(){
		for(int ii = 0; ii < map.length; ii++){
			for(int jj = 0; jj < map[ii].length; jj++){
				if(map[ii][jj] instanceof Explosion){
					Explosion explosion = (Explosion) map[ii][jj];
					if(explosion.tick()){
						map[ii][jj] = new Tile("green");
						if(upgrades[ii][jj] != null){
							map[ii][jj] = new Upgrade(upgrades[ii][jj]);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Method that takes the level as parameter to compute
	 * the amount of each upgrade to create
	 * @param level
	 */
	private void genUpgrades(int level){
		int bomb, fire, special, remote, skate, geta, door;
		bomb = (int) (3*(50.0/NBRICKS)+(Math.random()*2)-1);
		fire = 1;
		special = (level+1)%5 == 0 ? 1 : 0;
		remote = (level+1)%10 == 0 ? 1 : 0;
		skate = (level+1)%2 == 0 ? (int)(Math.random()*2) : 0; //50% chance
		geta = Math.random()<0.2 ? 1 : 0; //20% chance
		door = 1;
		while(bomb > 0){
			for(int ii = 0; ii < upgrades.length; ii++){
				for(int jj = 0; jj < upgrades[ii].length; jj++){
					if(map[ii][jj].getType().equals("brick") && Math.random() < (double) bomb/(double) NBRICKS && upgrades[ii][jj] == null){
						upgrades[ii][jj] = "bomb";
						bomb--;
					}
				}
			}
		}
		while(fire > 0){
			for(int ii = 0; ii < upgrades.length; ii++){
				for(int jj = 0; jj < upgrades[ii].length; jj++){
					if(map[ii][jj].getType().equals("brick") && Math.random() < (double) fire/(double) NBRICKS && upgrades[ii][jj] == null){
						upgrades[ii][jj] = "fire";
						fire--;
					}
				}
			}
		}
		while(special > 0){
			for(int ii = 0; ii < upgrades.length; ii++){
				for(int jj = 0; jj < upgrades[ii].length; jj++){
					if(map[ii][jj].getType().equals("brick") && Math.random() < (double) special/(double) NBRICKS && upgrades[ii][jj] == null){
						upgrades[ii][jj] = "special";
						special--;
					}
				}
			}
		}
		while(remote > 0){
			for(int ii = 0; ii < upgrades.length; ii++){
				for(int jj = 0; jj < upgrades[ii].length; jj++){
					if(map[ii][jj].getType().equals("brick") && Math.random() < (double) remote/(double) NBRICKS && upgrades[ii][jj] == null){
						upgrades[ii][jj] = "remote";
						remote--;
					}
				}
			}
		}
		while(skate > 0){
			for(int ii = 0; ii < upgrades.length; ii++){
				for(int jj = 0; jj < upgrades[ii].length; jj++){
					if(map[ii][jj].getType().equals("brick") && Math.random() < (double) skate/(double) NBRICKS && upgrades[ii][jj] == null){
						upgrades[ii][jj] = "skate";
						skate--;
					}
				}
			}
		}
		while(geta > 0){
			for(int ii = 0; ii < upgrades.length; ii++){
				for(int jj = 0; jj < upgrades[ii].length; jj++){
					if(map[ii][jj].getType().equals("brick") && Math.random() < (double) geta/(double) NBRICKS && upgrades[ii][jj] == null){
						upgrades[ii][jj] = "geta";
						geta--;
					}
				}
			}
		}
		while(door > 0){
			for(int ii = 0; ii < upgrades.length; ii++){
				for(int jj = 0; jj < upgrades[ii].length; jj++){
					if(map[ii][jj].getType().equals("brick") && Math.random() < (double) door/(double) NBRICKS && upgrades[ii][jj] == null){
						upgrades[ii][jj] = "door";
						door--;
					}
				}
			}
		}	
	}
	/**
	 * Method that takes the level as parameter to compute
	 * the amount of each enemy to create
	 * @param level
	 */
	private void genEnemies(int level) {
		int balloon, slime, antibomberman;
		balloon = (int) Math.floor(Math.random()*10 + 1);  
		slime = 0;
		if(level > 0){
			slime = 1 + (level+1)/4;
		}
		antibomberman = Math.random()*10 < 1 ? 1 : 0;

		while(balloon > 0){
			for(int ii = 0; ii < enemies.length; ii++){
				for(int jj = 0; jj < enemies[ii].length; jj++){
					if(map[ii][jj].getType().equals("green") && (Math.random() < (double) balloon/(double) ((17*17)- 50)) && enemies[ii][jj] == null){
						enemies[ii][jj] = "balloon";
						balloon--;
					}
				}
			}
		}
		while(slime > 0){
			for(int ii = 0; ii < enemies.length; ii++){
				for(int jj = 0; jj < enemies[ii].length; jj++){
					if(map[ii][jj].getType().equals("green") && (Math.random() < (double) slime/(double) ((17*17)- 50)) && enemies[ii][jj] == null){
						enemies[ii][jj] = "slime";
						slime--;
					}
				}
			}
		}
		while(antibomberman > 0){
			for(int ii = 0; ii < enemies.length; ii++){
				for(int jj = 0; jj < enemies[ii].length; jj++){
					if(map[ii][jj].getType().equals("green") && (Math.random() < (double) antibomberman/(double) ((17*17)- 50)) && enemies[ii][jj] == null){
						enemies[ii][jj] = "anti";
						antibomberman--;
					}
				}
			}
		}
	}

	/**
	 * @return type of <code>{@link Tile}</code> in map[][]
	 * @param x
	 * @param y
	 * @return
	 */
	public String getTypeAt(int x, int y){
		if(map[x][y] instanceof Explosion)
			return "explosion";
		if(map[x][y] instanceof Upgrade){
			return "upgrade";
		}
		if(x < map.length && y < map[x].length)
			return map[x][y].getType();
		return "null";
	}
	/**
	 * @param coords
	 * @return getTypeAt(coords.x, coords.y)
	 */
	public String getTypeAt(Coordinates coords){
		return getTypeAt(coords.x, coords.y);
	}
	/**
	 * @return door <code>{@link Coordinates}</code>
	 */
	public Coordinates getDoorSquare(){
		for(int ii = 0; ii < upgrades.length; ii++){
			for(int jj = 0; jj < upgrades[ii].length; jj++){
				if(upgrades[ii][jj] != null && upgrades[ii][jj].equals("door")){
					return new Coordinates(ii, jj);
				}
			}
		}
		return new Coordinates(1, 1);
	}

	/**
	 * @param x
	 * @param y
	 * @return <code>{@link Upgrade}</code>'s <code>{@link Tile}</code>
	 */
	public String consumeUpgradeAt(int x, int y){
		Upgrade upg = (Upgrade) map[x][y];
		String type = upg.getUpgradeType();
		if (!type.equals("door")){
			map[x][y] = new Tile("green");
			upgrades[x][y] = null;
		}
		return type;
	}
	/**
	 * @param coords
	 * Parses <code>{@link Coordinates}</code> for consumeUpgradeAt(coords.x, coords.y)
	 */
	public String consumeUpgradeAt(Coordinates coords){
		return consumeUpgradeAt(coords.x, coords.y);
	}

	/**
	 * Sets a <code>{@link Tile}</code> type in the map at [x][y]
	 * @param x
	 * @param y
	 * @param type
	 */
	public void setTypeAt(int x, int y, String type){
		map[x][y] = new Tile(type);
	}
	/**
	 * Parses <code>{@link Coordinates}</code> for setTypeAt(coords.x, coords.y, type)
	 * @param coords
	 * @param type
	 */
	public void setTypeAt(Coordinates coords, String type){
		setTypeAt(coords.x, coords.y, type);
	}

	/** sets a new <code>{@link Explosion}</code> at x, y.
	 * @param x
	 * @param y
	 * @param type
	 */
	public void setExplosionAt(int x, int y, String type){
		map[x][y] = new Explosion(type);
	}
	/**
	 * Parses <code>{@link Coordinates}</code> for setExplosionAt(coords.x, coords.y, type);
	 * @param coords
	 * @param type
	 */
	public void setExplosionAt(Coordinates coords, String type){
		setExplosionAt(coords.x, coords.y, type);
	}

	/**
	 * @return the map's dimension's <code>{@link Coordinates}</code>
	 */
	public Coordinates getDimensions(){
		return new Coordinates(dim.x, dim.y);
	}

	/**
	 * @param coords
	 * @return true if <code>{@link Tile}</code> in map is walkable at certain <code>{@link Coordinates}</code>
	 */
	public boolean isWalkableAt(Coordinates coords){
		return isWalkableAt(coords.x, coords.y);
	}
	/**
	 * @param x
	 * @param y
	 * @return true if <code>{@link Tile}</code> in map is walkable at x, y.
	 */
	public boolean isWalkableAt(int x, int y){
		return map[x][y].isWalkable();
	}
	
	/**
	 * @param coords
	 * @return the <code>{@link Tile}</code>'s sprite at certain position
	 */
	public String getSpriteAt(Coordinates coords){
		return getSpriteAt(coords.x, coords.y);
	}
	/**
	 * @param x
	 * @param y
	 * @return the <code>{@link Tile}</code>'s sprite at certain position
	 */
	public String getSpriteAt(int x, int y){
		if(explore && upgrades[x][y] != null){
			return new Upgrade(upgrades[x][y]).getSprite();
		}
		return map[x][y].getSprite();
	}
	/**
	 * @param restrictedSquare
	 * @return a <code>{@link Coordinates}</code> array consisted of the immediate positions sorrounding the parameter
	 */
	private Coordinates[] getPlayerPersonalSpace(Coordinates restrictedSquare) {
		Coordinates[] playerPersonalSpace = new Coordinates[] {
				   new Coordinates(restrictedSquare.x - 1, restrictedSquare.y + 1), 
				   new Coordinates(restrictedSquare.x - 1, restrictedSquare.y     ),
				   new Coordinates(restrictedSquare.x - 1, restrictedSquare.y - 1),
				   new Coordinates(restrictedSquare.x     , restrictedSquare.y + 1),
				   new Coordinates(restrictedSquare.x     , restrictedSquare.y     ),
				   new Coordinates(restrictedSquare.x     , restrictedSquare.y - 1),
				   new Coordinates(restrictedSquare.x + 1, restrictedSquare.y + 1),
				   new Coordinates(restrictedSquare.x + 1, restrictedSquare.y     ),
				   new Coordinates(restrictedSquare.x + 1, restrictedSquare.y - 1)};
		return playerPersonalSpace;
	}
	/**
	 * @param playerPersonalSpace
	 * @param coords
	 * @return true if a <code>{@link Coordinates}</code> is in playerPersonalSpace's array
	 */
	private boolean isInPlayerPersonalSpace(Coordinates[] playerPersonalSpace, Coordinates coords) {
		for (int ii = 0; ii < playerPersonalSpace.length; ii++ ) {
			if (playerPersonalSpace[ii].equals(coords)) return true;
		}
		return false;
	}
	/**
	 * @return a String[][] array consisted of the enemies' names in which will be their position
	 */
	public String[][] getEnemiesPos() {
		return enemies;
	}
	/**
	 * toggles explore mode, allowing the player to have x-ray cheat abilities
	 */
	public void toggleExploreMode(){
		explore = !explore;
	}
}
