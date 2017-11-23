package uc3m.bomberman.map;
public class Map{
	private Tile[][] map;
	private Coordinates dim;
	public Map(int dim){
		this.dim = new Coordinates(dim, dim);
	}
	private void createMap(){
		map = new Tile[dim.x][dim.y];
		/*TODO create tiles*/
	}
}
