package uc3m.bomberman.map;

import uc3m.bomberman.entity.Entity;

public class Tile /*TODO extends Entity*/{
	private final char TILE_TYPE[] = {'B', 'W', 'G'};
	private char type;
	private boolean walkable;
	private Entity inside;
	public Tile(String type){
		setTileType(type);
	}
	/**
	 * This method sets the type of the tile and its walkability
	 * @param type
	 * @return
	 */
	public char setTileType(String type){
		switch(type.toLowerCase()){
		case "brick": 
			walkable = false;
			return TILE_TYPE[0];
		case "wall": 
			walkable = false;
			return TILE_TYPE[1];
		case "green":
			walkable = true;
		default: 
			return TILE_TYPE[2];
		}
	}
	/*TODO getters y setters*/
}
