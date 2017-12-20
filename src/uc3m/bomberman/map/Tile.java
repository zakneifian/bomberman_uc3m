package uc3m.bomberman.map;
/**
 * This class holds all the methods and information regarding each Tile of the map
 *
 */
public class Tile{
	
	private final char TILE_TYPE[] = {'B', 'W', 'G'}; // 0- brick; 1- Wall; 2- Green; 4- Explosion
	private char type;
	private boolean walkable;
	
	/**
	 * Full constructor <code>{@link Tile}</code> constructor
	 * @param type
	 */
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
	/**
	 * @return the type of the <code>{@link Tile}</code>
	 */
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
	/**
	 * @return true if <code>{@link Tile}</code> is walkable
	 */
	public boolean isWalkable() {
		return walkable;
	}
	/**
	 * @return the String path of the sprite of certain <code>{@link Tile}</code>
	 */
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
