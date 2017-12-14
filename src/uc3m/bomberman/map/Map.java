package uc3m.bomberman.map;
public class Map{
	private Tile[][] map;
	private Coordinates dim;
	private int nBricks = 50;
	public Map(int dim){
		this.dim = new Coordinates(dim, dim);
		createMap();
	}
	private void createMap(){
		map = new Tile[dim.x][dim.y];
		//Create green cells
		for(int ii = 0; ii < map.length; ii++){
			for(int jj = 0; jj < map[ii].length; jj++){
				map[ii][jj] = new Tile("green");
			}
		}
		//Create Walls
		//TODO las casillas de arriba no pueden tener ladrillos, para que no se quede atrapado el jugador
		for(int ii = 0; ii < map.length; ii++){
			for(int jj = 0; jj < map[ii].length; jj++){
				if (ii == 0 || jj == 0 || ii == map.length - 1 || jj == map[ii].length - 1 || (ii % 2 == 0 && jj % 2 == 0))
				map[ii][jj] = new Tile("wall");
			}
		}		
		//Create Bricks
		int brickCount = 0;
		while ( brickCount < nBricks) {
			for(int ii = 0; ii < map.length; ii++){
				for(int jj = 0; jj < map[ii].length; jj++){
					//if there is no bricks or walls we will set a brick with a given probability of dim*dim
					if (!(ii == 1 && jj == 1) && !(ii == 1 && jj == 2) && !(ii == 2 && jj == 1) && brickCount < nBricks && (map[ii][jj] == null || map[ii][jj].getType().equals("green")) && (Math.random() < 2.0/(dim.x*dim.y - brickCount - 1))) {
						map[ii][jj] = new Tile("brick");
						brickCount++; 
					}
				}
			}
		}
	}
	//Get type of file
	public String getTypeAt(int x, int y){
		if(x < map.length && y < map[x].length)
			return map[x][y].getType();
		return "null";
	}
	public String getTypeAt(Coordinates coords){
		return getTypeAt(coords.x, coords.y);
	}
	//Set type of tile
	public void setTypeAt(int x, int y, String type){
		map[x][y] = new Tile(type);
	}
	public void setTypeAt(Coordinates coords, String type){
		setTypeAt(coords.x, coords.y, type);
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
