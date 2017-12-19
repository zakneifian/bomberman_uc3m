package uc3m.bomberman.map;

//TODO javadoc
public class Map{
	public static final int NBRICKS = 50;
	
	private Tile[][] map;
	private String[][] upgrades;
	private String[][] enemies;
	private Coordinates dim;
	
	private boolean explore = false;
	
	public Map(int dim, int level, Coordinates restrictedSquare){
		this.dim = new Coordinates(dim, dim);
		createMap(getPlayerPersonalSpace(restrictedSquare), level);
	}
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

	//Get type of file
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
	public String getTypeAt(Coordinates coords){
		return getTypeAt(coords.x, coords.y);
	}
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
	//Get upgrade type
	public String consumeUpgradeAt(int x, int y){
		Upgrade upg = (Upgrade) map[x][y];
		String type = upg.getUpgradeType();
		if (!type.equals("door")){
			map[x][y] = new Tile("green");
			upgrades[x][y] = null;
		}
		return type;
	}
	public String consumeUpgradeAt(Coordinates coords){
		return consumeUpgradeAt(coords.x, coords.y);
	}
	//Set type of tile
	public void setTypeAt(int x, int y, String type){
		map[x][y] = new Tile(type);
	}
	public void setTypeAt(Coordinates coords, String type){
		setTypeAt(coords.x, coords.y, type);
	}
	//Set explosion
	public void setExplosionAt(int x, int y, String type){
		map[x][y] = new Explosion(type);
	}
	public void setExplosionAt(Coordinates coords, String type){
		setExplosionAt(coords.x, coords.y, type);
	}
	//
	public Coordinates getDimensions(){
		return new Coordinates(dim.x, dim.y);
	}
	/*
	 * public void addInside(int x, int y, Upgrade upg){
	 * 		map[x][y].setInside(upg);
	 * }
	 * public Entity destroyTile(int x, int y){
	 * 		setTypeAt(x, y, "green");
	 * 		return map[x][y].getInside();
	 */
	public boolean isWalkableAt(Coordinates coords){
		return isWalkableAt(coords.x, coords.y);
	}
	public boolean isWalkableAt(int x, int y){
		return map[x][y].isWalkable();
	}
	
	public String getSpriteAt(Coordinates coords){
		return getSpriteAt(coords.x, coords.y);
	}
	public String getSpriteAt(int x, int y){
		if(explore && upgrades[x][y] != null){
			return new Upgrade(upgrades[x][y]).getSprite();
		}
		return map[x][y].getSprite();
	}
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
	private boolean isInPlayerPersonalSpace(Coordinates[] playerPersonalSpace, Coordinates coords) {
		for (int ii = 0; ii < playerPersonalSpace.length; ii++ ) {
			if (playerPersonalSpace[ii].equals(coords)) return true;
		}
		return false;
	}
	public String[][] getEnemiesPos() {
		return enemies;
	}
	public void toggleExploreMode(){
		explore = !explore;
	}
}
