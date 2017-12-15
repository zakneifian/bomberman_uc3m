package uc3m.bomberman.map;
public class Map{
	public static final int NBRICKS = 50;
	
	private Tile[][] map;
	private String[][] upgrades;
	private Coordinates dim;
	
	
	public Map(int dim, int level){
		this.dim = new Coordinates(dim, dim);
		createMap(level);
	}
	private void createMap(int level){
		map = new Tile[dim.x][dim.y];
		upgrades = new String[dim.x][dim.y];
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
			for(int ii = 0; ii < map.length; ii++){
				for(int jj = 0; jj < map[ii].length; jj++){
					//if there is no bricks or walls we will set a brick with a given probability of dim*dim
					if (!(ii == 1 && jj == 1) && !(ii == 1 && jj == 2) && !(ii == 2 && jj == 1) && brickCount < NBRICKS && (map[ii][jj] == null || map[ii][jj].getType().equals("green")) && (Math.random() < 2.0/(dim.x*dim.y - brickCount - 1))) {
						map[ii][jj] = new Tile("brick");
						brickCount++; 
					}
				}
			}
		}
		//determine the number of upgrades of each type depending on the level
		genUpgrades(level);		
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
						//TODO UPGRADE Aquí desaparecen las explosiones, así que:
						if(upgrades[ii][jj] != null){
							map[ii][jj] = new Upgrade(upgrades[ii][jj]);
							upgrades[ii][jj] = null;
						}
					}
				}
			}
		}
	}
	
	private void genUpgrades(int level){
		int bomb, fire, special, remote, skate, geta;
		bomb = (int) (3*(50.0/NBRICKS)+(Math.random()*2)-1);
		fire = 1;
		special = (level+1)%5 == 0 ? 1 : 0;
		remote = (level+1)%10 == 0 ? 1 : 0;
		skate = (level+1)%2 == 0 ? (int)(Math.random()*2) : 0;
		geta = Math.random()<0.2 ? 1 : 0;
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
	//Get upgrade type
	public String consumeUpgradeAt(int x, int y){
		Upgrade upg = (Upgrade) map[x][y];
		String type = upg.getUpgradeType();
		map[x][y] = new Tile("green");
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
		return map[x][y].getSprite();
	}


}
