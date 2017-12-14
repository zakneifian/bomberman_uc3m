package uc3m.bomberman.map;

public class Tile{
	private final char TILE_TYPE[] = {'B', 'W', 'G', 'E'};
	private char type;
	private boolean walkable;
	
	public Tile(String type){
		setType(type);
	}
	/**
	 * This method sets the type of the tile and its walkability
	 * @param type
	 * @return tyle type (char)
	 */
	public void setType(String sType){
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
		case "explosion":
			type = TILE_TYPE[3];
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
		case 'E':
			return "explosion";
		default:
			return "wall";
		}
	}
	public boolean isWalkable() {
		return walkable;
	}
	public String getSprite() {
		switch(type){
		case 'G':
			return null;
		case 'B':
			return "bricks.gif";
		case 'W':
			return "wall.gif";
		case 'E':
			return null; //probando
		default:
			return "wall.gif";
		}
	}
}
