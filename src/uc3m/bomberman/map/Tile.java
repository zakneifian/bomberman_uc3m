package uc3m.bomberman.map;
//TODO javadoc
public class Tile{
	
	private final char TILE_TYPE[] = {'B', 'W', 'G'}; // 0- brick; 1- Wall; 2- Green; 4- Explosion
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
		default: 
			type = TILE_TYPE[2];
			walkable = true;
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
	public String getSprite() {
		switch(type){
		case 'G':
			return null;
		case 'B':
			return "bricks.gif";
		case 'W':
			return "wall.gif";
		default:
			return "wall.gif";
		}
	}
}
