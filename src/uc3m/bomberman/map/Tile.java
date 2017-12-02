package uc3m.bomberman.map;

import uc3m.bomberman.entity.Entity;

public class Tile /*TODO extends Entity, si eso*/{
	private final char TILE_TYPE[] = {'B', 'W', 'G'};
	private char type;
	private boolean walkable;
	private Entity/*Upgrade*/ inside;
	public Tile(String type){
		setTileType(type);
	}
	/**
	 * This method sets the type of the tile and its walkability
	 * @param type
	 * @return tyle type (char)
	 */
	public void setTileType(String sType){
		switch(sType.toLowerCase()){
		case "brick": 
			walkable = false;
			type = TILE_TYPE[0];
			break;
		case "wall": 
			walkable = false;
			type = TILE_TYPE[1];
			break;
		case "green":
			type = TILE_TYPE[2];
			walkable = true;
			break;
		default: 
			type = TILE_TYPE[1];
			walkable = false;
		}
	}
	public String getType(){
		switch(type){
		case 'B':
			return "brick";
		case 'W':
			return "wall";
		case 'G':
			return "green";
		default:
			return "wall";
		}
	}
	public boolean isWalkable() {
		return walkable;
	}
	public Entity getInside() {
		return inside;
	}
	public void setInside(Entity/*Upgrade*/ inside) {
		this.inside = inside;
	}
}
